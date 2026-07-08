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

    public String coinToss() {
        boolean bool = this.rand.nextBoolean();
        String rez;
        if(bool) {
            rez = "PILE";
        } else {
            rez = "FACE";
        }
        return rez;
    }

    @Override
    public EtatManche preFlop(Vue vue) {
        int alea = rand.nextInt(100); // de 0 à 99
        if (alea < 45) {
            vue.croupierParleRandom("\nCoupier : Je checke !"); 
            return EtatManche.CHECK;
        } else if (alea >= 45 && alea < 90) {
            vue.croupierParleRandom("Je vais de l'avant !");
            return EtatManche.AVANT;
        } else if (alea >= 95 && alea <= 99) {
            vue.croupierParleRandom("Je me couche");
            return EtatManche.TAPIS;
        } else {
            vue.croupierParleRandom("Je fais tapis !");
            return EtatManche.COUCHER;
        }
    }


}
