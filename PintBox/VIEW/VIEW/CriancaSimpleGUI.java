package VIEW;

import MODEL.Crianca;
import CONTROLLER.CriancaController;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CriancaSimpleGUI extends Frame implements ActionListener {
    private CriancaController controller;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Componentes da interface
    private TextArea listaArea;
    private TextField nomeField;
    private TextField idadeField;
    private TextField descricaoField;
    private TextField nascimentoField;
    private TextField chegadaField;
    private TextField orfanatoField;
    private TextField buscaField;
    
    private Button novoBtn;
    private Button listarBtn;
    private Button buscarBtn;
    private Button sairBtn;
    private Button salvarBtn;
    private Button cancelarBtn;
    
    private Dialog formularioDialog;
    
    public CriancaSimpleGUI(CriancaController controller) {
        this.controller = controller;
        initializeGUI();
        atualizarLista();
    }
    
    private void initializeGUI() {
        setTitle("Sistema de Gerenciamento de Crianças - Orfanato v2.0");
        setSize(800, 600);
        setLayout(new BorderLayout());
        
        // Painel superior com título
        Panel tituloPanel = new Panel();
        Label titulo = new Label("Sistema de Gerenciamento de Crianças");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        tituloPanel.add(titulo);
        add(tituloPanel, BorderLayout.NORTH);
        
        // Painel central com área de lista
        listaArea = new TextArea("", 20, 80, TextArea.SCROLLBARS_BOTH);
        listaArea.setEditable(false);
        listaArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(listaArea, BorderLayout.CENTER);
        
        // Painel inferior com controles
        Panel controlPanel = new Panel(new FlowLayout());
        
        novoBtn = new Button("Nova Criança");
        listarBtn = new Button("Listar Todas");
        buscarBtn = new Button("Buscar");
        sairBtn = new Button("Sair");
        
        buscaField = new TextField(20);
        
        novoBtn.addActionListener(this);
        listarBtn.addActionListener(this);
        buscarBtn.addActionListener(this);
        sairBtn.addActionListener(this);
        
        controlPanel.add(new Label("Busca:"));
        controlPanel.add(buscaField);
        controlPanel.add(buscarBtn);
        controlPanel.add(new Label("    "));
        controlPanel.add(novoBtn);
        controlPanel.add(listarBtn);
        controlPanel.add(sairBtn);
        
        add(controlPanel, BorderLayout.SOUTH);
        
        // Configurar fechamento da janela
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        
        setLocationRelativeTo(null);
    }
    
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        
        switch (comando) {
            case "Nova Criança":
                abrirFormularioCadastro();
                break;
            case "Listar Todas":
                atualizarLista();
                break;
            case "Buscar":
                buscarCrianca();
                break;
            case "Sair":
                System.exit(0);
                break;
            case "Salvar":
                if (salvarCrianca()) {
                    formularioDialog.dispose();
                    atualizarLista();
                }
                break;
            case "Cancelar":
                formularioDialog.dispose();
                break;
        }
    }
    
    private void atualizarLista() {
        StringBuilder sb = new StringBuilder();
        sb.append("=" .repeat(80)).append("\n");
        sb.append("                    LISTA DE CRIANÇAS CADASTRADAS\n");
        sb.append("=".repeat(80)).append("\n\n");
        
        List<Crianca> criancas = controller.getCriancas();
        
        if (criancas.isEmpty()) {
            sb.append("                   Nenhuma criança cadastrada.\n");
        } else {
            for (Crianca crianca : criancas) {
                sb.append(formatarCrianca(crianca)).append("\n");
                sb.append("-".repeat(80)).append("\n");
            }
            sb.append("\nTotal: ").append(criancas.size()).append(" criança(s)\n");
        }
        
        listaArea.setText(sb.toString());
    }
    
    private String formatarCrianca(Crianca crianca) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("ID: %-3d | Nome: %-25s | Idade: %d anos\n",
                crianca.getId(), crianca.getNome(), crianca.getIdade()));
        sb.append(String.format("Nascimento: %-12s | Chegada: %-12s | Orfanato: %d\n",
                crianca.getDataNascimento().format(dateFormatter),
                crianca.getDataChegada().format(dateFormatter),
                crianca.getIdOrfanato()));
        sb.append(String.format("Descrição: %s", crianca.getDescricao()));
        return sb.toString();
    }
    
    private void buscarCrianca() {
        String busca = buscaField.getText().trim();
        if (busca.isEmpty()) {
            mostrarMensagem("Digite algo para buscar!");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(80)).append("\n");
        sb.append("                    RESULTADO DA BUSCA\n");
        sb.append("=".repeat(80)).append("\n\n");
        
        List<Crianca> encontradas;
        
        try {
            // Tenta buscar por ID primeiro
            int id = Integer.parseInt(busca);
            Crianca crianca = controller.buscarPorId(id);
            if (crianca != null) {
                sb.append(formatarCrianca(crianca)).append("\n");
                sb.append("Busca por ID: Criança encontrada!\n");
            } else {
                sb.append("Nenhuma criança encontrada com ID: ").append(id).append("\n");
            }
        } catch (NumberFormatException e) {
            // Se não é número, busca por nome
            encontradas = controller.buscarPorNome(busca);
            if (encontradas.isEmpty()) {
                sb.append("Nenhuma criança encontrada com nome: ").append(busca).append("\n");
            } else {
                for (Crianca crianca : encontradas) {
                    sb.append(formatarCrianca(crianca)).append("\n");
                    sb.append("-".repeat(80)).append("\n");
                }
                sb.append("Busca por nome: ").append(encontradas.size()).append(" criança(s) encontrada(s)\n");
            }
        }
        
        listaArea.setText(sb.toString());
    }
    
    private void abrirFormularioCadastro() {
        formularioDialog = new Dialog(this, "Cadastrar Nova Criança", true);
        formularioDialog.setLayout(new BorderLayout());
        formularioDialog.setSize(400, 350);
        
        // Painel do formulário
        Panel formPanel = new Panel(new GridLayout(7, 2, 5, 5));
        
        nomeField = new TextField(20);
        idadeField = new TextField(20);
        descricaoField = new TextField(20);
        nascimentoField = new TextField(20);
        chegadaField = new TextField(20);
        orfanatoField = new TextField(20);
        
        formPanel.add(new Label("Nome:"));
        formPanel.add(nomeField);
        formPanel.add(new Label("Idade:"));
        formPanel.add(idadeField);
        formPanel.add(new Label("Descrição:"));
        formPanel.add(descricaoField);
        formPanel.add(new Label("Nascimento (dd/MM/yyyy):"));
        formPanel.add(nascimentoField);
        formPanel.add(new Label("Chegada (dd/MM/yyyy):"));
        formPanel.add(chegadaField);
        formPanel.add(new Label("ID Orfanato:"));
        formPanel.add(orfanatoField);
        
        // Painel de botões
        Panel buttonPanel = new Panel(new FlowLayout());
        salvarBtn = new Button("Salvar");
        cancelarBtn = new Button("Cancelar");
        
        salvarBtn.addActionListener(this);
        cancelarBtn.addActionListener(this);
        
        buttonPanel.add(salvarBtn);
        buttonPanel.add(cancelarBtn);
        
        formularioDialog.add(formPanel, BorderLayout.CENTER);
        formularioDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        formularioDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                formularioDialog.dispose();
            }
        });
        
        formularioDialog.setLocationRelativeTo(this);
        formularioDialog.setVisible(true);
    }
    
    private boolean salvarCrianca() {
        try {
            String nome = nomeField.getText().trim();
            if (nome.isEmpty()) {
                mostrarMensagem("Nome é obrigatório!");
                return false;
            }
            
            int idade = Integer.parseInt(idadeField.getText().trim());
            if (idade < 0 || idade > 18) {
                mostrarMensagem("Idade deve ser entre 0 e 18 anos!");
                return false;
            }
            
            String descricao = descricaoField.getText().trim();
            LocalDate nascimento = LocalDate.parse(nascimentoField.getText().trim(), dateFormatter);
            LocalDate chegada = LocalDate.parse(chegadaField.getText().trim(), dateFormatter);
            int orfanato = Integer.parseInt(orfanatoField.getText().trim());
            
            if (nascimento.isAfter(LocalDate.now())) {
                mostrarMensagem("Data de nascimento não pode ser futura!");
                return false;
            }
            
            if (chegada.isBefore(nascimento)) {
                mostrarMensagem("Data de chegada não pode ser anterior ao nascimento!");
                return false;
            }
            
            controller.adicionarCrianca(nome, idade, descricao, nascimento, chegada, orfanato);
            mostrarMensagem("Criança cadastrada com sucesso!");
            return true;
            
        } catch (NumberFormatException e) {
            mostrarMensagem("Idade e ID do Orfanato devem ser números válidos!");
            return false;
        } catch (DateTimeParseException e) {
            mostrarMensagem("Formato de data inválido! Use dd/MM/yyyy");
            return false;
        } catch (Exception e) {
            mostrarMensagem("Erro ao salvar: " + e.getMessage());
            return false;
        }
    }
    
    private void mostrarMensagem(String mensagem) {
        Dialog msgDialog = new Dialog(this, "Mensagem", true);
        msgDialog.setLayout(new BorderLayout());
        msgDialog.setSize(300, 150);
        
        Label msgLabel = new Label(mensagem, Label.CENTER);
        Button okBtn = new Button("OK");
        
        okBtn.addActionListener(e -> msgDialog.dispose());
        
        Panel buttonPanel = new Panel(new FlowLayout());
        buttonPanel.add(okBtn);
        
        msgDialog.add(msgLabel, BorderLayout.CENTER);
        msgDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        msgDialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                msgDialog.dispose();
            }
        });
        
        msgDialog.setLocationRelativeTo(this);
        msgDialog.setVisible(true);
    }
}