package com.db.api.controllers;

import com.db.api.stubs.VotoStub;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static com.db.api.SqlProvider.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = inserirPauta),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = inserirSessao),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = inserirAssociado),
})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VotoControllerTest {

    private final String URL = "/api/v1/votos";
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    @DisplayName("Deve registrar voto de um associado valido em uma sessão existente com sucesso")
    void deveRegistrarVotoComSucesso() {
        given()
                .contentType(ContentType.JSON)
                .body(VotoStub.gerarVotoDtoValida())
                .when()
                .post(URL + "/salvar")
                .then()
                .contentType(ContentType.TEXT)
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("Voto salvo com sucesso!"));
    }
    @Test
    @DisplayName("Deve retornar um exceção ao tentar registrar voto em uma sessão inexistente")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB)
    void testRegistrarVotoSessaoInexistente() {
        given()
                .contentType(ContentType.JSON)
                .body(VotoStub.gerarVotoSessaoInvalida())
                .when()
                .post(URL + "/salvar")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("mensagem", equalTo("Registro não encontrado"))
                .body("detalhes", hasItem("Sessão não encontrada."));
    }
    @Test
    @DisplayName("Deve retornar um exceção ao tentar registrar voto de um associando o inexistente")
    void testRegistrarVotoAssociadoInexistente() {
        given()
                .contentType(ContentType.JSON)
                .body(VotoStub.gerarVotoAssociadoInvalida())
                .when()
                .post(URL + "/salvar")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("mensagem", equalTo("Registro não encontrado"))
                .body("detalhes", hasItem("Associado não encontrada."));
    }

    @Test
    @DisplayName("Deve retornar um exceção ao tentar registrar em uma sessão já encerrada")
    void testRegistrarVotoSessaoEncerrada() {
        given()
                .contentType(ContentType.JSON)
                .body(VotoStub.gerarVotoSessaoEncerrada())
                .when()
                .post(URL + "/salvar")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("mensagem", equalTo("Sessão encerrada"))
                .body("detalhes", hasItem("A sessão já foi encerrada e não aceita mais votos!"));
    }
}
