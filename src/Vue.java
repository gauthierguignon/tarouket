package src;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Vue {
    private final Scanner sc;

    public Vue() {
        this.sc = new Scanner(System.in);
    }

    // Afichage dans le terminal '\n' x1
    public void afficher1(String phrase) { 
        for(int i = 0; i < phrase.length(); i++) {
            System.out.print(phrase.charAt(i));
            try {
                Thread.sleep(30); //30
            } catch (InterruptedException e) {}
        }
        System.out.print("\n");
    }

    // Affichage dans le terminal '\n' x2
    public void afficher2(String phrase) { 
        for(int i = 0; i < phrase.length(); i++) {
            System.out.print(phrase.charAt(i));
            try {
                Thread.sleep(30); //30
            } catch (InterruptedException e) {}
        }
        System.out.print("\n\n");
    }

    // Affichage dans le terminal '\n' x0
    public void afficher(String phrase) { 
        for(int i = 0; i < phrase.length(); i++) {
            System.out.print(phrase.charAt(i));
            try {
                Thread.sleep(30); //30
            } catch (InterruptedException e) {}
        }
    }    

    public void clearScreen() { 
        System.out.print("\033[H\033[J");
    }

    public void exigerOui(String question) {
        String choix = "";
        do {
            if(choix.equals("non")) this.afficher2("\nCroupier : Nan mais t'as pas le choix en fait ... ");
            this.afficher2(question);
            this.afficher("Vous : ");
            choix = sc.nextLine();
        } while (!choix.equalsIgnoreCase("oui"));
    }

    public String demanderChoix(String question, String... valeursValides) {
        String choix;
        do {
            afficher2(question);
            afficher("Vous : ");
            choix = sc.nextLine();
        } while (!estValide(choix, valeursValides));
        choix = choix.toUpperCase(); // Renvoie toujours la réponse user en majuscule
        return choix;
    }

    public int demanderMise(List<Integer> valeursValides) {
        int valeur = -1;
        int counter = 0;
        do {
            if (counter > 1) {
                afficher2("\nCroupier : Capitaine, zêtes bourré ou quoi ?! Faut miser les cartes dans ta Mise. Allez on recommence !");
            }
            afficher2("\nCroupier : Tu veux relancer de combien mon coco ?");
            afficher("Vous : Je relance de ");
            String choix = sc.nextLine(); // user donne la valeur qu'il veut mettre dans son pot
            try {
                valeur = Integer.parseInt(choix); // valeur vaut -1 si error
            } catch (NumberFormatException e) {
                afficher("\nCroupier : Oh nan mais Capitaine ! Je veux un chiffre, pas des lettres !");
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
        this.afficher1(p1.potToString());
        this.afficher2("Total : " + p1.totalDuPot());
        this.afficher1(croupier.potToString());
        this.afficher2("Total : " + croupier.totalDuPot());
    }

    public void croupierParleRandom(String... phrases) {
        Random rand = new Random();
        int index = rand.nextInt(phrases.length);
        this.afficher2("\nCroupier : " + phrases[index]);
    }

    public void wait(int temps) {
        try {
            Thread.sleep(temps);
        } catch (InterruptedException e) { }
    }

}
