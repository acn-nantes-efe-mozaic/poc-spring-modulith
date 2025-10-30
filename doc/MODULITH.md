# 🌟 Spring Modulith — Guide d’Architecture (v2)

> **Contexte projet** : monolithe modulaire Spring pour *candidat*, *candidature*, *recruteur* + un module *shared* (événements).
> **Objectif** : poser des frontières claires, rendre les dépendances explicites, documenter et tester l’architecture.

---

## 1) Pourquoi Spring Modulith (ici) ?


| Besoin                         | Multi‑module Maven     | Microservices    | **Spring Modulith**                  |
| ------------------------------ | ----------------------- | ---------------- | ------------------------------------ |
| Frontières explicites         | Faibles (compile‑time) | Fortes (réseau) | **Fortes (runtime + vérification)** |
| Coût d’exploitation          | Bas                     | Élevé          | **Bas**                              |
| Refactorisation d’un existant | Lourde                  | Très lourde     | **Progressive**                      |
| Documentation intégrée       | Non                     | Non              | **Oui (Documenter + AsciiDoc)**      |

Modulith nous donne la **discipline d’un microservice** (contrats, limites, événements) **sans** la complexité d’exploitation.

---

## 2) Frontières & nommage : règles du jeu

- Chaque **sous‑package racine** de `com.accenture.modules` est un **module** :
  `candidat`, `candidature`, `recruteur`, `shared`.
- Par défaut, **tout est privé au module**.
  Pour exposer un contrat, on **annote le package** avec `@NamedInterface` (dans `package-info.java`).

### 2.1. Deux *types* d’API d’entrée (distinction clé)

> Un même service peut implémenter **deux interfaces** différentes — une pour le REST (interne), l’autre pour les autres modules (publique).


| API                         | But                                     | Visible par d’autres modules ? | Où ?                | Annotation                         |
| --------------------------- | --------------------------------------- | ------------------------------- | -------------------- | ---------------------------------- |
| **UseCase (REST)**          | Contrat utilisé par le**controller**   | Non                             | `...service.usecase` | 🚫*(pas de @NamedInterface)*       |
| **Gateway (inter‑module)** | Contrat utilisé par**un autre module** | Oui                             | `...service.gateway` | ✅`@NamedInterface("xxx.gateway")` |

**Exemple** (*module `recruteur`*) :

```java
// recruteur/service/usecase/OfferServiceUseCase.java
public interface OfferServiceUseCase {
    Offer create(String title, String description);
    List<Offer> all();
}

// recruteur/service/gateway/OfferServiceGateway.java
public interface OfferServiceGateway {
    boolean exists(UUID offerId);
}
```

`package-info.java` (gateway) :

```java
@org.springframework.modulith.NamedInterface("offer.gateway")
package com.accenture.modules.recruteur.service.gateway;
```

**Implémentation unique** (mutualisation métier) :

```java
@Service
@Transactional
@RequiredArgsConstructor
public class OfferService implements OfferServiceUseCase, OfferServiceGateway {
    private final OfferRepository repo;
    private final OfferEntityMapper mapper;
    // ...
}
```

### 2.2. Organisation type d’un module

```
recruteur/
 ├─ repository/           # Adaptateurs de persistance (Spring Data JPA)
 │   ├─ entity/           # Entités JPA (privées au module)
 │   └─ mapper/           # MapStruct Entity <-> Domaine
 ├─ service/
 │   ├─ domain/           # Modèle métier pur (POJOs, aucune dépendance Spring)
 │   ├─ usecase/          # Interfaces pour les controllers REST (internes)
 │   ├─ gateway/          # Interfaces inter‑modules (exposées)
 │   └─ listener/         # Réactions aux événements (post‑commit)
 └─ web/                  # REST (DTO, mapper, controller) – interne
```

> **Ne jamais** référencer depuis un autre module : `repository`, `web`, `service.domain`, `service.listener`, **ni** les *mappers* internes.

