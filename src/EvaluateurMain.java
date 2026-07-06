package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

public class EvaluateurMain {

    private final ArrayList<Card> cartes;
    private final Map<Face,Integer> comptageParFaces;
    private final Map<Color,Integer> comptageParCouleurs; 


    public EvaluateurMain(ArrayList<Card> cartes){
        this.cartes = cartes;
        this.comptageParFaces = this.comptageParFace();
        this.comptageParCouleurs = this.comptageParCouleur();
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
        for (int count : this.comptageParFaces.values()) {
            if(count == 2) return true;
        }
        return false;
    }

    // Renvoie True si la main possède exactement 2 paires
    private boolean possedeDeuxPaires() {
        int counter = 0;
        for (int count : this.comptageParFaces.values()) {
            if(count == 2) {
                counter ++;
            }
        }
        return counter == 2;
    }

    // Renvoie True si la main possède exactement un Brelan
    private boolean possedeBrelan() {
        if(this.possedeFull()) return false;
        for (int count : this.comptageParFaces.values()) {
            if(count == 3) {
                return true;
            }
        }
        return false;
    }

    // Renvoie True si la main possède exactement une Suite
    private boolean possedeSuite() {
        return possedeSuite(this.cartes);
    }
    
    // Utile pour tester la QuinteFlush
    private boolean possedeSuite(ArrayList<Card> cartes) {
        Collections.sort(cartes);
        int consecutives = 1;
        for (int i = 1; i<cartes.size(); i++) {
            int valeurCartePrécédente = cartes.get(i-1).getFace().getValeur();
            int valeurCarteCourante = cartes.get(i).getFace().getValeur();        
            if (valeurCarteCourante == valeurCartePrécédente) {
                continue; // la suite doit être détectée même avec des doublons
            }
            if (valeurCartePrécédente + 1 == valeurCarteCourante) {
                consecutives++;
            } else {
            consecutives = 1;
            }
        }
        return consecutives >= 5;
    }

    // Renvoie True si la main possède une Couleur
    private boolean possedeCouleur() {
        for (int count : this.comptageParCouleurs.values()) {
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }

    // Renvoie True si la main possède un Full
    private boolean possedeFull() {
        boolean brelan = false;
        boolean paire = false;
        for (int count : this.comptageParFaces.values()) {
            if (count == 3) brelan = true;
            if (count == 2) paire = true;
        }
        return brelan && paire;
    }

    // Renvoie True si la main possède un Carré
    private boolean possedeCarre() {
        for (int count : this.comptageParFaces.values()) {
            if(count == 4) {
                return true;
            }
        }
        return false;
    }

    // Renvoie True si la main possède une Suite et une Couleur
    private boolean possedeQuinteFlush() {
        return this.getQuinteFlushColor() != null;
    }

    // Renvoie la couleur de la Quinte-Flush ou null
    private Color getQuinteFlushColor() {
        Color[] colors = Color.values();
        for(int i = 0; i < 4; i++) {
            ArrayList<Card> cartesParCouleurs = new ArrayList<>();
            for(int j = 0; j < this.cartes.size(); j++) {
                if(this.cartes.get(j).getColor() == colors[i]) cartesParCouleurs.add(this.cartes.get(j));
            }
            if(this.possedeSuite(cartesParCouleurs)) return colors[i];
        }
        return null;
    }

    private boolean possedeQuinteFlushRoyale() {
        if(!this.possedeQuinteFlush()) return false;

        ArrayList<Integer> valeurs = new ArrayList<>(Arrays.asList(10, 11, 12, 13, 14));
        Color color = this.getQuinteFlushColor();

        for(Card carte : this.cartes) {
            if(carte.getColor() == color) {
                valeurs.remove(Integer.valueOf(carte.getFace().getValeur()));
            }
        }
        return valeurs.isEmpty();
    }

    private void trierMain() {
        Collections.sort(this.cartes);
    }

    public Combinaison meilleureCombinaison() {
        if(this.possedeQuinteFlushRoyale()) return Combinaison.QUINTE_FLUSH_ROYALE;
        if(this.possedeQuinteFlush()) return Combinaison.QUINTE_FLUSH;
        if (this.possedeCarre()) return Combinaison.CARRE;
        if (this.possedeFull()) return Combinaison.FULL;
        if (this.possedeCouleur()) return Combinaison.COULEUR;
        if (this.possedeSuite()) return Combinaison.SUITE;
        if (this.possedeBrelan()) return Combinaison.BRELAN;
        if (this.possedeDeuxPaires()) return Combinaison.DEUX_PAIRES;
        if (this.possedeUnePaire()) return Combinaison.PAIRE;
        return Combinaison.CARTE_HAUTE;
}

    public static void main(String[] args) {

        ArrayList<Card> main = new ArrayList<>();
        EvaluateurMain eval = new EvaluateurMain(main);
        int counter = 0;

        // // TEST : Main élatoire
        while (!eval.possedeQuinteFlushRoyale()) { // Changer la condition au besoin
            counter ++;
            main.clear();
            Deck deck = new Deck();
            deck.shuffle();
            for(int i = 0; i < 7; i++) {
                main.add(deck.drawRandomCard());
            }
            eval = new EvaluateurMain(main);
       }

        // // TEST : Main spécifique
        // main.add(new Card(Color.CARREAU, Face.DEUX));
        // main.add(new Card(Color.CARREAU, Face.HUIT));
        // main.add(new Card(Color.CARREAU, Face.NEUF));
        // main.add(new Card(Color.COEUR, Face.DIX));
        // main.add(new Card(Color.CARREAU, Face.DIX));
        // main.add(new Card(Color.CARREAU, Face.VALET));
        // main.add(new Card(Color.CARREAU, Face.CAVALIER));
        // eval = new EvaluateurMain(main);


        
        //Affichage
        eval.trierMain();
        System.out.println(counter);
        System.out.println(main);
        System.out.println(eval.meilleureCombinaison());
        System.out.println("Paire : \t" + eval.possedeUnePaire());
        System.out.println("Deux Paires : \t" + eval.possedeDeuxPaires());
        System.out.println("Brelan : \t" + eval.possedeBrelan());
        System.out.println("Suite : \t" + eval.possedeSuite());
        System.out.println("Couleur : \t" + eval.possedeCouleur());
        System.out.println("Full : \t\t" + eval.possedeFull());
        System.out.println("Carre : \t" + eval.possedeCarre());
        System.out.println("Quinte Flush : \t" + eval.possedeQuinteFlush());
        System.out.println("Quinte Flush Royale \t " + eval.possedeQuinteFlushRoyale());



    }

}