import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;


public class PraktikumTests {
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
                .then().assertThat().body("data.name", equalTo("Василий Васильев"));
    }

    @Test
    public void checkUserNameAndPrintResponseBody() {

        // отправляет запрос и сохраняет ответ в переменную response, экзмепляр класса Response
        Response response = given().auth().oauth2(token)
                .get("/api/users/me");
        // проверяет, что в теле ответа ключу name соответствует нужное имя пользователя
        response.then().assertThat().body("data.name", equalTo("Василий Васильев"));
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
    public void createNewPlaceAndCheckResponse() {
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
    @Test
    public void serializationTest(){
        Card card = new Card("Интересное место", "https://code.s3.yandex.net/qa-automation-engineer/java/files/paid-track/sprint1/photoSelenide.jpg");
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .and()
// сюда передали созданный обьект с нужными значениями полей
                .body(card)
                .when()
                .post("/api/cards")
                .then()
                .statusCode(201);
    }
    @Test
public void arraySerializationTest() {

        List<Card> cards = new ArrayList<Card>();
// добавили элементы
        cards.add(new Card("Рио де Жанейро", "линк1"));
        cards.add(new Card("Исилькуль", "линк2"));
        cards.add(new Card("Уругвай", "линк3"));

        given()
                .header("Content-type", "application/json")
                .auth().oauth2(token)
                .and()
// сюда передали созданный обьект с нужными значениями полей
                .body(cards)
                .when()
                .post("/api/cards")
                .then()
                // бэк не умеет принимать массив, поэтому статус код 400
                .statusCode(400);
    }
    @Test
    public void createNewCard2() {
        for (int i = 0; i < 10; i++) {
            // формат %s-%d: s - аргумент Москва, d - переменная i
            Card card = new Card(String.format("%s-%d", "Москва", i),
                    "https://code.s3.yandex.net/qa-automation-engineer/java/files/paid-track/sprint1/photoSelenium.jpg"); // экземпляр класса Card со значениями полей

            given()
                    .header("Content-type", "application/json") // передача Content-type в заголовке для указания типа файла
                    .auth().oauth2(token) // передача токена для аутентификации
                    .and()
                    .body(card) // передача объекта с данными
                    .when()
                    .post("/api/cards") // отправка POST-запроса
                    .then().statusCode(201); // проверка кода ответа
        }
    }
}