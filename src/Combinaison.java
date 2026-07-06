package src;

public enum Combinaison {
    PAIRE(100), DEUX_PAIRES(200), BRELAN(300), SUITE(400),
    COULEUR(500), FULL(600), CARRE(700);

    private final int multiplicateur;

    Combinaison(int multiplicateur) {
        this.multiplicateur = multiplicateur;
    }

    public int getMultiplicateur() {
        return multiplicateur;
    }
}
