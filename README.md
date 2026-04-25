# Catalogo - Configuracao do PostgreSQL

Este projeto usa Spring Boot + Spring Data JPA com PostgreSQL.

## 1. Pre-requisitos

- Java 17+
- Maven (ou usar `./mvnw`)
- PostgreSQL instalado e em execucao

## 2. Criar banco e usuario no PostgreSQL

Abra o `psql` como usuario administrador e execute:

```sql
CREATE DATABASE catalogo_db;

-- Opcional: criar usuario dedicado para a aplicacao
CREATE USER catalogo_user WITH PASSWORD 'catalogo123';
GRANT ALL PRIVILEGES ON DATABASE catalogo_db TO catalogo_user;
```

Se preferir, voce pode usar a interface do pgAdmin4 para criar seu banco e usar o usuário padrão do postgres.

## 3. Configurar o arquivo application.properties

Edite o arquivo [src/main/resources/application.properties](src/main/resources/application.properties) com os dados do seu banco:

```properties
spring.application.name=catalogo

# Conexao com PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/catalogo_db
spring.datasource.username=postgres
spring.datasource.password=123456
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate/JPA
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

*Também existe o arquivo application-h2.proprieties voltado para o uso de dados locais.


## 4. Rodar o projeto e verificar possíveis erros


Se a conexao com o banco estiver correta:

- a aplicacao sobe sem erros;
- as tabelas `tb_produto`, `tb_categoria` e `tb_usuario` sao criadas/atualizadas automaticamente.

Se der erro de conexao, confira:

- PostgreSQL esta ativo;
- banco existe;
- usuario/senha estao corretos;
- porta da URL (`5432`) corresponde a porta do seu PostgreSQL. *geralmente é a causa mais comum
