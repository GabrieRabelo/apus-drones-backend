package com.apus.drones.apusdronesbackend.service;

import com.amazonaws.services.securityhub.model.Product;
import com.apus.drones.apusdronesbackend.config.CustomUserDetails;
import com.apus.drones.apusdronesbackend.mapper.ProductDtoMapper;
import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.model.enums.Role;
import com.apus.drones.apusdronesbackend.repository.ProductImageRepository;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.repository.UserRepository;
import com.apus.drones.apusdronesbackend.service.dto.CreateProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.FileDTO;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.UpdateProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.apus.drones.apusdronesbackend.mapper.ProductDtoMapper.fromProductEntityList;

@Transactional
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final UserRepository userRepository;
    private final PartnerService partnerService;
    private final ImageUploadService imageUploadService;

    public ProductServiceImpl(ProductRepository productRepository, UserRepository userRepository, ProductImageRepository productImageRepository, PartnerService partnerService, ImageUploadService imageUploadService) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.userRepository = userRepository;
        this.partnerService = partnerService;
        this.imageUploadService = imageUploadService;
    }

    @Override
    public ResponseEntity<Void> create(CreateProductDTO productDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }

        CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
        UserEntity partner = userRepository.findAllByIdAndRole(details.getUserID(), Role.PARTNER)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Não foi possível encontrar o Parceiro com ID " + details.getUserID()));

        long mainFileCount = productDTO.getFiles().stream().map(FileDTO::isMainFile)
                .filter(isMain -> isMain)
                .count();

        if (productDTO.getFiles().size() > 0) {
            if (mainFileCount > 1) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apenas uma imagem principal é permitida");
            } else if (mainFileCount == 0) {
                productDTO.getFiles().get(0).setMainFile(true);
            }
        }

        List<ProductImage> images = uploadFiles(productDTO.getFiles());

        ProductEntity entity = ProductEntity.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .status(ProductStatus.ACTIVE)
                .weight(productDTO.getWeight())
                .quantity(productDTO.getQuantity())
                .deleted(Boolean.FALSE)
                .createDate(LocalDateTime.now())
                .user(partner)
                .productImages(images)
                .build();

        Long generatedId = productRepository.save(entity).getId();
        entity.setId(generatedId);

        for (ProductImage image : images) {
            image.setProduct(entity);
        }

        productImageRepository.saveAll(images);

        log.info("Saved new product entity with id [{}]", generatedId);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ProductDTO get(Long id) {
        return productRepository.findByIdAndDeletedFalse(id).map(ProductDtoMapper::fromProductEntity)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Não foi possível encontrar o produto com ID " + id));
    }

    @Override
    public List<ProductDTO> findAllActiveProductsByUserId(Long userId) {
        var resultFromDB = productRepository.findAllByUserIdAndStatusAndDeletedFalse(userId, ProductStatus.ACTIVE);
        return fromProductEntityList(resultFromDB);
    }

    @Override
    public List<ProductDTO> findAllActiveProductsByUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth.isAuthenticated()) {
            CustomUserDetails details = (CustomUserDetails) auth.getPrincipal();
            var resultFromDB = productRepository.findAllByUserIdAndStatusAndDeletedFalse(details.getUserID(), ProductStatus.ACTIVE);
            return fromProductEntityList(resultFromDB);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado.");
        }
    }

    @Override
    public ProductDTO update(Long id, UpdateProductDTO updateProductDTO) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Não foi possível encontrar o produto com ID " + id));

        updateProduct(updateProductDTO, entity);

        productRepository.save(entity);

        return ProductDtoMapper.fromProductEntity(entity);
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        ProductEntity entity = productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Não foi possível encontrar o produto com ID " + id));

        entity.setDeleted(Boolean.TRUE);

        productRepository.save(entity);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public List<ProductImage> uploadFiles(List<FileDTO> files) {
        String url = "";
        List<ProductImage> imageEntities = new ArrayList<>();

        for (FileDTO file : files) {

            try {
                url = imageUploadService.upload(file);
            } catch (SizeLimitExceededException e) {
                double actual = e.getActualSize() / 1048576D;
                double max = e.getPermittedSize() / 1048576D;
                log.error("Error at image upload, max size limite exceeded.", e);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Imagem ultrapassa o limite de tamanho, tamanho atual: " + actual + "MB, tamanho máximo permitido: " + max + "MB");
            }

            var prod =
                    ProductImage
                            .builder()
                            .url(url)
                            .isMain(file.isMainFile())
                            .build();

            imageEntities.add(prod);
        }

        return imageEntities;
    }

    public void updateProduct(UpdateProductDTO updateProductDTO, ProductEntity entity) {
        List<FileDTO> productImagesFiles = updateProductDTO.getFiles();
        List<String> removedImagesUrls = updateProductDTO.getRemovedImagesUrls();
        String mainImageUrl = updateProductDTO.getMainImageUrl();

        long mainFileCount = productImagesFiles != null
                ? productImagesFiles.stream().filter(FileDTO::isMainFile).count()
                : 0;

        if (mainFileCount > 1 || mainFileCount > 0 && mainImageUrl != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Apenas uma imagem principal é permitida");
        }

        if (removedImagesUrls != null && removedImagesUrls.stream().anyMatch(r -> r.equals(mainImageUrl))) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Uma imagem a ser removida não pode ser definida como principal"
            );
        }

        if (updateProductDTO.getDescription() != null)
            entity.setDescription(updateProductDTO.getDescription());

        if (updateProductDTO.getPrice() != null)
            entity.setPrice(updateProductDTO.getPrice());

        if (updateProductDTO.getStatus() != null)
            entity.setStatus(updateProductDTO.getStatus());

        if (updateProductDTO.getWeight() != null)
            entity.setWeight(updateProductDTO.getWeight());

        if (updateProductDTO.getQuantity() != null)
            entity.setQuantity(updateProductDTO.getQuantity());

        List<ProductImage> uploadedImages;

        if (productImagesFiles != null) {
            uploadedImages = uploadFiles(productImagesFiles);
            for (ProductImage img : uploadedImages) {
                img.setProduct(entity);
            }

            if (mainFileCount == 1 || mainImageUrl != null) {
                entity.getProductImages().stream().filter(ProductImage::getIsMain).forEach(pi -> pi.setIsMain(false));
            }

            entity.getProductImages().addAll(uploadedImages);
        }

        if (removedImagesUrls != null) {
            for (String url : removedImagesUrls) {
                productImageRepository.deleteByProductAndUrl(entity, url);
                entity.getProductImages().removeIf(img -> Objects.equals(img.getUrl(), url));
            }
        }

        if (!entity.getProductImages().isEmpty()) {
            if (mainImageUrl != null) {
                entity.getProductImages().stream()
                        .filter(pi -> pi.getUrl().equals(mainImageUrl))
                        .forEach(pi -> pi.setIsMain(true));
            }

            if (entity.getProductImages().stream().noneMatch(ProductImage::getIsMain)) {
                entity.getProductImages().get(0).setIsMain(true);
            }
        }
    }
}
