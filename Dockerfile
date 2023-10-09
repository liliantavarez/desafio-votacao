# Define a imagem base
FROM gradle:7.2.0-jdk11 AS build

# Defina o diretório de trabalho
WORKDIR /app

# Copie apenas os arquivos de build relacionados ao Gradle
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Execute a tarefa Gradle para baixar dependências, mas não a compilação
RUN gradle build --no-daemon || return 0

# Copie todos os outros arquivos
COPY . .

# Execute a tarefa Gradle para compilar o projeto
RUN gradle build --no-daemon

# Imagem base para a execução
FROM adoptopenjdk/openjdk11:jdk-11.0.12_7-alpine

# Defina o diretório de trabalho
WORKDIR /app

# Copie o arquivo JAR construído a partir do estágio anterior
COPY --from=build /build/libs/api-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta em que o aplicativo será executado
EXPOSE 8080

# Comando de entrada para iniciar o aplicativo
ENTRYPOINT ["java", "-jar", "app.jar"]
