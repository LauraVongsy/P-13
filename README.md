# Your Car Your Way – PoC Chat

##  Description
Ce projet est un **Proof of Concept (PoC)** pour l’application **Your Car Your Way**, une plateforme de location de voitures internationale.  
Le but de ce PoC est de démontrer la faisabilité de la **fonctionnalité de chat en ligne** permettant aux clients de communiquer avec le service client.

L’implémentation repose sur :
- Un **frontend Angular**
- Un **backend Spring Boot** fournissant un token d’accès Twilio.
- Une intégration avec **Twilio Conversations API** pour gérer le chat temps réel.

---

## Fonctionnalités couvertes dans le PoC
- Page d’accueil Angular avec bouton pour **ouvrir/fermer le chat**.
- **Composant standalone `ChatComponent`** permettant d’envoyer/recevoir des messages.
- Connexion au service Twilio via un **token généré côté backend**.


---

##  Stack technique
### Frontend
- **Angular 19 (standalone)**
- **HttpClient avec provideHttpClient()**
- **Twilio Conversations JS SDK**

### Backend
- **Java Spring Boot**
- **API REST** exposant `/api/chat/token` pour générer un token Twilio
- (PoC → pas de microservices, mais architecture finale prévue en microservices)

### Infrastructure (PoC)
- Application **Dockerisée** (frontend + backend)
- CI/CD (GitHub Actions envisagé pour la suite)


## Installation & Lancement
### 1. Cloner le projet

### 2. Lancer le backend
```bash
cd backend
./mvnw spring-boot:run
```
Le backend écoute sur **http://localhost:8080**

### 3. Lancer le frontend
```bash
cd frontend
npm install
npm start
```
Le frontend écoute sur **http://localhost:4200**

---

## Lancement avec Docker Compose
Le projet est fourni avec un fichier `docker-compose.yml` pour lancer la base de données (non utilisée pour le POC en l'état actuel, utilie pour une évolution potentielle)

### 1. Construire et lancer
```bash
docker-compose up -d
```

### 2. Accéder à l’application
- Frontend : [http://localhost:4200](http://localhost:4200)  
- Backend : [http://localhost:8080](http://localhost:8080)


## Fonctionnement attendu
- Accéder à `http://localhost:4200`
- Cliquer sur **"Ouvrir le chat"**
- Le composant `ChatComponent` s’affiche et se connecte à Twilio
- Les messages envoyés s’affichent en direct

---

##  Sécurité
- Les credentials Twilio sont secrets donc ils ne sont pas sur ce repo.
- Authentification utilisateur **non incluse dans ce PoC** 
- Les tokens Twilio sont générés côté backend pour éviter toute exposition côté client.
- Les communications se font en **HTTPS** dans la version finale.

---

## Limitations du PoC
- Pas de stockage persistant des messages (uniquement temps réel).
- Base de données prête pour des tests de sauvegardes de conversations ultérieures.
- L’interface du chat est volontairement simplifiée.

