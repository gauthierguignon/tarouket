package src;

public class GestionPot {
    
    private Player enAvant;
    private int toursConsecutifsEnAvant;
    private Player potentielleVictime;
    
    public GestionPot() {
        this.reinitialiser();
    }
    
    /**
     * Met à jour l'état "en avant" après chaque tour d'enchères.
     * Déclenche automatiquement le bon débarras si les conditions sont remplies.
     * 
     * @param p1 Premier joueur
     * @param croupier Croupier
     */
    public void mettreAJourEnAvant(Player p1, Croupier croupier) {
        int totP1 = p1.totalDuPot();
        int totCroupier = croupier.totalDuPot();
        
        Player nouveauEnAvant;
        
        if (totP1 > totCroupier) {
            nouveauEnAvant = p1;
        } else if (totP1 < totCroupier) {
            nouveauEnAvant = croupier;
        } else {
            // Égalité : personne n'est en avant
            nouveauEnAvant = null;
            this.toursConsecutifsEnAvant = 0;
            this.potentielleVictime = null;
            return;
        }
        
        // Si c'est le même joueur qui reste en avant
        if (nouveauEnAvant == this.enAvant) {
            this.toursConsecutifsEnAvant++;
        } else {
            // Changement de joueur en avant : réinitialiser le compteur
            this.enAvant = nouveauEnAvant;
            this.toursConsecutifsEnAvant = 1;
        }
        
        // Si 2 tours consécutifs : bon débarras déclenché
        if (this.toursConsecutifsEnAvant >= 2) {
            this.potentielleVictime = (this.enAvant == p1) ? croupier : p1;
        } else {
            this.potentielleVictime = null;
        }
    }
    
    /**
     * Signale qu'un bon débarras a été résolu.
     * Réinitialise les compteurs pour éviter 2 bon débarras d'affilée.
     */
    public void bonDebarrasResolu() {
        this.enAvant = null;
        this.potentielleVictime = null;
        this.toursConsecutifsEnAvant = 0;
    }
    
    public Player getPotentielleVictime() {
        return this.potentielleVictime;
    }
    
    public Player getEnAvant() {
        return this.enAvant;
    }
    
    public int getToursConsecutifs() {
        return this.toursConsecutifsEnAvant;
    }
    
    public void reinitialiser() {
        this.enAvant = null;
        this.potentielleVictime = null;
        this.toursConsecutifsEnAvant = 0;
    }
}
