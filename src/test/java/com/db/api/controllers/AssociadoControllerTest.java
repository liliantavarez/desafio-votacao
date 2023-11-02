package com.db.api.controllers;

import com.db.api.stubs.AssociadoStub;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.Objects;

import static com.db.api.SqlProvider.inserirAssociado;
import static com.db.api.SqlProvider.resetarDB;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB)
class AssociadoControllerTest {

    private final String URL = "/api/v1/associados";

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
    @DisplayName("Deve registrar um associado com sucesso")
    void deveRegistrarAssociadoComSucesso() {
        given()
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

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB)
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = inserirAssociado)
    @DisplayName("Deve buscar um associado pelo seu id com sucesso")
    void testBuscarAssociadoPorID() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + "/{id}", 1L)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("nome", equalTo("Carla Silva"))
                .body("cpf", equalTo("44271476072"));
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = resetarDB)
    @DisplayName("Deve retornar uma exceção ao tentar buscar um associado por um id inexistente no banco")
    void testBuscarAssociadoPorIDInexistente() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(URL + "/{id}", 1L)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE).log().all()
                .body("mensagem", equalTo("Registro não encontrado"))
                .body("detalhes", hasItem("Associado não encontrada."));
    }
}
