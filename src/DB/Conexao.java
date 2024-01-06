package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Conexao {
	public String loginAutenticado; // Variável pública para armazenar o login autenticado
	private String databaseURL;
	private String user;
	private String password;
	private Connection con;

	public Conexao() throws ClassNotFoundException, SQLException {
		databaseURL = "jdbc:postgresql://localhost:5432/users";
		user = "postgres";
		password = "postgres";
		Class.forName("org.postgresql.Driver");
		con = DriverManager.getConnection(databaseURL, user, password);

		System.out.println("Conexão realizada com sucesso.");
	}

	public boolean autenticarUsuario(String usuario, String senha) throws SQLException {
		String sql = "SELECT * FROM aluno WHERE login = ? AND password = ? " + "UNION "
				+ "SELECT * FROM professor WHERE login = ? AND password = ?";

		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, usuario);
			stmt.setString(2, senha);
			stmt.setString(3, usuario);
			stmt.setString(4, senha);

			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				loginAutenticado = usuario; // Armazena o login autenticado na variável pública
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean verificarLoginExistente(String login) throws SQLException {
		String sqlProfessor = "SELECT COUNT(*) AS count FROM professor WHERE login = ?";
		String sqlAluno = "SELECT COUNT(*) AS count FROM aluno WHERE login = ?";

		try (PreparedStatement stmtProfessor = con.prepareStatement(sqlProfessor);
				PreparedStatement stmtAluno = con.prepareStatement(sqlAluno)) {

			stmtProfessor.setString(1, login);
			stmtAluno.setString(1, login);

			ResultSet resultSetProfessor = stmtProfessor.executeQuery();
			ResultSet resultSetAluno = stmtAluno.executeQuery();

			int countProfessor = resultSetProfessor.next() ? resultSetProfessor.getInt("count") : 0;
			int countAluno = resultSetAluno.next() ? resultSetAluno.getInt("count") : 0;

			return (countProfessor > 0 || countAluno > 0);
		}
	}

	public void excluirTodosProfessoresAlunos() throws SQLException {
		String sqlDeleteProfessores = "DELETE FROM professor";
		String sqlDeleteAlunos = "DELETE FROM aluno";

		try (Statement stmt = con.createStatement()) {
			stmt.executeUpdate(sqlDeleteProfessores);
			stmt.executeUpdate(sqlDeleteAlunos);
		}
	}

	public String obterNomePorLogin(String login) throws SQLException {
		String sql = "SELECT name FROM aluno WHERE login = ? " + "UNION "
				+ "SELECT name FROM professor WHERE login = ?";

		System.out.println(login);
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, login);
			stmt.setString(2, login);

			ResultSet resultSet = stmt.executeQuery();

			// Adicione esses logs para debug
			System.out.println("Executando SQL: " + sql);
			System.out.println("Parâmetro de login: " + login);

			if (resultSet.next()) {
				String nome = resultSet.getString("name");
				System.out.println("Nome encontrado: " + nome);
				return nome;
			} else {
				System.out.println("Nenhum nome encontrado para o login: " + login);
				return null;
			}
		}
	}

	public void registrarAluno(String login, String senha, String nome) throws SQLException {
		String sqlInsertAluno = "INSERT INTO aluno (login, password, name) VALUES (?, ?, ?)";
		try (PreparedStatement stmt = con.prepareStatement(sqlInsertAluno)) {
			stmt.setString(1, login);
			stmt.setString(2, senha);
			stmt.setString(3, nome);
			stmt.executeUpdate();
		}
	}

	public void registrarProfessor(String login, String senha, String nome) throws SQLException {
		String sqlInsertProfessor = "INSERT INTO professor (login, password, name) VALUES (?, ?, ?)";
		try (PreparedStatement stmt = con.prepareStatement(sqlInsertProfessor)) {
			stmt.setString(1, login);
			stmt.setString(2, senha);
			stmt.setString(3, nome);
			stmt.executeUpdate();
		}
	}

	public boolean verificarTipoUsuario(String login) throws SQLException {
		String sqlProfessor = "SELECT COUNT(*) AS count FROM professor WHERE login = ?";

		try (PreparedStatement stmtProfessor = con.prepareStatement(sqlProfessor)) {
			stmtProfessor.setString(1, login);

			ResultSet resultSetProfessor = stmtProfessor.executeQuery();
			return resultSetProfessor.next() && resultSetProfessor.getInt("count") > 0;
		}
	}

	public List<String> obterMateriasPorProfessor(String login) throws SQLException {
		String sql = "SELECT m.nome_materia FROM materia m "
				+ "JOIN professor_materia pm ON m.id_materia = pm.id_materia "
				+ "JOIN professor p ON pm.id_professor = p.id_professor " + "WHERE p.login = ?";

		List<String> materias = new ArrayList<>();

		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, login);

			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				materias.add(resultSet.getString("nome_materia"));
			}
		}

		return materias;
	}

	public List<String> obterMateriasPorAluno(String login) throws SQLException {
		String sql = "SELECT m.nome_materia FROM materia m " + "JOIN aluno_materia am ON m.id_materia = am.id_materia "
				+ "JOIN aluno a ON am.id_aluno = a.id_aluno " + "WHERE a.login = ?";

		List<String> materias = new ArrayList<>();

		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, login);

			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				materias.add(resultSet.getString("nome_materia"));
			}
		}

		return materias;
	}

}
