package abuldovi.telegram.telegramApp.enums;

public enum Categories {
    RESTAURANT("Restaurant"),
    TRANSPORT("Transport"),
    RELAX("Relax"),
    FOOD("Food"),
    EDUCATION("Education"),
    OTHER("Other");

    public final String label;

    Categories(String laber) {
        this.label = laber;
    }
}
