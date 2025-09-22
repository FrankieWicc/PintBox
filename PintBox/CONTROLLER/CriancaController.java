package CONTROLLER;

import MODEL.Crianca;
import VIEW.CriancaView;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CriancaController {
    private List<Crianca> criancas;
    private CriancaView view;
    private int proximoId;

    public CriancaController() {
        this.criancas = new ArrayList<>();
        this.view = new CriancaView();
        this.proximoId = 1;
        inicializarDadosExemplo();
    }

    private void inicializarDadosExemplo() {
        criancas.add(new Crianca(proximoId++, "Ana Silva", 8, "Criança muito alegre e inteligente", 
                    LocalDate.of(2015, 3, 15), LocalDate.of(2023, 1, 10), 1));
        criancas.add(new Crianca(proximoId++, "João Santos", 10, "Gosta muito de esportes e matemática", 
                    LocalDate.of(2013, 7, 22), LocalDate.of(2022, 8, 5), 1));
        criancas.add(new Crianca(proximoId++, "Maria Oliveira", 6, "Muito criativa, adora desenhar", 
                    LocalDate.of(2017, 11, 3), LocalDate.of(2023, 4, 18), 2));
    }

    public void iniciarSistema() {
        view.exibirMensagemInicializacao();
        
        while (true) {
            view.exibirMenuPrincipal();
            int opcao = view.lerOpcaoMenu();
            
            if (opcao == -1) {
                continue; // Opção inválida, repetir menu
            }

            switch (opcao) {
                case 1:
                    cadastrarCrianca();
                    break;
                case 2:
                    listarCriancas();
                    break;
                case 3:
                    buscarPorId();
                    break;
                case 4:
                    buscarPorNome();
                    break;
                case 5:
                    atualizarCrianca();
                    break;
                case 6:
                    removerCrianca();
                    break;
                case 7:
                    encerrarSistema();
                    return;
            }
            
            view.pausar();
        }
    }

    public void cadastrarCrianca() {
        view.exibirTitulo("Cadastro de Nova Criança");
        
        try {
            String nome = view.lerTexto("Nome completo");
            if (nome.isEmpty()) {
                view.exibirMensagemErro("Nome é obrigatório!");
                return;
            }
            
            int idade = view.lerInteiro("Idade");
            if (idade < 0 || idade > 18) {
                view.exibirMensagemErro("Idade deve ser entre 0 e 18 anos!");
                return;
            }
            
            String descricao = view.lerTexto("Descrição");
            LocalDate dataNascimento = view.lerData("Data de nascimento");
            LocalDate dataChegada = view.lerData("Data de chegada");
            int idOrfanato = view.lerInteiro("ID do Orfanato");

            // Validar se data de nascimento não é futura
            if (dataNascimento.isAfter(LocalDate.now())) {
                view.exibirMensagemErro("Data de nascimento não pode ser futura!");
                return;
            }

            // Validar se data de chegada não é anterior ao nascimento
            if (dataChegada.isBefore(dataNascimento)) {
                view.exibirMensagemErro("Data de chegada não pode ser anterior ao nascimento!");
                return;
            }

            Crianca novaCrianca = new Crianca(proximoId++, nome, idade, descricao, dataNascimento, dataChegada, idOrfanato);
            criancas.add(novaCrianca);
            
            view.exibirMensagemSucesso("Criança cadastrada com sucesso! ID: " + novaCrianca.getId());
            
        } catch (Exception e) {
            view.exibirMensagemErro("Erro ao cadastrar criança: " + e.getMessage());
        }
    }

    public void listarCriancas() {
        view.exibirTitulo("Lista de Todas as Crianças");
        view.exibirListaCriancas(criancas);
    }

    public void buscarPorId() {
        view.exibirTitulo("Buscar Criança por ID");
        
        int id = view.lerInteiro("ID da criança");
        Crianca crianca = encontrarPorId(id);
        
        if (crianca != null) {
            view.exibirMensagemSucesso("Criança encontrada!");
            view.exibirCrianca(crianca);
        } else {
            view.exibirMensagemErro("Criança com ID " + id + " não encontrada.");
        }
    }

    public void buscarPorNome() {
        view.exibirTitulo("Buscar Criança por Nome");
        
        String nome = view.lerTexto("Nome da criança (ou parte do nome)");
        if (nome.isEmpty()) {
            view.exibirMensagemErro("Nome é obrigatório para busca!");
            return;
        }

        List<Crianca> criancasEncontradas = new ArrayList<>();
        for (Crianca crianca : criancas) {
            if (crianca.getNome().toLowerCase().contains(nome.toLowerCase())) {
                criancasEncontradas.add(crianca);
            }
        }

        if (criancasEncontradas.isEmpty()) {
            view.exibirMensagemInfo("Nenhuma criança encontrada com o nome: \"" + nome + "\"");
        } else {
            view.exibirMensagemSucesso(criancasEncontradas.size() + " criança(s) encontrada(s)!");
            view.exibirListaCriancas(criancasEncontradas);
        }
    }

    public void atualizarCrianca() {
        view.exibirTitulo("Atualizar Dados da Criança");
        
        int id = view.lerInteiro("ID da criança a ser atualizada");
        Crianca crianca = encontrarPorId(id);
        
        if (crianca == null) {
            view.exibirMensagemErro("Criança com ID " + id + " não encontrada.");
            return;
        }

        view.exibirMensagemInfo("Dados atuais:");
        view.exibirCrianca(crianca);
        
        view.exibirMensagemInfo("\nDigite os novos valores (pressione Enter para manter o valor atual):");
        
        try {
            String novoNome = view.lerTextoOpcional("Nome", crianca.getNome());
            crianca.setNome(novoNome);
            
            int novaIdade = view.lerInteiroOpcional("Idade", crianca.getIdade());
            if (novaIdade < 0 || novaIdade > 18) {
                view.exibirMensagemErro("Idade deve ser entre 0 e 18 anos! Mantendo valor anterior.");
            } else {
                crianca.setIdade(novaIdade);
            }
            
            String novaDescricao = view.lerTextoOpcional("Descrição", crianca.getDescricao());
            crianca.setDescricao(novaDescricao);

            view.exibirMensagemSucesso("Dados atualizados com sucesso!");
            view.exibirMensagemInfo("Dados atualizados:");
            view.exibirCrianca(crianca);
            
        } catch (Exception e) {
            view.exibirMensagemErro("Erro ao atualizar dados: " + e.getMessage());
        }
    }

    public void removerCrianca() {
        view.exibirTitulo("Remover Criança do Sistema");
        
        int id = view.lerInteiro("ID da criança a ser removida");
        Crianca crianca = encontrarPorId(id);
        
        if (crianca == null) {
            view.exibirMensagemErro("Criança com ID " + id + " não encontrada.");
            return;
        }

        view.exibirMensagemInfo("Criança que será removida:");
        view.exibirCrianca(crianca);
        
        boolean confirmar = view.confirmarAcao("\n⚠️  ATENÇÃO: Esta ação não pode ser desfeita! Confirma a remoção?");

        if (confirmar) {
            criancas.remove(crianca);
            view.exibirMensagemSucesso("Criança removida com sucesso!");
            view.exibirMensagemInfo("Total de crianças no sistema: " + criancas.size());
        } else {
            view.exibirMensagemInfo("Remoção cancelada.");
        }
    }

    private Crianca encontrarPorId(int id) {
        for (Crianca crianca : criancas) {
            if (crianca.getId() == id) {
                return crianca;
            }
        }
        return null;
    }

    private void encerrarSistema() {
        view.exibirMensagemSaida();
        view.fecharScanner();
    }

    // Métodos para a GUI
    public void adicionarCrianca(String nome, int idade, String descricao, LocalDate nascimento, LocalDate chegada, int orfanato) {
        Crianca novaCrianca = new Crianca(proximoId++, nome, idade, descricao, nascimento, chegada, orfanato);
        criancas.add(novaCrianca);
    }
    
    public Crianca buscarPorId(int id) {
        return encontrarPorId(id);
    }
    
    public List<Crianca> buscarPorNome(String nome) {
        List<Crianca> encontradas = new ArrayList<>();
        for (Crianca crianca : criancas) {
            if (crianca.getNome().toLowerCase().contains(nome.toLowerCase())) {
                encontradas.add(crianca);
            }
        }
        return encontradas;
    }
    
    public boolean removerCrianca(int id) {
        Crianca crianca = encontrarPorId(id);
        if (crianca != null) {
            criancas.remove(crianca);
            return true;
        }
        return false;
    }

    // Métodos getters para acesso externo se necessário
    public List<Crianca> getCriancas() {
        return new ArrayList<>(criancas);
    }

    public int getTotalCriancas() {
        return criancas.size();
    }
}