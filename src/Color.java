package src;

public enum Color {
    CARREAU("♦"), PIQUE("♠"), COEUR("♥"), TREFLE("☘");

    private final String symbole;

    Color(String symbole) {
        this.symbole = symbole;
    }

    @Override
    public String toString() {
        return symbole;
    }

}