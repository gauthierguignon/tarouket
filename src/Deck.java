package src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    
    private final ArrayList<Card> deck;
    private final Random rand = new Random();

    public Deck() {
        this.deck = new ArrayList<>();
        peuple(this.deck);
    }

    @Override
    public String toString() {
        String output = "";
        int counter = 0;
        for(int i = 0; i < this.deck.size(); i++) {
            output = output + this.deck.get(i) + "; ";
            counter ++;
            if(counter == 14) {
                output+="\n";
                counter = 0;
            }
        }
        return output;
    }

    private void peuple(ArrayList<Card> deck) {

        // créer 56 cartes vides
        for (int i = 0; i < 56; i++) {
            Card card = new Card();
            deck.add(card);
        }

        // mettre les couleurs
        int counterColor = 0;
        Color[] tabColor = Color.values();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 14; j++) {
                this.deck.get(j+counterColor).setColor(tabColor[i]);
            }
            counterColor = counterColor + 14;
        }

        // mettre les faces
        int counterFace = 0;
        Face[] tabFace = Face.values();
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 14; j++) {
                this.deck.get(j+counterFace).setFace(tabFace[j]);
            }
            counterFace = counterFace + 14;
        }
    }

    public void shuffle() {
        Collections.shuffle(deck);
        Collections.shuffle(deck);
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public Card drawRandomCard() {
        int index = rand.nextInt(deck.size());
        Card carte = deck.get(index);
        deck.remove(index);
        return carte;
    }

    public void addAll(ArrayList<Card> main) {
        deck.addAll(main);
    }

}
