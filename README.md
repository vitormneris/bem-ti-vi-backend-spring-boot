# Bemtevi - Backend

Bemtevi é o sistema backend do projeto de automação de um pet shop, desenvolvido para fornecer funcionalidades robustas de gerenciamento de operações, integração com clientes e suporte a serviços essenciais do estabelecimento.

O backend foi projetado para ser seguro, escalável e de fácil manutenção, fornecendo APIs REST, autenticação de usuários, integração com bancos de dados e serviços de terceiros.

---

## Tecnologias Utilizadas

### Infraestrutura e Serviços em Nuvem
- **AWS EC2**: Hospedagem do servidor da aplicação, permitindo acesso via internet.  
- **Red Hat Enterprise Linux (RHEL)**: Sistema operacional da instância EC2, conhecido por sua estabilidade e suporte corporativo.  
- **Amazon S3**: Armazenamento seguro e escalável de arquivos, como imagens.  

### Back-end
- **Java 21**: Linguagem principal do projeto, utilizada por sua robustez e recursos modernos.  
- **Spring Framework (Spring Boot)**:
  - Spring Boot Web: Criação de APIs REST.  
  - Spring Boot Security: Autenticação e autorização de usuários.  
  - Spring Boot Data JPA: Integração com Hibernate para manipulação de dados relacionais.  
  - Spring Boot Validation: Validação automática com Jakarta Bean Validation.  
  - Spring Boot Mail: Envio de e-mails de confirmação e notificação.  
  - Spring Boot DevTools: Recursos para facilitar o desenvolvimento (recarregamento automático, etc).  

### Banco de Dados
- **PostgreSQL**: Banco relacional para armazenamento confiável de dados.  
- **MongoDB**: Banco não relacional, utilizado para armazenar mensagens do sistema de chat.  

### Integração com Terceiros
- **Mercado Pago API**: Integração com pagamentos via Pix e geração de QR Codes dinâmicos.  
- **ViaCEP**: Automatização do cadastro de clientes utilizando a API de CEP.  

### Contêinerização
- **Docker**: Empacotamento e distribuição da aplicação com todas as dependências, facilitando o deploy em qualquer ambiente.  

---

## Pré-requisitos
- Java 21 instalado  
- Docker e Docker Compose  

---

## Instalação
### 1. Na pasta do projeto, gere os arquivos .jar com o comando abaixo
```bash
./mvnw clean package -Dmaven.test.skip=true
```
ou
```bash
./mvnw clean package -DskipTests
```
### 2. Em seguida, rode o seguinte comando Docker
```bash
docker compose up
```
