package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Vue {
    private final Scanner sc;

    public static final String RESET = "\u001B[0m";
    public static final String ROUGE = "\u001B[31m";
    public static final String BLEU   = "\u001B[34m";
    
    public Vue() {
        this.sc = new Scanner(System.in);
    }
    
    public static String getRESET() {
        return RESET;
    }

    public static String CarteRougetoString(Card c) {
        return "" + ROUGE + c.toString() + RESET;
    }

    public static String CarteNoirtoString(Card c) {
        return "" + BLEU + c.toString() + RESET;
    }

    public void afficher(String phrase, int n) {
        int i = 0;
            while (i < phrase.length()) {
                if (phrase.charAt(i) == '\u001B') {
                    // on a trouvé le début d'un code couleur, on cherche son 'm' de fin
                    int fin = phrase.indexOf('m', i);
                    System.out.print(phrase.substring(i, fin + 1));
                    i = fin + 1;
                } else {
                    // caractère normal
                    System.out.print(phrase.charAt(i));
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {}
                    i++;
                }
            }
        for(int counter = 0; counter < n; counter++) {
            System.out.println("");
        }
    }

    public void clearScreen() { 
        System.out.print("\033[H\033[J");
    }

    public void exigerOui(String question) {
        String choix = "";
        do {
            if(choix.equals("non")) this.afficher("\nCroupier : Nan mais t'as pas le choix en fait ... ", 2);
            this.afficher(question, 2);
            this.afficher("Vous : ",0);
            choix = sc.nextLine();
        } while (!choix.equalsIgnoreCase("oui"));
    }

    public String demanderChoix(String question, String... valeursValides) {
        String choix;
        do {
            afficher(question, 2);
            afficher("Vous : ", 0);
            choix = sc.nextLine();
        } while (!estValide(choix, valeursValides));
        choix = choix.toUpperCase(); // Renvoie toujours la réponse user en majuscule
        return choix;
    }

    public int demanderMise(List<Integer> valeursValides) {
        int valeur = -1;
        int counter = 0;
        do {
            if (counter >= 1) {
                afficher("\nCroupier : Capitaine, zêtes bourré ou quoi ?! Faut miser les cartes dans ta Mise. Allez on recommence !", 2);
            }
            afficher("\nCroupier : Tu veux relancer de combien mon coco ?", 2);
            afficher("Vous : Je relance de ", 0);
            String choix = sc.nextLine(); // user donne la valeur qu'il veut mettre dans son pot
            try {
                valeur = Integer.parseInt(choix); // valeur vaut -1 si error
            } catch (NumberFormatException e) {
                afficher("\nCroupier : Oh nan mais Capitaine ! Je veux un chiffre, pas des lettres !", 1);
            }
            counter++;
        } while (!valeursValides.contains(valeur));
        return valeur;
    }

    public static boolean estValide(String choix, String... valeursValides) {
        for (String valeur : valeursValides) {
            if (valeur.equalsIgnoreCase(choix)) {
                return true;
            }
        }
        return false;
    }

    public void afficherPots(Player p1, Croupier croupier) {
        this.afficher(p1.potToString(), 1);
        this.afficher("Total : " + p1.totalDuPot(), 2);
        this.afficher(croupier.potToString(), 1);
        this.afficher("Total : " + croupier.totalDuPot(), 2);
    }

    public void croupierParleRandom(String... phrases) {
        Random rand = new Random();
        int index = rand.nextInt(phrases.length);
        this.afficher("\nCroupier : " + phrases[index], 2);
    }

    public void wait(int temps) {
        try {
            Thread.sleep(temps);
        } catch (InterruptedException e) { }
    }

    public static String conversionCartesCouleurs(ArrayList<Card> riviere) {
        StringBuilder output = new StringBuilder();
        for(Card c : riviere) {
            if(c.getColor() == Color.CARREAU || c.getColor() == Color.COEUR) {
                output.append(CarteRougetoString(c)).append(" ");
            } else {
                output.append(CarteNoirtoString(c)).append(" ");
            }
        }
        return output.toString();
    }

    public static void main(String[] args) {

        Vue vue = new Vue();
        Deck deck = new Deck();

        ArrayList<Card> riviere = new ArrayList<>();

        for(int i = 0; i < 10; i++) {
            riviere.add(deck.drawRandomCard());
        }

        vue.afficher(Vue.conversionCartesCouleurs(riviere), 2);

        // Revoir complètement comment on affiche un player
        // il faut passer en paramètre des Card pour accéder à getColor()


        Tarouket tarouket = new Tarouket();
        tarouket.getPlayer().setCartes(deck.drawRandomCard(), deck.drawRandomCard());

        vue.afficher(tarouket.getPlayer().toString(), 1);

        
    }

}
