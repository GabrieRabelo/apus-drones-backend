# Leia-me Antes

### Executando o projeto
Para executar o projeto via terminal basta utilizar o comando abaixo:\
`./gradlew bootrun`\
Ou utilizar os executaveis da sua IDE.

### SonarQube
Para utilizar o report de qualidade de código é necessário fazer o download do servidor do SonarQube:
* https://www.sonarqube.org/downloads/

Como rodar o servidor pode ser encontrado no tutorial abaixo:
* https://docs.sonarqube.org/latest/setup/get-started-2-minutes/

### Documentação da API
Para acessar a página de documentação viva (Swagger) utilize o link abaixo, com o projeto rodando:
* http://localhost:8080/swagger-ui/#/

# Documentação padrão
Aqui é encontrada a documentação das tecnologias adicionadas no momento da criação do projeto.

### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.3/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.3/gradle-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.3/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data MongoDB](https://docs.spring.io/spring-boot/docs/2.5.3/reference/htmlsingle/#boot-features-mongodb)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with MongoDB](https://spring.io/guides/gs/accessing-data-mongodb/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)


### CHECKSTYLE
This project's checkstyle use the pattern describe for Google application with minor adjustments
the checkstyle description is localized in root folder named [checkstyle.xml](checkstyle.xml)
for mor details view the documentation of the checkstyle plugin [here](https://checkstyle.sourceforge.io/config.html)

# To run the checkstyle:
the checkstyle task will be trigger every time the gradle build command is executed.
or it can be trigger manually.
to do so, run the command bellow
```
$ ./gradlew checkstyleMain
```
After running this command a report.html will be generated at `build/reports/checkstyle/main.html`
containing all the "out-of-pattern" lines found in the project.

### GIT PRE-PUSH HOOK
To enable the pre-push hook on git is necessary to place an executable file named [pre-push](pre-push.sh) inside the
`.git/hooks` folder

There is, already, an executable file to create that link, so, to enable the pre-push hook
just execute the command
```
$ ./link-to-hook.sh
```
After done that, every push made will trigger the build command, if fails will not allow to do the push.

## for god’s sake, I really need to push this thing and the build is not passing
First, shame on you
<br>Secondly, you already check if the breaking testes / breaking build will not impact on the production environment?
<br>if the answer to that question is yes, you can run the following command. 

```
$ git push <origin> <branch> --no-verify
```
The command will not trigger the pre-push hook
<br>
this can also be used when pairing with someone, and you just need to pass the code to your colleague
