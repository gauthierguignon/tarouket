package src;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class EvaluateurMain {

    private final ArrayList<Card> cartes;

    public EvaluateurMain(ArrayList<Card> cartes){
        this.cartes = cartes;
    }

    // Compte le nombre de face dans les cartes
    private Map<Face, Integer> comptageParFace() {
        Map<Face,Integer> compte = new EnumMap<>(Face.class);
        for(int i = 0; i < this.cartes.size(); i++){
            Face face = this.cartes.get(i).getFace();
            if (compte.containsKey(face)) {
                compte.put(face, compte.get(face) + 1);
            } else {
                compte.put(face, 1);
            }
        }
        return compte;
    }

    // Compte le nombre de couleurs dans les cartes
    private Map<Color, Integer> comptageParCouleur() {
        Map<Color, Integer> compte = new EnumMap<>(Color.class);
        for(int i = 0; i < this.cartes.size(); i++){
            Color color = this.cartes.get(i).getColor();
            if(compte.containsKey(color)) {
                compte.put(color, compte.get(color) +1);
            } else {
                compte.put(color, 1);
            }
        }
        return compte;
    }

    // Renvoie True si la main possède exactement 1 paire
    private boolean possedeUnePaire() {
        if(this.possedeFull()) return false;
        if(this.possedeDeuxPaires()) return false;
        Map<Face,Integer> face = this.comptageParFace();
        for (int count : face.values()) {
            if(count == 2) return true;
        }
        return false;
    }

    // Renvoie True si la main possède exactement 2 paires
    private boolean possedeDeuxPaires() {
        Map<Face,Integer> face = this.comptageParFace();
        int counter = 0;
        for (int count : face.values()) {
            if(count == 2) {
                counter ++;
            }
        }
        return counter == 2;
    }


    // Renvoie True si la main possède exactement un Brelan
    private boolean possedeBrelan() {
        if(this.possedeFull()) return false;
        if (this.possedeCarre()) return false;
        Map<Face,Integer> face = this.comptageParFace();
        for (int count : face.values()) {
            if(count == 3) {
                return true;
            }
        }
        return false;
    }

    // Renvoie True si la main possède un Carré
    private boolean possedeCarre() {
        Map<Face,Integer> face = this.comptageParFace();
        for (int count : face.values()) {
            if(count == 4) {
                return true;
            }
        }
        return false;
    }

    // Renvoie True si la main possède une couleur
    private boolean possedeCouleur() {
        Map<Color, Integer> color = this.comptageParCouleur();
        for (int count : color.values()) {
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }

    // Renvoie True si la main possède un full
    private boolean possedeFull() {
        Map<Face,Integer> face = this.comptageParFace();
        boolean brelan = false;
        boolean paire = false;
        for (int count : face.values()) {
            if (count == 3) brelan = true;
            if (count == 2) paire = true;
        }
        return brelan && paire;
    }



    public static void main(String[] args) {

        ArrayList<Card> main = new ArrayList<>();
        EvaluateurMain eval = new EvaluateurMain(main);
        int counter = 0;

        // TEST : Main élatoire
        while (!eval.possedeCarre()) { // Changer la condition au besoin
            counter ++;
            main.clear();
            Deck deck = new Deck();
            deck.shuffle();
            for(int i = 0; i < 7; i++) {
                main.add(deck.drawRandomCard());
            }
        }
        
        //Affichage
        System.out.println(counter);
        System.out.println(main);
        System.out.println("Paire : \t" + eval.possedeUnePaire());
        System.out.println("Deux Paires : \t" + eval.possedeDeuxPaires());
        System.out.println("Brelan : \t" + eval.possedeBrelan());
        System.out.println("Suite ...");
        System.out.println("Couleur : \t" + eval.possedeCouleur());
        System.out.println("Full : \t\t" + eval.possedeFull());
        System.out.println("Carre : \t" + eval.possedeCarre());



    }

}