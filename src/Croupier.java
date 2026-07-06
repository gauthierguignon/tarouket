package src;

import java.util.Random;

public class Croupier extends Player {

    public Croupier(boolean bool) {
        super(bool);
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

    


}
