# API - Votação

> A API de Votação permite a realização de votações em pautas específicas. Ela oferece uma interface para criar novas pautas, registrar votos e consultar os resultados das votações.

## 💻 Pré-requisitos

- **Java 11 ou Superior**: É necessário ter o Java 11 ou uma versão mais recente instalada na sua máquina. Você pode verificar a versão do Java instalada usando o comando `java -version` no seu terminal.
- **PostgreSQL (Opcional)**: Se você deseja usar um banco de dados PostgreSQL local, é necessário instalá-lo na sua máquina. Você pode fazer o download do PostgreSQL em https://www.postgresql.org/download/ e seguir as instruções de instalação.
- **Docker (Opcional)**: Se preferir, você pode executar a aplicação com um banco de dados PostgreSQL em um contêiner Docker. Certifique-se de ter o Docker instalado na sua máquina. Você pode fazer o download do Docker em https://www.docker.com/get-started e seguir as instruções de instalação.
  
## 🚀 Instalando desafio-votacao

Para instalar o desafio-votacao, siga estas etapas:

### Clonando o Repositório

Abra seu terminal ou Prompt de Comando e execute o seguinte comando para clonar o repositório:

   ```
   git clone https://github.com/liliantavarez/desafio-votacao.git
   ```

   Isso fará o download do projeto para o seu computador.

### Configuração do Banco de Dados

Banco de Dados Local

Se você deseja usar um banco de dados PostgreSQL local, siga estas etapas:

1. Certifique-se de que o PostgreSQL está instalado e em execução na sua máquina.
2. Abra o arquivo application.properties localizado na pasta src/main/resources do projeto.
3. No arquivo application.properties, atualize as seguintes configurações com as credenciais do seu banco de dados local:

```
DATABASE_PASSWORD=sua_senha_local
DATABASE_USERNAME=seu_usuario_local
DATABASE_URL=jdbc:postgresql://localhost:5432/seu_banco_local
```

Banco de Dados Docker

Se você deseja usar um banco de dados PostgreSQL em um contêiner Docker, siga estas etapas:

1. Navegue até a pasta raiz do projeto onde está localizado o arquivo docker-compose.yaml.
2. Execute o seguinte comando para iniciar os contêineres em segundo plano (detached mode):
   
```
docker-compose up -d
```

No arquivo application.properties, as configurações já devem estar ajustadas para funcionar com o banco de dados PostgreSQL em um contêiner Docker. Não é necessário fazer alterações.

### Executando a Aplicação

Abra o projeto em seu IDE de preferência ou abra o terminal da sua IDE e execute o seguinte comando para iniciar a aplicação:

   ```
   ./gradlew bootRun
   ```
   O projeto será iniciado localmente.

### Executando os testes:

Você pode executar os testes da aplicação usando o seguinte comando:

   ```
   ./gradlew test
   ```

### Acessando a API
A aplicação estará disponível em:

Swagger: Você pode acessar a interface do Swagger para testar os endpoints da API em http://localhost:8080/swagger-ui/index.html.

## 📖 Documentação da API

### Associação de Associados

Endpoints para a entidade "Associado":

#### Cadastrar um Associado

Cadastra um associado no sistema.

```http
POST /api/v1/associados/cadastrar
```

| Parâmetro | Tipo     | Local da Passagem |
| :-------- | :------- | :---------------- |
| `nome`    | `string` | Body (corpo da requisição) |
| `cpf`     | `string` | Body (corpo da requisição) |

#### Buscar Associado por ID

Busca um associado pelo seu ID.

```http
GET /api/v1/associados/{id}
```

| Parâmetro | Tipo     | Local da Passagem |
| :-------- | :------- | :---------------- |
| `id`      | `integer` | Path (parâmetro da URL) |

### Pautas

Endpoints para a entidade "Pauta":

#### Cadastrar uma Pauta

Cadastra uma pauta no sistema.

```http
POST /api/v1/pautas/cadastrar
```

| Parâmetro   | Tipo     | Local da Passagem |
| :---------- | :------- | :---------------- |
| `titulo`    | `string` | Body (corpo da requisição) |
| `descricao` | `string` | Body (corpo da requisição) |

#### Buscar Pauta por ID

Busca uma pauta pelo seu ID.

```http
GET /api/v1/pautas/{id}
```

| Parâmetro | Tipo     | Local da Passagem |
| :-------- | :------- | :---------------- |
| `id`      | `integer` | Path (parâmetro da URL) |

### Sessões de Votação

Endpoints para a entidade "Sessão de Votação":

#### Iniciar uma Sessão de Votação

Inicia uma sessão de votação para uma pauta específica.

```http
POST /api/v1/sessoes/iniciarVotacao
```

