package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public final class Tarouket {

    public final Vue vue;
    private final Player p1;
    private final Croupier croupier;
    private final Deck deck;
    private final ArrayList<Card> riviere;
    private final Random rand;
    private Player joueurEnCours;
    private Player enAvant;
    private boolean limiteTapis;

    private final int PRE_FLOP = 0;
    private final int FLOP = 3;
    private final int TURN = 1;
    private final int RIVER = 1;

    // Note pour plus tard : 
    // Avoir Croupier extends Player n'était pas une mauvaise idée
    // Mais l'idéal serait d'avoir une interface stratégie
    // : niveaux de difficulté, maintenabilité ...
    // cf Strattegy Pattern


    public Tarouket() {

        vue = new Vue();
        rand = new Random();
        boolean bool = this.pileOuFace();
        if (!bool) {
            p1 = new Player(true);
            croupier = new Croupier(false);
            joueurEnCours = p1;
        } else {
            p1 = new Player(false);
            croupier = new Croupier(true);
            joueurEnCours = croupier; // pour le test on commence toujours avec P1 pour l'instant
        }

        // Création du jeu de cartes
        deck = new Deck();
        deck.shuffle();

        // Création de la rivière
        riviere = new ArrayList<>();
    }

    public void run() {
        do {
            this.jouerUneMain();
            this.alternerPremierJoueur();          
        } while (!this.finDePartie());
        this.theEnd();
    }
    
    private void alternerPremierJoueur() {
        p1.changePaire();
        joueurEnCours = p1.isPaire() ? p1 : croupier;
    }

    private void jouerUneMain() {
        distributionCartes();
        petiteBlinde();

        int[] phases = {PRE_FLOP, FLOP, TURN, RIVER};

        for (int nbCartes : phases) {
            if (nbCartes > PRE_FLOP) {
                peuplerRiviere(nbCartes);
                vue.afficher2(riviere.toString());
            }
            if (jouerTourEncheres() == ResultatTour.ABANDON) {
                finDeMain();
                return;
            } // Si un joueur se couche on ne compare pas les mains
        }
        comparerDeuxMains();
        finDeMain();
    }

    private ResultatTour jouerTourEncheres() {
        Player premier = joueurEnCours;
        Player second  = autreJoueur(premier);

        if (jouerAction(premier) == Choix.COUCHER) return ResultatTour.ABANDON;
        if (jouerAction(second)  == Choix.COUCHER) return ResultatTour.ABANDON;

        mettreAJourEnAvant();
        return ResultatTour.CONTINUE;
    }

    private Choix jouerAction(Player joueur) {
        Choix choix = joueur.demanderChoix(vue); 
        appliquerChoix(joueur, choix);
        return choix;
    }

    private void appliquerChoix(Player joueur, Choix choix) {
        switch(choix) {
            case CHECK -> {
                // rien à faire
            }
            case COUCHER -> {
                joueur.seCoucher(vue, autreJoueur(joueur));
            }
            case AVANT -> {
                joueur.allerDeLavant(vue, this);
            }
            default -> throw new IllegalStateException("Execution appliquerChoix() impossible : " + choix); //ne sera jamais exécuté
        }
    }

    private void mettreAJourEnAvant() {
        if (p1.totalDuPot() > croupier.totalDuPot()) {
            enAvant = p1;
        } else if (p1.totalDuPot() < croupier.totalDuPot()) {
            enAvant = croupier;
        } else {
            enAvant = null;
        }
    }

    private Player autreJoueur(Player joueur) {
        return joueur == p1 ? croupier : p1;
    }

    private boolean pileOuFace() {
        String choix = vue.demanderChoix("Croupier : Hello Moussaillon ! Tu dis Pile ou Face ? ", "PILE", "FACE");
        String coin = Croupier.coinToss();
        if(coin.equals(choix)) {
            vue.afficher1("\nCroupier : Gagné ! Tu auras le petit bout");
            vue.afficher2("Croupier : Mais cette fois, c'est moi qui commence ! ");
            return true;
        } else {
            vue.afficher1("\nCroupier : Perdu ! C'est moi qui aurai le petit bout");
            vue.afficher2("Croupier : Mais c'est toi qui commence ! ");
            return false;
        }
    }

    // distribution de 2 cartes à p1 et croupier
    private void distributionCartes() {
        vue.afficher("Croupier : Tarouket !\n\n");
        p1.setCartes(deck.drawRandomCard(), deck.drawRandomCard());
        croupier.setCartes(deck.drawRandomCard(), deck.drawRandomCard());
    }
    
    private boolean finDePartie() {
        if ((p1.totalDeMise() == 0 && p1.totalDuPot() == 0) || (croupier.totalDeMise() == 0 && croupier.totalDuPot() == 0)) return true;
        return false;
    }

    // Mise automatique de la petite Blinde
    private void petiteBlinde() {
        // affichage des mises avant la petite blind
        vue.afficher2("Mise du Croupier : " + croupier.getMise());
        vue.afficher2(p1.toString());
        vue.exigerOui("Croupier : Vous devez miser la petite blinde. Tu veux miser oui ou non ? ");
        vue.clearScreen();
        // ils effectuent la petite blind
        p1.petiteBlinde();
        croupier.petiteBlinde();
        // on affiche le pot des joueurs
        vue.afficherPots(p1, croupier);
        // on affiche la main et le pot de p1
        vue.afficher2(p1.toString());
    }

    private void finDeMain() {
        // On remet les cartes des joueureuses dans cartes
        ArrayList<Card> mainDeP1 = new ArrayList<>(Arrays.asList(p1.getCartes()));
        deck.addAll(mainDeP1);
        ArrayList<Card> mainDuCroupier = new ArrayList<>(Arrays.asList(croupier.getCartes()));
        deck.addAll(mainDuCroupier);
        // On retire les cartes des mains des joueurs
        p1.setCartes(null, null);
        croupier.setCartes(null, null);
        // On retire les cartes de la rivière
        this.riviere.clear();

        vue.afficher("Croupier : C'est la fin du tour ! ");
        if(!finDePartie()) {
            vue.afficher1("Je redistribue les cartes.");
        }
        vue.exigerOui("Tu es prêt ? (oui)");
        vue.clearScreen();
        if(!finDePartie()) {
            vue.afficher2("Croupier : Nouvelle manche ! ");
        }
    }

    private void theEnd() {
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

    private void comparerDeuxMains() {
        ArrayList<Card> mainCompleteP1 = new ArrayList<>(this.riviere);
        ArrayList<Card> mainCompleteCroupier = new ArrayList<>(this.riviere);

        mainCompleteP1.addAll(Arrays.asList(p1.getCartes()));
        mainCompleteCroupier.addAll(Arrays.asList(croupier.getCartes()));

        EvaluateurMain evalP1 = new EvaluateurMain(mainCompleteP1);
        EvaluateurMain evalCroupier = new EvaluateurMain(mainCompleteCroupier);

        boolean afficherHauteur = evalP1.meilleureCombinaison() == evalCroupier.meilleureCombinaison();

        vue.afficher2("Rivière : \t" + this.riviere);
        vue.afficher2("Votre main : \t" + Arrays.toString(p1.getCartes()) + " " + evalP1.description(afficherHauteur));
        vue.afficher2("Main du Croupier : \t" +  Arrays.toString(croupier.getCartes()) + " " + evalCroupier.description(afficherHauteur));

        if(evalP1.score() > evalCroupier.score()) {
            vue.afficher2("Tu GAGNES avec " + evalP1.meilleuresCartes(evalP1.meilleureCombinaison()));
            p1.recupererPots(croupier);
        } else {
            vue.afficher2("Je L'EMPORTE avec " + evalCroupier.meilleuresCartes(evalCroupier.meilleureCombinaison()));
            croupier.recupererPots(p1);
        }
    }

    private void peuplerRiviere(int n){
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
