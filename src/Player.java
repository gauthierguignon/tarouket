package src;

import java.util.ArrayList;

public class Player {
    
    private int num;
    private Card[] cartes;
    private Mise mise;
    private ArrayList<Integer> pot;

    public Player(int num, boolean bool) {
        this.num = num;
        this.cartes = new Card[2];
        this.mise = new Mise(bool);
        this.pot = new ArrayList<>();
    }

    public void petiteBlinde() {
        // mettre dans le pot
        this.pot.add(this.mise.getMise(0));
        //enlever de mise
        this.mise.mise.remove(0);
    }

    public void ajouterAuPot(int valeur) {
        // attention à bien vérifier le contenu de valeur avant de passer en paramètre !
        this.pot.add(valeur);
        //enlever de mise
        int index = this.mise.mise.indexOf(valeur);
        this.mise.mise.remove(index);
    }

    public String votrePotToToString() {
        String output = "Votre pot est de " + ": ";
        for(int i = 0; i < pot.size(); i++) {
            output += pot.get(i) + " ";
        }
        return output;
    }

    public String croupierPotToToString() {
        String output = "Le pot du croupier " + ": ";
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
        return "Vous avez en main : " + cartes[0] + " " + cartes[1] + "\nMise : " + mise.mise.toString(); 
    }

    public void setCartes(Card c1, Card c2) {
        cartes[0] = c1;
        cartes[1] = c2;
    }



}
