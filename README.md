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
| `votoEnum`  | `string` | Body (corpo da requisição) |

Valores Aceitos para votoEnum:

**SIM**: Indica um voto favorável.

**NAO**: Indica um voto contrário.

Certifique-se de utilizar um desses valores exatamente como indicado ao fazer uma requisição para a API.


# Votação

## Objetivo

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. Imagine que você deve criar uma solução para dispositivos móveis para gerenciar e participar dessas sessões de votação.
Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por
  um tempo determinado na chamada de abertura ou 1 minuto por default)
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado
  é identificado por um id único e pode votar apenas uma vez por pauta)
- Contabilizar os votos e dar o resultado da votação na pauta

Para fins de exercício, a segurança das interfaces pode ser abstraída e qualquer chamada para as interfaces pode ser considerada como autorizada. A solução deve ser construída em java, usando Spring-boot, mas os frameworks e bibliotecas são de livre escolha (desde que não infrinja direitos de uso).

É importante que as pautas e os votos sejam persistidos e que não sejam perdidos com o restart da aplicação.

O foco dessa avaliação é a comunicação entre o backend e o aplicativo mobile. Essa comunicação é feita através de mensagens no formato JSON, onde essas mensagens serão interpretadas pelo cliente para montar as telas onde o usuário vai interagir com o sistema. A aplicação cliente não faz parte da avaliação, apenas os componentes do servidor. O formato padrão dessas mensagens será detalhado no anexo 1.

## Como proceder

Por favor, realize o FORK desse repositório e implemente sua solução no FORK em seu repositório GItHub, ao final, notifique da conclusão para que possamos analisar o código implementado.

Lembre de deixar todas as orientações necessárias para executar o seu código.

### Tarefas bônus

- Tarefa Bônus 1 - Integração com sistemas externos
  - Criar uma Facade/Client Fake que retorna aleátoriamente se um CPF recebido é válido ou não.
  - Caso o CPF seja inválido, a API retornará o HTTP Status 404 (Not found). Você pode usar geradores de CPF para gerar CPFs válidos
  - Caso o CPF seja válido, a API retornará se o usuário pode (ABLE_TO_VOTE) ou não pode (UNABLE_TO_VOTE) executar a operação. Essa operação retorna resultados aleatórios, portanto um mesmo CPF pode funcionar em um teste e não funcionar no outro.

```
// CPF Ok para votar
{
    "status": "ABLE_TO_VOTE
}
// CPF Nao Ok para votar - retornar 404 no client tb
{
    "status": "UNABLE_TO_VOTE
}
```

Exemplos de retorno do serviço

### Tarefa Bônus 2 - Performance

- Imagine que sua aplicação possa ser usada em cenários que existam centenas de
  milhares de votos. Ela deve se comportar de maneira performática nesses
  cenários
- Testes de performance são uma boa maneira de garantir e observar como sua
  aplicação se comporta

### Tarefa Bônus 3 - Versionamento da API

○ Como você versionaria a API da sua aplicação? Que estratégia usar?

## O que será analisado

- Simplicidade no design da solução (evitar over engineering)
- Organização do código
- Arquitetura do projeto
- Boas práticas de programação (manutenibilidade, legibilidade etc)
- Possíveis bugs
- Tratamento de erros e exceções
- Explicação breve do porquê das escolhas tomadas durante o desenvolvimento da solução
- Uso de testes automatizados e ferramentas de qualidade
- Limpeza do código
- Documentação do código e da API
- Logs da aplicação
- Mensagens e organização dos commits

## Dicas

- Teste bem sua solução, evite bugs
- Deixe o domínio das URLs de callback passiveis de alteração via configuração, para facilitar
  o teste tanto no emulador, quanto em dispositivos fisicos.
  Observações importantes
- Não inicie o teste sem sanar todas as dúvidas
- Iremos executar a aplicação para testá-la, cuide com qualquer dependência externa e
  deixe claro caso haja instruções especiais para execução do mesmo
  Classificação da informação: Uso Interno

## Anexo 1

### Introdução

A seguir serão detalhados os tipos de tela que o cliente mobile suporta, assim como os tipos de campos disponíveis para a interação do usuário.

### Tipo de tela – FORMULARIO

A tela do tipo FORMULARIO exibe uma coleção de campos (itens) e possui um ou dois botões de ação na parte inferior.

O aplicativo envia uma requisição POST para a url informada e com o body definido pelo objeto dentro de cada botão quando o mesmo é acionado. Nos casos onde temos campos de entrada
de dados na tela, os valores informados pelo usuário são adicionados ao corpo da requisição. Abaixo o exemplo da requisição que o aplicativo vai fazer quando o botão “Ação 1” for acionado:

```
POST http://seudominio.com/ACAO1
{
    “campo1”: “valor1”,
    “campo2”: 123,
    “idCampoTexto”: “Texto”,
    “idCampoNumerico: 999
    “idCampoData”: “01/01/2000”
}
```

Obs: o formato da url acima é meramente ilustrativo e não define qualquer padrão de formato.

### Tipo de tela – SELECAO

A tela do tipo SELECAO exibe uma lista de opções para que o usuário.

O aplicativo envia uma requisição POST para a url informada e com o body definido pelo objeto dentro de cada item da lista de seleção, quando o mesmo é acionado, semelhando ao funcionamento dos botões da tela FORMULARIO.

# desafio-votacao
