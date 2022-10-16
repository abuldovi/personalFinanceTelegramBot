package abuldovi.telegram.telegramApp.enums;

public enum Emoji {
    PENCIL(null, '\u270F'),
    CROSS_MARK(null, '\u274C'),
    HEAVY_PLUS_SIGN(null, '\u2795'),
    HOUSE('\uD83C', '\uDFE0'),
    CREDIT_CARD('\uD83D', '\uDCB3'),
    MONEY_WITH_WINGS('\uD83D', '\uDCB8'),
    CALENDAR('\uD83D', '\uDCC5'),
    BAR('\uD83D', '\uDCCA'),
    MONEY_BUG('\uD83D', '\uDCB0');


    final Character firstChar;
    final Character secondChar;

    Emoji(Character firstChar, Character secondChar) {
        this.firstChar = firstChar;
        this.secondChar = secondChar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (this.firstChar != null) {
            sb.append(this.firstChar);
        }
        if (this.secondChar != null) {
            sb.append(this.secondChar);
        }

        return sb.toString();
    }
}
