package src;

public enum Combinaison {
    CARTE_HAUTE(1), PAIRE(100), DEUX_PAIRES(200), BRELAN(300), SUITE(400),
    COULEUR(500), FULL(600), CARRE(700), QUINTE_FLUSH(800), QUINTE_FLUSH_ROYALE(900);

    private final int multiplicateur;

    Combinaison(int multiplicateur) {
        this.multiplicateur = multiplicateur;
    }

    public int getMultiplicateur() {
        return multiplicateur;
    }
}
