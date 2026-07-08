package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public final class Tarouket {

    private final Vue vue;
    private final Player p1;
    private final Croupier croupier;
    private final Deck deck;
    private final ArrayList<Card> riviere;
    private final Random rand;

    //constructeur
    public Tarouket() {

        vue = new Vue();
        rand = new Random();
        boolean bool = this.pileOuFace();
        if (!bool) {
            p1 = new Player(true);
            croupier = new Croupier(false);
        } else {
            p1 = new Player(false);
            croupier = new Croupier(true);
        }

        // Création du jeu de cartes
        deck = new Deck();
        deck.shuffle();

        // Création de la rivière
        riviere = new ArrayList<>();
    }

    //Squelette du jeu
    public void run() {
        do {
            this.jouerUneMain(); // A la fin de la main on regarde si la partie est terminée
            this.alternerPremierJoueur();          
        } while (!this.finDePartie());
        this.theEnd();
    }
    
    public void alternerPremierJoueur() {
        p1.changePaire();
        croupier.changePaire();
    }

    private void jouerUneMain() {
        distributionCartes();
        vue.afficher2("Mise du Croupier : " + croupier.getMise());
        vue.afficher2(p1.toString());
        petiteBlinde();

        EtatManche etat = tourDeParole(p1); // renvoie le gagnant si coucher, sinon null
        if (etat == EtatManche.COUCHER) {
            return; // on évalue la condition de fin de partie
        }



        // sinon on continue : flop, turn, river...
    }

    private EtatManche tourDeParole(Player p) {
        EtatManche etat = joueur.demanderChoix(vue);
        
        switch (etat) {
            case COUCHER -> joueur.seCoucher();
            case AVANT   -> joueur.allerDeLavant();
            case CHECK   -> { /* rien à faire */ }
        }
        return etat;
    }

    // Tirage a pile ou face
    public boolean pileOuFace() {
        String choix = vue.demanderChoix("Croupier : Hello Moussaillon ! Tu dis Pile ou Face ? ", "PILE", "FACE");
        String coin = croupier.coinToss();
        if(coin.equals(choix)) {
            vue.afficher("\nCroupier : Gagné ! tu auras le petit bout\n");
            // vue.afficher("Croupier : Et c'est moi qui Deal");
            return true;
        } else {
            vue.afficher("\nCroupier : Perdu ! ton adversaire aura le petit bout\n");
            // vue.afficher("Croupier : Mais c'est toi qui Deal");
            return false;
        }
    }

    // distribution des cartes aux joueureuses
    public void distributionCartes() {
        vue.afficher("Croupier : Tarouket !\n\n");
        p1.setCartes(deck.drawRandomCard(), deck.drawRandomCard());
        croupier.setCartes(deck.drawRandomCard(), deck.drawRandomCard());
    }
    
    // Définition d'une fin de partie
    public boolean finDePartie() {
        if ((p1.totalDeMise() == 0 && p1.totalDuPot() == 0) || (croupier.totalDeMise() == 0 && croupier.totalDuPot() == 0)) return true;
        return false;
    }

    // Mise automatique de la petite Blinde
    public void petiteBlinde() {
        vue.exigerOui("Croupier : Vous devez miser la petite blinde. Tu veux miser oui ou non ? ");
        vue.clearScreen();
        p1.petiteBlinde();
        croupier.petiteBlinde();
        vue.afficherPots(p1, croupier);
        vue.afficher2(p1.toString());
    }

    public void finDeMain() {
        // On remet les cartes des joueureuses dans cartes
        ArrayList<Card> mainDeP1 = new ArrayList<>(Arrays.asList(p1.getCartes()));
        deck.addAll(mainDeP1);
        ArrayList<Card> mainDuCroupier = new ArrayList<>(Arrays.asList(croupier.getCartes()));
        deck.addAll(mainDuCroupier);
        // On retire les cartes des mains des joueurs
        p1.setCartes(null, null);
        croupier.setCartes(null, null);
    }

    public void theEnd() {
        vue.clearScreen();

        if(p1.getMise().isEmpty()) {
            vue.croupierParleRandom("Tu as perdu !");
            vue.croupierParleRandom("La prochaine fois Mousaillon, on met de l'argent sur la table ! ",
                "Reviens quand tu veux, t'as besoin de t'entraîner !",
                "Ma parole ! C'était comme jouer contre un chimpanzé !",
                "On parie 50 et on se fait ta revanche ?",
                "Pas sûr que t'entrainer suffise à me battre... "
            );
        } else {
            vue.afficher2("Croupier : Tu as gagné !");
            vue.croupierParleRandom("Un coup de chance certainement ...", 
                "Tu m'autorises une revanche ?",
                "Espèce de trognon de pomme oxydé ! Tu t'en tireras pas si facilement !",
                "Ma foi ... qui eût cru que tu en étais capable ?"
            );
        }

        vue.afficher2("A la prochaine, pour une partie de Tarouket !");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        System.out.print("· ");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        System.out.print("· ");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
        System.out.print("·\n");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {}
    }

    public void comparerDeuxMains() {
        ArrayList<Card> mainCompleteP1 = new ArrayList<>(this.riviere);
        ArrayList<Card> mainCompleteCroupier = new ArrayList<>(this.riviere);

        mainCompleteP1.addAll(Arrays.asList(p1.getCartes()));
        mainCompleteCroupier.addAll(Arrays.asList(croupier.getCartes()));

        EvaluateurMain evalP1 = new EvaluateurMain(mainCompleteP1);
        EvaluateurMain evalCroupier = new EvaluateurMain(mainCompleteCroupier);

        vue.afficher2("Rivière : \t" + this.riviere);
        vue.afficher2("Votre main : \t" + Arrays.toString(p1.getCartes()));
        vue.afficher2("Main du Croupier : \t" +  Arrays.toString(croupier.getCartes()));

        if(evalP1.score() > evalCroupier.score()) {
            vue.afficher2("Tu GAGNES avec " + evalP1.meilleuresCartes(evalP1.meilleureCombinaison()));
            p1.recupererPots(croupier);
        } else {
            vue.afficher2("Je L'EMPORTE avec " + evalCroupier.meilleuresCartes(evalCroupier.meilleureCombinaison()));
            croupier.recupererPots(p1);
        }
        this.finDeMain();
    }

    public void peuplerRiviere(int n){
        for (int i = 0; i < n; i++) {
            riviere.add(deck.drawRandomCard());
        }
    }

    public Player getPlayer() {
        return p1;
    }

    public Croupier getCroupier(){
        return croupier;
    }

    
    // test
    // Pour lancer ce test il faut désactiver l'afficahe du vue 
    // sinon ça prend trop de temps à chaque essai
    // l22 et l96

    public static void main(String[] args) {
        Tarouket tarouket;
        EvaluateurMain evalP1;
        EvaluateurMain evalCroupier;
        int counter = 0;

        do {
            counter++;
            tarouket = new Tarouket();
            tarouket.distributionCartes();

            for (int i = 0; i < 5; i++) {
                tarouket.riviere.add(tarouket.deck.drawRandomCard());
            }

            ArrayList<Card> mainP1 = new ArrayList<>(tarouket.riviere);
            mainP1.addAll(Arrays.asList(tarouket.p1.getCartes()));
            evalP1 = new EvaluateurMain(mainP1);

            ArrayList<Card> mainCroupier = new ArrayList<>(tarouket.riviere);
            mainCroupier.addAll(Arrays.asList(tarouket.croupier.getCartes()));
            evalCroupier = new EvaluateurMain(mainCroupier);

        } while (!(evalP1.meilleureCombinaison() == Combinaison.CARRE
                && evalCroupier.meilleureCombinaison() == Combinaison.PAIRE));

        System.out.println("Trouvé après " + counter + " essais.");
        tarouket.comparerDeuxMains();
    }

}
