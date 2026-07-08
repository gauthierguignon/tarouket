# CLI Tarouket

# Informations Techniques

## Compilation

~~bash
javac -d bin -cp bin src/*.java
~~

## Execution

~~bash
java -cp bin src.Main
~~

# Histoire

Le Tarouket est un jeu d'argent à deux joueurs dérivé du poker, traditionnellement associé aux milieux maritimes. Je situe son apparition au milieu des années 90, en France, sur les côtes normandes.

Présenté comme une adaptation du Texas Hold'em pour deux joueurs, le Tarouket se distingue par l'utilisation d'un jeu de tarot traditionnel. Les cartes d'atout remplissent la fonction habituellement dévolue aux jetons de mise. 

Les règles spécifiques du "en avant" et du "bon débarras" lui confèrent une identité forte.

La popularité du jeu est généralement attribuée à la simplicité de ses règles, à la diversité des stratégies qu'il permet de développer ainsi qu'à son faible coût matériel. L'utilisation d'un jeu de tarot, largement répandu en France, a notamment contribué à sa diffusion auprès de publics ne disposant pas de matériel de poker spécialisé.


# Règles du Tarouket

## Introduction

Le Tarouket est un jeu de cartes pour deux joueurs qui mélange les combinaisons du poker et les atouts du tarot. Les atouts ne servent pas à jouer des plis : ils remplacent les jetons et constituent la mise des joueurs tout au long de la partie.

Chaque manche se déroule au tour par tour, selon une succession de phases d'enchères et de révélations de cartes communes, jusqu'à ce qu'un joueur se couche ou que les deux mains soient comparées à l'abattage.

## 1. Matériel

Le jeu se joue avec un jeu de tarot modifié : les atouts 10, 21 et l'Excuse sont retirés. Le paquet compte alors 56 cartes classiques et 20 atouts. Les atouts servent de jetons de mise.

Chaque joueur reçoit 10 atouts en début de partie : c'est son capital, appelé sa **mise**.

## 2. Détermination du premier joueur

En tout début de partie, les deux joueurs tirent à pile ou face. Le gagnant reçoit les atouts impairs (léger avantage : il possède la plus petite carte). Le joueur ayant les atouts **impairs** est celui qui parle en premier lors de la **première main**.

**À chaque nouvelle main, le rôle de "premier joueur" est inversé** entre les deux joueurs, indépendamment du déroulement de la main précédente.

## 3. Déroulement d'une main

1. **Petite blinde** — chaque joueur engage obligatoirement son plus petit atout dans le pot. Ce n'est pas un choix.
2. **Préflop** — chaque joueur reçoit ses cartes deux privées, puis un tour d'enchères a lieu (voir §4).
3. **Flop** — 3 cartes communes sont révélées, puis un tour d'enchères a lieu.
4. **Turn** — une 4ᵉ carte commune est révélée, puis un tour d'enchères a lieu.
5. **River** — une 5ᵉ carte commune est révélée, puis un tour d'enchères a lieu.
6. **Abattage** — si la main n'a pas été interrompue par un coucher, les deux joueurs montrent leurs cartes et on compare les combinaisons.

Le premier joueur désigné pour la main (§2) est celui qui parle en premier à **chaque** tour d'enchères de cette main (préflop, flop, turn, river) — l'ordre de parole ne change jamais pendant une main, seulement d'une main à l'autre.

## 4. Le tour d'enchères

À son tour de parole, un joueur dispose des actions suivantes :

- **Check** — ne rien ajouter au pot. Utilisé aussi bien pour passer que pour "accepter" implicitement la mise de l'adversaire (il n'existe pas d'action "suivre" séparée : checker après une mise adverse revient à l'accepter).
- **Miser** — ajouter des atouts au pot, en dépassant le total déjà engagé par l'adversaire, ou en l'égalisant.
- **Tapis** — miser la totalité de ses atouts restants (ou son dernier atout).
- **Coucher** — abandonner la main. Le pot revient à l'adversaire.

**Il n'existe pas d'obligation de suivre une mise**, sauf dans le cas précis du "bon débarras" (voir §6). Un joueur qui a checké ne peut pas revenir sur sa décision plus tard dans le même tour d'enchères.

**Il n'existe pas de relance libre** : en dehors du mécanisme du bon débarras, un joueur ne peut pas miser une seconde fois par-dessus la mise de l'adversaire au sein du même tour d'enchères.

## 5. Le "en avant"

À la fin de chaque tour d'enchères (une fois que les deux joueurs se sont exprimés), on compare le total des atouts que chaque joueur a engagés dans le pot depuis le début de la main :

- Si les deux totaux sont **égaux**, personne n'est "en avant".
- Si l'un des deux totaux est **strictement supérieur** à l'autre, le joueur ayant le total le plus élevé est déclaré **"en avant"**.

Le statut de "en avant" se calcule à la fin de **chaque** tour d'enchères et peut changer de joueur, disparaître, ou se maintenir d'un tour à l'autre selon les mises.

## 6. Le "bon débarras"

Un **bon débarras** se déclenche quand un joueur est **"en avant" à deux tours d'enchères consécutifs** :

- Le joueur était déjà "en avant" à l'issue du tour précédent ;
- Il mise à nouveau ce tour-ci, en creusant l'écart (son total redevient strictement supérieur à celui de l'adversaire) ;
- Le second tour consécutif où il termine "en avant" constitue le bon débarras.

**Conséquence du bon débarras :** l'adversaire perd la possibilité de se coucher. Il est obligé, à son tour de parole, soit d'égaliser le total du pot du joueur qui a fait le bon débarras, soit de le dépasser.

**Rechargement :** un bon débarras ne peut pas être déclenché deux tours d'enchères de suite. Une fois un bon débarras résolu, le compteur de tours consécutifs "en avant" repart de zéro. Pour qu'un nouveau bon débarras devienne possible, le joueur (peu importe lequel des deux) doit d'abord redevenir "en avant" à un tour, puis le rester au tour suivant.

**Rupture du streak :** si à la fin d'un tour d'enchères le statut "en avant" change de joueur (ou disparaît parce que les pots sont égalisés), le compteur de tours consécutifs repart à zéro pour le nouveau joueur "en avant" (il ne compte alors que depuis ce tour-là). Toute égalisation du pot, à n'importe quel moment de la main, remet le compteur à zéro. Cf exemple 8. 

## 7. Le tapis

Si un joueur mise tapis (la totalité de ses atouts restants, ou son dernier atout), **seules les 3 premières cartes communes (le flop) sont prises en compte** pour comparer les mains à l'abattage — même si le turn et la river ont déjà été révélés à ce moment de la main. Pour rappel, il n'est jamais obligatoire de suivre au tarouket, sauf dans le cas d'un bon débarras. Cette règle vaut pour le tapis.

Si l'adversaire se couche face au tapis, la main se termine immédiatement sans comparaison de cartes.

## 8. Fin de la main et comptage des points

Le Tarouket se joue à **jeu ouvert** : à chaque fin de main non interrompue par un coucher, les deux joueurs montrent leurs cartes.

Les mains sont départagées selon les combinaisons classiques du poker. Il n'y a pas de système de points à calculer, à une exception : **l'As vaut 1 point**, comme au tarot (et non une valeur haute comme au poker classique), ce qui peut influer sur le classement des suites et des combinaisons impliquant l'As. De même, au poker les cartes cavaliers n'existent pas.

# Exemples de mains
Disons que J1 est toujours celui qui parle en premier dans ces exemples.

# Exemple 1 :
Mise de la petite blind.
--préflop--
J1 mise
J2 check
--fin de la mise--
Est-ce qu'un un joueur à un pot supérieur à l'autre ? oui : J1 va de l'avant. il pourra tenter un bon débarras au tour suivant. 
--révélation du flop--
J1 mise et fait un bon débarras
Est-ce qu'un le pot de d'un joueur a été deux fois consécutif plus haut que le pot de l'autre joueur ? oui : c'est un "bon débarras".
J2 est obligé de suivre ou de dépasser le pot de P1
...

# Exemple 2 : 
Mise de la petite blind
--préflop--
J1 check
J2 mise
-- fin de la mise--
Est-ce qu'un un joueur à un pot supérieur à l'autre ? oui : J2 va de l'avant. il pourra tenter un bon débarras au tour suivant.
--révélation du flop--
J1 check
J2 mise
Est-ce qu'un le pot de d'un joueur a été deux fois consécutif plus haut que le pot de l'autre joueur ? oui : c'est un "bon débarras".
J1 doit égaliser ou dépasser le pot de J2
...

# Exemple 3 : 
Mise de la petite blind
--préflop--
J1 check
J2 mise
-- fin de la mise--
Est-ce qu'un un joueur à un pot supérieur à l'autre ? oui : J2 va de l'avant. il pourra tenter un bon débarras au tour suivant.
--révélation du flop--
J1 mise et dépasse le pot de J2
J2 mise et dépasse le pot de J1
Est-ce qu'un le pot de d'un joueur a été deux fois consécutif plus haut que le pot de l'autre joueur ? oui : c'est un "bon débarras".
J1 doit égaliser ou dépasser le pot de J2

# Exemple 4 : 
Mise de la petite blind
--préflop--
J1 check
J2 mise
-- fin de la mise--
Est-ce qu'un un joueur à un pot supérieur à l'autre ? oui : J2 va de l'avant. il peut faire un bon débarras au tour suivant.
--révélation du flop--
J1 mise et dépasse le pot de J2
J2 check
Est-ce qu'un le pot de d'un joueur a été deux fois consécutif plus haut que le pot de l'autre joueur ? non. J2 ne pourra pas faire de bon débarras au tour suivant car il faut avoir fait un "en avant" au tour précédent. 
Est-ce qu'un un joueur à un pot supérieur à l'autre ? oui : J1 va de l'avant. il pourra tenter un bon débarras au tour suivant.

# Exemple 5 : 
Mise de la petite blind
--préflop--
J1 mise
J2 mise mais moins que J1
-- fin de la mise--
Est-ce qu'un un joueur à un pot supérieur à l'autre ? oui : J1 va de l'avant. il peut faire un bon débarras au tour suivant.

# Exemple 6 :
Mise de la petite blind
--préflop--
J1 mise
J2 mise et égalise le pot de J1
-- fin de la mise--
Est-ce qu'un joueur à un pot supérieur à l'autre ? non. Pas de "en avant" ici

# Exemple 7 : 
Mise de la petite blind
--préflop--
J1 check
J2 check
-- fin de la mise--
Est-ce qu'un joueur à un pot supérieur à l'autre ? non. Pas de "en avant" ici
On révèle le flop

# Exemple 8 :
Mise de la petite blind
--préflop--
J1 check
J2 mise
Est-ce qu'un joueur à un pot supérieur à l'autre ? oui. J2 est "en avant" ici
--révélation du flop--
J1 mise et égalise le pot de J2
**--> J2 n'est plus en avant**
J2 mise et dépasse le pot de J1
Est-ce qu'un joueur à un pot supérieur à l'autre ? oui. J2 est "en avant" ici
...
