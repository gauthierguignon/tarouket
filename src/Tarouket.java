package src;

import java.util.ArrayList;
import java.util.Arrays;

public final class Tarouket {

    private final Vue vue;
    private final Player p1;
    private final Croupier croupier;
    private ArrayList<Card> cartes;

    //constructeur
    public Tarouket() {

        vue = new Vue();
        boolean bool = this.pileOuFace();
        if (!bool) {
            p1 = new Player(true);
            croupier = new Croupier(false);
        } else {
            p1 = new Player(false);
            croupier = new Croupier(true);
        }

        // Création du jeu de cartes
        Deck deck = new Deck();
        deck.shuffle();
        cartes = new ArrayList<>(deck.getDeck());
    }

    //Squelette du jeu
    public void run() {

        do {
            this.distributionCartes();
            vue.afficher2("Mise du Croupier : " + croupier.getMise().toString());
            vue.afficher2(p1.toString());
            
            this.petiteBlinde();            
            String choix1 = this.premierChoix();

            switch (choix1) {
                case ("CHECK") -> {
                    // Premier tour du croupier
                    vue.afficher1("Croupier: C'est à mon tour de jouer !");
                    
                    // do { 
                    //     vue.afficher2(Arrays.toString(croupier.getCartes()));
                    //     vue.demanderChoix("test", "");
                    // } while (true);

                    // Définir si le croupier mise ou check ou fais tapis
                }
                case ("AVANT") -> {
                    // Premier tour du croupier
                    vue.afficher1("Croupier: C'est à mon tour de jouer !");
                    // Définir si le croupier mise ou laisse passer
                }
                case ("COUCHER") -> {
                    // On saute à l'évaluation de la condition finDePartie()
                    continue;
                }
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
            return true;
        } else {
            vue.afficher("\nCroupier : Perdu ! ton adversaire aura le petit bout\n");
            return false;
        }
    }

    // distribution des cartes aux joueureuses
    public void distributionCartes() {
        vue.afficher("Croupier : Tarouket !\n\n");
        p1.setCartes(cartes.get(0), cartes.get(1));
        croupier.setCartes(cartes.get(2), cartes.get(3));
        cartes.subList(0, 4).clear();
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
        vue.croupierParleRandom("C'est la fin du tour ! Je redistribue les cartes.");
        vue.wait(1000);
        vue.clearScreen();
        vue.afficher2("Croupier : Nouvelle manche ! ");
        
        // On remet les cartes des joueureuses dans cartes
        ArrayList<Card> mainDeP1 = new ArrayList<>(Arrays.asList(p1.getCartes()));
        cartes.addAll(mainDeP1);
        ArrayList<Card> mainDuCroupier = new ArrayList<>(Arrays.asList(croupier.getCartes()));
        cartes.addAll(mainDuCroupier);
        // On retire les cartes des mains des joueurs
        p1.setCartes(null, null);
        croupier.setCartes(null, null);

        croupier.recupererPots(p1);
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

    public String premierChoix() {
            
            String choix;
            do { 
                choix = vue.demanderChoix("Croupier : Tu peux checker (1), aller de l'avant (2) ou te coucher(3)", "1", "2", "3");
            } while (!choix.equals("1") && !choix.equals("2") && !choix.equals("3"));

            switch(choix) {
                case "1" -> {
                    vue.afficher2("\nCroupier : Tu checkes !"); 
                    return "CHECK";
                }
                case "2" -> {
                        this.allerDeLavant();
                        return "AVANT";
                    }
                case "3" -> {
                    this.seCoucher();
                    return "COUCHER";
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

}
