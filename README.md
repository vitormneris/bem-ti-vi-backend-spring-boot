# Bemtevi - backend

## Instalação

### 1. Crie um container postgres com o docker
```
docker run \
--name postgres-17-container \
-e POSTGRES_USER=root \
-e POSTGRES_PASSWORD=root \
-e POSTGRES_DB=bemtivi_database \
-p 5432:5432 \
-d postgres:17
```

### 2. Gere os arquivos .jar do projeto
`./mvnw clean package`

### 3. Exclua ou pare o container postgres
```
docker rm -f postgres-17-container
ou
docker stop postgres-17-container
```

### 4. Rode o comando docker compose
```
docker compose up
```





