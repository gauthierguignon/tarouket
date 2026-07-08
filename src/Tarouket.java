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

        boolean joueurCommence = true; // A changer pour que le croupier puisse commencer à DEALER
        //joueurCommence != p1.getMise().isPaire();

        EtatManche choix;

        do {
            joueurCommence = !joueurCommence;

            this.distributionCartes();
            vue.afficher2("Mise du Croupier : " + croupier.getMise().toString());
            vue.afficher2(p1.toString());
            this.petiteBlinde();
            
            if(joueurCommence) {
                choix = this.joueurPremierChoix();
                if (choix == EtatManche.COUCHER) continue;
            } else {
                choix = this.croupierPremierChoix(EtatManche.DEALER);
                if (choix == EtatManche.COUCHER) continue;
            }

            if(joueurCommence) {
                choix = this.JoueurSecondChoix(choix);
                if (choix == EtatManche.COUCHER) continue;
            } else {
                choix = this.croupierSecondChoix();
                if (choix == EtatManche.COUCHER) continue;
            }

            if(joueurCommence) {
                choix = this.JoueurTroisiemeChoix(choix);
                if (choix == EtatManche.COUCHER) continue;
            } else {
                choix = this.croupierTroisiemeChoix();
                if (choix == EtatManche.COUCHER) continue;
            }

            
        } while (!this.finDePartie());
        
        this.theEnd();

    }
    
    // Tirage a pile ou face
    public boolean pileOuFace() {
        String choix = vue.demanderChoix("Croupier : Hello Moussaillon ! Tu dis Pile ou Face ? ", "PILE", "FACE");
        String coin = Croupier.coinToss();
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

    public void relancer() {
        vue.afficher1("\nVous : Je relance !");
        vue.croupierParleRandom("Tu penses m'effrayer ? C'est ce qu'on va voir !",
            "T'as une paire d'AS ? Mon oeil oui !",
            "AHAH ! Je m'y attendais à celle-là !",
            "Tu bluffes Martini !"
        );
        int valeur = vue.demanderMise(p1.getMise().getMise());
        // mettre dans le pot de p1 valeur
        vue.clearScreen();
        vue.afficher2("Croupier : Tu as misé " + valeur + " !");
        p1.ajouterAuPot(valeur);
        vue.afficherPots(p1, croupier);
        vue.afficher2(p1.toString());
    }

    public void seCoucher() {
        vue.croupierParleRandom("Eh ben mon coco ! T'es pas un aventurier toi ... ", 
            "Ah ouais ?! Pas très courageux.",
            "Tu l'as joues sur le long terme. Tu as raison !",
            "Faudrait peut-être jouer un jour, nan ?",
            "On se demande bien qui va gagner",
            "Chacun sa technique"
        );
        vue.afficher2("Le croupier avait en main : " + Arrays.toString(croupier.getCartes()));
        vue.croupierParleRandom("C'est la fin du tour ! Je redistribue les cartes.");
        vue.exigerOui("Tu es prêt ? (oui)");
        vue.clearScreen();
        vue.afficher2("Croupier : Nouvelle manche ! ");
        
        this.finDeMain();

        croupier.recupererPots(p1);
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

    public void faireTapis() {
        vue.clearScreen();

        vue.croupierParleRandom("Tonnere de Brest !", "Nom d'un pilon vermoulu !");
        vue.croupierParleRandom("Tu fais tapis !");
        vue.croupierParleRandom("Petit rappel, quand on fait tapis on ne considère que le flop. Capiche ?");
        
        // Ajouter au pot toute la mise
        p1.ajouterAuPot(p1.getMise().getMise());
        // Vider la mise du joueur
        p1.getMise().clear();

        vue.afficherPots(p1, croupier);
        vue.afficher2(p1.toString());

    }

    public void allerDeLavant() {

        // Premier tour
        boolean tapis = false;
        String choix = vue.demanderChoix("\nCroupier : Tu veux relancer (1) ou faire tapis (2) ?", "1", "2");

        switch (choix) {
            case "1" -> {
                this.relancer();
            }
            case "2" -> {
                this.faireTapis();
                tapis = true;
            }
        }

        int counter = 2;
        // Tours suivants sauf si tapis
        if(!tapis) {
            do {
                String prompt = "\nCroupier : Tu veux relancer une " + counter + "e fois (oui) ou tu t'arrêtes là (non) ?";
                choix = vue.demanderChoix(prompt, "oui", "non");

                if (choix.equals("OUI")) {
                    vue.afficher("\nVous : Je relance une " + counter + "e fois");
                    this.relancer();
                    counter ++;
                } else {
                    vue.afficher2("\nVous : Je m'arrête là.\n");
                }
            } while (!choix.equals("NON"));
        }
    }

    public EtatManche joueurPremierChoix() {
            
            String choix;
            do { 
                choix = vue.demanderChoix("Croupier : Tu peux checker (1), aller de l'avant (2) ou te coucher(3)", "1", "2", "3");
            } while (!choix.equals("1") && !choix.equals("2") && !choix.equals("3"));

            switch(choix) {
                case "1" -> {
                    vue.afficher2("\nCroupier : Tu checkes !"); 
                    return EtatManche.CHECK;
                }
                case "2" -> {
                        this.allerDeLavant();
                        return EtatManche.AVANT;
                    }
                case "3" -> {
                    this.seCoucher();
                    return EtatManche.COUCHER;
                }
            }
            throw new IllegalStateException("Choix impossible : " + choix); //ne sera jamais exécuté
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

    public EtatManche croupierPremierChoix(EtatManche etat) {
        switch (etat) {
            case DEALER -> { // le croupier commence à dealer
                this.croupierCheck();
                return EtatManche.CHECK;
            }
            case CHECK -> { // le joueur check
                vue.afficher1("Croupier: C'est à mon tour de jouer !");
                // Définir si le croupier mise ou check ou fait tapis
                int alea = rand.nextInt(100); // de 0 à 99
                if (alea < 50) {
                    this.croupierCheck();
                    this.peuplerRiviere(3);
                    vue.afficher2("Voici le flop : " + this.riviere.toString());
                    return EtatManche.CHECK;
                } else if (alea >= 50 && alea < 95) {
                    // this.croupierVaDeLavant();
                    return EtatManche.AVANT;
                } else {
                    // this.croupierFaisTapis();
                    return EtatManche.TAPIS;
                }

            }
            case AVANT -> { // le joueur est allé de l'avant
                vue.afficher1("Croupier: C'est à mon tour de jouer !");
                // Définir si le croupier mise ou laisse passer
                return EtatManche.AVANT;
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + etat);
        }
    }

    public void croupierCheck(){
        vue.afficher2("Je checke");
    }

    public void peuplerRiviere(int n){
        for (int i = 0; i < n; i++) {
            riviere.add(deck.drawRandomCard());
        }
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