---

## 3) Communication inter‑modules : événements & contrats

### 3.1. Contrats synchrones (Gateway)

- **À utiliser** quand tu as besoin d’une **réponse immédiate** (ex: valider l’existence d’une offre).
- Exemple (dans `candidature`) :

```java
@RequiredArgsConstructor
@Service
public class CandidatureService {
    private final OfferServiceGateway offers; // 👈 dépend d’un contrat public

    public void create(UUID offerId, UUID candidatId) {
        if (!offers.exists(offerId)) throw new IllegalArgumentException("Offre inconnue");
        // ...
    }
}
```

### 3.2. Contrats asynchrones (Événements de domaine)

- **À utiliser** pour propager un changement **sans couplage temporel**.
- Les événements sont **post‑commit** et peuvent être **persistés** (table `event_publication`) grâce à `spring-modulith-events-jpa`.

**Emetteur** (dans `candidature`) :

```java
events.publishEvent(new CandidatureCreatedEvent(saved.getId(), saved.getCandidateId(), saved.getOfferId(), saved.getCreatedAt()));
```

**Listener** (dans `recruteur`) :

```java
@Component
@RequiredArgsConstructor
public class CandidatureEventsListener {

    private final OfferRepository repo;

    @ApplicationModuleListener // 👈 enregistre la dépendance inter‑module
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void onCandidatureCreated(CandidatureCreatedEvent evt) {
        repo.findById(evt.offerId()).ifPresent(offer -> {
            offer.setNbCandidatures(offer.getNbCandidatures() + 1);
            repo.save(offer);
        });
    }
}
```

**Événement partagé** (module `shared`) :

```java
// shared/events/CandidatureCreatedEvent.java
public record CandidatureCreatedEvent(UUID id, UUID candidateId, UUID offerId, Instant createdAt) {}
```

`package-info.java` :

```java
@org.springframework.modulith.NamedInterface("event")
package com.accenture.modules.shared.events;
```

> 📝 Avec `events-jpa`, tu verras dans les logs : *Registering publication…* puis *Marking publication… completed* après traitement.

---

## 4) Ce que nous avons corrigé (pièges réels rencontrés)

1. **Cycle de dépendance** `candidature ↔ recruteur`
   ➜ causé par l’usage d’un *mapper interne* d’un autre module.
   ✅ **Fix** : ne dépendre que d’interfaces exposées (`gateway`) ou d’événements.
2. **Événement non reçu** malgré `publishEvent`
   ➜ le type de l’événement n’était pas **exposé** (`@NamedInterface`) ou n’était pas **dans le module partagé**.
   ✅ **Fix** : déplacer l’événement dans `shared.events` annoté, et utiliser `@ApplicationModuleListener`.
3. **Conflit de bean `CandidatureEventsListener`**
   ➜ deux classes avec le même simple name dans deux modules.
   ✅ **Fix** : nommage distinct et/ou un seul listener par responsabilité, package clair.
4. **Confusion “UseCase vs Gateway”**
   ➜ les autres modules appelaient des interfaces destinées aux controllers REST.
   ✅ **Fix** : séparer `service.usecase` (interne) et `service.gateway` (public) + dual‑implémentation.

---

## 5) Documentation & validation d’architecture

### 5.1. Vérifier la modularité (test)

```java
@SpringBootTest
class ModularityTest {
    @Test
    void verify() {
        ApplicationModules modules = ApplicationModules.of(SpringModulithApplication.class);
        modules.verify(); // cycles, accès non exposés, etc.
    }
}
```

### 5.2. Générer la doc des modules

Dans un test dédié :

```java
@SpringBootTest
class ModularityDocumentationTest {
    @Test
    void generateDocs() {
        ApplicationModules modules = ApplicationModules.of(SpringModulithApplication.class);
        new Documenter(modules).writeDocumentation(); // gen AsciiDoc + PlantUML
    }
}
```

