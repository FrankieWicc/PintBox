package VIEW;

import MODEL.Crianca;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class CriancaView {
    private Scanner scanner;
    private DateTimeFormatter dateFormatter;

    public CriancaView() {
        this.scanner = new Scanner(System.in);
        this.dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public void exibirMenuPrincipal() {
        System.out.println("\n" + "=".repeat(55));
        System.out.println("       SISTEMA DE GERENCIAMENTO DE CRIANÇAS");
        System.out.println("=".repeat(55));
        System.out.println("1. Cadastrar nova criança");
        System.out.println("2. Listar todas as crianças");
        System.out.println("3. Buscar criança por ID");
        System.out.println("4. Buscar criança por nome");
        System.out.println("5. Atualizar dados de criança");
        System.out.println("6. Remover criança");
        System.out.println("7. Sair");
        System.out.println("=".repeat(55));
        System.out.print("Escolha uma opção (1-7): ");
    }

    public int lerOpcaoMenu() {
        try {
            int opcao = Integer.parseInt(scanner.nextLine().trim());
            if (opcao >= 1 && opcao <= 7) {
                return opcao;
            } else {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            exibirMensagemErro("Opção inválida! Digite um número entre 1 e 7.");
            return -1;
        }
    }

    public void exibirTitulo(String titulo) {
        System.out.println("\n" + "-".repeat(50));
        System.out.println("   " + titulo.toUpperCase());
        System.out.println("-".repeat(50));
    }

    public void exibirMensagemSucesso(String mensagem) {
        System.out.println("✅ " + mensagem);
    }

    public void exibirMensagemErro(String mensagem) {
        System.out.println("❌ " + mensagem);
    }

    public void exibirMensagemInfo(String mensagem) {
        System.out.println("ℹ️  " + mensagem);
    }

    public String lerTexto(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine().trim();
    }

    public int lerInteiro(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + ": ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new NumberFormatException();
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                exibirMensagemErro("Por favor, digite um número válido.");
            }
        }
    }

    public LocalDate lerData(String prompt) {
        while (true) {
            try {
                System.out.print(prompt + " (dd/MM/aaaa): ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new DateTimeParseException("Data vazia", input, 0);
                }
                return LocalDate.parse(input, dateFormatter);
            } catch (DateTimeParseException e) {
                exibirMensagemErro("Data inválida! Use o formato dd/MM/aaaa (ex: 15/03/2010)");
            }
        }
    }

    public String lerTextoOpcional(String prompt, String valorAtual) {
        System.out.print(prompt + " (atual: " + valorAtual + ") [Enter para manter]: ");
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? valorAtual : input;
    }

    public Integer lerInteiroOpcional(String prompt, int valorAtual) {
        while (true) {
            try {
                System.out.print(prompt + " (atual: " + valorAtual + ") [Enter para manter]: ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return valorAtual;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                exibirMensagemErro("Por favor, digite um número válido ou pressione Enter para manter o valor atual.");
            }
        }
    }

    public boolean confirmarAcao(String mensagem) {
        while (true) {
            System.out.print(mensagem + " (s/n): ");
            String resposta = scanner.nextLine().trim().toLowerCase();
            if (resposta.equals("s") || resposta.equals("sim")) {
                return true;
            } else if (resposta.equals("n") || resposta.equals("nao") || resposta.equals("não")) {
                return false;
            } else {
                exibirMensagemErro("Resposta inválida! Digite 's' para sim ou 'n' para não.");
            }
        }
    }

    public void exibirCrianca(Crianca crianca) {
        System.out.println("\n┌" + "─".repeat(60) + "┐");
        System.out.printf("│ %-58s │%n", "ID: " + crianca.getId() + " | Nome: " + crianca.getNome());
        System.out.printf("│ %-58s │%n", "Idade: " + crianca.getIdade() + " anos");
        System.out.printf("│ %-58s │%n", "Nascimento: " + crianca.getDataNascimento().format(dateFormatter));
        System.out.printf("│ %-58s │%n", "Chegada: " + crianca.getDataChegada().format(dateFormatter));
        System.out.printf("│ %-58s │%n", "Orfanato ID: " + crianca.getIdOrfanato());
        System.out.println("│ " + "─".repeat(58) + " │");
        
        // Quebrar descrição em linhas se necessário
        String descricao = crianca.getDescricao();
        if (descricao.length() <= 56) {
            System.out.printf("│ %-58s │%n", "Descrição: " + descricao);
        } else {
            System.out.printf("│ %-58s │%n", "Descrição:");
            for (int i = 0; i < descricao.length(); i += 56) {
                String parte = descricao.substring(i, Math.min(i + 56, descricao.length()));
                System.out.printf("│ %-58s │%n", "  " + parte);
            }
        }
        System.out.println("└" + "─".repeat(60) + "┘");
    }

    public void exibirListaCriancas(List<Crianca> criancas) {
        if (criancas.isEmpty()) {
            exibirMensagemInfo("Nenhuma criança cadastrada no sistema.");
            return;
        }

        exibirMensagemInfo("Total de crianças encontradas: " + criancas.size());
        for (Crianca crianca : criancas) {
            exibirCrianca(crianca);
        }
    }

    public void exibirMensagemSaida() {
        System.out.println("\n" + "=".repeat(55));
        System.out.println("         SISTEMA ENCERRADO COM SUCESSO");
        System.out.println("       Obrigado por usar nosso sistema!");
        System.out.println("=".repeat(55));
    }

    public void exibirMensagemInicializacao() {
        System.out.println("=".repeat(55));
        System.out.println("    INICIALIZANDO SISTEMA DE GERENCIAMENTO");
        System.out.println("              DE CRIANÇAS - ORFANATO");
        System.out.println("                 VERSÃO 2.0");
        System.out.println("=".repeat(55));
        System.out.println("Sistema carregado com sucesso! ✅");
    }

    public void pausar() {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    public void fecharScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}