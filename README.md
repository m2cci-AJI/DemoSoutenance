plan de charge
Cette application a été générée à l'aide de JHipster 5.8.2, vous pouvez trouver de la documentation et de l'aide sur https://www.jhipster.tech/documentation-archive/v5.8.2 .

### Pile technologique sur le responsable de la conception Web Angular 7 frontale avec compatibilité avec Twitter Bootstrap HTML5 Boilerplate (Chrome, FireFox, Microsoft Edge…) Prise en charge WebSocket avec Spring Websocket

### Pile technologique pour le backend:

Spring Boot v 2.0.8 Maven pour configurer la construction, le test et les exécutions du mode «développement» et «production» de l'application Spring Security Spring Data JPA et Hibernate MySQL Database WebSocket avec Spring Websocket Database mis à jour avec Liquibase Elasticsearch pour permettre la recherche dans la base de données

Développement
Avant de pouvoir créer ce projet, vous devez installer et configurer les dépendances suivantes sur votre ordinateur:

Node.js : Nous utilisons Node pour exécuter un serveur Web de développement et construire le projet. Selon votre système, vous pouvez installer Node à partir de la source ou en tant que bundle pré-packagé.
Après avoir installé Node, vous devriez pouvoir exécuter la commande suivante pour installer les outils de développement. Vous n'aurez besoin d'exécuter cette commande que lorsque les dépendances changent dans package.json .

npm install
Nous utilisons des scripts npm et Webpack comme système de construction.

Exécutez les commandes suivantes dans deux terminaux distincts pour créer une expérience de développement heureuse où votre navigateur s'actualise automatiquement lorsque les fichiers changent sur votre disque dur.

./mvnw
npm start
Npm est également utilisé pour gérer les dépendances CSS et JavaScript utilisées dans cette application. Vous pouvez mettre à niveau les dépendances en spécifiant une version plus récente dans package.json . Vous pouvez également exécuter npm updateet npm installgérer les dépendances. Ajoutez l' helpindicateur sur n'importe quelle commande pour voir comment vous pouvez l'utiliser. Par exemple npm help update,.

La npm runcommande répertorie tous les scripts disponibles pour exécuter ce projet.

Gérer les dépendances
Par exemple, pour ajouter la bibliothèque Leaflet en tant que dépendance d'exécution de votre application, vous devez exécuter la commande suivante:

npm install --save --save-exact leaflet
Pour bénéficier des définitions de type TypeScript du référentiel DefinitelyTyped en cours de développement, vous devez exécuter la commande suivante:

npm install --save-dev --save-exact @types/leaflet
Ensuite, vous importeriez les fichiers JS et CSS spécifiés dans les instructions d'installation de la bibliothèque pour que Webpack les connaisse: Modifiez le fichier src / main / webapp / app / vendor.ts :

import 'leaflet/dist/leaflet.js';
Modifiez le fichier src / main / webapp / content / css / vendor.css :

@import '~leaflet/dist/leaflet.css';
Remarque: il reste encore quelques autres choses à faire pour Leaflet que nous ne détaillerons pas ici.

Pour plus d'instructions sur la façon de développer avec JHipster, consultez Utilisation de JHipster en développement .

Utiliser angular-cli
Vous pouvez également utiliser la CLI angulaire pour générer du code client personnalisé.

Par exemple, la commande suivante:

ng generate component my-component
va générer quelques fichiers:

create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
Bâtiment pour la production
Pour optimiser l'application ChargePlan pour la production, exécutez:

./mvnw -Pprod clean package
Cela concatènera et réduira les fichiers CSS et JavaScript du client. Il sera également modifié index.htmlpour référencer ces nouveaux fichiers. Pour vous assurer que tout fonctionne, exécutez:

java -jar target/*.war
Ensuite, accédez à http: // localhost: 9090 dans votre navigateur.

Reportez-vous à Utilisation de JHipster en production pour plus de détails.

Essai
Pour lancer les tests de votre application, exécutez:

./mvnw clean test
Tests client
Les tests unitaires sont exécutés par Jest et écrits avec Jasmine . Ils sont situés dans src / test / javascript / et peuvent être exécutés avec:

npm test
Pour plus d'informations, reportez-vous à la page Exécution des tests .

Qualité du code
Le sonar est utilisé pour analyser la qualité du code. Vous pouvez démarrer un serveur Sonar local (accessible sur http: // localhost: 9001 ) avec:

docker-compose -f src/main/docker/sonar.yml up -d
Ensuite, exécutez une analyse Sonar:

./mvnw -Pprod clean test sonar:sonar
Pour plus d'informations, reportez-vous à la page Qualité du code .

Utiliser Docker pour simplifier le développement (facultatif)
Vous pouvez utiliser Docker pour améliorer votre expérience de développement JHipster. Un certain nombre de configurations de composition de docker sont disponibles dans le dossier src / main / docker pour lancer les services tiers requis.

Par exemple, pour démarrer une base de données mysql dans un conteneur Docker, exécutez:

docker-compose -f src/main/docker/mysql.yml up -d
Pour l'arrêter et retirer le conteneur, exécutez:

docker-compose -f src/main/docker/mysql.yml down
Vous pouvez également ancrer complètement votre application et tous les services dont elle dépend. Pour ce faire, commencez par créer une image docker de votre application en exécutant:

./mvnw package -Pprod verify jib:dockerBuild
Exécutez ensuite:

docker-compose -f src/main/docker/app.yml up -d
Pour plus d'informations, reportez-vous à Utilisation de Docker et Docker-Compose , cette page contient également des informations sur le sous-générateur docker-compose ( jhipster docker-compose), qui est capable de générer des configurations de docker pour une ou plusieurs applications JHipster.

Intégration continue (facultatif)
Pour configurer CI pour votre projet, exécutez le sous-générateur ci-cd ( jhipster ci-cd), cela vous permettra de générer des fichiers de configuration pour un certain nombre de systèmes d'intégration continue. Consultez la page Configuration de l'intégration continue pour plus d'informations.
