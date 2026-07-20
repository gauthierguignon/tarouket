package src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        return "Le Croupier avait en main : " + this.mainToString() + "\nMise du Croupier : " + this.getMise().toString(); 
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
    public Choix demanderChoix(Vue vue, Tarouket tarouket) {
        // s'éxécute seulement si le joueur à misé juste avant
        if(this == tarouket.getpotentielleVictime()) {
            return Choix.BON_DEBARRAS;
        }
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

    private boolean chercherUneCarte(int objectif, List<Integer> cartes) {
        if(cartes.contains(objectif)) {
            this.ajouterAuPot(objectif);
            return true;
        }
        return false;
    }

    private boolean chercherDeuxCartes(int objectif, List<Integer> cartes) {
        // s'il existe 2 cartes qui atteignent ou dépassent l'objectif
        // on prend les 2 cartes qui ont le plus petit dépassement

        int meilleuredepassement = Integer.MAX_VALUE;
        int depassement;
        int carte1 = -1;
        int carte2 = -1;


        for (int i = 0; i < cartes.size() - 1; i++) {
            for (int j = i + 1; j < cartes.size(); j++) {
                int somme = cartes.get(i) + cartes.get(j);
                if(somme >= objectif) {
                    depassement = somme - objectif;
                    if(depassement < meilleuredepassement) {
                        carte1 = i;
                        carte2 = j;
                        meilleuredepassement = depassement;
                    }
                }
            }
        }
        if(carte1 != -1) {
            int valeur1 = cartes.get(carte1);
            int valeur2 = cartes.get(carte2);
            this.ajouterAuPot(valeur1);
            this.ajouterAuPot(valeur2);
            return true;
        }
        return false;
    }

    private boolean chercherCombinaison (int objectif, List<Integer> cartes) {
        // Si on ne peut pas atteindre l'objectif avec 1 ou 2 cartes
        // Alors on prend la carte la plus haute et on ajoute des cartes faibles
        // jusqu'à atteindre ou dépasser l'objectif
        // on renvoie la meilleure combinaison si elle existe
        // C'est pas parfait mais c'est ce que ferait un humain

        int meilleuredepassement = Integer.MAX_VALUE;
        List<Integer> meilleureCombinaison = new ArrayList<>();
        int totalMeilleurCombinaison = 0;

        for (int i = 1; i < cartes.size(); i++) {
            List<Integer> combinaison = new ArrayList<>();
            int indexGrosse = cartes.size() - i;
            int carteGrosse = cartes.get(indexGrosse);
            combinaison.add(carteGrosse);
            int totalCombinaison = carteGrosse;

            for (int j = 0; j < cartes.size(); j++) {
                if (j == indexGrosse) continue;
                int carte = cartes.get(j);
                combinaison.add(carte);
                totalCombinaison += carte;
                if (totalCombinaison >= objectif) break;
            }

            int depassement = totalCombinaison - objectif;
            if (totalCombinaison >= objectif && depassement < meilleuredepassement) {
                meilleuredepassement = depassement;
                meilleureCombinaison = new ArrayList<>(combinaison);
                totalMeilleurCombinaison = totalCombinaison;

                if (meilleuredepassement == 0) break; // impossible de faire mieux
            }
        }

        if (totalMeilleurCombinaison < objectif) {
            return false;
        }
        this.ajouterAuPotList(meilleureCombinaison);
        return true;
    }

    @Override
    public Choix bonDebarras(Vue vue, Tarouket tarouket) {

        vue.clearScreen();
        vue.croupierParleRandom("Bon bas ... j'ai pas le choix. Je dois au moins égaliser ton pot.");
        vue.afficherPots(tarouket.getPlayer(), this);

        int objectif = tarouket.getPlayer().totalDuPot() - this.totalDuPot();
        ArrayList<Integer> cartes = this.getMise().getMise();
        Collections.sort(cartes);

        if(chercherUneCarte(objectif, cartes)) {
            vue.afficher(this.potToString(), 1);
            vue.afficher("Total : " + this.totalDuPot(), 2);
            tarouket.initBonDebarras();
            return Choix.AVANT;
        }
        if(chercherDeuxCartes(objectif, cartes)) {
            vue.afficher(this.potToString(), 1);
            vue.afficher("Total : " + this.totalDuPot(), 2);
            tarouket.initBonDebarras();
            return Choix.AVANT;
        }
        if(chercherCombinaison(objectif, cartes)) {
            vue.afficher(this.potToString(), 1);
            vue.afficher("Total : " + this.totalDuPot(), 2);
            tarouket.initBonDebarras();
            return Choix.AVANT;
        }
        return Choix.TAPIS;
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
        vue.afficher("Croupier : Je mise " + valeur + " !", 2);
        vue.afficherPots(tarouket.getPlayer(), tarouket.getCroupier());
        vue.afficher(tarouket.getPlayer().toString(), 2);
        tarouket.mettreAJourEnAvant();
    }

    @Override
    public void faireTapis(Vue vue, Tarouket tarouket) {
        vue.clearScreen();
        vue.croupierParleRandom("Je fais tapis !");
        
        // Ajouter au pot toute la mise
        this.ajouterAuPotTouteLaMise(this.getMise().getMise());
        // Vider la mise du joueur
        this.getMise().clear();

        vue.afficherPots(tarouket.getPlayer(), this);
        tarouket.mettreAJourEnAvant();
    }

    @Override
    public void seCoucher(Vue vue, Player p) {
        vue.croupierParleRandom("Pas besoin des prendre des risques",
        "Je joue intelligemment moi !",
        "Il faut de la sagesse pour abandonner une main."
        );
        vue.afficher("Croupier : J'avais en main " + Vue.conversionCartesCouleurs(new ArrayList<>(Arrays.asList(this.getCartes()))), 2);
        vue.exigerOui("On passe à la suite ? (oui)");
        vue.clearScreen();
        p.recupererPots(this);
    }

    @Override
    public String getNom() {
        return "Croupier";
    }

}