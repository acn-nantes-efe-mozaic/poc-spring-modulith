-- Seed initial data for local H2 database
-- Two candidates
INSERT INTO candidats (id, first_name, last_name, email, nombre_candidatures)
VALUES ('d8f1a997-6f3a-43a5-b79a-be060da9d8c9', 'Alice', 'Durand', 'alice.durand@example.com', 0);
INSERT INTO candidats (id, first_name, last_name, email, nombre_candidatures)
VALUES ('3e13b64a-e4fd-425d-b5e0-0028a048aeae', 'Bob', 'Martin', 'bob.martin@example.com', 0    );

-- Three offers
INSERT INTO offers (id, title, description, nb_candidatures)
VALUES ('d608ec13-30ed-4bc0-9807-4e298b413b87', 'Développeur Java', 'Développeur Java Spring Boot', 0);
INSERT INTO offers (id, title, description, nb_candidatures)
VALUES ('247b7069-9ebe-4eb8-bc13-e5f3b1a4afc1', 'DevOps Engineer', 'CI/CD, Kubernetes, Cloud', 0);
INSERT INTO offers (id, title, description, nb_candidatures)
VALUES ('1e056ad1-5819-4d6e-a938-129661fe850b', 'Data Engineer', 'Pipelines de données, Spark', 0);
