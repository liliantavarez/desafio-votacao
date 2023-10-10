# Fase 1: Construção do projeto sem testes
FROM gradle:7.2.0-jdk11 AS build

# Copie apenas os arquivos de build relacionados ao Gradle
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Execute a tarefa Gradle para baixar dependências e compilar o projeto, sem testes
RUN gradle build --no-daemon

# Fase 2: Construção da imagem final
FROM adoptopenjdk/openjdk11:jdk-11.0.12_7-alpine

# Copie o arquivo JAR construído a partir do estágio anterior
COPY --from=build /build/libs/api-1.0-SNAPSHOT.jar app.jar

# Exponha a porta em que o aplicativo será executado
EXPOSE 8080

# Comando de entrada para iniciar o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]