Les fichiers sont générés dans : `target/spring-modulith-docs/`
(*ex. `module-recruteur.adoc`, `components.puml`, `all-docs.adoc`, …*)

### 5.3. Transformer en HTML (Asciidoctor **exactement** comme dans le projet)

```xml
<plugin>
  <groupId>org.asciidoctor</groupId>
  <artifactId>asciidoctor-maven-plugin</artifactId>
  <version>3.0.0</version>
  <executions>
    <execution>
      <id>generate-html-docs</id>
      <phase>verify</phase>
      <goals>
        <goal>process-asciidoc</goal>
      </goals>
      <configuration>
        <sourceDirectory>${project.build.directory}/spring-modulith-docs</sourceDirectory>
        <outputDirectory>${project.build.directory}/spring-modulith-docs/html</outputDirectory>
        <backend>html5</backend>
        <attributes>
          <toc>left</toc>
          <sectnums>true</sectnums>
          <source-highlighter>coderay</source-highlighter>
          <toclevels>3</toclevels>
          <icons>font</icons>
        </attributes>
      </configuration>
    </execution>
  </executions>
</plugin>
```

➡️ Résultat : `target/spring-modulith-docs/html/index.html`

---

## 6) Conventions & check‑list

### 6.1. Nommage & packages

- `service.usecase` → **UseCase** (REST, interne)
- `service.gateway` → **Gateway** (inter‑module, public via `@NamedInterface`)
- `service.domain` → **POJOs** métier (aucun import Spring/JPA)
- `service.listener` → **@ApplicationModuleListener** (post‑commit)
- `repository.entity` / `repository.mapper` → **privés au module**
- `web.dto` / `web.mapper` / `web.*Controller` → **strictement internes**

### 6.2. Principes

- **Jamais** de dépendance vers un package non exposé d’un autre module.
- **Préférer** les événements pour les réactions *à posteriori*.
- **Dual interface** : un service peut implémenter *UseCase* **et** *Gateway*.
- **DTOs REST ≠ objets domaine** : mapper explicitement.
- **Transactions courtes** ; listener avec `REQUIRES_NEW` si nécessaire.

### 6.3. Commandes utiles

```bash
# Build + tests + génération de la doc AsciiDoc puis HTML
./mvnw clean verify

# Lancer l’app
./mvnw spring-boot:run
```

---

## 7) Exemple concret : module `recruteur` (extrait)

```
recruteur/
 ├─ repository/entity/OfferEntity.java
 ├─ repository/mapper/OfferEntityMapper.java
 ├─ service/domain/Offer.java
 ├─ service/usecase/OfferServiceUseCase.java
 ├─ service/gateway/OfferServiceGateway.java   # @NamedInterface("offer.gateway")
 ├─ service/listener/CandidatureEventsListener.java
 ├─ service/OfferService.java                  # impl UseCase + Gateway
 └─ web/dto + web/mapper + web/OfferController.java
```

**Contrats exposés** : `service.gateway` (inter‑module), `shared.events` (événements).
**Non exposé** : tout le reste (web, repository, domain, mappers).

---

## 8) FAQ (rapide)

- **Faut‑il `@EnableModulith` ?** → Non (1.3+).
- **Pourquoi mon listener ne logge rien ?** → Vérifie : type d’événement **exposé** + `@ApplicationModuleListener` + transaction **committée**.
- **Puis‑je appeler un mapper d’un autre module ?** → Non (interne). Passe par un **Gateway** ou un **événement**.
- **Où vivent les événements ?** → Dans un module **shared.events** annoté `@NamedInterface("event")`.

---

## 9) Références

- Docs : https://docs.spring.io/spring-modulith/reference/
- Samples : https://github.com/spring-projects/spring-modulith-samples

---

**Auteur** : Emmanuel Fernandez - Accenture — Architecture Java
**Version** : 2.0 — Oct. 2025
