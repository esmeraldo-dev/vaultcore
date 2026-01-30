# 🏦 VaultCore - Sistema de Pagamentos Simplificado

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)
![JUnit5](https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)

> **"Não é só sobre transferir dinheiro, é sobre garantir que ele não suma no caminho."**

O **VaultCore** é uma API RESTful robusta desenvolvida para simular operações financeiras de uma carteira digital (semelhante ao PicPay Simplificado). O foco principal deste projeto foi implementar regras de negócio complexas, garantir a integridade dos dados transacionais e fornecer uma arquitetura limpa e testável.

---

## 🚀 Tecnologias Utilizadas

O projeto foi construído com a stack moderna do ecossistema Java:

- **Java 17**: Linguagem base (LTS).
- **Spring Boot 3.4**: Framework principal para injeção de dependência e configuração automática.
- **Spring Data JPA / Hibernate**: Persistência e modelagem de dados.
- **PostgreSQL**: Banco de dados relacional (Produção/Dev).
- **H2 Database**: Banco em memória para testes rápidos.
- **Spring Security + JWT**: (Implementado para proteção de rotas sensíveis).
- **Spring Cloud OpenFeign**: Integração com serviços externos (Autorizador).
- **Lombok**: Redução de boilerplate code.
- **JUnit 5 & Mockito**: Testes Unitários e de Integração.
- **Swagger / OpenAPI**: Documentação viva da API.

---

## ⚙️ Arquitetura e Design Patterns

O projeto segue uma arquitetura em camadas bem definida:

1.  **Controllers (`@RestController`)**: Entradas da API, tratamento de DTOs e repasse para a camada de serviço.
2.  **Services (`@Service`)**: Onde a mágica acontece. Contém toda a lógica de negócio, validações de saldo, regras de lojista e orquestração de transações.
3.  **Repositories (`@Repository`)**: Interface com o banco de dados via JPA.
4.  **Domain (`@Entity`)**: Modelagem das tabelas (User, Wallet, Transaction).
5.  **Exception Handling (`@ControllerAdvice`)**: Tratamento global de erros para retornar respostas JSON amigáveis e padronizadas (RFC 7807).

---

## 🛠️ Funcionalidades Principais

- [x] **Cadastro de Usuários**: Criação de contas para usuários Comuns e Lojistas, com validação única de CPF e E-mail.
- [x] **Transferências Financeiras**:
    - Transações atômicas com `@Transactional` (Rollback garantido em caso de erro).
    - Validação de saldo disponível.
    - Regra de negócio: **Lojistas apenas recebem**, não enviam transferências.
- [x] **Consultas Externas**: Validação da transação via serviço mockado (simulando autorizador externo).
- [x] **Notificações Assíncronas**: Envio de notificação (mock) processado em background para não travar a requisição do usuário.
- [x] **Testes Automatizados**: Cobertura de testes de integração para fluxos críticos (Criação de Usuário, Transferência).

---

## 🛠️ Como rodar o Projeto

Este guia foi estruturado para ambientes **Windows** utilizando a IDE **IntelliJ IDEA**, garantindo uma configuração rápida e eficiente.

### 📋 Pré-requisitos

1. **JDK 17** ou superior instalado e configurado nas variáveis de ambiente (`JAVA_HOME`).
2. **PostgreSQL** instalado e com o serviço ativo.
3. **IntelliJ IDEA** (Community ou Ultimate).

---

### Passo a Passo

### 🚀 Passo a Passo para Configuração Local

#### 1. Preparação do Banco de Dados
A aplicação necessita de um banco de dados previamente criado para realizar a migração das tabelas.
* Utilize o **pgAdmin 4** ou sua ferramenta SQL de preferência.
* Execute o comando para criar o banco: 
```sql
CREATE DATABASE vaultcore_db;
```
#### 2. Clonagem do Repositório
Abra o terminal (Powershell ou CMD) e execute os comandos abaixo:
```bash
git clone https://github.com/esmeraldo-dev/vaultcore.git
cd vaultcore
```
### 3. Importação do Projeto
  - Abra o IntelliJ IDEA.
  - Vá em File > Open e selecione a pasta raiz do projeto vaultcore.
  - Aguarde a importação das dependências pelo Maven (o progresso pode ser acompanhado no canto inferior direito da IDE).
### 4. Configuração das Propriedades
Para que a aplicação se conecte ao seu banco de dados local, edite o arquivo: 
```src/main/resources/application.properties```

Atualize as seguintes chaves com as suas credenciais:
```Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/vaultcore_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```
### 5. Execução da Aplicação
Você pode iniciar o projeto de duas formas:

  -Via IDE: Localize a classe ```VaultCoreApplication.java```, clique com o botão direito e selecione a opção Run.

  -Via Terminal (Maven Wrapper):
```PowerShell
.\mvnw.cmd spring-boot:run
```
### 6. Acesso à Documentação
Com a aplicação rodando, a documentação interativa dos endpoints (Swagger/OpenAPI) estará disponível em: 🔗 http://localhost:8080/swagger-ui.html

### 🔮 Melhorias Futuras (Roadmap)

Como todo bom software, este está em constante evolução. Os próximos passos mapeados são:

[ ] Controle de Concorrência: Implementar Optimistic Locking (@Version) ou Pessimistic Locking para evitar Race Conditions em transferências simultâneas extremas.

[ ] CI/CD: Configurar pipeline no GitHub Actions para deploy automático.

[ ] Observabilidade: Adicionar logs estruturados e métricas com Prometheus/Grafana.



Desenvolvido por Vinícius Esmeraldo.
