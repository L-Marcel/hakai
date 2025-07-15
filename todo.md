# Primeiro, crie o QuestionVariantStorage

1. Ele precisa armazenar um `List<QuestionVariant>`;
2. O nome do arquivo deve ser o `UUID` da `Question` (original);
3. Tem que ter todos os método que tem no `QuestionStorage` do `framework`.

# Sobre o PersistentRoom

1.  Não vai precisar de `Enum`, só um `Boolean` para dizer se está aberto ou fechado;
2. Além disso, só vai precisar do `UUID` do `Room`.

# Fluxo

Explicando o fluxo:

- O dono da sala abre o painel;
- No painel, ele pode alterar a duração da sala;
- O botão de lançar é trocado pelo botão de inciar ou pausar;
- Quando tiver a duração e as variantes geradas, o dono pode iniciar;
- Ao clicar em gerar, as variantes são enviadas por uma requisição `post` e salvas utilizando po `QuestionVariantStorage`;
- O participante só pode entrar na sala se o `PersistentRoom` estiver `aberto`;
- Ao entrar, ele solicita via requisição `get` as `List<QuestionVariant>`:
    - Ainda vai precisar usar o método de `sendAll` que eu criei, para que passe pelo
    `DistributionStrategy` que tu criou;
    - Ou seja, o participante ainda recebe via websocket as questões.


