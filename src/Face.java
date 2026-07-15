package src;

public enum Face {

    AS(1, "1"), DEUX(2, "2"), TROIS(3, "3"), QUATRE(4, "4"), CINQ(5, "5"), SIX(6, "6"), SEPT(7, "7"), HUIT(8, "8"), NEUF(9, "9"), DIX(10, "10"), VALET(11, "Valet"), CAVALIER(12, "Cavalier"), DAME(13, "Dame"), ROI(14, "Roi");

    private final int valeur;
    private final String nom;

    Face(int valeur, String nom) {
        this.valeur = valeur;
        this.nom = nom;
    }

    public int getValeur() {
        return valeur;
    }

    @Override
    public String toString() {
        return nom;
    }
    
    
}
