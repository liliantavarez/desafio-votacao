package com.db.api.controllers;

import com.db.api.stubs.PautaStub;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static com.db.api.SqlProvider.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB)
class PautaControllerTest {

    private final String URL = "/api/v1/pautas";
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    @DisplayName("Deve cadastrar pauta com sucesso")
    void deveCadastrarPautaComSucesso() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(PautaStub.gerarPautaDtoValida())
                .when()
                .post(URL + "/cadastrar")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.CREATED.value())
                .body("titulo", equalTo(PautaStub.gerarPautaDtoValida().getTitulo()))
                .body("descricao", equalTo(PautaStub.gerarPautaDtoValida().getDescricao()));
    }

    @Test
    @DisplayName("Deve retornar um exceção ao tentar criar pauta com título em branco")
    void testCriarPautaComTituloEmBranco() {
        given()
                .contentType("application/json")
                .body(PautaStub.gerarPautaDtoTituloVazio())
                .when()
                .post(URL + "/cadastrar")
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body("mensagem", equalTo("Erro de validação"))
                .body("detalhes", hasItem("Por favor, informe o titulo da pauta."));
    }
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = inserirPauta)
    @DisplayName("Deve buscar uma pauta pelo seu id com sucesso")
    void testBuscarPautaPorID() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + "/{id}", 1L)
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("titulo", equalTo("Novas funcionalidades"));
    }
    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB)
    @DisplayName("Deve retornar uma exceção ao tentar buscar um pauta por um id inexistente no banco")
    void testBuscarPautaPorIDInexistente() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + "/{id}", 1L)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .body("mensagem", equalTo("Registro não encontrado"))
                .body("detalhes", hasItem("A pauta requerida não foi encontrado!"));
    }
}
