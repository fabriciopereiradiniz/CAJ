package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Conexao {
	public String loginAutenticado; // Variável pública para armazenar o login autenticado
	public int idAluno; // Variável pública para armazenar o login autenticado
	private String databaseURL;
	private String user;
	private String password;
	private Connection con;
	private Map<Integer, List<String>> notificacoes;

	public Conexao() throws ClassNotFoundException, SQLException {
		databaseURL = "jdbc:postgresql://localhost:5432/users"; //mudar a porta 
		user = "postgres";
		password = "postgres";
		Class.forName("org.postgresql.Driver");
		con = DriverManager.getConnection(databaseURL, user, password);
		notificacoes = new HashMap<>();
		
		System.out.println("Conexão realizada com sucesso.");
	}

/* 	public boolean autenticarUsuario(String usuario, String senha) throws SQLException {
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
	} */

	public boolean autenticarUsuario(String usuario, String senha) throws SQLException {
		// Query for aluno table
		String alunoQuery = "SELECT * FROM aluno WHERE login = ? AND password = ?";
		
		// Query for professor table
		String professorQuery = "SELECT * FROM professor WHERE login = ? AND password = ?";
		
		try {
			// Try aluno query first
			try (PreparedStatement stmt = con.prepareStatement(alunoQuery)) {
				stmt.setString(1, usuario);
				stmt.setString(2, senha);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					loginAutenticado = usuario; // Store the authenticated login in the public variable
					idAluno = resultSet.getInt("id_aluno");
					return true;
				}
			}
			
			// If aluno query didn't authenticate, try professor query
			try (PreparedStatement stmt = con.prepareStatement(professorQuery)) {
				stmt.setString(1, usuario);
				stmt.setString(2, senha);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					loginAutenticado = usuario; // Store the authenticated login in the public variable
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle SQLException appropriately
		}
		
		// If authentication fails for both aluno and professor
		return false;
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
		// Query for aluno table
		String alunoQuery = "SELECT name FROM aluno WHERE login = ?";
		
		// Query for professor table
		String professorQuery = "SELECT name FROM professor WHERE login = ?";
		
		try {
			// Try aluno query first
			try (PreparedStatement stmt = con.prepareStatement(alunoQuery)) {
				stmt.setString(1, login);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					return resultSet.getString("name");
				}
			}
			
			// If aluno query didn't return a result, try professor query
			try (PreparedStatement stmt = con.prepareStatement(professorQuery)) {
				stmt.setString(1, login);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					return resultSet.getString("name");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// Handle SQLException appropriately
		}
		
		System.out.println("Nenhum nome encontrado para o login: " + login);
		return null;
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

	public List<Integer> obterAlunosPorMateria(String materia) throws SQLException {
        String sql = "SELECT am.id_aluno " +
                     "FROM aluno_materia am " +
                     "INNER JOIN materia m ON am.id_materia = m.id_materia " +
                     "WHERE m.nome_materia = ?";

        List<Integer> alunos = new ArrayList<>();

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, materia);

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                alunos.add(resultSet.getInt("id_aluno"));
            }
        }

        return alunos;
    }

	public String obterNomeAlunoPorId(int idAluno) throws SQLException {
        String sql = "SELECT name FROM aluno WHERE id_aluno = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, idAluno);

            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("name");
            } else {
                return null;
            }
        }
    }

	public int obterIdAluno(String nome) throws SQLException {
    	String idAlunoQuery = "SELECT id_aluno FROM aluno WHERE name =?";
    	try (PreparedStatement stmt = con.prepareStatement(idAlunoQuery)) {
        	stmt.setString(1, nome);
        	ResultSet resultSet = stmt.executeQuery();
        	if (resultSet.next()) {
            	return resultSet.getInt(1);
        	}
    	} catch (SQLException e) {
        	e.printStackTrace();
        	// Handle SQLException appropriately
    	}
		return 0;
	}
	public int obteIdMateriaPorNome(String materiaName) throws SQLException {
		String idMateriaQuery = "SELECT id_materia FROM materia WHERE nome_materia = ?";

		try (PreparedStatement stmt = con.prepareStatement(idMateriaQuery)) {
			stmt.setString(1, materiaName);
			ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("id_materia");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
    }

	public List<Object> obterAlunoInfoPorId(int idAluno, int idMateria) throws SQLException {
		String alunoNameQuery = "SELECT name FROM aluno WHERE id_aluno = ?";
		String materiaQuery = "SELECT absences, grade1, grade2, grade3, grade4, final_exam FROM aluno_materia WHERE id_aluno = ? AND id_materia = ?";
		List<Object> alunoInfo = new ArrayList<>();

		alunoInfo.add(idAluno);

		try {
			try (PreparedStatement stmt = con.prepareStatement(alunoNameQuery)) {
				stmt.setInt(1, idAluno);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					alunoInfo.add(resultSet.getString("name"));
				}
			}
			
			try (PreparedStatement stmt = con.prepareStatement(materiaQuery)) {
				stmt.setInt(1, idAluno);
				stmt.setInt(2, idMateria);
				ResultSet resultSet = stmt.executeQuery();
		
				if (resultSet.next()) {
					alunoInfo.add(resultSet.getInt("absences"));
					alunoInfo.add(resultSet.getDouble("grade1"));
					alunoInfo.add(resultSet.getDouble("grade2"));
					alunoInfo.add(resultSet.getDouble("grade3"));
					alunoInfo.add(resultSet.getDouble("grade4"));
					alunoInfo.add(resultSet.getDouble("final_exam"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return alunoInfo;
	}

	public void atualizarInfoAluno(int idAluno, int idMateria, int faltas, double grade1, double grade2, double grade3, double grade4, double finalExam) throws SQLException {
		String sql = "UPDATE aluno_materia SET absences = ?, grade1 = ?, grade2 = ?, grade3 = ?, grade4 = ?, final_exam = ? WHERE id_aluno = ? AND id_materia = ?";
	
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, faltas);
			stmt.setDouble(2, grade1);
			stmt.setDouble(3, grade2);
			stmt.setDouble(4, grade3);
			stmt.setDouble(5, grade4);
			stmt.setDouble(6, finalExam);
			stmt.setInt(7, idAluno);
			stmt.setInt(8, idMateria);
			
			inserirNotificacao(idAluno, idMateria);
	
			stmt.executeUpdate();
			
		}
	}

	public String obterNomeProfessorPorMateria(String materia) throws SQLException {
		String sql = "SELECT p.name " +
					 "FROM professor_materia pm " +
					 "INNER JOIN professor p ON pm.id_professor = p.id_professor " +
					 "INNER JOIN materia m ON pm.id_materia = m.id_materia " +
					 "WHERE m.nome_materia = ?";
	
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setString(1, materia);
	
			ResultSet resultSet = stmt.executeQuery();
	
			if (resultSet.next()) {
				return resultSet.getString("name");
			} else {
				return null; // No professor found for the given materia
			}
		}
	}
	
	public void inserirNotificacao(int idAluno, int idMateria) throws SQLException {
		String materia;
		materia = obterNomeMateriaPorId(idMateria);
        notificacoes.computeIfAbsent(idAluno, k -> new ArrayList<>()).add(materia);
    }

    private String obterNomeMateriaPorId(int idMateria) {
		String sql = "SELECT nome_materia FROM materia WHERE id_materia =?";
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, idMateria);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("nome_materia");
            }
        } catch (SQLException e) {
			e.printStackTrace();
        }
		// Handle SQLException appropriately
		return null;
	}

	public List<String> obterNotificacoesNaoLidas(int idAluno) {
        List<String> notificacoesDoAluno = notificacoes.getOrDefault(idAluno, new ArrayList<>());
        notificacoes.remove(idAluno);
        return notificacoesDoAluno;
    }

	public Boolean notificacaoIsTrue(String nome) throws SQLException{
		int id = obterIdAluno(nome);
		List<String> notificacoes = obterNotificacoesNaoLidas(id);
		if(notificacoes.size() > 0) {
			return true;
		}else{
			return false;
		}

	}

}
