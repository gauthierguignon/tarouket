package src;

import java.util.ArrayList;
import java.util.List;

public class Mise {
    
    private boolean paire;
    private ArrayList<Integer> mise;

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

    public boolean contains(int valeur) { 
        return mise.contains(valeur); 
    }

    //retire la 1ere occurence de valeur trouvée
    public void remove(int valeur) {
        mise.remove(Integer.valueOf(valeur));
    }

    public void addAll(List<Integer> l){
        mise.addAll(l);
    }

    // retire la première valeur
    public void removeFirst() {
        mise.remove(0);
    }

    public void clear() {
        mise.clear();
    }

    public void sort() {
        mise.sort(null);
    }

    public boolean isEmpty() {
        return mise.isEmpty();
    }

    @Override
    public String toString() {
        return mise.toString();
    }

    public ArrayList<Integer> getMise() {
        return mise;
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

    public boolean isPaire() {
        return this.paire;
    }

    public void changePaire() {
        this.paire = !paire;
    }

}
