package src;

import java.util.ArrayList;
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
            term(p1.toString());
            
            tarouket.petiteBlinde(p1, p2);

            // Premier choix
            
            Scanner sc = new Scanner(System.in);
            String choix;
            do { 
                term("Croupier : Tu peux checker (1), aller de l'avant (2) ou te coucher(3)");
                choix = sc.nextLine();
            } while (!choix.equals("1") && !choix.equals("2") && !choix.equals("3"));

            switch(choix) {

                case "1" -> { // Player1 à checké

                }

                case "2" -> {  // Player1 va de l'avant
                    Random rand2 = new Random();
                    int rez2 = rand2.nextInt(4)+1;
                    switch(rez2) {
                        case 1 -> term("\nTu penses m'effrayer ? C'est ce qu'on va voir !");
                        case 2 -> term("\nT'as une paire d'AS ? Mon oeil oui !");
                        case 3 -> term("\nAHAH ! Je m'y attendais à celle-là !");
                        case 4 -> term("\nTu bluffes Martini !");
                    }
                    Scanner sc2 = new Scanner(System.in);
                    String choix2;
                    ArrayList<Integer> miseDeP1;
                    int valeur = -1;
                    int counter = 0;
                    do {
                        if(counter > 2) {
                            term("Capitaine, zêtes bourré ou quoi ?! Faut miser les cartes dans ta Mise. Allez on recommence ! ");
                        }
                        term("Tu veux relancer de combien mon coco ? Moi, j'ai pas besoin de te suivre.");
                        choix2 = sc2.nextLine();
                        try {
                            valeur = Integer.parseInt(choix2); // user donne la valeur qu'il veut mettre dans son pot
                        } catch (NumberFormatException e) {
                            term("Oh nan mais Capitaine ! Je veux un chiffre, pas des lettres !");
                        }
                        miseDeP1 = p1.getMise().mise; // List de ce que P1 peut miser
                        counter ++;
                    } while (!miseDeP1.contains(valeur));

                    // mettre dans le pot de p1 valeur
                    p1.ajouterAuPot(valeur);
                    tarouket.afficherPots(p1, p2);
                    term(p1.toString());

                }


                case "3" -> { // Player 1 se couche
                    Random rand3 = new Random();
                    int rez3 = rand3.nextInt(6)+1;
                    switch(rez3) {
                        case 1 -> term("Eh ben mon coco ! T'es pas un aventurier toi ... ");
                        case 2 -> term("Ah ouais ?! Pas très courageux.");
                        case 3 -> term("Tu l'as joues sur le long terme. Tu as raison !");
                        case 4 -> term("Faudrait peut-être jouer un jour, nan ?");
                        case 5 -> term("On se demande qui va gagner");
                        case 6 -> term("Chacun sa technique");
                    }
                    System.out.println("C'est la fin du tour ! Je redistribue les cartes.");

                } 


            }

        } while (false); //while (finDePartie(p1, p2));


    }
    
    // Tirage a pile ou face
    public static boolean PileOuFace() {
        
        Scanner sc = new Scanner(System.in);
        String choix;
        do {
            term("Croupier : Hello Moussaillon ! Tu es le joueur 1. Tu dis Pile ou Face ? ");
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
    public static void term(String phrase) {
        for(int i = 0; i<phrase.length(); i++) {
            System.out.print(phrase.charAt(i));
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                
            }
        }
        System.out.println("\n");
    }

    // Définition d'une fin de partie
    public static boolean finDePartie(Player p1, Player p2) {
        if (p1.totalDeMise() == 0 || p2.totalDeMise() == 0) return false;
        return true;
    }

    // Mise automatique de la petite Blinde
    public static void petiteBlinde(Player p1, Player p2) {
            
        Scanner sc = new Scanner(System.in);
        String choix;
        do {
            term("Vous devez miser la petite blinde. Tu vas miser oui ou non ? ");
            System.out.print("Vous : ");
            choix = sc.nextLine();
        } while (!choix.equalsIgnoreCase("oui"));

            System.out.print("\033[H\033[J");

            p1.petiteBlinde();
            p2.petiteBlinde();
            
            tarouket.afficherPots(p1, p2);

            term(p1.toString());
    }

    public static void afficherPots(Player p1, Player p2) {
        term(p1.potToString());
        term("Total : " + p1.totalDuPot());
        System.out.println("\n");
        term(p2.potToString());
        term("Total : " + p2.totalDuPot());
    }

}
