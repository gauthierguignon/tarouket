package src;

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

    @Override
    public Choix demanderChoix(Vue vue) {
        int alea = rand.nextInt(100); // de 0 à 99
        if (alea < 45) {
            vue.croupierParleRandom("Je checke !"); 
            return Choix.CHECK;
        } else if (alea >= 45 && alea < 95) {
            vue.croupierParleRandom("Je vais de l'avant !");
            return Choix.AVANT;
        } else if (alea >= 95 && alea < 98) {
            vue.croupierParleRandom("Je me couche");
            return Choix.TAPIS;
        } else {
            vue.croupierParleRandom("Je fais tapis !");
            return Choix.COUCHER;
        }
    }


}
