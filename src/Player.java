package src;

import java.util.ArrayList;
import java.util.Collection;

public class Player {
    
    private Card[] cartes;
    private Mise mise;
    private ArrayList<Integer> pot;

    public Player(boolean bool) {
        this.cartes = new Card[2];
        this.mise = new Mise(bool);
        this.pot = new ArrayList<>();
    }

    public void petiteBlinde() {
        // mettre dans le pot
        this.pot.add(this.mise.getMise(0));
        //enlever de mise
        this.mise.removeFirst();
    }

    public void ajouterAuPot(int valeur) {
        if (!this.mise.contains(valeur)) {
            throw new IllegalArgumentException("Valeur " + valeur + " absente de la mise du joueur");
        }
        this.pot.add(valeur);
        this.mise.remove(valeur);
    }

    public String potToString() {
        String output = "Votre pot est de " + ": ";
        for(int i = 0; i < pot.size(); i++) {
            output += pot.get(i) + " ";
        }
        return output;
    }

    public Card[] getCartes() {
        return this.cartes;
    }

    public Mise getMise() {
        return this.mise;
    }

    public int totalDeMise() {
        return this.mise.total();
    }

    public int totalDuPot() {
        int counter = 0;
        for(int i = 0; i < this.pot.size(); i++) {
            counter = counter + this.pot.get(i);
        }
        return counter;
    }

    public ArrayList<Integer> getPot() {
        return this.pot;
    }

    public void Viderpot() {
        this.pot.clear();
    }

    public String toString() {
        return "Vous avez en main : " + cartes[0] + " " + cartes[1] + "\nVotre Mise : " + mise.toString(); 
    }

    public void setCartes(Card c1, Card c2) {
        cartes[0] = c1;
        cartes[1] = c2;
    }

    public void ajouterAuPot(Collection<Integer> valeurs) {
        this.pot.addAll(valeurs);
    }

    // adversaire perd son pot et tout retourner dans la Mise de Player
    public void recupererPots(Player adversaire) {
        this.getMise().addAll(adversaire.getPot());
        this.getMise().addAll(this.getPot());
        adversaire.Viderpot();
        this.Viderpot();
        this.getMise().sort();
    }

}
