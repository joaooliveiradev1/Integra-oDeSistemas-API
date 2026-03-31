🤝 ApoiaCe API
API RESTful para gerenciamento de campanhas de crowdfunding, organizando projetos por categorias e conectando criadores a apoiadores.

Trabalho acadêmico — Entrega: 09/04/2026

📋 Sobre o Projeto
API desenvolvida com Java 17 + Spring Boot 3.x e banco de dados MySQL 8, sem autenticação. CRUD completo para 3 recursos que modelam um sistema de crowdfunding regional.

Tecnologia	Versão
Java	17
Spring Boot	3.x
MySQL	8
Gradle	8.x


🗂️ Recursos
👤 Usuários (/usuarios)
Campo	Tipo	Descrição
id	Long	Identificador único
nome	String	Nome completo
email	String	E-mail (único)
telefone	String	Número de contato
cpf	String	CPF (único)
dataCadastro	LocalDate	Data de criação da conta


🏷️ Categorias (/categorias)
Campo	Tipo	Descrição
id	Long	Identificador único
nome	String	Nome da categoria
descricao	String	Descrição da categoria
icone	String	Ícone representativo (ex: saude)
ativa	Boolean	Se a categoria está disponível
cor	String	Cor de destaque em hex (ex: #01696f)


📢 Projetos (/projetos)
Campo	Tipo	Descrição
id	Long	Identificador único
titulo	String	Título do projeto
descricao	String	Descrição detalhada
meta	BigDecimal	Valor alvo de arrecadação
status	String	ATIVO, ENCERRADO, PAUSADO
dataInicio	LocalDate	Data de início
usuarioId	Long	FK → Usuário responsável
categoriaId	Long	FK → Categoria do projeto


🔗 Relacionamentos

Um Usuário pode criar vários Projetos

Uma Categoria agrupa vários Projetos

Buscar por categoria retorna todos os projetos vinculados

Buscar por usuário retorna todos os seus projetos

🚀 Endpoints
Usuários
text
GET    /usuarios           → Lista todos os usuários
GET    /usuarios/{id}      → Busca usuário por ID
POST   /usuarios           → Cria novo usuário
PUT    /usuarios/{id}      → Atualiza usuário
DELETE /usuarios/{id}      → Remove usuário
Categorias
text
GET    /categorias             → Lista todas as categorias
GET    /categorias/{id}        → Busca categoria por ID
GET    /categorias/ativas      → Lista somente categorias ativas
POST   /categorias             → Cria nova categoria
PUT    /categorias/{id}        → Atualiza categoria
DELETE /categorias/{id}        → Remove categoria
Projetos
text
GET    /projetos                          → Lista todos os projetos
GET    /projetos/{id}                     → Busca projeto por ID
GET    /projetos/usuario/{usuarioId}      → Projetos de um usuário
GET    /projetos/categoria/{categoriaId}  → Projetos de uma categoria
GET    /projetos/status/{status}          → Filtra por status
POST   /projetos                          → Cria novo projeto
PUT    /projetos/{id}                     → Atualiza projeto
DELETE /projetos/{id}                     → Remove projeto
🛠️ Como Rodar Localmente
Pré-requisitos
Java 17+

Gradle

MySQL 8 rodando na porta 3306

Passos
Clone o repositório:

bash
git clone https://github.com/seu-usuario/apoiace-api.git
cd apoiace-api
Crie o banco de dados:

sql
CREATE DATABASE apoiace;
Configure src/main/resources/application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/apoiace?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Fortaleza
spring.datasource.username=apoiace
spring.datasource.password=sua_senha
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.open-in-view=false

Rode a aplicação:

bash
./gradlew bootRun
Acesse: http://localhost:8080

☁️ Deploy
API disponível em produção:
🔗 https://apoiace-api.up.railway.app (substituir pelo link real após deploy)

🧪 Exemplos de Requisição
Criar usuário:

text
POST /usuarios
Content-Type: application/json

{
  "nome": "João Oliveira",
  "email": "joao@email.com",
  "telefone": "85999999999",
  "cpf": "123.456.789-00",
  "dataCadastro": "2026-04-01"
}
Criar categoria:

text
POST /categorias
Content-Type: application/json

{
  "nome": "Saúde",
  "descricao": "Projetos voltados para saúde e bem-estar comunitário.",
  "icone": "saude",
  "ativa": true,
  "cor": "#01696f"
}
Criar projeto:

text
POST /projetos
Content-Type: application/json

{
  "titulo": "Reforma da UBS do Conjunto Ceará",
  "descricao": "Arrecadação para reforma da unidade básica de saúde do bairro.",
  "meta": 15000.00,
  "status": "ATIVO",
  "dataInicio": "2026-04-09",
  "usuarioId": 1,
  "categoriaId": 1
}
👥 Equipe
Nome	GitHub
João Antonio Pimentel (joaooliveiradev1)
Jully Emerson Ribeiro Costa (EmsRibeiro)
Victor Gomes Barbosa (victorgomesbarbosa1)

📄 Licença
MIT
