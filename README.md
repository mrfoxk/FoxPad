# 🦊 FoxPad

O **FoxPad** é um editor de texto minimalista, leve e funcional desenvolvido em Java Swing.

---

## 📜 História do Projeto

Anteontem eu estava no meu notebook e resolvi abrir o VS Code nele com a ideia de configurar o ambiente para voltar a programar em Java, foi mais um desafio pessoal, porque o notebook trava bastante e eu queria ver se era possível rodar tudo liso, até porque o IntelliJ IDEA nele seria impraticável.. E funcionou!

Com o VS Code configurado, resolvi criar um jogo em Java, a ideia inicial era fazer um jogo via terminal onde um caractere `#` se movia pela tela, mas descobri que não existe um método direto no Java para capturar teclas sem fazer muita gambiarra (assim como no Python) kkkkk

A partir daí, pensei: *"Por que não criar uma janela, colocar um quadrado na tela e programar para mover?"*. Eu fiz isso, mas notei um detalhe curioso: a janela não tinha exatamente o tamanho que eu especificava; ficava uns pixels menor.. testei e vi que a barra de título da janela devorava esse espaço.

Para entender melhor como as janelas do Swing funcionavam, me deu vontade de adicionar uma barra de menu, uma coisa puxou a outra, depois de entender os menus, resolvi encarar o desafio de construir um **editor de texto minimalista** só para ver no que dava.

No primeiro dia, fiz uma versão usando bastante o auto-completar do VS Code, mas o código ficou extremamente lento e bugado. Ontem pensei: *"Consigo fazer algo melhor que isso"*. Resolvi refazer tudo do zero, aplicando boas práticas de código limpo (*Clean Code*) e a convenção da linguagem. E assim nasceu o **FoxPad**!

> **Nota:** Fiquei alguns anos sem programar em Java e estou retornando agora, então ainda me considero bem iniciante nisso, mas curti bastante o resultado

---

## 🛠️ Como Compilar e Rodar

Não deixei o arquivo compilado (`.jar` ou `.class`) no repositório porque acho muito legal a experiência de você mesmo compilar o código na sua máquina

### Pré-requisitos
* **JDK (Java Development Kit)** versão 11 ou superior instalado.

### Passo a Passo


1. **Clone o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/FoxPad.git
   cd FoxPad
   ```

2. **Compile o projeto e gere o arquivo JAR:**
   ```bash
   javac Main.java
   jar cfe foxpad.jar Main *.class
   ```

3. **Execute o programa:**
    * **Modo padrão:**
      ```bash
      java -jar foxpad.jar
      ```
    * **Passando um arquivo via linha de comando (CLI):**
      ```bash
      java -jar foxpad.jar meu_arquivo.txt
      ```

---

## 🚀 Próximos Passos (Roadmap)

Tenho várias ideias de funcionalidades para adicionar nas próximas versões, entre elas um utilitário de criptografia que permita abrir e salvar arquivos criptografados utilizando **AES-256** junto a um algoritmo de derivação de chaves robusto como **Argon2** ou **PBKDF2**.

Porém, resolvi postar essa versão inicial focando em entregar uma base sólida e funcional com o essencial, se você tiver ideias, recomendações ou quiser dar um fork no projeto para adicionar uma funcionalidade nova, fique à vontade!

---

Se testar o projeto, me conta o que achou! :]
