package src;

public class Card implements Comparable<Card>{

    private Color color;
    private Face face;

    public Card(Color color, Face face) {
        this.color = color;
        this.face = face;
    }

    public Card() {
        this.color = null;
        this.face = null;
    }

    public Color getColor() {
        return this.color;
    }

    public Face getFace() {
        return this.face;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    public String toString() {
        return face.toString() + color.toString();
    }

        @Override
    public int compareTo(Card autre) {
        return this.face.getValeur() - autre.face.getValeur();
    }

}

