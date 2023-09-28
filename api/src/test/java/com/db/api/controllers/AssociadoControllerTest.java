package com.db.api.controllers;

import com.db.api.stubs.AssociadoStub;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static com.db.api.SqlProvider.inserirAssociado;
import static com.db.api.SqlProvider.resetarDB;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB)
class AssociadoControllerTest {

    private final String URL = "/api/v1/associados";
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    @DisplayName("Deve registrar um associado com sucesso")
    void deveRegistrarAssociadoComSucesso() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(AssociadoStub.gerarAssociadoDtoValida())
                .when()
                .post(URL + "/cadastrar")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.CREATED.value())
                .body("nome", equalTo(AssociadoStub.gerarAssociadoDtoValida().getNome()));
    }

    @Test
    @DisplayName("Deve retornar um exceção ao tentar registrar associado com cpf inválido")
    void testRegistrarAssociadoCpfInvalido() {
        given()
                .contentType("application/json")
                .body(AssociadoStub.gerarAssociadoDtoCpfInvalida())
                .when()
                .post(URL + "/cadastrar")
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body("mensagem", equalTo("Erro de validação"))
                .body("detalhes", hasItem("número do registro de contribuinte individual brasileiro (CPF) inválido"));
    }
    @Test
    @DisplayName("Deve retornar um exceção ao tentar registrar associado sem informar seu nome")
    void testRegistrarAssociadoNomeVazio() {
        given()
                .contentType("application/json")
                .body(AssociadoStub.gerarAssociadoDtoNomeVazio())
                .when()
                .post(URL + "/cadastrar")
                .then()
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body("mensagem", equalTo("Erro de validação"))
                .body("detalhes", hasItem("Por favor informe o nome do associado!"))
                .body("detalhes", hasItem("tamanho deve ser entre 3 e 150"));
    }
    @Test
    @DisplayName("Deve retornar um exceção ao tentar registrar associado com cpf já cadastrado no sistema")
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = inserirAssociado)
    void testRegistrarAssociadoMaisDeUmaVez() {
        given()
                .contentType("application/json")
                .body(AssociadoStub.gerarAssociadoDtoValida())
                .when()
                .post(URL + "/cadastrar")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("mensagem", equalTo("CPF já cadastrado"))
                .body("detalhes", hasItem("O associado com esse cpf já está registrado no sistema!"));
    }
}
