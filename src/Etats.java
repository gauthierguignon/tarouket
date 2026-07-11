package src;

public enum Etats {

    PRE_FLOP(0, ""), FLOP(3, "du Flop"), TURN(1, "du Turn"), RIVER(1, "de la Rivière");

    private final int nbCartes;
    private final String nom;

    private Etats(int nbCartes, String nom) {
        this.nbCartes = nbCartes;
        this.nom = nom;
    }

    public int getValeur() {
        return this.nbCartes;
    }

    public String getNom() {
        return this.nom;
    }

}
