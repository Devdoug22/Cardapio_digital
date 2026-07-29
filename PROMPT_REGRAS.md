# Diretrizes de Desenvolvimento - Menu Digital

## Stack & Configuração
- Linguagem: Java 17+
- Framework: Spring Boot 3
- Banco de Dados: Spring Data JPA com PostgreSQL/H2

## Arquitetura e Padrões de Código
- Siga estritamente a arquitetura em camadas: Controller -> Service -> Repository.
- Regras de negócio devem ficar EXCLUSIVAMENTE na camada Service.
- Controllers não devem acessar Repositories diretamente.
- DTOs (Data Transfer Objects) devem ser utilizados para entrada e saída de dados na API. Nunca exponha Entidades JPA diretamente nos Controllers.
- Utilize Lombok para redução de código boilerplate (`@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`).
- Utilize `@Builder` em DTOs e Entidades para facilitar a construção de objetos nos testes.

## Tratamento de Erros & Validação
- Respostas da API devem utilizar `ResponseEntity`.
- Lançar exceções customizadas para erros de negócio (ex: `ResourceNotFoundException`) tratadas por um `@ControllerAdvice` global.

## Qualidade & Testes
- Toda nova lógica adicionada na camada Service DEVE conter testes unitários obrigatórios utilizando JUnit 5 e Mockito.
- Garantir cobertura para cenários de sucesso e exceção.