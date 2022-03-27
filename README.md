# INFOB318 : projet individuel

* Titre: BCSim2D
* Client(s) : James Ortiz Vega
* Étudiant: Sow Babacar

Le but de ce projet est de faire un simulateur de robots mindstorms. L'utilisateur écrit une suite d'instructions dans le langage NBC et l'application
simule le comportement du robot selon le fichier passé par l'utilisateur.

## Requirements 
Avoir une version java installé sur son PC
Il faut ajouter la libraire KGradientPanel.  
Aller dans File->ProjectStructure -> Librairies

# Comment lancer l'application 
* Executer le fichier jar contenur dans le fichier bin 
* ou bien lancer depuis java

# Lancement de l'application 

## 1. Page d'accueil
Lorsque vous lancer le projet vous arrivez sur une page d'accueil.
Appuyez sur le bouton commencez 

## 2. Page d'initialisation 
Apès la page d'accueil vous trouvez la page d'initialisation.
Dans cette page, il y a une image de robot à gauche. Cliquez sur les boutons
A,B,C,1,2,3 ou 4 pour mettre des roues ansi que les capteurs que vous désirez.

* Après avoir mis vos roues et vos capteurs, cliquez sur le bouton OK situé en bas de page 
sur la gauche

## 3. Page de simulation
Dans cette page, il y a différents cadres. Nous avons un cadre noir à droite.
Cliquez sur ce cadre pour importer le fichier qui contient le code NBC.
Vous pouvez également cliquer sur le bouton **import NBC** pour importer un fichier.

Vous avez la possibilité d'ajouter des obstacles ou des lignes en cliquant
sur les images situés sur votre droite.

# Démarrez la simulation 
- Après avoir chargé un fichier rempli de code NBC, placez votre robot sur le cadre ligné du mileu.
- Appuyez ensuite sur le bouton run situé sur la navbar
- Pour arrêter la simulation appuyez sur le bouton abort situé dans la navbar également

# Possibilités
  
- jouer de la musique
- avancer et reculer
- stopper les moteurs
- faire des opérations arithmétiques
- faire des comparaisons et des jump(fonctionnalité NBC et non visuelle)

# Non fait
- Macro
- subroutines
- thread non main
- commentaires
- operations logiques (or, and ...)

# commande reconnues avec les problèmes s'il y en a

| Commande               | fait quoi       | problemes |
| :---------------:      |:---------------:|:--------------:|
| OnFwd(OUT_ **.. ,** vitesse) | permet d'avancer le robot en activant les moteurs **..**    |  |
| OnRev(OUT_ **..**, vitesse)  | permet de reculer le robot| |
| PlayTone(TONE_C **.** , durée)|permet jouer de la musique |on joue toujours la même musique la même durée |
|brcmp| faire des comparaisons et brancher en fonction du résultat|parfois il ne fait pas tous les tours de boucle|
|brtst|faire des comparaisons et brancher sur un label|même souci que brcmp|
|jmp|faire des jump vers des labels||
|Off(OUT_ **..**)|mettre la vitesse du moteur à 0||
|add var,var,var|permet de faire des additions||
|sub var, var, var|permet de faire des soustractions||
|mul var, var, var|permet de faire des multiplications||
|div var, var, var|permet de faire des divisions||
|set var, value|permet d'initialiser une variable||
|#define var|permet d'initailiser une constante var||
|wait time|permet d'attedre une certaine durée le temps de faire une action|dure un tout petit peu plus que la durée en temps réel|
|cmp & tst|faire des comparaisons simples entre label||
|EQ|equivalent de = (equal to)  ||
|LT| equivalent de > (smaller than) ||
|GT| equivalent de < (larger than) ||
|SetSensorTouch(IN_ **.**)|indique au robot que le capteur connecté à l'entrée IN_ **.**) sera un capteur de toucher||
|ReadSensor(IN_ **. ,** var)|lit la valeur actuelle du capteur, la traduit en une valeur appropriée selon le type de capteur sélectionné et la renvoie dans le 2e paramètre||
|SetSensorLight(IN_ **.**)|indique au robot que le capteur connecté à l'entrée IN_ **.**) sera un capteur de lumière ||
|dseg segment ... dseg end) | ces commandes sont ignorés lorsque qu'on les voit car on a seulement besoin des variables à l'intérieur entre les 2 déclarations||
|thread main| ignorée également on passe juste la ligne et on exécute les instructions qui sont à l'intérieur ||

# Problèmes majeurs

1.  certains codes marchent quand on exécute le code avec la classe main mais pas avec le fichier jar.
2. L'image du pont ne s'affiche pas à la bonne taille  
4. Parfois la page d'accuel ne s'affiche pas 
4. Il arrive parfois que le nombre de passage dans les boucles ne soit pas correct

