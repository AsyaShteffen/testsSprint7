import org.junit.Test;

public class Card {
    //задаем поля, в которые будем записывать поля json
    private String name;
    private String link;

    // создаем конструктор, через который будем создавать объект с полями
    public Card(String name, String link) {
        this.name = name;
        this.link = link;
    }
    //конструктор без параметров для сериализации
    public Card() {
    }
    // геттер и сеттер поля link
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    // геттер и сеттер поля name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
