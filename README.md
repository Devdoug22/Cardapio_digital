# 🍽️ Cardápio Digital - API RESTful

API RESTful desenvolvida em Java com Spring Boot para gerenciamento de itens de cardápio, focada em boas práticas de arquitetura, validação de dados e tratamento global de exceções.

## 🚀 Tecnologias Utilizadas
* **Java 17 / Spring Boot**
* **Spring Data JPA & PostgreSQL / H2**
* **Bean Validation (`spring-boot-starter-validation`)**
* **Swagger / OpenAPI** (Documentação interativa da API)
* **Postman** (Testes de integração e requisições HTTP)

## 📌 Principais Funcionalidades & Arquitetura
* **Validação de DTOs:** Uso de anotações `@Valid` para garantir a integridade dos dados antes da persistência.
* **Tratamento Global de Exceções (`@RestControllerAdvice`):** Interceptação customizada de erros de validação (`MethodArgumentNotValidException`), IDs não encontrados e falhas de requisição HTTP, retornando respostas padronizadas com status HTTP adequados (`400 Bad Request`, `404 Not Found`, `405 Method Not Allowed`).
* **Padrão Controller-Service-Repository:** Separação clara de responsabilidades da aplicação.

## 🛠️ Endpoints Principais

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/cardapio` | Cadastra um novo item no cardápio (requer DTO validado) |
| `GET` | `/cardapio` | Lista todos os itens cadastrados |
| `GET` | `/cardapio/{id}` | Busca um item específico pelo ID |
| `PUT` | `/cardapio/{id}` | Atualiza os dados de um item existente |
| `DELETE` | `/cardapio/{id}` | Remove um item do cardápio |

## 📖 Como Executar
1. Clone o repositório: `git clone https://github.com/seu-usuario/Cardapio_digital.git`
2. Configure as credenciais do banco de dados no `application.properties`.
3. Execute a aplicação via Maven ou IDE: `./mvnw spring-boot:run`
4. Acesse a documentação Swagger em: `http://localhost:8080/swagger-ui.html`