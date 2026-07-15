# To Do :
 
## Affichage : 
	quand ya le turn : 
	- ré-afficher les cartes en mains du user
	- ré-afficher la mise du croupier
	... 
	Est-ce qu'on pourrait séparer l'affiichage des informations : mains + mise + qui est en avant 
	des informations courantes : dialogues ... 
	Compliqué mais A essayer
	
	Le plus simple : changer clearScreen() pour toujours afficher les infos essentielles : mise, pots, cartes de p1, riviere

## Mise : 
	au lieu de prompter 2x sur l'enchère, directement demander les atouts sous forme de String séparés par un espace, et parse la String.
	"facile" à faire

## Couleurs :
    Mettre en couleur les cartes : Carreau et coeur en rouge et trefle et pique en noir
-> CF capture d'écran sur l'affichage de fin


## Intelligence du Croupier :
    Actuellement le croupier est particulièrment stupide ...
    + se coucher, s'il n'a pas de combinaison ET qu'il est victime potentielle ET qu'il n'est pas dans la phase rivière
    (sinon il se couche alors qu'un check ne lui fait pas prendre de risque)
    + faire tapis quand il a une bonne main avec le FLOP ET qu'on est dans le TURN ou la RIVIERE. La pure stratégie tarouket.
    + pouvoir aller de l'avant avec n atouts.
    
    
    

# Bug Report :

+ Si le croupier va de l'avant et qu'il mise son dernier atout au turn, ce n'est pas lu comme un Tapis. 
Ca consièdre la rivière alors que ça devrait prendre le flop.

+ sur une égalité : départager les mains en affichant la plus haute carte des mains des joueurs sans prendre en compte les cartes communes. Là c'est juste un problème d'affichage.

+ sur une égalité totale : les joueurs n'ont rien et ont en main exactement les mêmes cartes. Préoir le cas de figure et remettre leurs pots respectif dans leurs mains.


