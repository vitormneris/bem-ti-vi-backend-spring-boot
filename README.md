# Bemtevi - backend

## Instalação

### 1. Na pasta do projeto, gere os arquivos .jar com o comando abaixo
```
./mvnw clean package -Dmaven.test.skip=true
ou
./mvnw clean package -DskipTests
```

### 2. Em seguida, rode o seguinte comando Docker (necessário ter o Docker instalado em seu sistema operacional)
```
docker compose up
```
