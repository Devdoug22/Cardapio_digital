# 🍽️ Menu Digital - Full-Stack App (Focus: Backend)

O **Menu Digital** é um sistema completo desenvolvido para a gestão e exibição de itens de restaurantes ou lanchonetes. O projeto foi construído separando estritamente as responsabilidades entre uma API REST robusta no ecossistema Java e uma interface de validação dinâmica em React.

> **Nota de Arquitetura:** Como meu foco de carreira e estudos é 100% voltado para o **Desenvolvimento Backend**, utilizei ferramentas de Inteligência Artificial para acelerar a construção da interface visual em React. Isso me permitiu blindar meu tempo para focar na qualidade do código Java, padronização de rotas, injeção de dependências e regras de negócio.

---

## 🛠️ Tecnologias e Ferramentas

### Backend (Foco Principal)
*   **Java 17**
*   **Spring Boot 3.x**
*   **Spring Data JPA** (Persistência e comunicação com o banco)
*   **Lombok** (Produtividade e eliminação de código boilerplate através de anotações como `@RequiredArgsConstructor`)
*   **PostgreSQL / MySQL** (Banco de dados relacional)

### Frontend (Interface de Validação)
*   **React** (Componentização e gerenciamento de estado)
*   **Axios** (Consumo da API REST)

---

## 📐 Estrutura do Projeto

O repositório está organizado de forma monorepo para facilitar a visualização do ecossistema:
```text
├── backend/       # API REST em Spring Boot
└── frontend/      # Interface Web em React