package src;

public enum Face {

    AS(1), DEUX(2), TROIS(3), QUATRE(4), CINQ(5), SIX(6), SEPT(7), HUIT(8), NEUF(9), DIX(10), VALET(11), CAVALIER(12), DAME(13), ROI(14);

    private final int valeur;

    Face(int valeur) {
        this.valeur = valeur;
    }

    public int getValeur() {
        return valeur;
    }


    
}
