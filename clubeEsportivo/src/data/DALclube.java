package src.data;

import java.sql.*;
import java.util.ArrayList;
import src.model.cadastros;

public class DALclube {

    private static final String path = "jdbc:sqlite:clube.db";

    public static Connection conectar() {
        try {
            return DriverManager.getConnection(path);
        } catch (SQLException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
            return null;
        }
    }

    public static void desconectar(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public static ArrayList<cadastros> getCadastros(Connection connection) {
        ArrayList<cadastros> sociosCadastrados = new ArrayList<>();
        try {
            String sql = "SELECT c.matricula, c.nome, c.nascimento, c.genero, c.cpf, " +
                         "p.nomePlano, f.formaPag " +
                         "FROM cadastros c " +
                         "LEFT JOIN planos p ON c.idPlano = p.idPlano " +
                         "LEFT JOIN formaPagamento f ON c.idPag = f.idPag";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int matricula = resultSet.getInt("matricula");
                String nome = resultSet.getString("nome");
                int nascimento = resultSet.getInt("nascimento");
                String genero = resultSet.getString("genero");
                long cpf = resultSet.getLong("cpf");

                cadastros c = new cadastros(nome, nascimento, genero, cpf, 0, 0);
                c.setMatricula(matricula);

                System.out.println("Matrícula: " + matricula);
                System.out.println("Nome: " + nome);
                System.out.println("Nascimento: " + nascimento);
                System.out.println("Gênero: " + genero);
                System.out.println("CPF: " + cpf);
                System.out.println("Plano: " + resultSet.getString("nomePlano"));
                System.out.println("Pagamento: " + resultSet.getString("formaPag"));
                System.out.println(" ");

                sociosCadastrados.add(c);
            }

            resultSet.close();
            statement.close();

        } catch (SQLException erro) {
            System.out.println("Erro ao buscar cadastros: " + erro.getMessage());
        }
        return sociosCadastrados;
    }

    public static void inserirCadastro(Connection connection, cadastros cadastro) {
        try {
            String sql = "INSERT INTO cadastros (nome, nascimento, genero, cpf, idPlano, idPag) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, cadastro.getNome());
            preparedStatement.setInt(2, cadastro.getNascimento());
            preparedStatement.setString(3, cadastro.getGenero());
            preparedStatement.setLong(4, cadastro.getCpf());
            preparedStatement.setInt(5, cadastro.getIdPlano());
            preparedStatement.setInt(6, cadastro.getIdPag());

            int linhasAfetadas = preparedStatement.executeUpdate();

            if (linhasAfetadas == 1) {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");
                if (rs.next()) {
                    int matriculaGerada = rs.getInt(1);
                    cadastro.setMatricula(matriculaGerada);
                    System.out.println("\nCadastro realizado com sucesso!");
                    System.out.println("Matrícula gerada: " + matriculaGerada + "\n");
                }
                rs.close();
                stmt.close();
            } else {
                System.out.println("\nNenhuma linha do BD foi afetada!");
            }

        } catch (SQLException e) {
            System.out.println("Erro de SQL: " + e.getMessage());
        }
    }

    public static void atualizarCadastro(Connection connection, int matricula, String coluna, String novoValor) {
        try {
            String sql = "UPDATE cadastros SET " + coluna + " = ? WHERE matricula = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, novoValor);
            preparedStatement.setInt(2, matricula);

            int linhasAfetadas = preparedStatement.executeUpdate();
            if (linhasAfetadas == 1) {
                System.out.println("\nCadastro atualizado com sucesso!\n");
            } else {
                System.out.println("\nNenhuma linha do BD foi afetada!\n");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar: " + e.getMessage());
        }
    }

    public static void deletarCadastro(Connection connection, int matricula) {
        try {
            String sql = "DELETE FROM cadastros WHERE matricula = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, matricula);

            int linhasAfetadas = preparedStatement.executeUpdate();
            if (linhasAfetadas == 1) {
                System.out.println("\nCadastro excluído com sucesso!\n");
            } else {
                System.out.println("\nNenhuma linha do BD foi afetada!\n");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao deletar: " + e.getMessage());
        }
    }

    public static void mostrarPlanos(Connection connection) {
        try {
            String sql = "SELECT idPlano, nomePlano FROM planos";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("idPlano") + " | Plano: " + rs.getString("nomePlano"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Erro ao listar planos: " + e.getMessage());
        }
    }

    public static void mostrarFormasPagamento(Connection connection) {
        try {
            String sql = "SELECT idPag, formaPag FROM formaPagamento";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("idPag") + " | Pagamento: " + rs.getString("formaPag"));
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Erro ao listar formas de pagamento: " + e.getMessage());
        }
    }

    public static void listarFeedbacks(Connection connection) {
        try {
            String sql = "SELECT f.idFeed, f.tipoFeedback, f.textoFeedback, c.nome " +
                         "FROM feedbacks f " +
                         "LEFT JOIN cadastros c ON f.idReclamante = c.matricula";

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n--- Feedbacks ---");
            while (rs.next()) {
                System.out.println("ID Feedback: " + rs.getInt("idFeed"));
                System.out.println("Tipo: " + rs.getString("tipoFeedback"));
                System.out.println("Texto: " + rs.getString("textoFeedback"));
                System.out.println("Enviado por: " + rs.getString("nome"));
                System.out.println(" ");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Erro ao listar feedbacks: " + e.getMessage());
        }
    }

    public static void inserirFeedback(Connection connection, String tipo, String texto, int idReclamante) {
        try {
            String sql = "INSERT INTO feedbacks (tipoFeedback, textoFeedback, idReclamante) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, tipo);
            ps.setString(2, texto);
            ps.setInt(3, idReclamante);
            ps.executeUpdate();
            ps.close();

            System.out.println("\nFeedback inserido com sucesso!\n");
        } catch (SQLException e) {
            System.out.println("Erro ao inserir feedback: " + e.getMessage());
        }
    }
}
