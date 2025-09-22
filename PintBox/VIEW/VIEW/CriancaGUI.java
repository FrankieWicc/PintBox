package VIEW;

import MODEL.Crianca;
import CONTROLLER.CriancaController;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CriancaGUI extends JFrame {
    private CriancaController controller;
    private DefaultTableModel tableModel;
    private JTable table;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    // Componentes do formulário
    private JTextField nomeField;
    private JTextField idadeField;
    private JTextField descricaoField;
    private JTextField nascimentoField;
    private JTextField chegadaField;
    private JTextField orfanatoField;
    private JTextField buscaField;
    
    public CriancaGUI(CriancaController controller) {
        this.controller = controller;
        initializeGUI();
        atualizarTabela();
    }
    
    private void initializeGUI() {
        setTitle("Sistema de Gerenciamento de Crianças - Orfanato v2.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Configurar aparência
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            // Usar aparência padrão se falhar
        }
        
        // Criar componentes principais
        criarBarraMenu();
        criarPainelPrincipal();
        criarPainelInferior();
        
        // Configurar janela
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        
        // Ícone da aplicação
        try {
            setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        } catch (Exception e) {
            // Ignorar se não houver ícone
        }
    }
    
    private void criarBarraMenu() {
        JMenuBar menuBar = new JMenuBar();
        
        // Menu Arquivo
        JMenu arquivoMenu = new JMenu("Arquivo");
        JMenuItem novoItem = new JMenuItem("Nova Criança");
        JMenuItem sairItem = new JMenuItem("Sair");
        
        novoItem.addActionListener(e -> abrirFormularioCadastro());
        sairItem.addActionListener(e -> System.exit(0));
        
        arquivoMenu.add(novoItem);
        arquivoMenu.addSeparator();
        arquivoMenu.add(sairItem);
        
        // Menu Editar
        JMenu editarMenu = new JMenu("Editar");
        JMenuItem editarItem = new JMenuItem("Editar Selecionada");
        JMenuItem removerItem = new JMenuItem("Remover Selecionada");
        
        editarItem.addActionListener(e -> editarCriancaSelecionada());
        removerItem.addActionListener(e -> removerCriancaSelecionada());
        
        editarMenu.add(editarItem);
        editarMenu.add(removerItem);
        
        // Menu Ajuda
        JMenu ajudaMenu = new JMenu("Ajuda");
        JMenuItem sobreItem = new JMenuItem("Sobre");
        sobreItem.addActionListener(e -> mostrarSobre());
        ajudaMenu.add(sobreItem);
        
        menuBar.add(arquivoMenu);
        menuBar.add(editarMenu);
        menuBar.add(ajudaMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void criarPainelPrincipal() {
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        
        // Título
        JLabel titulo = new JLabel("Sistema de Gerenciamento de Crianças", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titulo.setForeground(new Color(0, 100, 0));
        
        // Painel de busca
        JPanel painelBusca = criarPainelBusca();
        
        // Tabela
        criarTabela();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new TitledBorder("Lista de Crianças Cadastradas"));
        
        painelPrincipal.add(titulo, BorderLayout.NORTH);
        painelPrincipal.add(painelBusca, BorderLayout.CENTER);
        painelPrincipal.add(scrollPane, BorderLayout.SOUTH);
        
        add(painelPrincipal, BorderLayout.CENTER);
    }
    
    private JPanel criarPainelBusca() {
        JPanel painel = new JPanel(new FlowLayout());
        painel.setBorder(new TitledBorder("Buscar Criança"));
        
        buscaField = new JTextField(20);
        JButton buscarNomeBtn = new JButton("Buscar por Nome");
        JButton buscarIdBtn = new JButton("Buscar por ID");
        JButton mostrarTodosBtn = new JButton("Mostrar Todas");
        
        buscarNomeBtn.addActionListener(e -> buscarPorNome());
        buscarIdBtn.addActionListener(e -> buscarPorId());
        mostrarTodosBtn.addActionListener(e -> atualizarTabela());
        
        painel.add(new JLabel("Busca:"));
        painel.add(buscaField);
        painel.add(buscarNomeBtn);
        painel.add(buscarIdBtn);
        painel.add(mostrarTodosBtn);
        
        return painel;
    }
    
    private void criarTabela() {
        String[] colunas = {"ID", "Nome", "Idade", "Nascimento", "Chegada", "Orfanato", "Descrição"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela somente leitura
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        
        // Configurar larguras das colunas
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Nome
        table.getColumnModel().getColumn(2).setPreferredWidth(60);  // Idade
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Nascimento
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Chegada
        table.getColumnModel().getColumn(5).setPreferredWidth(80);  // Orfanato
        table.getColumnModel().getColumn(6).setPreferredWidth(200); // Descrição
        
        // Adicionar listener para duplo clique
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    editarCriancaSelecionada();
                }
            }
        });
    }
    
    private void criarPainelInferior() {
        JPanel painel = new JPanel(new FlowLayout());
        
        JButton novoBtn = new JButton("Nova Criança");
        JButton editarBtn = new JButton("Editar");
        JButton removerBtn = new JButton("Remover");
        JButton sairBtn = new JButton("Sair");
        
        // Configurar botões
        novoBtn.setBackground(new Color(0, 150, 0));
        novoBtn.setForeground(Color.WHITE);
        editarBtn.setBackground(new Color(0, 100, 200));
        editarBtn.setForeground(Color.WHITE);
        removerBtn.setBackground(new Color(200, 50, 50));
        removerBtn.setForeground(Color.WHITE);
        
        novoBtn.addActionListener(e -> abrirFormularioCadastro());
        editarBtn.addActionListener(e -> editarCriancaSelecionada());
        removerBtn.addActionListener(e -> removerCriancaSelecionada());
        sairBtn.addActionListener(e -> System.exit(0));
        
        painel.add(novoBtn);
        painel.add(editarBtn);
        painel.add(removerBtn);
        painel.add(Box.createHorizontalStrut(50));
        painel.add(sairBtn);
        
        add(painel, BorderLayout.SOUTH);
    }
    
    private void atualizarTabela() {
        tableModel.setRowCount(0);
        List<Crianca> criancas = controller.getCriancas();
        
        for (Crianca crianca : criancas) {
            Object[] row = {
                crianca.getId(),
                crianca.getNome(),
                crianca.getIdade() + " anos",
                crianca.getDataNascimento().format(dateFormatter),
                crianca.getDataChegada().format(dateFormatter),
                crianca.getIdOrfanato(),
                crianca.getDescricao()
            };
            tableModel.addRow(row);
        }
    }
    
    private void abrirFormularioCadastro() {
        JDialog dialog = new JDialog(this, "Cadastrar Nova Criança", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = criarFormulario(true);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton salvarBtn = new JButton("Salvar");
        JButton cancelarBtn = new JButton("Cancelar");
        
        salvarBtn.addActionListener(e -> {
            if (salvarCrianca()) {
                dialog.dispose();
                atualizarTabela();
            }
        });
        
        cancelarBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(salvarBtn);
        buttonPanel.add(cancelarBtn);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private JPanel criarFormulario(boolean isNovo) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Campos do formulário
        nomeField = new JTextField(20);
        idadeField = new JTextField(20);
        descricaoField = new JTextField(20);
        nascimentoField = new JTextField(20);
        chegadaField = new JTextField(20);
        orfanatoField = new JTextField(20);
        
        // Labels e campos
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        panel.add(nomeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Idade:"), gbc);
        gbc.gridx = 1;
        panel.add(idadeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        panel.add(descricaoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Nascimento (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        panel.add(nascimentoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Chegada (dd/MM/yyyy):"), gbc);
        gbc.gridx = 1;
        panel.add(chegadaField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("ID Orfanato:"), gbc);
        gbc.gridx = 1;
        panel.add(orfanatoField, gbc);
        
        return panel;
    }
    
    private boolean salvarCrianca() {
        try {
            String nome = nomeField.getText().trim();
            if (nome.isEmpty()) {
                mostrarErro("Nome é obrigatório!");
                return false;
            }
            
            int idade = Integer.parseInt(idadeField.getText().trim());
            if (idade < 0 || idade > 18) {
                mostrarErro("Idade deve ser entre 0 e 18 anos!");
                return false;
            }
            
            String descricao = descricaoField.getText().trim();
            LocalDate nascimento = LocalDate.parse(nascimentoField.getText().trim(), dateFormatter);
            LocalDate chegada = LocalDate.parse(chegadaField.getText().trim(), dateFormatter);
            int orfanato = Integer.parseInt(orfanatoField.getText().trim());
            
            if (nascimento.isAfter(LocalDate.now())) {
                mostrarErro("Data de nascimento não pode ser futura!");
                return false;
            }
            
            if (chegada.isBefore(nascimento)) {
                mostrarErro("Data de chegada não pode ser anterior ao nascimento!");
                return false;
            }
            
            // Adicionar através do controller
            controller.adicionarCrianca(nome, idade, descricao, nascimento, chegada, orfanato);
            
            mostrarSucesso("Criança cadastrada com sucesso!");
            limparFormulario();
            return true;
            
        } catch (NumberFormatException e) {
            mostrarErro("Idade e ID do Orfanato devem ser números válidos!");
            return false;
        } catch (DateTimeParseException e) {
            mostrarErro("Formato de data inválido! Use dd/MM/yyyy");
            return false;
        } catch (Exception e) {
            mostrarErro("Erro ao salvar: " + e.getMessage());
            return false;
        }
    }
    
    private void limparFormulario() {
        nomeField.setText("");
        idadeField.setText("");
        descricaoField.setText("");
        nascimentoField.setText("");
        chegadaField.setText("");
        orfanatoField.setText("");
    }
    
    private void editarCriancaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            mostrarInfo("Selecione uma criança para editar!");
            return;
        }
        
        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Crianca crianca = controller.buscarPorId(id);
        
        if (crianca == null) {
            mostrarErro("Criança não encontrada!");
            return;
        }
        
        // Abrir formulário de edição
        abrirFormularioEdicao(crianca);
    }
    
    private void abrirFormularioEdicao(Crianca crianca) {
        JDialog dialog = new JDialog(this, "Editar Criança", true);
        dialog.setLayout(new BorderLayout());
        
        JPanel formPanel = criarFormulario(false);
        
        // Preencher campos com dados atuais
        nomeField.setText(crianca.getNome());
        idadeField.setText(String.valueOf(crianca.getIdade()));
        descricaoField.setText(crianca.getDescricao());
        nascimentoField.setText(crianca.getDataNascimento().format(dateFormatter));
        chegadaField.setText(crianca.getDataChegada().format(dateFormatter));
        orfanatoField.setText(String.valueOf(crianca.getIdOrfanato()));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton salvarBtn = new JButton("Salvar Alterações");
        JButton cancelarBtn = new JButton("Cancelar");
        
        salvarBtn.addActionListener(e -> {
            if (atualizarCrianca(crianca)) {
                dialog.dispose();
                atualizarTabela();
            }
        });
        
        cancelarBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(salvarBtn);
        buttonPanel.add(cancelarBtn);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private boolean atualizarCrianca(Crianca crianca) {
        try {
            String nome = nomeField.getText().trim();
            if (nome.isEmpty()) {
                mostrarErro("Nome é obrigatório!");
                return false;
            }
            
            int idade = Integer.parseInt(idadeField.getText().trim());
            if (idade < 0 || idade > 18) {
                mostrarErro("Idade deve ser entre 0 e 18 anos!");
                return false;
            }
            
            String descricao = descricaoField.getText().trim();
            
            crianca.setNome(nome);
            crianca.setIdade(idade);
            crianca.setDescricao(descricao);
            
            mostrarSucesso("Dados atualizados com sucesso!");
            return true;
            
        } catch (NumberFormatException e) {
            mostrarErro("Idade deve ser um número válido!");
            return false;
        } catch (Exception e) {
            mostrarErro("Erro ao atualizar: " + e.getMessage());
            return false;
        }
    }
    
    private void removerCriancaSelecionada() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            mostrarInfo("Selecione uma criança para remover!");
            return;
        }
        
        int id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String nome = (String) tableModel.getValueAt(selectedRow, 1);
        
        int resposta = JOptionPane.showConfirmDialog(
            this,
            "Tem certeza que deseja remover " + nome + "?\nEsta ação não pode ser desfeita!",
            "Confirmar Remoção",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (resposta == JOptionPane.YES_OPTION) {
            if (controller.removerCrianca(id)) {
                mostrarSucesso("Criança removida com sucesso!");
                atualizarTabela();
            } else {
                mostrarErro("Erro ao remover criança!");
            }
        }
    }
    
    private void buscarPorNome() {
        String nome = buscaField.getText().trim();
        if (nome.isEmpty()) {
            mostrarInfo("Digite um nome para buscar!");
            return;
        }
        
        List<Crianca> encontradas = controller.buscarPorNome(nome);
        
        tableModel.setRowCount(0);
        for (Crianca crianca : encontradas) {
            Object[] row = {
                crianca.getId(),
                crianca.getNome(),
                crianca.getIdade() + " anos",
                crianca.getDataNascimento().format(dateFormatter),
                crianca.getDataChegada().format(dateFormatter),
                crianca.getIdOrfanato(),
                crianca.getDescricao()
            };
            tableModel.addRow(row);
        }
        
        if (encontradas.isEmpty()) {
            mostrarInfo("Nenhuma criança encontrada com o nome: " + nome);
        } else {
            mostrarSucesso(encontradas.size() + " criança(s) encontrada(s)!");
        }
    }
    
    private void buscarPorId() {
        try {
            int id = Integer.parseInt(buscaField.getText().trim());
            Crianca crianca = controller.buscarPorId(id);
            
            tableModel.setRowCount(0);
            if (crianca != null) {
                Object[] row = {
                    crianca.getId(),
                    crianca.getNome(),
                    crianca.getIdade() + " anos",
                    crianca.getDataNascimento().format(dateFormatter),
                    crianca.getDataChegada().format(dateFormatter),
                    crianca.getIdOrfanato(),
                    crianca.getDescricao()
                };
                tableModel.addRow(row);
                mostrarSucesso("Criança encontrada!");
            } else {
                mostrarInfo("Nenhuma criança encontrada com ID: " + id);
            }
        } catch (NumberFormatException e) {
            mostrarErro("ID deve ser um número válido!");
        }
    }
    
    private void mostrarSobre() {
        JOptionPane.showMessageDialog(
            this,
            "Sistema de Gerenciamento de Crianças - Orfanato\n" +
            "Versão 2.0\n\n" +
            "Desenvolvido em Java com arquitetura MVC\n" +
            "Interface gráfica com Swing\n\n" +
            "Funcionalidades:\n" +
            "• Cadastro de crianças\n" +
            "• Busca por nome ou ID\n" +
            "• Edição de dados\n" +
            "• Remoção segura\n" +
            "• Validação de dados\n\n" +
            "© 2025 Sistema Orfanato",
            "Sobre o Sistema",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void mostrarSucesso(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarInfo(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }
}