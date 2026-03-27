package src;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Scanner;
import src.data.DALclube;
import src.model.cadastros;

public class clubeEsportivo {

    public static void main(String[] args) {
        Connection connection = DALclube.conectar();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Bem vindo ao sistema de cadastro de sócios!");
            System.out.println("Escolha a opção que deseja utilizar: ");
            System.out.println("1. Mostrar sócios cadastrados");
            System.out.println("2. Inserir cadastro");
            System.out.println("3. Atualizar cadastro");
            System.out.println("4. Deletar cadastro");
            System.out.println("5. Inserir feedback");
            System.out.println("6. Mostrar feedbacks");
            System.out.println("0. Sair do programa");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 1) {
                DALclube.getCadastros(connection);
            } else if (opcao == 2) {
                inserirCadastros(connection, scanner);
            } else if (opcao == 3) {
                atualizarCadastro(connection, scanner);
            } else if (opcao == 4) {
                deletarCadastro(connection, scanner);
            } else if (opcao == 5) {
                inserirFeedback(connection, scanner);
            } else if (opcao == 6) {
                DALclube.listarFeedbacks(connection);
            } else if (opcao == 0) {
                System.out.println("\nFinalizando o programa...");
                break;
            } else {
                System.out.println("\nOpção inválida!");
            }
        }

        DALclube.desconectar(connection);
    }

    public static void inserirCadastros(Connection connection, Scanner scanner) {
        System.out.println("Inserção de Sócios:");

        System.out.print("Digite o nome: ");
        String nome = scanner.nextLine();

        System.out.print("Digite a data de nascimento (ddmmaaaa): ");
        int nascimento = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Digite o gênero: ");
        String genero = scanner.nextLine();

        System.out.print("Digite o CPF (somente números): ");
        long cpf = scanner.nextLong();
        scanner.nextLine();

        // -------- PLANOS --------
        System.out.println("\nPlanos disponíveis:");
        DALclube.mostrarPlanos(connection);
        int idPlano;
        while (true) {
            System.out.print("Digite o ID do plano: ");
            idPlano = scanner.nextInt();
            scanner.nextLine();

            if (idExiste(connection, "planos", "idPlano", idPlano)) {
                break;
            } else {
                System.out.println("ID do plano inválido! Digite um número entre as opções mostradas acima.\n");
            }
        }

        // -------- FORMAS DE PAGAMENTO --------
        System.out.println("\nFormas de pagamento disponíveis:");
        DALclube.mostrarFormasPagamento(connection);
        int idPag;
        while (true) {
            System.out.print("Digite o ID da forma de pagamento: ");
            idPag = scanner.nextInt();
            scanner.nextLine();

            if (idExiste(connection, "formaPagamento", "idPag", idPag)) {
                break;
            } else {
                System.out.println("ID da forma de pagamento inválido! Digite um número entre as opções mostradas acima.\n");
            }
        }

        cadastros novoCadastro = new cadastros(nome, nascimento, genero, cpf, idPlano, idPag);
        DALclube.inserirCadastro(connection, novoCadastro);
    }

    public static void atualizarCadastro(Connection connection, Scanner scanner) {
        DALclube.getCadastros(connection);
        System.out.println("Atualização de Cadastros");
        System.out.print("Digite a matrícula que deseja alterar: ");
        int matricula = scanner.nextInt();
        scanner.nextLine();

        boolean continuarAtualizando = true;
        while (continuarAtualizando) {
            System.out.println("\nDigite o nome da coluna que deseja atualizar (nome, nascimento, genero, cpf, plano, forma de pagamento) ou 'sair': ");
            String coluna = scanner.nextLine().toLowerCase();

            if (coluna.equals("sair")) {
                continuarAtualizando = false;
            } else {
                System.out.print("Digite o novo valor para '" + coluna + "': ");
                String novoValor = scanner.nextLine();
                DALclube.atualizarCadastro(connection, matricula, coluna, novoValor);
            }

            System.out.print("Deseja atualizar outra coluna? (sim/não): ");
            String resposta = scanner.nextLine().toLowerCase();
            if (resposta.equals("não") || resposta.equals("nao")) {
                continuarAtualizando = false;
            }
        }
    }

    public static void deletarCadastro(Connection connection, Scanner scanner) {
        DALclube.getCadastros(connection);
        System.out.print("Digite a matrícula que deseja excluir: ");
        int matricula = scanner.nextInt();
        scanner.nextLine();

        DALclube.deletarCadastro(connection, matricula);
    }

    public static void inserirFeedback(Connection connection, Scanner scanner) {
        System.out.println("Inserção de Feedback:");

        System.out.print("Confira as matrículas e, em seguida, digite a matrícula do sócio que está enviando o feedback: ");
        System.out.println();
        DALclube.getCadastros(connection);
        int idReclamante = scanner.nextInt();
        scanner.nextLine();

        String tipo;
        while (true) {
            System.out.print("Digite o tipo de feedback (positivo ou negativo): ");
            tipo = scanner.nextLine().trim().toLowerCase();

            if (tipo.equals("positivo") || tipo.equals("negativo")) {
                break;
            } else {
                System.out.println("Tipo de feedback digitado incorretamente, escolha novamente (positivo ou negativo).");
            }
        }

        System.out.print("Digite o texto do feedback: ");
        String texto = scanner.nextLine();

        DALclube.inserirFeedback(connection, tipo, texto, idReclamante);
    }

    // -------- Função auxiliar simples de validação --------
    private static boolean idExiste(Connection connection, String tabela, String coluna, int id) {
        try {
            String sql = "SELECT 1 FROM " + tabela + " WHERE " + coluna + " = " + id;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            boolean existe = rs.next();
            rs.close();
            stmt.close();

            return existe;
        } catch (SQLException e) {
            System.out.println("Erro ao verificar ID: " + e.getMessage());
            return false;
        }
    }
}
