package src;

import java.util.ArrayList;
import java.util.Arrays;

public final class Tarouket {

    private final Vue vue;
    private final Player p1;
    private final Croupier croupier;
    private final Deck deck;
    private final ArrayList<Card> riviere;
    private Player joueurEnCours;
    private final GestionPot gestionPot;

    // Note pour plus tard : 
    // Avoir Croupier extends Player n'était pas une mauvaise idée
    // Mais l'idéal serait d'avoir une interface stratégie
    // : niveaux de difficulté, maintenabilité ...
    // cf Strattegy Pattern


    public Tarouket() {

        vue = new Vue();
        boolean bool = this.pileOuFace();
        if (!bool) {
            p1 = new Player(true);
            croupier = new Croupier(false);
            joueurEnCours = p1; // la bonne valeur c'est p1 ici
        } else {
            p1 = new Player(false);
            croupier = new Croupier(true);
            joueurEnCours = croupier; // A changer pour les tests
        }

        // Création du jeu de cartes
        deck = new Deck();
        deck.shuffle();

        // Création de la rivière
        riviere = new ArrayList<>();

        // Création de la gesiton du Bon débarras
        gestionPot = new GestionPot();

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

    public Player getpotentielleVictime() {
        return gestionPot.getPotentielleVictime();
    }

    private void jouerUneMain() {
        distributionCartes();
        petiteBlinde();
        initBonDebarras();

        for (Etats etat : Etats.values()) {
            if (etat.getValeur() > Etats.PRE_FLOP.getValeur()) {
                peuplerRiviere(etat.getValeur());
                vue.afficher("Révélation " + etat.getNom() + " : ", 0);
                vue.afficher(Vue.conversionCartesCouleurs(riviere), 2);
                // vue.afficher(p1.toString());
            }
            ResultatTour rez = jouerTourEncheres();
            switch (rez) {
                case ABANDON -> {
                    // Si un joueur se couche on ne compare pas les mains
                    finDeMain();
                    return;
                }
                case QUICKSTOP -> {
                    // Si un joueur fait tapis on compare tout de suite les mains
                    troisCartesDansLaRiviere();
                    comparerDeuxMains();
                    finDeMain();
                    return;
                }
            }
        }
        comparerDeuxMains();
        finDeMain();
    }

    private ResultatTour jouerTourEncheres() {
        Player premier = joueurEnCours;
        Player second  = autreJoueur(premier);

        Choix choixPremier = jouerAction(premier);
        if (choixPremier == Choix.COUCHER) return ResultatTour.ABANDON;

        Choix choixSecond = jouerAction(second);
        if (choixSecond == Choix.COUCHER) return ResultatTour.ABANDON;

        if(second.totalDuPot()>premier.totalDuPot() && premier == gestionPot.getPotentielleVictime()) {
            choixPremier = premier.bonDebarras(vue, this);
        }

        if (choixPremier == Choix.TAPIS || choixSecond == Choix.TAPIS) {
            return ResultatTour.QUICKSTOP;
        }

        return ResultatTour.CONTINUE;
    }

    // Renvoie le choix appliqué
    private Choix jouerAction(Player joueur) {
        Choix choix = joueur.demanderChoix(vue, this); 
        choix = appliquerChoix(joueur, choix);
        return choix;
    }

    private Choix appliquerChoix(Player joueur, Choix choix) {
        switch(choix) {
            case CHECK -> {
                // rien à faire
                return choix;
            }
            case COUCHER -> {
                joueur.seCoucher(vue, autreJoueur(joueur));
                return choix;
            }
            case AVANT -> {
                choix = joueur.allerDeLavant(vue, this); // TAPIS ou AVANT
                return choix;
            }
            case BON_DEBARRAS -> {
                choix = joueur.bonDebarras(vue, this);
                return choix;
            }
            default -> throw new IllegalStateException("Execution appliquerChoix() impossible : " + choix); //ne sera jamais exécuté
        }
    }

    private void troisCartesDansLaRiviere() {
        vue.croupierParleRandom("Dans le cas d'un tapis on ne considère que le flop ! (les 3 premières cartes)");
        if (riviere.size() > 3) {
            riviere.subList(3, riviere.size()).clear();
        } else if (riviere.size() < 3) {
            peuplerRiviere(Etats.FLOP.getValeur());
        }
    }

    public void mettreAJourEnAvant() {
        gestionPot.mettreAJourEnAvant(p1, croupier);
    }

    public Player autreJoueur(Player joueur) {
        return joueur == p1 ? croupier : p1;
    }

    private boolean pileOuFace() {
        String choix = vue.demanderChoix("Croupier : Hello Moussaillon ! Tu dis Pile ou Face ? ", "PILE", "FACE");
        String coin = Croupier.coinToss();
        if(coin.equals(choix)) {
            vue.afficher("\nCroupier : Gagné ! Tu auras le petit bout", 1);
            vue.afficher("Croupier : Mais cette fois, c'est moi qui commence ! ", 2);
            return true;
        } else {
            vue.afficher("\nCroupier : Perdu ! C'est moi qui aurai le petit bout", 0);
            vue.afficher("Croupier : Mais c'est toi qui commence ! ", 2);
            return false;
        }
    }
    // distribution de 2 cartes à p1 et croupier
    private void distributionCartes() {
        vue.afficher("Croupier : Tarouket !", 2);
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
        vue.afficher("Mise du Croupier : " + croupier.getMise(), 2);
        vue.afficher(p1.toString(), 2);
        vue.exigerOui("Croupier : Vous devez miser la petite blinde. Tu veux miser oui ou non ? ");
        vue.clearScreen();
        // ils effectuent la petite blind
        p1.petiteBlinde();
        croupier.petiteBlinde();
        // on affiche le pot des joueurs
        vue.afficherPots(p1, croupier);
        // on affiche la main et le pot de p1
        vue.afficher(p1.toString(), 2);
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

        vue.afficher("Croupier : C'est la fin du tour ! ", 0);
        if(!finDePartie()) {
            vue.afficher("Je redistribue les cartes. ", 0);
        }
        vue.exigerOui("Tu es prêt ? (oui)");
        vue.clearScreen();
        if(!finDePartie()) {
            vue.afficher("Croupier : Nouvelle manche ! ", 2);
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
            vue.afficher("Croupier : Tu as gagné !", 2);
            vue.croupierParleRandom("Un coup de chance certainement ...", 
                "Tu m'autorises une revanche ?",
                "Espèce de trognon de pomme oxydé ! Tu t'en tireras pas si facilement !",
                "Ma foi ... qui eût cru que tu en étais capable ?"
            );
        }

        vue.afficher("A la prochaine, pour une partie de Tarouket !", 2);
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

        vue.afficher("Rivière : \t" + Vue.conversionCartesCouleurs(riviere), 2);
        vue.afficher("Votre main : \t" + p1.mainToString() + " " + evalP1.description(afficherHauteur), 2);
        vue.afficher("Main du Croupier : \t" +  croupier.mainToString() + " " + evalCroupier.description(afficherHauteur), 2);

        if(evalP1.score() > evalCroupier.score()) {
            vue.afficher("Tu GAGNES avec " + Vue.conversionCartesCouleurs(evalP1.meilleuresCartes(evalP1.meilleureCombinaison())),2);
            p1.recupererPots(croupier);
        } else {
            vue.afficher("Je L'EMPORTE avec " + Vue.conversionCartesCouleurs(evalCroupier.meilleuresCartes(evalCroupier.meilleureCombinaison())),2);
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

    public void initBonDebarras() {
        gestionPot.reinitialiser();
    }

    // private void testSystem(Player p) {
    //     // a utiliser dans jouerTourEncheres()
    //     System.out.println("" + p.getNom()  + " A pris sa décision");
    //     System.out.print("Joueur en Avant : ");
    //     System.out.println(enAvant == null ? "null" : Objects.toString(enAvant.getNom(), "null"));
    //     System.out.println("Tours consécutifs en avant : " + toursConsecutifsEnAvant);
    //     System.out.print("La victime potentielle est : ");
    //     System.out.println(potentielleVictime == null ? "null" : Objects.toString(potentielleVictime.getNom(), "null"));
    // }

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
