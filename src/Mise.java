package src;

import java.util.ArrayList;

public class Mise {
    
    private final boolean paire;
    public ArrayList<Integer> mise;

    public Mise(boolean paire) {
        this.paire = paire;
        mise = new ArrayList<>();
        if (paire) {
            peupleTrue(this.mise);
        } else {
            peupleFalse(this.mise);
        }
    }

    public Integer getMise(int i) {
        return this.mise.get(i);
    }

    public int total() {
        int counter = 0;
        for(int i = 0; i < this.mise.size(); i++) {
            counter = counter + this.mise.get(i);
        }
        return counter;
    }

    private void peupleTrue(ArrayList<Integer> mise) {
        for(int i = 1; i < 21; i++) {
            if(i%2==0) mise.add(i);
        }
        mise.remove(4); // on retire le 10
    }

    private void peupleFalse(ArrayList<Integer> mise) {
        for(int i = 1; i < 21; i++) {
            if(i%2==1) mise.add(i);
        }
    }

    @Override
    public String toString() {
        String output = "";
        for(int i = 0; i < mise.size(); i++) {
            output += mise.get(i) + "; ";
        }
        return output;
    }

    public boolean isPaire() {
        return this.paire;
    }

}
