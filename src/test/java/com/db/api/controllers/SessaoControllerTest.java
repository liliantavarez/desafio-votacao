package com.db.api.controllers;

import com.db.api.stubs.SessaoStub;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import java.util.Objects;

import static com.db.api.SqlProvider.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB)
class SessaoControllerTest {

    private final String URL = "/api/v1/sessoes";

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
    @DisplayName("Deve iniciar uma sessão de votação com sucesso")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = inserirPauta)
    void deveIniciarSessaoComSucesso() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(SessaoStub.gerarSessaoRequest())
                .when()
                .post(URL)
                .then()
                .contentType(ContentType.TEXT)
                .statusCode(HttpStatus.CREATED.value())
                .body(equalTo("Iniciada votação da pauta: " + SessaoStub.gerarSessaoDtoValida().getPauta().getTitulo()));
    }

    @Test
    @DisplayName("Deve retornar um exceção ao iniciar uma sessão de votação com uma data de encerramento invalida")
    void testIniciarSessaoComDataDeEncerramentoAnteriorADataAtual() {
        given()
                .contentType("application/json")
                .body(SessaoStub.gerarSessaoDtoDataEncerramentoInvalida())
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body("mensagem", equalTo("Erro de validação"))
                .body("detalhes", hasItem("deve ser uma data futura"));
    }

    @Test
    @DisplayName("Deve retornar um exceção ao iniciar uma sessão para uma pauta inexistente")
    void testIniciarSessaoPautaInexistente() {
        given()
                .contentType("application/json")
                .body(SessaoStub.gerarSessaoDtoValida())
                .when()
                .post(URL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("mensagem", equalTo("Registro não encontrado"))
                .body("detalhes", hasItem("A pauta requerida não foi encontrado!"));
    }

    @Test
    @DisplayName("Deve obter resultado de sessao com votos favoráveis")
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = inserirVoto),
    })
    void testObterResultadoSessaoAprovada() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + "/{id}/resultado", 1)
                .then().log().all()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body("resultadoSessao", equalTo("APROVADA"));
    }

    @Test
    @DisplayName("Deve obter resultado de sessao com votos contrários")
    @SqlGroup({
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB),
            @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = inserirVoto),
    })
    void testObterResultadoSessaoReprovada() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + "/{id}/resultado", 2)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body("resultadoSessao", equalTo("REPROVADA"));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = inserirPauta)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = inserirSessao)
    @DisplayName("Deve buscar uma sessão pelo seu id com sucesso")
    void testBuscarSessãodoPorID() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + "/{id}", 2L)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("pauta.titulo", equalTo("Planejamento segundo semestre"))
                .body("resultadoSessao", equalTo("APROVADA"));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB)
    @DisplayName("Deve retornar uma exceção ao tentar buscar uma sessão por um id inexistente no banco")
    void testBuscarSessaoPorIDInexistente() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + "/{id}", 1L)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .body("mensagem", equalTo("Registro não encontrado"))
                .body("detalhes", hasItem("Sessão não encontrada."));
    }
}
