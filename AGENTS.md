# Orientações para agentes

Biblioteca Scala que transforma URNs LexML e seus fragmentos em rótulos e
referências legislativas em português. O artefato é um JAR compilado com Maven.

## Onde trabalhar

As fontes estão em `src/main/scala/br/gov/lexml/urnformatter/`:

- `Urn2Rotulo`: rótulo do dispositivo, como `Art. 1º`.
- `Urn2Nome`: nome com a hierarquia, como `caput do art. 1º`.
- `Urn2Format`: funções compartilhadas de numeração e complementos.
- `compacto/Urn2NomeCompacto`: referências compactas a uma ou várias URNs,
  com contexto opcional. Usa `UrnParser`, `AgrupadorUrn` e `Nomeador`.

Ao alterar regras de formatação, consulte as seções de rótulos do `README.md`
e os testes da API afetada em `src/test/scala/br/gov/lexml/urnformatter/`.
As três APIs têm saídas distintas; preserve essas diferenças. Trate espaços,
acentos, ordinais, pontuação e maiúsculas como parte do contrato da saída.

## Compilação e validação

Use o Maven Wrapper e JDK 17. Consulte o `pom.xml` para as versões de Scala,
dependências e plugins; consulte a seção de compilação do `README.md` para
configurar o JDK e o IntelliJ. O `release` do compilador Java define o alvo
do bytecode Java, não o JDK usado para executar o Maven ou o compilador Scala.

```sh
./mvnw -version
./mvnw clean verify
```

Para validar uma suíte durante o desenvolvimento:

```sh
./mvnw -Dtest=Urn2NomeCompactoTest test
```

Os testes existentes estendem `junit.framework.TestCase` e usam métodos com
prefixo `test`. Acrescente casos de entrada e saída na suíte correspondente
quando mudar o comportamento. A suíte de nomes se chama `Uren2NomeTest`
(com essa grafia); a de rótulos se chama `Urn2RotuloTest`.

Após mudanças de código ou dependências, execute `./mvnw clean verify` e
confira os resultados em `target/surefire-reports/`. Informe o JDK utilizado
e eventuais falhas. Sucesso pelo Maven não comprova execução pelo IntelliJ;
ao atualizar Scala, recarregue o projeto Maven na IDE para alinhar as versões.
Para mudanças apenas de documentação, confira os caminhos, os comandos e
`git diff --check`.

## Cuidados nas alterações

Preserve as assinaturas públicas dos métodos `format` e os comportamentos
existentes fora do escopo da mudança. Mantenha o estilo do arquivo editado e
os termos de domínio e documentação em português. Atualize a documentação quando mudar os
requisitos de compilação ou as regras documentadas de formatação.

Use o ciclo normal de Maven para validar localmente. O perfil `release` e
os comandos da seção de release do `README.md` são destinados à publicação,
quando ela fizer parte da tarefa.

Em Java/Scala, importe as classes no início do arquivo e use seus nomes simples no código
(ex.: `Arrays` com `import java.util.Arrays;`). Use nomes qualificados apenas
quando necessário para resolver conflitos de nomes.
