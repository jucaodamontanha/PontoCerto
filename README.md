# Ponto Certo

Um sistema de gerenciamento de banco de horas e usuários para empresas, desenvolvido com **Java** e **Spring Boot**.

## Tecnologias Utilizadas

- **Java**: Linguagem principal do projeto.
- **Spring Boot**: Framework para criação de aplicações web.
- **Maven**: Gerenciador de dependências.
- **Jakarta Persistence (JPA)**: Para mapeamento objeto-relacional.
- **Spring Security**: Para autenticação e autorização.
- **Jackson**: Para serialização e desserialização de JSON.
- **PostgreSQL**: Banco de dados relacional utilizado.
- **Docker Compose**: Para orquestração de contêineres.

## Estrutura do Projeto

### Modelos

- **BancoDeHoras**: Representa o banco de horas de um usuário, com saldo positivo ou negativo.
- **Empresa**: Representa uma empresa, contendo uma lista de usuários associados.
- **Usuario**: Representa um usuário do sistema, com informações de autenticação e associação a uma empresa.

### Relacionamentos

- **BancoDeHoras** possui um relacionamento `@OneToOne` com **Usuario**.
- **Empresa** possui um relacionamento `@OneToMany` com **Usuario**.
- **Usuario** possui um relacionamento `@ManyToOne` com **Empresa**.

### Segurança

O projeto utiliza o **Spring Security** para gerenciar autenticação e autorização. A classe `Usuario` implementa a interface `UserDetails` para integração com o Spring Security.

## Como Executar

1. Certifique-se de ter o **Docker** e o **Docker Compose** instalados.
2. Clone o repositório:
   ```bash
   git clone https://github.com/jucaodamontanha/ponto-certo.git
   

3. Navegue até o diretório do projeto:
   ```bash
   cd ponto-certo

 4. Suba os contêineres com o Docker Compose:
      ```bash
      docker-compose up -d
    
5. Acesse o banco de dados PostgreSQL através do contêiner:
   ```bash
   docker exec -it ponto-certo-postgres psql -U postgres
   ```
6. Acesse a aplicação em seu navegador:
   ```
    http://localhost:8080
    ```
7. Para parar os contêineres, execute
```bash
   docker-compose down
   ```
## Configuração do Banco de Dados

O projeto utiliza o **PostgreSQL** como banco de dados. O arquivo `docker-compose.yml` já está configurado para criar um contêiner com o PostgreSQL. Certifique-se de que as credenciais no arquivo `application.properties` correspondem às do `docker-compose.yml`.
   
## Contribuição

1. Faça um fork do repositório.
2. Crie uma branch para sua feature:
   ```bash
   git checkout -b minha-feature
   
3. Faça commit das suas alterações:
   ```bash
    git commit -m "Adicionando minha feature"
4. Faça push para a branch:
    ```bash
    git push origin minha-feature
    ```
5. Abra um Pull Request.
## Licença
Este projeto está licenciado sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
