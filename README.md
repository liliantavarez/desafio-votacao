# API - Votação

> A API de Votação permite a realização de votações em pautas específicas. Ela oferece uma interface para criar novas pautas, registrar votos e consultar os resultados das votações.

## 💻 Pré-requisitos

- **Java 11 ou Superior**: É necessário ter o Java 11 ou uma versão mais recente instalada na sua máquina. Você pode verificar a versão do Java instalada usando o comando `java -version` no seu terminal.

## 🚀 Instalando desafio-votacao

Para instalar o desafio-votacao, siga estas etapas:

**Windows:**

1. Abra seu terminal ou Prompt de Comando.

2. Execute o seguinte comando para clonar o repositório:

   ```
   git clone https://github.com/liliantavarez/desafio-votacao.git
   ```

   Isso fará o download do projeto para o seu computador.

3. Abra o projeto em seu IDE de preferência e o execute ou abra o terminal da sua IDE e executar o comando:

   ```
   ./gradlew bootRun
   ```
   O projeto será iniciado localmente.

4. Executando os testes:

   ```
   ./gradlew test
   ```

O serviço fica disponivel em: 
### [Swagger](http://localhost:8080/swagger-ui/index.html)

```
http://localhost:8080/swagger-ui/index.html
```

## Documentação da API

Aqui estão os endpoints separados por entidade na documentação e com informações sobre onde os parâmetros devem ser passados:

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
| `id`      | `string` | Path (parâmetro da URL) |

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
| `id`      | `string` | Path (parâmetro da URL) |

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
| `id`      | `string` | Path (parâmetro da URL) |

#### Buscar Resultado de Votação por ID da Sessão

Busca o resultado de votação de uma sessão pelo seu ID.

```http
GET /api/v1/sessoes/{id}/resultado
```

| Parâmetro | Tipo     | Local da Passagem |
| :-------- | :------- | :---------------- |
| `id`      | `string` | Path (parâmetro da URL) |

### Votos

Endpoints para a entidade "Voto":

#### Salvar Voto

Salva o voto de um associado em uma sessão de votação.

```http
POST /api/v1/votos/salvar
```

| Parâmetro   | Tipo     | Local da Passagem |
| :---------- | :------- | :---------------- |
| `sessao_id` | `string` | Body (corpo da requisição) |
| `associado_cpf` | `string` | Body (corpo da requisição) |
| `votoEnum`  | `string` | Body (corpo da requisição) |

Valores Aceitos para votoEnum:

**SIM**: Indica um voto favorável.

**NAO**: Indica um voto contrário.

Certifique-se de utilizar um desses valores exatamente como indicado ao fazer uma requisição para a API.
