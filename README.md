# Projet de Réseaux 2A ENSEIRB-MATMECA
```
   _______________                        |*\_/*|________
  |  ___________  |     .-.     .-.      ||_/-\_|______  |
  | |           | |    .****. .****.     | |           | |
  | |   0   0   | |    .*****.*****.     | |   0   0   | |
  | |     -     | |     .*********.      | |     -     | |
  | |   \___/   | |      .*******.       | |   \___/   | |
  | |___     ___| |       .*****.        | |___________| |
  |_____|\_/|_____|        .***.         |_______________|
    _|__|/ \|_|_.............*.............._|________|_
   / ********** \                          / ********** \
 /  ************  \                      /  ************  \
--------------------                    --------------------
```

## Réalisation d'une application d'échange de fichiers P2P
Languages :  
* **Java** pour les *pairs*
* **C** pour le *tracker*

## Version centralisée

### Tracker
Pour compiler le tracker : `make`  
Possiblité de spécifier un nombre de threads avec : `-z <nb_threads>`  
Pour lancer les tests : `make tests`

On lance le tracker avec : `build/tracker <port>`  

Il est possible de ne pas spécifier de numéro de port au lancement, 
auquel cas c'est le port par défaut, qui est spécifié dans le fichier de configuration *config.ini*, qui est utilisé.


### Pairs
Pour compiler le pair : `make`  
Pour lancer les tests : `make tests`

On lance un pair dans le répertoir `build/` avec : `java gui`

### Documentation
Le code da la partie tracker est commenté de façon a pouvoir générer la documentation avec **Doxygen**.  
Pour cela, dans le dossier `tracker/` il suffit de lancer la commande : `make doxygen`
Ensuite, dans le dossier `html/` généré il suffit d'aller chercher le fichier `index.html` pour avoir accès à la documentation.