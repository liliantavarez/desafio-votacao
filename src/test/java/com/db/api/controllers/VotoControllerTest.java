package com.db.api.controllers;

import com.db.api.stubs.VotoStub;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Options;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Objects;

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
@AutoConfigureWireMock(port = Options.DYNAMIC_PORT)
class VotoControllerTest {

    private final String URL = "/api/v1/votos";

    @Autowired
    private Environment environment;

    private int getServerPort() {
        return Integer.parseInt(Objects.requireNonNull(environment.getProperty("local.server.port")));
    }

    @BeforeEach
    void setup() {
        RestAssured.port = getServerPort();
    }

    @Test
    @DisplayName("Deve registrar voto de um associado valido em uma sessão existente com sucesso")
    void deveRegistrarVotoComSucesso() {
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/5ae973d7a997af13f0aaf2bf60e65803/9/44271476072"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"situacao\":\"Regular\"}")));

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
