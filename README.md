# API - Votação

> A API de Votação permite a realização de votações em pautas específicas. Ela oferece uma interface para criar novas pautas, registrar votos e consultar os resultados das votações.</br>
> Este projeto abrange diversos aspectos, desde a definição das entidades e endpoints até a configuração do ambiente com Docker.


### Estrutura do Projeto
A implementação segue uma estrutura modular, dividida em pacotes distintos para melhor organização e manutenção do código. 
Temos seções dedicadas a entidades como client, config, controllers, dtos, enums, exception, models, repositories e services.

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

## 🔥 Acessando a API
A aplicação estará disponível em:

- Swagger Local: http://localhost:8080/swagger-ui/index.html
- OnRender: https://api-votacao.onrender.com/swagger-ui/index.html

O Swagger é utilizado para gerar documentação automática da API. 
O arquivo api-docs.json descreve detalhadamente cada endpoint, suas entradas e saídas, proporcionando uma visão clara das funcionalidades disponíveis e 
facilitando a compreensão e utilização da API. 

## 🔁 Comunicação Externa 
Com o objetivo de verificar a situação do CPF, foi incorporada uma chamada à API externa ([API - CPF.CNPJ](https://www.cpfcnpj.com.br/dev/)). 
Essa integração não apenas valida, mas também enriquece os dados dos associados, proporcionando uma camada adicional de verificação </br>
e aprimorando a qualidade das informações obtidas. Além disso, para facilitar testes e assegurar a robustez do sistema, foi implementado o uso do WireMock, uma ferramenta que simula serviços externos, permitindo testes mais abrangentes e eficazes da integração.

## 📖 Documentação da API

### Associação de Associados

Endpoints para a entidade "Associado":

#### Cadastrar um Associado

Cadastra um associado no sistema.

```http
POST /api/v1/associados
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
POST /api/v1/pautas
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
POST /api/v1/sessoes
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
POST /api/v1/votos
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

## 🎲 Modelagem de dados
O modelo de dados é representado por entidades como Pauta, Associado, Voto, e Sessao. 
Essas entidades refletem as principais componentes do sistema, permitindo o registro de pautas, associados, votos, e sessões de votação.

![exported_from_idea drawio](https://github.com/liliantavarez/desafio-votacao/assets/51184806/9601ba09-3a2e-4ad7-a189-fb47c7f9a0d8)

## 👨‍💻 Tecnologias Utilizadas

Essas foram as principais tecnologias e bibliotecas utilizadas no projeto "desafio-votacao" afim de garantir a conformidade com as regras de validação, gerenciar o esquema do banco de dados e fornecer documentação e testes integrados.

1. **Spring Boot (2.7.17)**: O Spring Boot é uma estrutura poderosa para a criação de aplicativos Java com facilidade. Ele oferece uma configuração simplificada, desenvolvimento baseado em anotações e um ecossistema maduro que facilita o desenvolvimento e a manutenção de aplicativos. 

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

12. **WireMock**: Usado para realizar teste de integração com a API que consulta a situação do CPF, pois permite testar o comportamento da aplicação em relação a esses serviços sem a necessidade de ambientes reais de desenvolvimento ou testes.

13. **Docker**: O Docker é usado para criar contêineres isolados para a aplicação e o banco de dados PostgreSQL. Isso garante um ambiente de desenvolvimento e testes consistente e reproduzível, além de facilitar a implantação da aplicação em servidores, como o OnRender.


## 🤗 Considerações Finais
A solução apresentada aborda de maneira abrangente os requisitos do desafio, desde a modelagem de dados até a implementação de chamadas externas e a disponibilização da API com Docker, cada aspecto foi cuidadosamente tratado.</br>
Este projeto demonstra não apenas a implementação das funcionalidades propostas, mas também a aplicação de boas práticas de desenvolvimento, desde a modelagem cuidadosa dos dados até a integração de serviços externos, a configuração Docker e documentação eficaz. 
