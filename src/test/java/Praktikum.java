import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;


public class Praktikum {
    private final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2NDAxOTE0YTI0OGQ0NTAwMzYyN2Y2MzEiLCJpYXQiOjE2ODE1NTQyODksImV4cCI6MTY4MjE1OTA4OX0.-qW0pQOBVqMkwOtHY5nI1OxdN9cHxJpEmvd9yuzlA58";
    // аннотация Before показывает, что метод будет выполняться перед каждым тестовым методом
    @Before
    public void setUp() {
        // эндпоинты лучше выносить в метод before
        // если в классе будет несколько тестов, указывать её придётся только один раз
        RestAssured.baseURI = "https://qa-mesto.praktikum-services.ru";
    }

    // создаём метод автотеста
    @Test
    public void getMyInfoStatusCode() {
        // метод given() помогает сформировать запрос
        given()
                // указываем протокол и данные авторизации
                .auth().oauth2(token)
                // отправляем GET-запрос с помощью метода get, недостающую часть URL (ручку) передаём в него в качестве параметра
                .get("/api/users/me")
                // проверяем, что статус-код ответа равен 200
                .then().statusCode(200);
    }

    @Test
    public void checkUserName() {
        given()
                // указываем протокол и данные авторизации
                .auth().oauth2(token)
                // отправляем GET-запрос с помощью метода get, недостающую часть URL (ручку) передаём в него в качестве параметра
                .get("/api/users/me")
                // проверяем: что имя пользователя равно "Жак-Ив Кусто"
                .then().assertThat().body("data.name", equalTo("Жак-Ив Кусто"));
    }

    @Test
    public void checkUserNameAndPrintResponseBody() {

        // отправляет запрос и сохраняет ответ в переменную response, экзмепляр класса Response
        Response response = given().auth().oauth2(token)
                .get("/api/users/me");
        // проверяет, что в теле ответа ключу name соответствует нужное имя пользователя
        response.then().assertThat().body("data.name", equalTo("Жак-Ив Кусто"));
        // выводит тело ответа на экран
        System.out.println(response.body().asString());
    }
    @Test
    public void addNewCard() {
        File json = new File("src/test/resources/newCard.json");
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .and()
                .body(json)
                .when()
                .post("/api/cards")
                .then().statusCode(201);
    }
    @Test
    public void createNewPlaceAndCheckResponse(){
        File json = new File("src/test/resources/newCard.json");
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(token)
                        .and()
                        .body(json)
                        .when()
                        .post("/api/cards");
        response.then().assertThat().body("data._id", notNullValue())
                .and()
                .statusCode(201);
    }
}