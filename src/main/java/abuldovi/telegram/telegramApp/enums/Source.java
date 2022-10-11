package abuldovi.telegram.telegramApp.enums;

public enum Source {

        CASH("Cash"),
        VTB("VTB"),
        SBER("SBER"),
        VTBMIR("VTB MIR"),
        TINKOFF("Tinkoff"),
        OTHER("Other");
        public final String label;

        Source(String label) {
            this.label = label;
        }
    }

