import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main extends JFrame {

    private JFileChooser arquivo = new JFileChooser();
    private JTextArea editor = new JTextArea();
    private String conteudoSalvo = "";

    public static void main(String[] args){
        new Main(args);
    }

    public Main(String[] args){
        // Configuracoes da janela
        FileNameExtensionFilter filtroTxt = new FileNameExtensionFilter("Documentos de Texto (*.txt)", "txt");
        arquivo.setFileFilter(filtroTxt); // configura o filtro de arquivos do seletor para arquivos de texto
        this.setSize(600, 600); // tamanho da janela
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // x nao fechara mais a janela
        this.setLocationRelativeTo(null);
        setupListeners(); // prepara os eventos do programa
        setupMenuBar(); // insere os menus a janela principal
        addTextEditor(); //adiciona o editor de texto a janela do programa
        this.setVisible(true);

        abrirArquivoPorParametro(args); // coleta nomes de arquivos nos argumentos pra abrir arquivos rapidamente
        atualizarTitulo();
    }

    // prepara os eventos
    private void setupListeners(){
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                sairDoPrograma();
            }
        });

        editor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                atualizarTitulo();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                atualizarTitulo();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
    }

    // prepara a barra de menu
    private void setupMenuBar(){
        //criacao da barra de menu
        JMenuBar barraDeMenu = new JMenuBar();

        // 1. Menu "Arquivo"

        JMenu menuArquivo = new JMenu("Arquivo");

        // 1.1 "Menu" / "Novo"
        JMenuItem opcaoNovo = new JMenuItem("Novo"); // Titulo
        opcaoNovo.addActionListener(new ActionListener() { // Evento
            @Override
            public void actionPerformed(ActionEvent e) {
                novoArquivo();
            }
        });
        menuArquivo.add(opcaoNovo); // Adiciona a opcao
        opcaoNovo.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK)
        );

        // 1.2 "Menu" / "Abrir"
        JMenuItem opcaoAbrir = new JMenuItem("Abrir.."); // Titulo
        opcaoAbrir.addActionListener(new ActionListener() { // Evento
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirArquivo();
            }
        });
        menuArquivo.add(opcaoAbrir); // Adiciona a opcao
        opcaoAbrir.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK)
        );

        // 1.3 "Menu" / "Salvar"
        JMenuItem opcaoSalvar = new JMenuItem("Salvar"); // Titulo
        opcaoSalvar.addActionListener(new ActionListener() { // Evento
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarArquivo();
            }
        });
        menuArquivo.add(opcaoSalvar); // Adiciona a opcao
        opcaoSalvar.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK)
        );
        // 1.4 "Menu" / "Salvar como..."
        JMenuItem opcaoSalvarComo = new JMenuItem("Salvar como.."); // Titulo
        opcaoSalvarComo.addActionListener(new ActionListener() { // Evento
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarArquivoComo();
            }
        });
        menuArquivo.add(opcaoSalvarComo); // Adiciona a opcao
        opcaoSalvarComo.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)
        );

        menuArquivo.addSeparator(); // adiciona uma separacao antes do sair

        // 1.5 "Menu" / "Sair"
        JMenuItem opcaoSair = new JMenuItem("Sair"); // Titulo
        opcaoSair.addActionListener(new ActionListener() { // Evento
            @Override
            public void actionPerformed(ActionEvent e) {
                sairDoPrograma();
            }
        });
        menuArquivo.add(opcaoSair); // Adiciona a opcao
        opcaoSair.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK)
        );


        barraDeMenu.add(menuArquivo); // adiciona o menu arquivo a barra de menu
        this.setJMenuBar(barraDeMenu); // adiciona a barra de menu a janela
    }

    // verifica se um nome de arquivo foi especificado como parametro na hora que o programa foi abrir
    private void abrirArquivoPorParametro(String[] args){
        if(args.length > 0){
            //existe parametro de nome de arquivo
            File file = new File(args[0]);
            arquivo.setSelectedFile(file);
            if (file.exists() && file.isFile()) lerArquivo();

        }
    }

    // adiciona o editor a janela
    private void addTextEditor(){
        editor.setFont(new Font("Consolas", Font.PLAIN, 16));
        editor.setLineWrap(true);
        editor.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(editor);
        this.add(scroll);
    }

    // verifica se existe dados pra salvar
    private boolean temDadosSemSalvar(){
        return !editor.getText().equals(conteudoSalvo);
    }

    // verifica se tem arquivos no seletor de arquivos ou se esta vazio
    private boolean temArquivoNoSeletor(){
        return arquivo.getSelectedFile() != null;
    }

    // exibe a janela que pergunta se o usuario deseja salvar o arquivo
    private int confirmarSalvamento(String filename){
        if (filename == null) filename = "Sem titulo";
        return JOptionPane.showConfirmDialog(
                        this,
                "Deseja salvar as alteracoes em \"" + filename + "\"?",
                "FoxPad",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE
                );
    }

    // rotina de criacao de novos arquivos
    private void novoArquivo(){
        //verifica se existe dados sem salvar
        if(temDadosSemSalvar()) {
            int confirmacao = confirmarSalvamento((arquivo.getSelectedFile() != null) ? arquivo.getSelectedFile().getName() : "Sem titulo");
            if (confirmacao == JOptionPane.CANCEL_OPTION || confirmacao == JOptionPane.CLOSED_OPTION) {
                return;
            } else if (confirmacao == JOptionPane.YES_OPTION) {
                if (!temArquivoNoSeletor() && !selecionarArquivoParaEscrita()) {
                    //usuario nao tinha arquivo no seletor e nao o selecionou na janela, ele desistiu
                    //como ele tem dados nao salvos ele nao pode continuar
                    return;
                }
                // se chegou ate aqui ele tem arquivo no seletor e escolheu salvar
                escreverArquivo();
            }
        }
        //se chegou aqui podemos criar um arquivo novo pois nada sera perdido
        editor.setText("");
        arquivo.setSelectedFile(null);
        conteudoSalvo = "";
        atualizarTitulo();


    }

    // rotina de abertura de arquivo
    private void abrirArquivo(){
        // verifica se existe dados sem salvar
        if(temDadosSemSalvar()){
            //se sim pergunta o que o usuario quer fazer
            int confirmacao = confirmarSalvamento((arquivo.getSelectedFile() != null)? arquivo.getSelectedFile().getName() : "Sem titulo");
            if (confirmacao == JOptionPane.YES_OPTION){
                // usuario informou que deseja salvar
                if(!temArquivoNoSeletor() && !selecionarArquivoParaEscrita()){
                    //usuario nao tinha arquivo no seletor e nao o selecionou na janela, ele desistiu
                    //como ele tem dados nao salvos ele nao pode continuar
                    return;
                }
                // se chegou ate aqui ele tem arquivo no seletor e escolheu salvar
                escreverArquivo();
            }
            if (confirmacao == JOptionPane.CANCEL_OPTION || confirmacao == JOptionPane.CLOSED_OPTION){
                //o usuario clicou em cancelar, finalizar loop e retornar daqui
                return;
            }
        }
        // se chegou ate aqui ou ele salvou ou nao tinha nada pra salvar
        if(selecionarArquivoParaLeitura()){
            //foi solicitado arquivo pra abrir e ele o informou
            lerArquivo();
        }
    }

    // rotina de salvamento de arquivo
    private void salvarArquivo(){
        if(!temArquivoNoSeletor()){
            //se tem dados para que sejam salvos e nao tem arquivo no seletor..
            if(!selecionarArquivoParaEscrita()){
                //se falhar ao tentar selecionar arquivo para escrever
                return; //entao retorne
            }
        }
        // se chegar ate aqui tem arquivo no seletor
        escreverArquivo();
    }

    // rotina para salvar arquivo como..
    private void salvarArquivoComo(){
        if(selecionarArquivoParaEscrita()){
            escreverArquivo();
        }
    }

    private void sairDoPrograma(){
        if(temDadosSemSalvar()){
            int confirmar = confirmarSalvamento((arquivo.getSelectedFile() != null)? arquivo.getSelectedFile().getName() : "Sem titulo");

            // se o usuario responder cancelar
            if (confirmar == JOptionPane.CANCEL_OPTION || confirmar == JOptionPane.CLOSED_OPTION){
                // usuario cancelou a saida
                return;
            }
            if (confirmar == JOptionPane.NO_OPTION){
                int reconfirmar = JOptionPane.showConfirmDialog(
                        this,
                        "Tem certeza que deseja sair sem salvar?",
                        "FoxPad",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (reconfirmar != JOptionPane.YES_OPTION){
                    // usuario cancelou na ultima hora
                    return;
                }
            }

            if (confirmar == JOptionPane.YES_OPTION){
                // verifica se tem arquivo no seletor
                if(!temArquivoNoSeletor()){
                    // nao temos um arquivo no seletor, nesse caso tentamos abrir a janela e selecionar um
                    if (!selecionarArquivoParaEscrita()){
                        //se a selecao falhar cancelamos a operacao e nao saimos do programa
                        return;
                    }
                }
                // temos arquivo no seletor nesse caso vamos escrever
                if (!escreverArquivo()){
                    //se a escrita de arquivo falhar por algum motivo os dados nao foram salvos, nesse caso nao feche o programa
                    return;
                }
            }
        }
        dispose();
    }

    // logica para abrir o arquivo que esta no seletor e colocar o conteudo no editor
    private void lerArquivo(){
        try{
            String conteudoDoArquivo = Files.readString(arquivo.getSelectedFile().toPath());
            editor.setText(conteudoDoArquivo);
            conteudoSalvo = conteudoDoArquivo;
            atualizarTitulo();
        } catch (IOException e){
            mensagemDeErro("Erro de leitura", "Nao foi possivel abrir o arquivo");
        }
    }

    // logica pra escrever no arquivo
    private boolean escreverArquivo(){
        try{
            Files.writeString(Path.of(arquivo.getSelectedFile().getPath()), editor.getText());
            conteudoSalvo = editor.getText();
            atualizarTitulo();
            return true;
        } catch (IOException e) {
            mensagemDeErro("Erro de escrita", "Nao foi possivel escrever no arquivo especificado");
        }
        return false;
    }

    // funcao que exibe a janela pra escolha de arquivo com finalidade de leitura
    private boolean selecionarArquivoParaLeitura(){
        int resultado = arquivo.showOpenDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION){
            return true;
        }
        return false;
    }

    // funcao que exibe a janela pra escolha de arquivo com finalidade de escrita
    private boolean selecionarArquivoParaEscrita(){
        int resultado = arquivo.showSaveDialog(null);
        if (resultado == JFileChooser.APPROVE_OPTION){
            return true;
        }
        return false;
    }

    // garante que o simbolo de status de edicao esteja sempre atualizado
    private void atualizarTitulo(){
        this.setTitle((temDadosSemSalvar()? "*" : "") + (arquivo.getSelectedFile() != null? arquivo.getSelectedFile().getName() : "Sem titulo") + " - FoxPad");
    }

    // mostra mensagens de erro
    private void mensagemDeErro(String titulo, String mensagem){
        JOptionPane.showMessageDialog(
                this,
                mensagem,
                titulo,
                JOptionPane.ERROR_MESSAGE
        );
    }
}