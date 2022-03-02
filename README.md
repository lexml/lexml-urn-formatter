# API de renderização de rótulos a partir de URN

Requisitos da API de renderização de rótulos a partir de URN.

Referências de implementação em:
- `lexml-parser-projeto-lei`: https://github.com/lexml/lexml-parser-projeto-lei/blob/master/src/main/scala/br/gov/lexml/parser/pl/rotulo/rotuloParser.scala
- `lexml-parser-projeto-lei`: https://github.com/lexml/lexml-parser-projeto-lei/blob/master/src/main/scala/br/gov/lexml/parser/pl/output/LexmlRenderer.scala#L20
- `lexml-parser-projeto-lei-ws`: https://github.com/lexml/lexml-parser-projeto-lei-ws/blob/master/src/main/scala/br/gov/lexml/parser/pl/ws/tasks/FragmentFormatter.scala

A URN dos dispositivos contém informação suficiente para gerar os rótulos de normas. 


## Compilação Scala

Scala precisa ser compilado com Java 8.

First run `/usr/libexec/java_home -V` which will output something like the following:

```sh
% /usr/libexec/java_home -V
Matching Java Virtual Machines (2):
    11.0.4, x86_64:	"AdoptOpenJDK 11"	/Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home
    1.8.0_232, x86_64:	"AdoptOpenJDK 8"	/Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home
```

Then, use 

```sh
export JAVA_HOME=`/usr/libexec/java_home -v 1.8`
```

Now when you run `java -version` you will see the version 1.8.


## Rótulo de Artigo

### artN 

- `Se N = "1u"`, deve-se gerar "Art. Único." (acho que esse caso não ocorre com o nosso atual acervo)
- `Se N < 10`, deve-se gerar "Art. 2º"  (com o símbolo de ordinal, sem ponto)
- `Se N > 9`, deve-se gerar  "Art. 12." (sem o símbolo de ordinal, com ponto)`

### artN-M

Regra geral (válida também para qualquer dispositivo ou agrupador de artigo): se qualquer número vier seguido de hífen e de um número adicional, deve-se concatenar ao número existente no rótulo um hífen e a letra maiúscula considerando que a letra A corresponde ao número 1. 

Pode acontecer  artN-M-Z. E também o número pode ser maior do que 26. Nesse caso, deve-se recomeçar adicionando letras adicionais, pois a base do número é 26. 

- Por exemplo, `art1-27` = "Art. 1º-AA"
- Por exemplo, `art2-28` = "Art. 1º-AB"

## Rótulo de Dispositivos de artigos

### Rótulo de caput (`artN_cpt`)

O caput não possui rótulo (quem possui rótulo é o artigo). 

Se for necessário gerar um rótulo expandido/contextual, deve-se concatenar o do artigo com o nome ``caput``.

### Rótulo de parágrafo (`artM_parN`)

- `Se N = "1u"', deve-se gerar "Parágrafo Único.". (acho que esse caso não ocorre com o nosso atual acervo)
- `Se N < 10', deve-se gerar "§ 2º"  (com o símbolo de ordinal, sem ponto)
- `Se N > 9', deve-se gerar  "§ 12." (sem o símbolo de ordinal, com ponto)

### Rótulo de inciso (`inc`)

Os incisos são apresentados em algarismos romanos maiúsculos seguidos de branco e de um travessão curto (não é hífen) e de um branco. 

Exemplos: 
- `art3_par2_inc3` = "III –";   

### Rótulo de alínea (`ali`)

As alíneas são apresentadas em letras minúsculas seguidas de fecha-parêntesis.

Exemplos: 
- `art3_par2_inc3_ali4` = "d)";   
- `art3_par2_inc3_ali5-2` = "e-B)".

### Rótulo de Item

Os itens são apresentados em números arábicos seguidos de ponto.

Ex: `art3_par2_inc3_ali4_ite8` = "8."

## Rótulo de Agrupadores de Artigo

### Agrupadores Parte (`part`), Livro (`liv`), Título (`tit`), Capítulo (`cap`) 

Esses agrupadores são escritos em letras maiúsculas seguidos do número em algarismos romanos.

- Ex: `tit2_cap3` =  "CAPÍTULO III"

### Agrupadores  Seção (`sec`) e Subseção (`sub`) 

Esses agrupadores são escritos com a primeira maiúscula seguida de número romano em maiúsculas.

- Ex: `cap2_sec3_sub1` = "Subseção I".

## Regra geral

Se a urn for de um bloco de alteração, vale as regras acima de acordo com o último componente.

- Ex: `art3_cpt_alt1_art4_par2` = "§ 2º"
- Ex: `art3_cpt_alt1_tit3` =  "TÍTULO III"

## Release

Importar chave

```
gpg --import lexml.public.key

gpg --allow-secret-key-import --import lexml.secret.key
```

Para publicar uma versão no Maven central, você precisa de acesso ao repositório e configurar o acesso
no maven. Insira o snippet abaixo - com o correto user_name e api_key - no `~/.m2/settings.xml`.

```xml
	....
    <servers>
    ...
        <server>
          <id>ossrh</id>
          <username>XXX</username>
          <password>XXX</password>
        </server>
      </servers>
      ...
      <profiles>
     ...
        <profile>
          <id>gpg</id>
          <properties>
            <gpg.executable>gpg2</gpg.executable>
            <gpg.keyname>XXXX</gpg.keyname>
            <gpg.passphrase>XXXX</gpg.passphrase>
          </properties>
        </profile>
      </profiles>
  ....
  <activeProfiles>
    ...
    <activeProfile>gpg</activeProfile>
  </activeProfiles>
        
```

Depois execute o comando abaixo para fazer o release do projeto:

```
    mvn -Prelease release:prepare
    mvn -Prelease release:perform
```
