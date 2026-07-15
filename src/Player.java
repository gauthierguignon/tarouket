package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Player {
    
    private Card[] cartes;
    private Mise mise;
    private ArrayList<Integer> pot;

    public Player(boolean bool) {
        this.cartes = new Card[2];
        this.mise = new Mise(bool);
        this.pot = new ArrayList<>();
    }

    public void petiteBlinde() {
        // On s'assure que la mise est triée
        mise.sort();
        // mettre dans le pot l'atout le plus faible
        this.pot.add(this.mise.getMise(0));
        //enlever de mise cet atout
        this.mise.removeFirst();
    }

    public void ajouterAuPot(int valeur) {
        if (!this.mise.contains(valeur)) {
            throw new IllegalArgumentException("Valeur " + valeur + " absente de la mise du joueur");
        }
        this.pot.add(valeur);
        this.mise.remove(valeur);
    }
    
    public void ajouterAuPotTouteLaMise(Collection<Integer> valeurs) {
        // Ajouter au pot toutes les valeurs de la mise
        this.pot.addAll(valeurs);
        // Vider la mise du joueur
        this.getMise().clear();
    }

    public void ajouterAuPotList(Collection<Integer> valeurs){
        for(Integer valeur : valeurs) {
            ajouterAuPot(valeur);
        }
    }

    public String potToString() {
        String output = "Votre pot est de " + ": ";
        for(int i = 0; i < pot.size(); i++) {
            output += pot.get(i) + " ";
        }
        return output;
    }

    public Card[] getCartes() {
        return this.cartes;
    }

    public Mise getMise() {
        return this.mise;
    }

    public int totalDeMise() {
        return this.mise.total();
    }

    public int totalDuPot() {
        int counter = 0;
        for(int i = 0; i < this.pot.size(); i++) {
            counter = counter + this.pot.get(i);
        }
        return counter;
    }

    protected ArrayList<Integer> getPot() {
        return this.pot;
    }

    public void Viderpot() {
        this.pot.clear();
    }

    @Override
    public String toString() {
        return "Vous avez en main : " + this.mainToString() + "\nVotre mise : " + mise.toString();
    }

    public String mainToString() {
        return Vue.conversionCartesCouleurs(new ArrayList<>(Arrays.asList(cartes)));
    }

    public void setCartes(Card c1, Card c2) {
        cartes[0] = c1;
        cartes[1] = c2;
    }

    // adversaire perd son pot et tout retourne dans la Mise de Player
    public void recupererPots(Player adversaire) {
        this.getMise().addAll(adversaire.getPot());
        this.getMise().addAll(this.getPot());
        adversaire.Viderpot();
        this.Viderpot();
        this.getMise().sort();
    }

    public void changePaire() {
        this.mise.changePaire();
    }

    public boolean isPaire() {
        return this.mise.isPaire();
    }

    public Choix demanderChoix(Vue vue, Tarouket tarouket) {

        String choix;

        if(this == tarouket.getpotentielleVictime()) {
            return Choix.BON_DEBARRAS;
        } else {
            String question = "Croupier : Tu peux checker (1), aller de l'avant (2) ou te coucher(3)";
            choix = vue.demanderChoix(question, "1", "2", "3");
        }

            switch(choix) {
                case "1" -> {
                    vue.afficher("Vous : Je checke !", 2); 
                    return Choix.CHECK;
                }
                case "2" -> {
                    vue.afficher("Vous : Je vais de l'avant !", 2);
                        return Choix.AVANT;
                    }
                case "3" -> {
                    vue.afficher("Vous : Je me couche !", 2);
                    return Choix.COUCHER;
                }
            }
            throw new IllegalStateException("Choix impossible : " + choix); //ne sera jamais exécuté
    }

    public void seCoucher(Vue vue, Player croupier) {
        vue.croupierParleRandom("Eh ben mon coco ! T'es pas un aventurier toi ... ", 
            "Ah ouais ?! Pas très courageux.",
            "Tu la joues sur le long terme. Tu as raison !",
            "Faudrait peut-être jouer un jour, nan ?",
            "On se demande bien qui va gagner",
            "Chacun sa technique"
        );
        vue.afficher("Le croupier avait en main : " + Arrays.toString(croupier.getCartes()), 2);
        vue.exigerOui("On passe à la suite ? (oui)");
        vue.clearScreen();
        croupier.recupererPots(this);
    }

    public Choix allerDeLavant(Vue vue, Tarouket tarouket) {

        // Premier tour
        String choix = vue.demanderChoix("\nCroupier : Tu veux relancer (1) ou faire tapis (2) ?", "1", "2");

        switch (choix) {
            case "1" -> {
                this.relancer(vue, tarouket);
            }
            case "2" -> {
                this.faireTapis(vue, tarouket);
                return Choix.TAPIS;
            }
        }

        int counter = 2;
        // Tours suivants sauf si tapis
            do {
                String prompt = "\nCroupier : Tu veux relancer une " + counter + "e fois (oui) ou tu t'arrêtes là (non) ?";
                choix = vue.demanderChoix(prompt, "oui", "non");

                if (choix.equals("OUI")) {
                    vue.afficher("\nVous : Je relance une " + counter + "e fois", 1);
                    this.relancer(vue, tarouket);
                    counter ++;
                } else {
                    vue.afficher("\nVous : Je m'arrête là.", 2);
                }
            } while (!choix.equals("NON"));
        return Choix.AVANT;
    }

    public void relancer(Vue vue, Tarouket tarouket) {
        vue.afficher("\nVous : Je relance !", 2);
        vue.croupierParleRandom("Tu penses m'effrayer ? C'est ce qu'on va voir !",
            "T'as une paire d'AS ? Mon oeil oui !",
            "AHAH ! Je m'y attendais à celle-là !",
            "Tu bluffes Martini !"
        );
        int valeur = vue.demanderMise(tarouket.getPlayer().getMise().getMise());
        // mettre dans le pot de p1 valeur
        vue.clearScreen();
        vue.afficher("Croupier : Tu as misé " + valeur + " !", 2);
        this.ajouterAuPot(valeur);
        vue.afficherPots(tarouket.getPlayer(), tarouket.getCroupier());
        vue.afficher(tarouket.getPlayer().toString(), 2);
        tarouket.mettreAJourEnAvant();
    }

    public void faireTapis(Vue vue, Tarouket tarouket) {
        vue.clearScreen();

        vue.croupierParleRandom("Tonnere de Brest !", "Nom d'un pilon vermoulu !");
        vue.croupierParleRandom("Tu fais tapis !");
        
        // Ajouter au pot toute la mise
        this.ajouterAuPotTouteLaMise(this.getMise().getMise());

        vue.afficherPots(this, tarouket.getCroupier());
        vue.afficher(this.toString(), 1);
        tarouket.mettreAJourEnAvant();
    }

    public Choix bonDebarras(Vue vue, Tarouket tarouket) {
        // le joueur peut être forcé de faire tapis
        // donc faut retourner le bon choix
        
        if(this.totalDeMise() + totalDuPot() < tarouket.autreJoueur(this).totalDeMise()) {
            vue.croupierParleRandom("C'est un bon débarras ! Tu n'as pas le choix, tu dois faire Tapis !");
            return Choix.TAPIS;
        }
        do {
            vue.croupierParleRandom("C'est un bon débarras ! Tu dois au moins égaliser le pot adverse !");
            int valeur = vue.demanderMise(tarouket.getPlayer().getMise().getMise());
            vue.clearScreen();
            vue.afficher("Croupier : Tu as misé " + valeur + " !", 1);
            this.ajouterAuPot(valeur);
            vue.afficherPots(tarouket.getPlayer(), tarouket.getCroupier());
            vue.afficher(tarouket.getPlayer().toString(), 2);
        } while (this.totalDuPot() < tarouket.autreJoueur(this).totalDuPot());
        // le bon debarras est effectué
        // On remet les compteurs à zéro
        tarouket.initBonDebarras();
        return Choix.AVANT;
    }

    public String getNom() {
        return "Joueur";
    }


}
