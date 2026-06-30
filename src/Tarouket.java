package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Tarouket {

    private final Scanner sc = new Scanner(System.in);
    private final Player p1;
    private final Player p2;
    private ArrayList<Card> cartes;

    public Tarouket() {

        boolean bool = pileOuFace();
        if (!bool) {
            p1 = new Player(1, true);
            p2 = new Player(2, false);
        } else {
            p1 = new Player(1, false);
            p2 = new Player(2, true);
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
            term2("Mise du Croupier : " + p2.getMise().toString());
            term2(p1.toString());
            
            this.petiteBlinde();            
            String choix1 = this.premierChoix();

            switch (choix1) {
                case ("CHECK") -> {
                    // Premier tour du croupier
                    term("Croupier: C'est à mon tour de jouer !");
                    // Définir si le croupier mise ou check ou fais tapis
                }
                case ("AVANT") -> {
                    // Premier tour du croupier
                    term("Croupier: C'est à mon tour de jouer !");
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
        
        String choix;
        do {
            term2("Croupier : Hello Moussaillon ! Tu dis Pile ou Face ? ");
            choix = sc.nextLine();
        } while (!choix.equalsIgnoreCase("Pile") && !choix.equalsIgnoreCase("Face"));
        choix = choix.toUpperCase();
        
        Random rand = new Random();
        boolean bool = rand.nextBoolean();
        String rez;
        if(bool) {
            rez = "PILE";
        } else {
            rez = "FACE";
        }
        
        if(rez.equals(choix)) {
            term("\nCroupier : Gagné ! tu auras le petit bout\n");
            return true;
        } else {
            term("\nCroupier : Perdu ! ton adversaire aura le petit bout\n");
            return false;
        }
    }

    // distribution des cartes aux joueureuses
    public void distributionCartes() {
        term("Croupier : Tarouket !\n\n");
        p1.setCartes(cartes.get(0), cartes.get(1));
        p2.setCartes(cartes.get(2), cartes.get(3));
        cartes.subList(0, 4).clear();
    }
    
    // Affichage dans le terminal \n x2
    public static void term2(String phrase) {
        for(int i = 0; i<phrase.length(); i++) {
            System.out.print(phrase.charAt(i));
            try {
                Thread.sleep(30); //30
            } catch (InterruptedException e) {
                
            }
        }
        System.out.println("\n");
    }

    // Afichage dans le terminal \n x1
    public static void term(String phrase) {
        for(int i = 0; i<phrase.length(); i++) {
            System.out.print(phrase.charAt(i));
            try {
                Thread.sleep(30); //30
            } catch (InterruptedException e) {
                
            }
        }
        System.out.println("");
    }

    // Affichage dans le terminal sans \n
    public static void ter(String phrase) {
        for(int i = 0; i<phrase.length(); i++) {
            System.out.print(phrase.charAt(i));
            try {
                Thread.sleep(30); //30
            } catch (InterruptedException e) {
                
            }
        }
    }

    // Définition d'une fin de partie
    public boolean finDePartie() {
        if ((p1.totalDeMise() == 0 && p1.totalDuPot() == 0) || (p2.totalDeMise() == 0 && p2.totalDuPot() == 0)) return true;
        return false;
    }

    // Mise automatique de la petite Blinde
    public void petiteBlinde() {
            
        String choix = "";
        do {
            if(choix.equals("non"))term2("\nCroupier : Nan mais t'as pas le choix en fait ... ");
            term2("Croupier : Vous devez miser la petite blinde. Tu vas miser oui ou non ? ");
            System.out.print("Vous : ");
            choix = sc.nextLine();
        } while (!choix.equalsIgnoreCase("oui"));

            Tarouket.clearScreen();

            p1.petiteBlinde();
            p2.petiteBlinde();
            
            this.afficherPots();

            term2(p1.toString());
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[J");
    }

    public void afficherPots() {
        term(p1.votrePotToToString());
        term2("Total : " + p1.totalDuPot());
        term(p2.croupierPotToToString());
        term2("Total : " + p2.totalDuPot());
    }

    public void relancer() {
        Random rand2 = new Random();
        int rez2 = rand2.nextInt(4)+1;
        switch(rez2) {
            case 1 -> term("\nCroupier : Tu penses m'effrayer ? C'est ce qu'on va voir !");
            case 2 -> term("\nCroupier : T'as une paire d'AS ? Mon oeil oui !");
            case 3 -> term("\nCroupier : AHAH ! Je m'y attendais à celle-là !");
            case 4 -> term("\nCroupier : Tu bluffes Martini !");
        }
        
        String choix2;
        List<Integer> miseDeP1;
        int valeur = -1;
        int counter = 0;
        do {
            if(counter > 1) {
                term2("\nCroupier : Capitaine, zêtes bourré ou quoi ?! Faut miser les cartes dans ta Mise. Allez on recommence ! ");
            }
            term2("\nCroupier : Tu veux relancer de combien mon coco ? Moi, j'ai pas besoin de te suivre.");
            ter("Vous : Je relance de ");
            choix2 = sc.nextLine();
            try {
                valeur = Integer.parseInt(choix2); // user donne la valeur qu'il veut mettre dans son pot
            } catch (NumberFormatException e) {
                term("\nCroupier : Oh nan mais Capitaine ! Je veux un chiffre, pas des lettres !");
            }
            miseDeP1 = p1.getMise().getMise(); // List de ce que P1 peut miser
            counter ++;
        } while (!miseDeP1.contains(valeur));

        // mettre dans le pot de p1 valeur
        Tarouket.clearScreen();
        term2("Croupier : Tu as misé " + valeur + "!");
        p1.ajouterAuPot(valeur);
        this.afficherPots();
        term2(p1.toString());
    }

    public void seCoucher() {
        Random rand3 = new Random();
        int rez3 = rand3.nextInt(6)+1;
        switch(rez3) {
            case 1 -> term2("Croupier : Eh ben mon coco ! T'es pas un aventurier toi ... ");
            case 2 -> term2("Croupier : Ah ouais ?! Pas très courageux.");
            case 3 -> term2("Croupier : Tu l'as joues sur le long terme. Tu as raison !");
            case 4 -> term2("Croupier : Faudrait peut-être jouer un jour, nan ?");
            case 5 -> term2("Croupier : On se demande bien qui va gagner");
            case 6 -> term2("Croupier : Chacun sa technique");
        }
        term2("Croupier : C'est la fin du tour ! Je redistribue les cartes.");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) { }
        Tarouket.clearScreen();
        term2("Croupier : Nouvelle manche ! ");
        
        // On remet les cartes des joueureuses dans cartes
        ArrayList<Card> mainDeP1 = new ArrayList<>(Arrays.asList(p1.getCartes()));
        cartes.addAll(mainDeP1);
        ArrayList<Card> mainDeP2 = new ArrayList<>(Arrays.asList(p2.getCartes()));
        cartes.addAll(mainDeP2);
        // On retire les cartes des mains des joueurs
        p1.setCartes(null, null);
        p2.setCartes(null, null);

        // Donner le pot de p1 à p2
        ArrayList<Integer> potDeP1 = new ArrayList<>(p1.getPot());
        p2.getMise().addAll(potDeP1);

        // Mettre le pot de p2 dans la mise de p2
        ArrayList<Integer> potDeP2 = new ArrayList<>(p2.getPot());
        p2.getMise().addAll(potDeP2);

        // Vider le pot des joueureuses
        p1.Viderpot();
        p2.Viderpot();

        p2.getMise().sort();
    }

    public void faireTapis() {
        Tarouket.clearScreen();
        Random rand2 = new Random();
        int rez2 = rand2.nextInt(2)+1;
        switch(rez2) {
            case 1 -> term2("\nCroupier : Tonnere de Brest !");
            case 2 -> term2("\nCroupier : Nom d'un pilon vermoulu !");
        }
        term("Croupier : Tu fais tapis !");
        term2("Croupier : Petit rappel, quand on fait tapis on ne considère que le flop. Capiche ?");
        
        // Ajouter au pot toute la mise
        p1.ajouterAuPot(p1.getMise().getMise());
        // Vider la mise du joueur
        p1.getMise().clear();

        this.afficherPots();
        term2(p1.toString());

    }

    public void allerDeLavant() {

        // Premier tour obligatoire
        boolean tapis = false;
        term2("\nCroupier : Tu veux relancer (1) ou faire tapis (2) ?");
        String choix2 = sc.nextLine();

        switch (choix2) {
            case "1" -> {
                term("\nVous : Je relance !");
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
                term2(prompt);
                // prompt = "\nCroupier : Tu veux relancer d'avantage (1) ou tu t'arrêtes là (2) ?";
                ter("Vous : ");
                choix2 = sc.nextLine();
                if (choix2.equals("oui")) {
                    term("\nVous : Je relance une " + counter + "e fois");
                    this.relancer();
                    counter ++;
                } else {
                    term2("\nVous : Je m'arrête là.\n");
                }
            } while (!choix2.equals("non"));
        }


    }

    public String premierChoix() {
            
            String choix;
            do { 
                term2("Croupier : Tu peux checker (1), aller de l'avant (2) ou te coucher(3)");
                System.out.print("Vous : ");
                choix = sc.nextLine();
            } while (!choix.equals("1") && !choix.equals("2") && !choix.equals("3"));

            switch(choix) {
                case "1" -> {
                    term2("\nCroupier : Tu checkes !"); 
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
        Tarouket.clearScreen();

        if(p1.getMise().isEmpty()) {
            Random rand3 = new Random();
            int rez3 = rand3.nextInt(5)+1;
            term2("Croupier : Tu as perdu !");
            switch(rez3) {
                case 1 -> term2("Croupier : La prochaine fois Mousaillon, on met de l'argent sur la table ! ");
                case 2 -> term2("Croupier : Reviens quand tu veux, t'as besoin de t'entraîner !");
                case 3 -> term2("Croupier : Ma parole ! T'es aussi mauvais qu'un chimpanzé !");
                case 4 -> term2("Croupier : On parie 50 et on se fait ta revanche ?");
                case 5 -> term2("Croupier : Pas sûr que t'entrainer suffise à me battre... ");
            }
        } else {
            Random rand3 = new Random();
            int rez3 = rand3.nextInt(4)+1;
            term2("Croupier : Tu as gagné !");
            switch(rez3) {
                case 1 -> term2("Croupier : Un coup de chance certainement ...");
                case 2 -> term2("Croupier : Tu m'autorises une revanche ?");
                case 3 -> term2("Croupier : Espèce de trognon de pomme oxydé ! Tu t'en tireras pas si facilement !");
                case 4 -> term2("Croupier : Ma foi ... qui eût cru que tu en étais capable ?");
            }
        }

        term2("A la prochaine, pour une partie de Tarouket !");
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