| Parâmetro          | Tipo     | Local da Passagem |
| :----------------- | :------- | :---------------- |
| `titulo`           | `string` | Body (corpo da requisição) |
| `dataEncerramento` | `string` | Body (corpo da requisição) (Opcional) |

#### Buscar Sessão de Votação por ID

Busca uma sessão de votação pelo seu ID.

```http
GET /api/v1/sessoes/{id}
```

| Parâmetro | Tipo     | Local da Passagem |
| :-------- | :------- | :---------------- |
| `id`      | `integer` | Path (parâmetro da URL) |

#### Buscar Resultado de Votação por ID da Sessão

Busca o resultado de votação de uma sessão pelo seu ID.

```http
GET /api/v1/sessoes/{id}/resultado
```

| Parâmetro | Tipo     | Local da Passagem |
| :-------- | :------- | :---------------- |
| `id`      | `integer` | Path (parâmetro da URL) |

### Votos

Endpoints para a entidade "Voto":

#### Salvar Voto

Salva o voto de um associado em uma sessão de votação.

```http
POST /api/v1/votos/salvar
```

| Parâmetro   | Tipo     | Local da Passagem |
| :---------- | :------- | :---------------- |
| `sessao_id` | `integer` | Body (corpo da requisição) |
| `associado_cpf` | `string` | Body (corpo da requisição) |
| `votoEnum`  | `string` | Body (corpo da requisição) |

Valores Aceitos para votoEnum:

**SIM**: Indica um voto favorável.

**NAO**: Indica um voto contrário.

Certifique-se de utilizar um desses valores exatamente como indicado ao fazer uma requisição para a API.

## 👨‍💻 Tecnologias Utilizadas

Essas foram as principais tecnologias e bibliotecas utilizadas no projeto "desafio-votacao" afim de garantir a conformidade com as regras de validação, gerenciar o esquema do banco de dados e fornecer documentação e testes integrados.

1. **Spring Boot (2.7.17)**: O Spring Boot é uma estrutura poderosa para a criação de aplicativos Java com facilidade. Ele oferece uma configuração simplificada, desenvolvimento baseado em anotações e um ecossistema maduro que facilita o desenvolvimento e a manutenção de aplicativos. A versão 2.7.17 é usada para garantir suporte a longo prazo (LTS).

2. **Spring Cloud OpenFeign**: O Spring Cloud OpenFeign é uma parte do Spring Cloud que oferece uma maneira mais simples de criar clientes HTTP para serviços REST. Isso é útil para se comunicar com outras APIs, como a de consulta de CPF.

3. **Hibernate Validator (6.2.0.Final)**: O Hibernate Validator é uma implementação da especificação de validação do Bean Validation, que permite a validação de objetos de domínio, incluindo entidades JPA. É usado para garantir que os dados enviados ou recebidos pela API estejam de acordo com as regras de validação.

4. **Spring Boot Starter Validation**: O Starter Validation faz parte do ecossistema Spring Boot e fornece suporte à validação de dados. Ele funciona em conjunto com o Hibernate Validator para aplicar regras de validação aos objetos do modelo.

5. **Spring Boot Starter Data JPA**: O Starter Data JPA é uma parte do Spring Boot que simplifica o uso do Spring Data JPA para acesso a bancos de dados relacionais. É usado para persistir os dados do aplicativo no banco de dados.

6. **Flyway**: O Flyway é uma ferramenta de migração de banco de dados que ajuda a gerenciar e versionar as alterações do esquema do banco de dados. É usado para manter o esquema do banco de dados consistente com a evolução do aplicativo.

7. **Spring Boot DevTools**: As ferramentas de desenvolvimento do Spring Boot são úteis para o desenvolvimento e depuração rápidos. Elas oferecem recursos como reinicialização automática do aplicativo e funcionalidade avançada de depuração.

8. **PostgreSQL (ou H2 Database para testes)**: O PostgreSQL é um banco de dados relacional de código aberto amplamente utilizado. Ele é escolhido para armazenar dados em ambientes de produção. O H2 Database é usado para testes, proporcionando um ambiente de teste isolado.

9. **Lombok**: O Projeto Lombok é uma biblioteca Java que ajuda a reduzir a verbosidade do código, gerando automaticamente getters, setters, construtores e outros métodos comuns. Isso melhora a legibilidade do código e reduz a probabilidade de erros.

10. **RestAssured**: O RestAssured é uma biblioteca Java usada para testar APIs REST de forma simples e legível. É utilizado para escrever testes de integração que verificam se os endpoints da API funcionam conforme o esperado.

11. **Springdoc OpenAPI (Swagger)**: O Springdoc OpenAPI é uma biblioteca que gera automaticamente documentação interativa da API (usando o Swagger) com base nas anotações do Spring. Isso ajuda os desenvolvedores a entender e testar a API facilmente.

