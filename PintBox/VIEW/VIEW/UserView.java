package View;

import Model.User;
import java.util.List;
import java.util.Scanner;

public class UserView {

    private Scanner scanner;

    public UserView() {
        scanner = new Scanner(System.in);
    }

    // Mostra todos os usuários
    public void mostrarUsuarios(List<User> usuarios) {
        System.out.println("\n=== Lista de Usuários ===");
        if (usuarios.isEmpty()) {
            System.out.println("Nenhum usuário cadastrado.");
        } else {
            for (User u : usuarios) {
                System.out.println(u);
            }
        }
    }

    // Mostra um único usuário
    public void mostrarUsuario(User u) {
        System.out.println("\n=== Detalhes do Usuário ===");
        System.out.println(u);
    }

    // Lê os dados de um novo usuário
    public User lerNovoUsuario() {
        System.out.println("\n=== Cadastro de Novo Usuário ===");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        return new User(nome, email);
    }

    // Mostra menu simples
    public int mostrarMenu() {
        System.out.println("\n=== MENU ===");
        System.out.println("1. Listar usuários");
        System.out.println("2. Adicionar usuário");
        System.out.println("3. Sair");
        System.out.print("Escolha uma opção: ");
        return scanner.nextInt();
    }

    // Limpa o buffer (evita problemas ao ler texto após números)
    public void limparBuffer() {
        scanner.nextLine();
    }
}
