package com.apus.drones.apusdronesbackend.service;

import com.apus.drones.apusdronesbackend.mapper.ProductDtoMapper;
import com.apus.drones.apusdronesbackend.model.entity.ProductEntity;
import com.apus.drones.apusdronesbackend.model.entity.ProductImage;
import com.apus.drones.apusdronesbackend.model.entity.UserEntity;
import com.apus.drones.apusdronesbackend.model.enums.ProductStatus;
import com.apus.drones.apusdronesbackend.repository.ProductImageRepository;
import com.apus.drones.apusdronesbackend.repository.ProductRepository;
import com.apus.drones.apusdronesbackend.service.dto.CreateProductDTO;
import com.apus.drones.apusdronesbackend.service.dto.FileDTO;
import com.apus.drones.apusdronesbackend.service.dto.ProductDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.apus.drones.apusdronesbackend.mapper.ProductDtoMapper.fromProductEntityList;

@Transactional
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final PartnerService partnerService;
    private final ImageUploadService imageUploadService;

    public ProductServiceImpl(ProductRepository productRepository, ProductImageRepository productImageRepository, PartnerService partnerService, ImageUploadService imageUploadService) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.partnerService = partnerService;
        this.imageUploadService = imageUploadService;
    }

    @Override
    public ResponseEntity<Void> create(CreateProductDTO productDTO) {
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

        // TODO obter o parceiro da autenticação
        // partnerService.get may throw a ResponseStatusException
        this.partnerService.get(productDTO.getPartner());

        UserEntity partner = UserEntity.builder().id(productDTO.getPartner()).build();

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
    public ResponseEntity<Void> update(Long id, ProductDTO productDTO) {
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Não foi possível encontrar o produto com ID " + id));

        updateProduct(productDTO, entity);

        productRepository.save(entity);

        return new ResponseEntity<>(HttpStatus.OK);
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

    public void updateProduct(ProductDTO productDTO, ProductEntity entity) {
        if (productDTO.getDescription() != null)
            entity.setDescription(productDTO.getDescription());

        if (productDTO.getPrice() != null)
            entity.setPrice(productDTO.getPrice());

        if (productDTO.getStatus() != null)
            entity.setStatus(productDTO.getStatus());

        if (productDTO.getWeight() != null)
            entity.setWeight(productDTO.getWeight());

        if (productDTO.getQuantity() != null)
            entity.setQuantity(productDTO.getQuantity());

        if (productDTO.getImagesUrls() != null) {
            List<ProductImage> productImages = productDTO.getImagesUrls().stream().map(
                    url -> {
                        ProductImage productImage = productImageRepository
                                .findProductImageByProductAndUrl(entity, url);
                        return productImage != null
                                ? productImage
                                : ProductImage.builder()
                                .url(url)
                                .isMain(false)
                                .product(entity)
                                .build();
                    }
            ).collect(Collectors.toList());

            if (!productImages.isEmpty())
                productImages.get(0).setIsMain(true);

            productImageRepository.deleteAllByProduct(entity);
            entity.setProductImages(productImages);
        }
    }
}
