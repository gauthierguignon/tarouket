package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
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

    // Renvoie true si la main possède une QFR
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

    // Main : "Roi, Roi, 9, 7, 3" -> {ROI:2, NEUF:1, SEPT:1, TROIS:1} -> trié : [13, 9, 7, 3]
    private List<Integer> valeursTrieesParImportance(ArrayList<Card> cinqCartes) {

        Map<Face, Integer> comptageLocal = new EnumMap<>(Face.class);
        for (Card carte : cinqCartes) {
            Face face = carte.getFace();
            if (comptageLocal.containsKey(face)) {
                comptageLocal.put(face, comptageLocal.get(face) + 1);
            } else {
                comptageLocal.put(face, 1);
            }
        }

        List<Face> faces = new ArrayList<>(comptageLocal.keySet());

        Comparator<Face> comparateur = new Comparator<Face>() {

            @Override
            public int compare(Face f1, Face f2) {

                int compte1 = comptageParFaces.get(f1);
                int compte2 = comptageParFaces.get(f2);

                // On compare d'abord le nombre d'occurrences
                if (compte1 > compte2) {
                    return -1;
                }
                if (compte1 < compte2) {
                    return 1;
                }

                // Si même nombre d'occurrences, on compare la valeur
                if (f1.getValeur() > f2.getValeur()) {
                    return -1;
                }
                if (f1.getValeur() < f2.getValeur()) {
                    return 1;
                }

                return 0;
            }
        };

        faces.sort(comparateur);

        List<Integer> valeurs = new ArrayList<>();

        for (Face face : faces) {
            valeurs.add(face.getValeur());
        }

        return valeurs;
    }

    // Calcul du kicker en base 15
    private long encoderEnBase15(List<Integer> valeurs) {
        long resultat = 0;
        for (int valeur : valeurs) {
            resultat = resultat * 15 + valeur;
        }
        return resultat;
    }

    public long score() {
        Combinaison combinaison = this.meilleureCombinaison();
        ArrayList<Card> cinqCartes = this.meilleuresCartes(combinaison);
        long scoreKickers = this.encoderEnBase15(this.valeursTrieesParImportance(cinqCartes));
        return combinaison.ordinal() * 1_000_000 + scoreKickers;
    }

    // Renvoie les 5 meilleurs cartes
    private ArrayList<Card> cartesPourCarteHaute() {
        ArrayList<Card> resultat = new ArrayList<>(this.cartes);
        resultat.sort(Collections.reverseOrder());
        int nombreACopier = Math.min(5, resultat.size());
        return new ArrayList<>(resultat.subList(0, nombreACopier));
    }

    private ArrayList<Card> cartesPourPaire() {

        // Identifier la valeur de la paire dans comptageParFace
        int valeurPaire = 0;
        for (Map.Entry<Face, Integer> e : this.comptageParFaces.entrySet()) {
            if (e.getValue() == 2) valeurPaire = e.getKey().getValeur();
        }
        // On isole la paire des autres cartes
        ArrayList<Card> resultat = new ArrayList<>();
        ArrayList<Card> kickers = new ArrayList<>();
        for (Card carte : this.cartes) {
            if (carte.getFace().getValeur() == valeurPaire) {
                resultat.add(carte);
            } else {
                kickers.add(carte);
            }
        }
        // On ajoute les kickers les plus hauts à resultat
        kickers.sort(Collections.reverseOrder());
        int nombreDeKickersAPrendre = Math.min(3, kickers.size());
        for (int i = 0; i < nombreDeKickersAPrendre; i++) {
            resultat.add(kickers.get(i));
        }
        return resultat;
    }

    private ArrayList<Card> cartesPourDeuxPaires() {

        // On trouve la valeur de la paire haute et basse
        List<Integer> valeursPaires = new ArrayList<>();
        for (Map.Entry<Face, Integer> e : this.comptageParFaces.entrySet()) {
            if (e.getValue() == 2) valeursPaires.add(e.getKey().getValeur());
        }
        valeursPaires.sort(Collections.reverseOrder());
        int paireHaute = valeursPaires.get(0);
        int paireBasse = valeursPaires.get(1);

        // On met les 2 paires dans résultat
        ArrayList<Card> resultat = new ArrayList<>();
        ArrayList<Card> autres = new ArrayList<>();
        for (Card carte : this.cartes) {
            int valeur = carte.getFace().getValeur();
            if (valeur == paireHaute || valeur == paireBasse) {
                resultat.add(carte);
            } else {
                autres.add(carte);
            }
        }
        if (!autres.isEmpty()) {
            autres.sort(Collections.reverseOrder());
            resultat.add(autres.get(0)); // on n'ajoute le kicker que s'il en existe un
        }
        return resultat;
}

    private ArrayList<Card> cartesPourBrelan() {

        int valeurBrelan = 0;
        for (Map.Entry<Face, Integer> e : this.comptageParFaces.entrySet()) {
            if (e.getValue() == 3) valeurBrelan = e.getKey().getValeur();
        }
        ArrayList<Card> resultat = new ArrayList<>();
        ArrayList<Card> kickers = new ArrayList<>();
        for (Card carte : this.cartes) {
            if (carte.getFace().getValeur() == valeurBrelan) {
                resultat.add(carte);
            } else {
                kickers.add(carte);
            }
        }
        int nombreDeKickersAPrendre = Math.min(2, kickers.size());
        for (int i = 0; i < nombreDeKickersAPrendre; i++) {
            resultat.add(kickers.get(i));
        }
        return resultat;
    }

    // trouve 5 valeurs différentes qui se suivent dans la liste qu'on lui donne
    private ArrayList<Card> cartesPourSuite(ArrayList<Card> cartesDisponibles) {

        ArrayList<Card> triees = new ArrayList<>(cartesDisponibles);
        Collections.sort(triees);

        // on ignore les doublons
        List<Integer> valeursUniques = new ArrayList<>();
        Map<Integer, Card> carteParValeur = new HashMap<>();
        for (Card carte : triees) {
            int valeur = carte.getFace().getValeur();
            if (!carteParValeur.containsKey(valeur)) {
                valeursUniques.add(valeur);
                carteParValeur.put(valeur, carte);
            }
        }

        int meilleurDepart = -1;
        int consecutives = 1;
        for (int i = 1; i < valeursUniques.size(); i++) {
            if (valeursUniques.get(i) == valeursUniques.get(i - 1) + 1) {
                consecutives++;
                if (consecutives >= 5) meilleurDepart = valeursUniques.get(i);
            } else {
                consecutives = 1;
            }
        }

        ArrayList<Card> resultat = new ArrayList<>();
        for (int valeur = meilleurDepart; valeur > meilleurDepart - 5; valeur--) {
            resultat.add(carteParValeur.get(valeur));
        }
        return resultat;
    }

    private ArrayList<Card> cartesPourCouleur() {
        Color couleurGagnante = null;
        for (Map.Entry<Color, Integer> e : this.comptageParCouleurs.entrySet()) {
            if (e.getValue() >= 5) couleurGagnante = e.getKey();
        }
        ArrayList<Card> cartesCouleur = this.cartesDeCouleur(couleurGagnante);
        cartesCouleur.sort(Collections.reverseOrder());
        return new ArrayList<>(cartesCouleur.subList(0, 5));
    }

    // Utilitaire : toutes les cartes d'une couleur donnée
    private ArrayList<Card> cartesDeCouleur(Color couleur) {
        ArrayList<Card> resultat = new ArrayList<>();
        for (Card carte : this.cartes) {
            if (carte.getColor() == couleur) resultat.add(carte);
        }
        return resultat;
    }

    private ArrayList<Card> cartesPourFull() {
        int valeurBrelan = 0;
        for (Map.Entry<Face, Integer> e : this.comptageParFaces.entrySet()) {
            if (e.getValue() >= 3 && e.getKey().getValeur() > valeurBrelan) valeurBrelan = e.getKey().getValeur();
        }
        int valeurPaire = 0;
        for (Map.Entry<Face, Integer> e : this.comptageParFaces.entrySet()) {
            int valeur = e.getKey().getValeur();
            if (valeur == valeurBrelan) continue;
            if (e.getValue() >= 2 && valeur > valeurPaire) valeurPaire = valeur;
        }
        ArrayList<Card> resultat = new ArrayList<>();
        int nbBrelanAjoutees = 0;
        int nbPaireAjoutees = 0;
        for (Card carte : this.cartes) {
            int valeur = carte.getFace().getValeur();
            if (valeur == valeurBrelan && nbBrelanAjoutees < 3) {
                resultat.add(carte);
                nbBrelanAjoutees++;
            } else if (valeur == valeurPaire && nbPaireAjoutees < 2) {
                resultat.add(carte);
                nbPaireAjoutees++;
            }
        }
        return resultat;
    }

    private ArrayList<Card> cartesPourCarre() {
        int valeurCarre = 0;
        for (Map.Entry<Face, Integer> e : this.comptageParFaces.entrySet()) {
            if (e.getValue() == 4) valeurCarre = e.getKey().getValeur();
        }
        ArrayList<Card> resultat = new ArrayList<>();
        Card meilleurKicker = null;
        for (Card carte : this.cartes) {
            if (carte.getFace().getValeur() == valeurCarre) {
                resultat.add(carte);
            } else if (meilleurKicker == null || carte.getFace().getValeur() > meilleurKicker.getFace().getValeur()) {
                meilleurKicker = carte;
            }
        }
        if (meilleurKicker != null) {
            resultat.add(meilleurKicker);
        }
        return resultat;
    }

    // renvoie les 5 cartes qui constituent la meilleure combinaison
    public ArrayList<Card> meilleuresCartes(Combinaison combinaison) {
        switch (combinaison) {
            case QUINTE_FLUSH_ROYALE, QUINTE_FLUSH -> {
                return this.cartesPourSuite(this.cartesDeCouleur(this.getQuinteFlushColor()));
            }
            case CARRE ->{
                return this.cartesPourCarre();
            }
            case FULL ->{
                return this.cartesPourFull();
            }
            case COULEUR ->{
                return this.cartesPourCouleur();
            }
            case SUITE ->{
                return this.cartesPourSuite(this.cartes);
            }
            case BRELAN ->{
                return this.cartesPourBrelan();
            }
            case DEUX_PAIRES ->{
                return this.cartesPourDeuxPaires();
            }
            case PAIRE ->{
                return this.cartesPourPaire();
            }
            default ->{
                return this.cartesPourCarteHaute();
            }
        }
    }

    // Construit une phrase lisible décrivant la main, avec la hauteur qui compte
    // Le booléen affiche la hauteur pour départager
    public String description(boolean afficherHauteur) {
        Combinaison combinaison = this.meilleureCombinaison();
        ArrayList<Card> cinqCartes = this.meilleuresCartes(combinaison);
        List<Integer> valeurs = this.valeursTrieesParImportance(cinqCartes);

        switch (combinaison) {
            case PAIRE -> {
                if (afficherHauteur) {
                    return "Paire de " + this.faceParValeur(valeurs.get(0))
                        + ", hauteur " + this.faceParValeur(valeurs.get(1));
                }
                return "Paire de " + this.faceParValeur(valeurs.get(0));
            }
            case DEUX_PAIRES -> {
                if (afficherHauteur) {
                    return "Deux paires, " + this.faceParValeur(valeurs.get(0))
                        + " et " + this.faceParValeur(valeurs.get(1))
                        + ", hauteur " + this.faceParValeur(valeurs.get(2));
                }
                return "Deux paires, " + this.faceParValeur(valeurs.get(0))
                    + " et " + this.faceParValeur(valeurs.get(1));
            }
            case BRELAN -> {
                if (afficherHauteur) {
                    return "Brelan de " + this.faceParValeur(valeurs.get(0))
                        + ", hauteur " + this.faceParValeur(valeurs.get(1));
                }
                return "Brelan de " + this.faceParValeur(valeurs.get(0));
            }
            case CARRE -> {
                if (afficherHauteur) {
                    return "Carré de " + this.faceParValeur(valeurs.get(0))
                        + ", hauteur " + this.faceParValeur(valeurs.get(1));
                }
                return "Carré de " + this.faceParValeur(valeurs.get(0));
            }
            case FULL -> {
                return "Full aux " + this.faceParValeur(valeurs.get(0))
                    + " par les " + this.faceParValeur(valeurs.get(1));
            }
            default -> {
                return combinaison + ", hauteur " + this.faceParValeur(valeurs.get(0));
            }
        }
    }

    // Retrouve la Face correspondant à une valeur numérique (ex: 13 -> ROI)
    private Face faceParValeur(int valeur) {
        for (Face face : Face.values()) {
            if (face.getValeur() == valeur) {
                return face;
            }
        }
        return null; // ne devrait jamais arriver si valeur vient bien d'une carte existante
    }

    public static void main(String[] args) {

        // TESTER UNE MAIN 

        // ArrayList<Card> main = new ArrayList<>();
        // EvaluateurMain eval = new EvaluateurMain(main);
        // int counter = 0;

    //     // // TEST : une main aléatoire
    //     while (!eval.possedeQuinteFlush()) { // Changer la condition au besoin
    //         counter ++;
    //         main.clear();
    //         Deck deck = new Deck();
    //         deck.shuffle();
    //         for(int i = 0; i < 7; i++) {
    //             main.add(deck.drawRandomCard());
    //         }
    //         eval = new EvaluateurMain(main);
    //    }

        // // TEST : une main spécifique
        // main.add(new Card(Color.CARREAU, Face.DEUX));
        // main.add(new Card(Color.CARREAU, Face.HUIT));
        // main.add(new Card(Color.CARREAU, Face.NEUF));
        // main.add(new Card(Color.COEUR, Face.DIX));
        // main.add(new Card(Color.CARREAU, Face.DIX));
        // main.add(new Card(Color.CARREAU, Face.VALET));
        // main.add(new Card(Color.CARREAU, Face.CAVALIER));
        // eval = new EvaluateurMain(main);

        // //Affichage
        // eval.trierMain();
        // System.out.println(counter);
        // System.out.println(main);
        // System.out.println(eval.meilleureCombinaison());


        // TESTER 2 MAINS L'UNE CONTRE L'AUTRE

        ArrayList<Card> riviere = new ArrayList<>();
        ArrayList<Card> mainJ1 = new ArrayList<>();        
        ArrayList<Card> mainJ2 = new ArrayList<>();

        EvaluateurMain joueur1 = new EvaluateurMain(riviere);
        EvaluateurMain joueur2 = new EvaluateurMain(riviere);

        int counter = 0;

        do {
            counter ++;
            riviere.clear();
            mainJ1.clear();
            mainJ2.clear();
            Deck deck = new Deck();
            deck.shuffle();
            // cartes rivière
            for(int i = 0; i < 1; i++) {
                riviere.add(deck.drawRandomCard());
            }
            // cartes p1
            mainJ1.addAll(riviere);
            for(int i = 0; i < 2; i++) {
                mainJ1.add(deck.drawRandomCard());
            }
            // cartes p2
            mainJ2.addAll(riviere);
            for(int i = 0; i < 2; i++) {
                mainJ2.add(deck.drawRandomCard());
            }

            joueur1 = new EvaluateurMain(mainJ1);
            joueur2 = new EvaluateurMain(mainJ2);
        } while (!(joueur1.meilleureCombinaison() == Combinaison.PAIRE && joueur2.meilleureCombinaison() == Combinaison.PAIRE));

        // On retire la rivière des mains des joueurs pour l'affichage des mains
        ArrayList<Card> duoJ1 = new ArrayList<>(mainJ1);
        duoJ1.removeAll(riviere);
        ArrayList<Card> duoJ2 = new ArrayList<>(mainJ2);
        duoJ2.removeAll(riviere);

        boolean afficherHauteur = joueur1.meilleureCombinaison() == joueur2.meilleureCombinaison();

        //Affichage
        System.out.println(counter + "\n");
        System.out.println("Rivière : " + riviere + "\n");
        System.out.println("Main de J1 : " + duoJ1 + " " + joueur1.description(afficherHauteur) + "\n");
        System.out.println("Main de J2 : " + duoJ2 + " " + joueur2.description(afficherHauteur) + "\n");

        if(joueur1.score() > joueur2.score()) {
            System.out.println("J1 GAGNE avec " + joueur1.meilleuresCartes(joueur1.meilleureCombinaison()));
        } else {
            System.out.println("J2 L'EMPORTE avec " + joueur2.meilleuresCartes(joueur2.meilleureCombinaison()));
        }


    }

}