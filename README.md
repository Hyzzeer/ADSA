# ADSA
Application Development Security Android

#TODO : 
 - LogIn page
 - Accounts
 - Sign Up page 
 - Use API : Recover information (name(=endpoint n1), accounts and values(=endpoint n2)) securely with TLS

#IMPORTANT :
 - App available offline
 - Access is restricted. User must be authenticated  
 - It must be impossible to recover API url, data and debugging information


#Requirements ---

This application must be available offline. ✓
A refresh button allows the user to update its accounts. ✓
Access to the application is restricted ✓
Exchanges with API must be secure ( with TLS) ✓

##Explain how you ensure user is the right one starting the app

Lorsque que l'utilisateur entre pour la première fois dans l'application, il lui est demandé d'entrer ses crendentials : 
    - name
    - lastname
    - password (inutile car le backend de l'api n'implémente pas de vérification)
Puis un Pin de 4 chiffres lui est demandé. Ce dernier lui sera demandé à chaque overture de l'application.
Ainsi nous sommes sûr que seul l'utilisateur peut regarder ses comptes.    

##How do you securely save user's data on your phone ?

Nous sauvegardons les données de l'utilisateur dans une base de donnée local à l'aide de la librairie SQLiteCipher. Celle ci est chiffré à l'aide du pin de l'utilisateur.
Ainsi si un attaquant réussi à obtenir par, un quelconque moyen, la base de données, il sera obligé de procéder à une attaque par force brute afin de lire son contenu.

Pour plus de sécurité, l'utilisateur a 3 essais lors de la saisie de son code pin. Dans le cas ou il échouerait à 3 reprises, la base de donnée est supprimée et l'utilisaeur devra se reconnecter en online.

##How did you hide the API url ?

???

##Screenshots of your application

???
