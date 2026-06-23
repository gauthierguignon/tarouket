package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class tarouket {
 
    public static void main(String[] args) {
        
        // Attributions des mises
        boolean bool = tarouket.PileOuFace();
        
        Player p1;
        Player p2;

        if(!bool) {
            p1 = new Player(1, true);
            p2 = new Player (2, false);
        } else {
            p1 = new Player(1, false);
            p2 = new Player (2, true);
        }

        // Création du jeu de cartes
        Deck deck = new Deck();
        deck.shuffle();
        ArrayList<Card> cartes = new ArrayList<>(deck.getDeck());

        
        
        do {
            distributionCartes(p1, p2, cartes);
            term2("Mise du Croupier : " + p2.getMise().mise.toString());
            term2(p1.toString());
            
            tarouket.petiteBlinde(p1, p2);

            // Premier choix
            
            Scanner sc = new Scanner(System.in);
            String choix;
            do { 
                term2("Croupier : Tu peux checker (1), aller de l'avant (2) ou te coucher(3)");
                System.out.print("Vous : ");
                choix = sc.nextLine();
            } while (!choix.equals("1") && !choix.equals("2") && !choix.equals("3"));

            switch(choix) {

                case "1" -> { // Player1 à checké

                }

                case "2" -> {  // Player1 va de l'avant

                    tarouket.allerDeLavant(p1, p2);

                    // TODO
                    // mettre cette méthode dans un do while :
                    // le joueur doit pouvoir faire tapis ou miser plusieurs atouts

                }


                case "3" -> { // Player 1 se couche
                    tarouket.seCoucher(p1, p2, cartes);
                }
            }
        //} while (false);
        } while (finDePartie(p1, p2));

            

    }
    
    // Tirage a pile ou face
    public static boolean PileOuFace() {
        
        Scanner sc = new Scanner(System.in);
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
    public static void distributionCartes(Player p1, Player p2, ArrayList<Card> cartes) {
        term("Croupier : Tarouket !\n\n");
        p1.setCartes(cartes.get(0), cartes.get(1));
        p2.setCartes(cartes.get(2), cartes.get(3));
        cartes.subList(0, 4).clear();
    }
    
    // Affichage dans le terminal
    public static void term2(String phrase) {
        for(int i = 0; i<phrase.length(); i++) {
            System.out.print(phrase.charAt(i));
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                
            }
        }
        System.out.println("\n");
    }

    // Afichage sans \n
    public static void term(String phrase) {
        for(int i = 0; i<phrase.length(); i++) {
            System.out.print(phrase.charAt(i));
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                
            }
        }
        System.out.println("");
    }

    // Définition d'une fin de partie
    public static boolean finDePartie(Player p1, Player p2) {
        if (p1.totalDeMise() == 0 || p2.totalDeMise() == 0) return false;
        return true;
    }

    // Mise automatique de la petite Blinde
    public static void petiteBlinde(Player p1, Player p2) {
            
        Scanner sc = new Scanner(System.in);
        String choix = "";
        do {
            if(choix.equals("non"))term2("\nCroupier : Nan mais t'as pas le choix en fait ... ");
            term2("Croupier : Vous devez miser la petite blinde. Tu vas miser oui ou non ? ");
            System.out.print("Vous : ");
            choix = sc.nextLine();
        } while (!choix.equalsIgnoreCase("oui"));

            tarouket.clearScreen();

            p1.petiteBlinde();
            p2.petiteBlinde();
            
            tarouket.afficherPots(p1, p2);

            term2(p1.toString());
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[J");
    }

    public static void afficherPots(Player p1, Player p2) {
        term(p1.votrePotToToString());
        term2("Total : " + p1.totalDuPot());
        term(p2.croupierPotToToString());
        term2("Total : " + p2.totalDuPot());
    }

    public static void allerDeLavant(Player p1, Player p2) {
        Random rand2 = new Random();
        int rez2 = rand2.nextInt(4)+1;
        switch(rez2) {
            case 1 -> term("\nCroupier : Tu penses m'effrayer ? C'est ce qu'on va voir !");
            case 2 -> term("\nCroupier : T'as une paire d'AS ? Mon oeil oui !");
            case 3 -> term("\nCroupier : AHAH ! Je m'y attendais à celle-là !");
            case 4 -> term("\nCroupier : Tu bluffes Martini !");
        }
        Scanner sc2 = new Scanner(System.in);
        String choix2;
        ArrayList<Integer> miseDeP1;
        int valeur = -1;
        int counter = 0;
        do {
            if(counter > 1) {
                term2("\nCroupier : Capitaine, zêtes bourré ou quoi ?! Faut miser les cartes dans ta Mise. Allez on recommence ! ");
            }
            term2("\nCroupier : Tu veux relancer de combien mon coco ? Moi, j'ai pas besoin de te suivre.");
            choix2 = sc2.nextLine();
            try {
                valeur = Integer.parseInt(choix2); // user donne la valeur qu'il veut mettre dans son pot
            } catch (NumberFormatException e) {
                term("\nCroupier : Oh nan mais Capitaine ! Je veux un chiffre, pas des lettres !");
            }
            miseDeP1 = p1.getMise().mise; // List de ce que P1 peut miser
            counter ++;
        } while (!miseDeP1.contains(valeur));

        // mettre dans le pot de p1 valeur
        tarouket.clearScreen();
        term2("Croupier : tu as misé " + valeur + "!");
        p1.ajouterAuPot(valeur);
        tarouket.afficherPots(p1, p2);
        term2(p1.toString());
    }

    public static void seCoucher(Player p1, Player p2, ArrayList<Card> cartes ) {
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
        tarouket.clearScreen();
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
        p2.getMise().mise.addAll(potDeP1);

        // Mettre le pot de p2 dans la mise de p2
        ArrayList<Integer> potDeP2 = new ArrayList<>(p2.getPot());
        p2.getMise().mise.addAll(potDeP2);

        // Vider le pot des joueureuses
        p1.Viderpot();
        p2.Viderpot();

        p2.getMise().mise.sort(null);
    }

}
