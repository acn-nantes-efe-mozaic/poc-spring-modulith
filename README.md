# 🌟 Application de démonstration Spring Modulith

> **Objectif** : illustrer une architecture modulaire avec Spring Modulith, en réécrivant une application monolithique traditionnelle selon les principes du Domain‑Driven Design et de l'architecture hexagonale.

## 📋 Table des matières

- [Introduction](#-introduction)
- [Configuration technique](#️-configuration-technique)
- [Structure du projet](#-structure-du-projet)
- [Architecture des modules](#-architecture-des-modules)
  - [Module `candidat`](#-module-candidat)
  - [Module `recruteur`](#-module-recruteur)
  - [Module `candidature`](#-module-candidature)
  - [Module `notification`](#-module-notification)
  - [Module `shared`](#-module-shared)
- [Communication entre modules](#-communication-entre-modules)
- [Ports d'entrée : UseCase vs Gateway](#-ports-dentrée-usecase-vs-gateway)
- [Architecture technique](#-architecture-technique)
- [Documentation des dépendances](#-documentation-des-dépendances)
- [Démarrage rapide](#-démarrage-rapide)
- [Tests](#-tests)
- [Dépannage](#-dépannage)

---

## 🧭 Introduction

Spring Modulith est une extension de **Spring Boot** qui permet de structurer une application monolithique en **modules fortement cohésifs et faiblement couplés**, tout en conservant un **déploiement unique**.
Chaque module devient un **sous-domaine métier autonome**, possédant ses propres entités, cas d'usage, interfaces publiques et événements.

---

## ⚙️ Configuration technique


| Élément            | Version                                  |
| -------------------- | ---------------------------------------- |
| **Java**             | 25                                       |
| **Spring Boot**      | 3.5.7                                    |
| **Spring Modulith**  | 1.4.3                                    |
| **Base de données** | H2 en mémoire                           |
| **ORM**              | Spring Data JPA                          |
| **Mapping**          | MapStruct                                |
| **Documentation**    | AsciiDoctor + Spring Modulith Documenter |

---

## 🗂️ Structure du projet

```
src/main/java/com/accenture/modules/
 ├── candidat/          # Gestion des candidats
 ├── candidature/       # Gestion des candidatures
 ├── recruteur/         # Gestion des recruteurs et offres
 ├── notification/      # Envoi de notifications asynchrones
 └── shared/            # Événements et éléments partagés
```

---

## 🧩 Architecture des modules

### 🔹 Module `candidat`

**Rôle :** gérer les informations des candidats (création, consultation).**Expose :**

- un **port REST interne** (`CandidatServiceUseCase`), utilisé par le contrôleur ;
- un **port inter‑modulaire** (`CandidatServiceGateway`), visible par les autres modules.

**Structure :**

```
candidat/
 ├─ repository/           # Entités JPA + Repository Spring Data
 ├─ service/
 │   ├─ domain/           # Objet métier Candidat
 │   ├─ usecase/          # Interface REST interne (UseCase)
 │   ├─ gateway/          # Interface inter-module (Gateway)
 │   └─ CandidatService   # Implémente UseCase + Gateway
 └─ web/                  # DTO + Controller REST
```

### 🔹 Module `recruteur`

**Rôle :** gérer les offres d'emploi et le nombre de candidatures associées.**Expose :**

- `OfferServiceUseCase` → utilisé par le contrôleur REST ;
- `OfferServiceGateway` → utilisé par d'autres modules (ex: `candidature`).

**Structure :**

```
recruteur/
 ├─ repository/           # JPA + mappers internes
 ├─ service/
 │   ├─ domain/           # Modèle métier Offer
 │   ├─ usecase/          # Interface REST interne
 │   ├─ gateway/          # Interface publique inter-module
 │   ├─ listener/         # Écoute les événements du module candidature
 │   └─ OfferService      # Implémente UseCase + Gateway
 └─ web/                  # REST controller interne
```

### 🔹 Module `candidature`

**Rôle :** gérer la création et la consultation des candidatures.**Communication :**

- Vérifie l'existence du candidat et de l'offre via `CandidatServiceGateway` et `OfferServiceGateway` ;
- Publie un événement `CandidatureCreatedEvent` pour le module `recruteur`.

### 🔹 Module `notification`

**Rôle :** écouter les événements `CandidatureCreatedEvent` et envoyer un e‑mail ou une alerte.
**Communication :** purement asynchrone, via les événements du module `shared`.

### 🔹 Module `shared`

**Rôle :** regrouper les événements métier partagés entre modules.
Contient notamment :

```java
public record CandidatureCreatedEvent(UUID id, UUID candidateId, UUID offerId, Instant createdAt) {}
```

---

## 🔄 Communication entre modules


| Type                    | Couplage | Exemple                                          | Usage                       |
| ----------------------- | -------- | ------------------------------------------------ | --------------------------- |
| **Synchrone (Gateway)** | Fort     | `offerServiceGateway.exists(offerId)`            | Validation ou vérification |
| **Asynchrone (Event)**  | Faible   | `publishEvent(new CandidatureCreatedEvent(...))` | Réaction post-création    |

---

## 🧠 Ports d'entrée : UseCase vs Gateway

Chaque service peut implémenter deux types d'interfaces :


| Type      | Usage                                       | Visibilité | Annotation                         | Exemple                  |
| --------- | ------------------------------------------- | ----------- | ---------------------------------- | ------------------------ |
| `UseCase` | Utilisée par le contrôleur REST du module | Interne     | 🚫 non annotée                    | `CandidatServiceUseCase` |
| `Gateway` | Utilisée par d'autres modules              | Publique    | ✅`@NamedInterface("xxx.gateway")` | `CandidatServiceGateway` |

---

## 🧩 Architecture technique

### Vérification de la modularité

```java
@SpringBootTest
class ModularityTest {
    @Test
    void verifyArchitecture() {
        ApplicationModules modules = ApplicationModules.of(SpringModulithApplication.class);
        modules.verify();
    }
}
```

## 📦 Documentation des dépendances

### Dépendances principales

```xml
<!-- Spring Boot Starter -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Modulith -->
<dependency>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-starter</artifactId>
    <version>1.4.3</version>
</dependency>

<!-- Persistence -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Mapping -->
<dependency>
    <groupId>org.mapstruct</groupId>
    <artifactId>mapstruct</artifactId>
    <version>1.5.5.Final</version>
</dependency>
```

### Dépendances optionnelles

```xml
<!-- Pour la documentation -->
<dependency>
    <groupId>org.springframework.modulith</groupId>
    <artifactId>spring-modulith-docs</artifactId>
    <optional>true</optional>
</dependency>

<!-- Pour les tests -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### Gestion des dépendances entre modules

Chaque module peut déclarer ses dépendances vers d'autres modules via les interfaces marquées avec `@NamedInterface`. Par exemple, le module `candidature` dépend du module `candidat` via l'interface `CandidatServiceGateway`.

## 🚀 Démarrage rapide

1. **Prérequis**

   - Java 25
   - Maven 3.9+
   - PostgreSQL 15+ (optionnel, H2 est inclus par défaut)
2. **Configuration**

   ```bash
   # Cloner le dépôt
   git clone [url-du-repo]
   cd spring-modulith-demo

   # Lancer l'application
   ./mvnw spring-boot:run
   ```
3. **Accès**

   - Application : http://localhost:8080
   - Console H2 : http://localhost:8080/h2-console
   - Documentation : http://localhost:8080/docs

## 🧪 Tests

```bash
# Exécuter tous les tests
./mvnw test

# Vérifier l'architecture
./mvnw test -Dtest=ModularityTest

# Générer la documentation
./mvnw test -Dtest=DocumentationTest
```

## 🛠 Dépannage

### Problèmes courants

1. **Erreur de dépendances cycliques**

   - Vérifier que les dépendances entre modules sont bien déclarées via des interfaces `@NamedInterface`
   - Utiliser des événements pour les dépendances inverses
2. **Événements non reçus**

   - Vérifier que la méthode de publication est bien dans une transaction (`@Transactional`)
   - Vérifier que l'événement est bien publié dans le module `shared`
   - Vérifier que le listener est correctement annoté avec `@ApplicationModuleListener`
3. **Problèmes de configuration**

   - Vérifier que les packages sont correctement structurés sous `com.accenture.modules`

---


**Auteur** : Emmanuel Fernandez - Accenture — Architecture Java
**Version** : 2.0 — Oct. 2025
