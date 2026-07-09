package src;

import java.util.Arrays;
import java.util.Random;

public class Croupier extends Player {

    private Random rand;

    public Croupier(boolean bool) {
        super(bool);
        rand = new Random();
    }    

    @Override
    public String potToString() {
        String output = "Le pot du croupier " + ": ";
            for(int i = 0; i < this.getPot().size(); i++) { 
                output += this.getPot().get(i) + " "; 
            }
        return output;
    }

    public static String coinToss() {
        Random rand = new Random();
        boolean bool = rand.nextBoolean();
        String rez;
        if(bool) {
            rez = "PILE";
        } else {
            rez = "FACE";
        }
        return rez;
    }

    public String toString() {
        return "Le Croupier avait en main : " + this.getCartes()[0] + " " + this.getCartes()[1] + "\nMise du Croupier : " + this.getMise().toString(); 
    }

    @Override
    public void ajouterAuPot(int valeur) {
        if (!this.getMise().contains(valeur)) {
            throw new IllegalArgumentException("Valeur " + valeur + " absente de la mise du Croupier");
        }
        this.getPot().add(valeur);
        this.getMise().remove(valeur);
    }

    @Override
    public Choix demanderChoix(Vue vue) {
        int alea = rand.nextInt(100); // de 0 à 99
        if (alea < 45) {
            vue.croupierParleRandom("Je checke !"); 
            return Choix.CHECK;
        } else if (alea >= 45 && alea < 90) {
            vue.croupierParleRandom("Je vais de l'avant !");
            return Choix.AVANT;
        } else {
            vue.croupierParleRandom("Je me couche");
            return Choix.COUCHER;
        }
    }

    @Override
    public Choix allerDeLavant(Vue vue, Tarouket tarouket) {
        // Est-ce que le croupier mise ou fait tapis
        int alea = rand.nextInt(100);
        if(alea < 95) {
            this.relancer(vue, tarouket);
            return Choix.AVANT;
            // plus tard ajouter la possiblité de miser plusieurs atouts à la relance
            // if(alea > 70) {
            // int n = rand.nextInt();
            // relancer(n, vue, tarouket) ...
        } else {
            this.faireTapis(vue, tarouket);
            return Choix.TAPIS;
        }
    }

    @Override
    public void relancer(Vue vue, Tarouket tarouket) {
        vue.croupierParleRandom("Accroche toi bien, je relance !");
        vue.exigerOui("Tu es prêt ? (oui)");
        // Pour l'instant on va dire que le croupier mise de façon aléatoire
        int indice = rand.nextInt(this.getMise().getMise().size());
        int valeur = this.getMise().getMise(indice);

        this.ajouterAuPot(valeur);
        // affichage
        vue.clearScreen();
        vue.afficher2("Croupier : Je mise " + valeur + " !");
        vue.afficherPots(tarouket.getPlayer(), tarouket.getCroupier());
        vue.afficher2(tarouket.getPlayer().toString());
    }

    @Override
    public void faireTapis(Vue vue, Tarouket tarouket) {
        vue.clearScreen();
        vue.croupierParleRandom("Je fais tapis !");
        
        // Ajouter au pot toute la mise
        this.ajouterAuPot(this.getMise().getMise());
        // Vider la mise du joueur
        this.getMise().clear();

        vue.afficherPots(tarouket.getPlayer(), this);
    }

    @Override
    public void seCoucher(Vue vue, Player croupier) {
        vue.croupierParleRandom("Pas besoin des prendre des risques",
        "Je joue intelligemment moi !",
        "Il faut de la sagesse pour abandonner une main."
        );
        vue.afficher2("Croupier : J'avais en main " + Arrays.toString(croupier.getCartes()));
        vue.exigerOui("On passe à la suite ? (oui)");
        vue.clearScreen();
        croupier.recupererPots(this);
    }


    public static void main(String[] args) {
        // mettre le vue du tarouket en public
            Tarouket tarouket = new Tarouket();
            tarouket.getCroupier().relancer(tarouket.vue, tarouket);
    }



}