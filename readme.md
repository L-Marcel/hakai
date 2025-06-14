# Sumário
- [Instruções para devs (geral)](#instruções-para-devs-geral)
    - [Commits](#commits)
        - [Exemplo](#exemplo)
        - [Justificativa](#justificativa)
    - [Branchs](#branchs)
        - [Exemplo](#exemplo-1)
- [Instruções para devs (backend)](#instruções-para-devs-backend)
- [Instruções para devs (frontend)](#instruções-para-devs-frontend)
    - [Tunelamento](#tunelamento)

# Instruções para devs (geral)

Para manter o projeto por inteiro rodando, será necessário manter os três servidores rodando (banco de dados, _backend_ e _frontend_).

Deixo como recomendação a utilização do [VSCode](https://code.visualstudio.com/) para todo o projeto. Também estarei utilizando o [pgAdmin4](https://www.pgadmin.org/download/) para visualizar e gerenciar o banco de dados.

Recomendo deixar todas as _queries_ dentro da pasta `queries` do projeto, assim todos nós conseguiremos utilizar.

Fique atento ao momento de gerar os _commits_, faça isso com frequência. Atenção, segue abaixo as _tags_ padrões de nomeação de _commits_:

## Commits
- `[feat]` -> Implementação parcial ou completa de uma determinada funcionalidade;
- `[merge]` -> União de duas _branchs_;
- `[fix]` -> Correção de um erro antigo;
- `[docs]` -> Alterações envolvendo documentação ou comentários;
- `[style]` -> Formatação de código;
- `[refactor]` -> Refatoração de código, melhorias que não alteram a funcionalidade;
- `[test]` -> Testes de qualquer tipo;
- `[init]` -> Código inicial do projeto, normalmente gerado por alguma ferramenta;
- `[chore]` -> Vamos definir aqui como tudo que não se encaixar nas definições anteriores.  

Meu conselho é inverter a lógica. Geralmente, pensamos no nome na hora de realizar o _commit_ (eu faço assim). Agora, vamos pensar no nome antes (não por completo, apenas a tag).

### Exemplo

Estou entrando para começar a implementação de uma nova funcionalidade, que envolve criar uma sala.

O _commit_: `[feat] Iniciando implementação de criação de salas`.

O nome já sugere que não terminei. Vamos supor que seja porque eu encontrei um _bug_ no caminho. Então agora tenho que resolver esse _bug_.

O _commit_: `[fix] Corrigindo erro de salas duplicadas`.

Agora estou indo terminar a funcionalidade.

O _commit_: `[feat] Implementando criação de salas`.

Agora todos sabemos que a funcionalidade foi implementada.

### Justificativa

É chato manter esse padrão, mas assim conseguimos manter o histórico do código organizado, bem definido e separado.

## Branchs

Crie uma _branch_ sempre que for inicializar uma nova funcionalidade. O nome dela tem que ser sugestivo, de modo que possamos identificar a funcionalidade. Assim que terminar, abra um `Pull Request` para a _branch main_ e aguarde avaliação.

### Exemplo

Estou indo implementar a criação de canais. Logo criarei a _branch_ `criação-canais`.

# Instruções para devs (backend)

Certifique-se de ter o [Java](https://www.oracle.com/br/java/technologies/downloads/) (v21 - LTS) instalado. Além disso, é bom ter o [Maven](https://maven.apache.org/install.html) também.

Também certifique-se de ter o [Docker](https://docs.docker.com/desktop/setup/install/windows-install/). Pode ser meio complicado, deixo esse [manual](https://efficient-sloth-d85.notion.site/Instalando-Docker-e-Docker-Compose-7953729d22554795b50033c4c19eae70) como recomendação.

Para executar a aplicação `Java`, estou utilizando o pacote de extensões [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack). Com ele, basta abrir o arquivo `backend/src/main/java/app/hakai/backend/BackendApplication.java` e você verá um botão de _play_ no canto superior esquerdo.

Vale destacar que você precisará criar um arquivo `backend/src/main/resources/secret.properties`, responsável por armazenar nossas chaves secretas do [Open Router](https://openrouter.ai/). As chaves são separadas por vírgula e são utilizadas de forma distribuída. Segue um exemplo deste arquivo:

```properties
ai.keys=<CHAVE 1>,<CHAVE 2>
```

Para inicializar o banco de dados `Postgres` através do `Docker`, execute:

```cmd
cd backend
docker-compose up -d
```

Se der erro e você tiver instalado o `Docker` corretamente, provavelmente se esqueceu de inicializar o `Docker Desktop`.

# Instruções para devs (frontend)

O gerenciador de pacotes utilizado no _frontend_ é o [pnpm](https://pnpm.io/pt/installation). Portanto, o instale. Talvez seja necessário instalar o [node](https://nodejs.org/pt) (v22.14.0 - LTS) antes também.

Um vez que tenha instalado tudo, execute os seguintes comandos dentro da raíz do projeto clonado:

```cmd
cd frontend
pnpm install
```

Agora, crie um arquivo `.env` na pasta `frontend` com o conteúdo idêntico ao arquivo `.env.example`, disponível na mesma pasta.

Feito isso, para executar `localmente`, uma vez dentro da pasta `frontend`, basta:

```cmd
pnpm dev
```

## Tunelamento

Como não vamos levar para a produção essa aplicação, para tornar toda aplicação algo mais apresentável, estamos utilziando o [Ngrok](https://ngrok.com/) para relizar um `tunelamento` do nosso `localhost` para um domínio público na internet.

Para isso, temos um `script` que faz toda a lógica de sobrescrita das variáveis de ambiente do `frontend`. Para tal, se faz necessário que crie um conta no `Ngrok`, obtenha um `token` e coloque ele no arquivo `.env` criado anteriormente.

Já deixamos uma variável de ambiente no arquivo de `.env.example` que representa a variável que você deve criar.

Para executar com o `tunelamento`, use:

```cmd
pnpm ngrok
```

Ao invés de:

```cmd
pnpm dev
```