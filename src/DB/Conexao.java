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
	public String loginAutenticado;
	public int idAluno;
	private String databaseURL;
	private String user;
	private String password;
	private Connection con;
	private Map<Integer, List<String>> notificacoes;

	public Conexao() throws ClassNotFoundException, SQLException {
		databaseURL = "jdbc:postgresql://localhost:5432/users";
		user = "postgres";
		password = "postgres";
		Class.forName("org.postgresql.Driver");
		con = DriverManager.getConnection(databaseURL, user, password);
		notificacoes = new HashMap<>();

		System.out.println("Conexao realizada com sucesso.");
	}

	public boolean autenticarUsuario(String usuario, String senha) throws SQLException {
		String alunoQuery = "SELECT * FROM aluno WHERE login = ? AND password = ?";
		String professorQuery = "SELECT * FROM professor WHERE login = ? AND password = ?";
		String administradorQuery = "SELECT * FROM administrador WHERE login = ? AND password = ?";
		
		try {
			try (PreparedStatement stmt = con.prepareStatement(alunoQuery)) {
				stmt.setString(1, usuario);
				stmt.setString(2, senha);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					loginAutenticado = usuario;
					idAluno = resultSet.getInt("id_aluno");
					return true;
				}
			}
			
			try (PreparedStatement stmt = con.prepareStatement(professorQuery)) {
				stmt.setString(1, usuario);
				stmt.setString(2, senha);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					loginAutenticado = usuario;
					return true;
				}
			}
			
			try (PreparedStatement stmt = con.prepareStatement(administradorQuery)) {
				stmt.setString(1, usuario);
				stmt.setString(2, senha);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					loginAutenticado = usuario;
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	

public boolean verificarLoginExistente(String login) throws SQLException {
    String sqlProfessor = "SELECT COUNT(*) AS count FROM professor WHERE login = ?";
    String sqlAluno = "SELECT COUNT(*) AS count FROM aluno WHERE login = ?";
    String sqlAdministrador = "SELECT COUNT(*) AS count FROM administrador WHERE login = ?";

    try (PreparedStatement stmtProfessor = con.prepareStatement(sqlProfessor);
         PreparedStatement stmtAluno = con.prepareStatement(sqlAluno);
         PreparedStatement stmtAdministrador = con.prepareStatement(sqlAdministrador)) {

        stmtProfessor.setString(1, login);
        stmtAluno.setString(1, login);
        stmtAdministrador.setString(1, login);

        ResultSet resultSetProfessor = stmtProfessor.executeQuery();
        ResultSet resultSetAluno = stmtAluno.executeQuery();
        ResultSet resultSetAdministrador = stmtAdministrador.executeQuery();

        int countProfessor = resultSetProfessor.next() ? resultSetProfessor.getInt("count") : 0;
        int countAluno = resultSetAluno.next() ? resultSetAluno.getInt("count") : 0;
        int countAdministrador = resultSetAdministrador.next() ? resultSetAdministrador.getInt("count") : 0;

        return (countProfessor > 0 || countAluno > 0 || countAdministrador > 0);
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
		String alunoQuery = "SELECT name FROM aluno WHERE login = ?";
		String professorQuery = "SELECT name FROM professor WHERE login = ?";
		String administradorQuery = "SELECT name FROM administrador WHERE login = ?";
		
		try {
			try (PreparedStatement stmt = con.prepareStatement(alunoQuery)) {
				stmt.setString(1, login);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					return resultSet.getString("name");
				}
			}
			
			try (PreparedStatement stmt = con.prepareStatement(professorQuery)) {
				stmt.setString(1, login);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					return resultSet.getString("name");
				}
			}
			
			try (PreparedStatement stmt = con.prepareStatement(administradorQuery)) {
				stmt.setString(1, login);
				ResultSet resultSet = stmt.executeQuery();
				if (resultSet.next()) {
					return resultSet.getString("name");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
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

	public int verificarTipoUsuario(String login) throws SQLException {
		String sqlAluno = "SELECT COUNT(*) AS count FROM aluno WHERE login = ?";
		String sqlProfessor = "SELECT COUNT(*) AS count FROM professor WHERE login = ?";
		String sqlAdministrador = "SELECT COUNT(*) AS count FROM administrador WHERE login = ?";
	
		try (PreparedStatement stmtAluno = con.prepareStatement(sqlAluno)) {
			stmtAluno.setString(1, login);
			ResultSet resultSetAluno = stmtAluno.executeQuery();
			if (resultSetAluno.next() && resultSetAluno.getInt("count") > 0) {
				return 1; // Aluno
			}
		}
	
		try (PreparedStatement stmtProfessor = con.prepareStatement(sqlProfessor)) {
			stmtProfessor.setString(1, login);
			ResultSet resultSetProfessor = stmtProfessor.executeQuery();
			if (resultSetProfessor.next() && resultSetProfessor.getInt("count") > 0) {
				return 2; // Professor
			}
		}
	
		try (PreparedStatement stmtAdministrador = con.prepareStatement(sqlAdministrador)) {
			stmtAdministrador.setString(1, login);
			ResultSet resultSetAdministrador = stmtAdministrador.executeQuery();
			if (resultSetAdministrador.next() && resultSetAdministrador.getInt("count") > 0) {
				return 3; // Administrador
			}
		}
	
		return 0;
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
				return null;
			}
		}
	}

	public List<Par<Integer, String>> mostrarTodosAlunos() throws SQLException {
		List<Par<Integer, String>> alunos = new ArrayList<>();

		String sql = "SELECT id_aluno, name FROM aluno";

		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				int idAluno = resultSet.getInt("id_aluno");
				String nomeAluno = resultSet.getString("name");
				alunos.add(new Par<>(idAluno, nomeAluno));
			}
		}

		return alunos;
	}

	public List<Par<Integer, String>> mostrarTodosProfessores() throws SQLException {
		List<Par<Integer, String>> professores = new ArrayList<>();

		String sql = "SELECT id_professor, name FROM professor";

		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			ResultSet resultSet = stmt.executeQuery();

			while (resultSet.next()) {
				int idProfessor = resultSet.getInt("id_professor");
				String nomeProfessor = resultSet.getString("name");
				professores.add(new Par<>(idProfessor, nomeProfessor));
			}
		}

		return professores;
	}

	public List<String> obterMateriasPorIdProfessor(int idProfessor) throws SQLException {
		String sql = "SELECT m.nome_materia FROM materia m "
				+ "JOIN professor_materia pm ON m.id_materia = pm.id_materia "
				+ "JOIN professor p ON pm.id_professor = p.id_professor " + "WHERE p.id_professor = ?";
	
		List<String> materias = new ArrayList<>();
	
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, idProfessor);
	
			ResultSet resultSet = stmt.executeQuery();
	
			while (resultSet.next()) {
				materias.add(resultSet.getString("nome_materia"));
			}
		}
	
		return materias;
	}
	
	public List<String> obterMateriasPorIdAluno(int idAluno) throws SQLException {
		String sql = "SELECT m.nome_materia FROM materia m " + "JOIN aluno_materia am ON m.id_materia = am.id_materia "
				+ "JOIN aluno a ON am.id_aluno = a.id_aluno " + "WHERE a.id_aluno = ?";
	
		List<String> materias = new ArrayList<>();
	
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, idAluno);
	
			ResultSet resultSet = stmt.executeQuery();
	
			while (resultSet.next()) {
				materias.add(resultSet.getString("nome_materia"));
			}
		}
	
		return materias;
	}

	public List<String> obterTodasAsMaterias() throws SQLException {
		String sql = "SELECT nome_materia FROM materia";
	
		List<String> materias = new ArrayList<>();
	
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			ResultSet resultSet = stmt.executeQuery();
	
			while (resultSet.next()) {
				materias.add(resultSet.getString("nome_materia"));
			}
		}
	
		return materias;
	}

	public List<Par<Integer, String>> obterTodasAsMateriasComId() throws SQLException {
		String sql = "SELECT id_materia, nome_materia FROM materia";
		List<Par<Integer, String>> materias = new ArrayList<>();
	
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			ResultSet resultSet = stmt.executeQuery();
	
			while (resultSet.next()) {
				int id = resultSet.getInt("id_materia");
				String nome = resultSet.getString("nome_materia");
				materias.add(new Par<>(id, nome));
			}
		}
	
		return materias;
	}
	
	public void deletarAssociacoesMateria(int idMateria) throws SQLException {
		String sqlAlunoMateria = "DELETE FROM aluno_materia WHERE id_materia = ?";
		try (PreparedStatement stmt = con.prepareStatement(sqlAlunoMateria)) {
			stmt.setInt(1, idMateria);
			stmt.executeUpdate();
		}
	
		String sqlProfessorMateria = "DELETE FROM professor_materia WHERE id_materia = ?";
		try (PreparedStatement stmt = con.prepareStatement(sqlProfessorMateria)) {
			stmt.setInt(1, idMateria);
			stmt.executeUpdate();
		}
	}
	
	public void deletarMateria(int idMateria) throws SQLException {
		deletarAssociacoesMateria(idMateria);
	
		String sqlMateria = "DELETE FROM materia WHERE id_materia = ?";
		try (PreparedStatement stmt = con.prepareStatement(sqlMateria)) {
			stmt.setInt(1, idMateria);
			stmt.executeUpdate();
		}
	}
	
	public boolean criarMateria(String nomeMateria) throws SQLException {
		String sqlCheck = "SELECT COUNT(*) FROM materia WHERE nome_materia = ?";
		try (PreparedStatement stmtCheck = con.prepareStatement(sqlCheck)) {
			stmtCheck.setString(1, nomeMateria);
			ResultSet resultSet = stmtCheck.executeQuery();
			if (resultSet.next() && resultSet.getInt(1) > 0) {
				return false;
			}
		}
	
		String sqlInsert = "INSERT INTO materia (nome_materia) VALUES (?)";
		try (PreparedStatement stmtInsert = con.prepareStatement(sqlInsert)) {
			stmtInsert.setString(1, nomeMateria);
			stmtInsert.executeUpdate();
			return true;
		}
	}
	
	public void adicionarRelacaoAlunoMateria(int idAluno, int idMateria) throws SQLException {
		String sql = "INSERT INTO aluno_materia (id_aluno, id_materia) VALUES (?, ?)";
		
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, idAluno);
			stmt.setInt(2, idMateria);
			stmt.executeUpdate();
		}
	}
	
	public void removerRelacaoAlunoMateria(int idAluno, int idMateria) throws SQLException {
		String sql = "DELETE FROM aluno_materia WHERE id_aluno = ? AND id_materia = ?";
		
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, idAluno);
			stmt.setInt(2, idMateria);
			stmt.executeUpdate();
		}
	}

	public void adicionarRelacaoProfessorMateria(int idProfessor, int idMateria) throws SQLException {
		String sql = "INSERT INTO professor_materia (id_professor, id_materia) VALUES (?, ?)";
		
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, idProfessor);
			stmt.setInt(2, idMateria);
			stmt.executeUpdate();
		}
	}
	
	public void removerRelacaoProfessorMateria(int idProfessor, int idMateria) throws SQLException {
		String sql = "DELETE FROM professor_materia WHERE id_professor = ? AND id_materia = ?";
		
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, idProfessor);
			stmt.setInt(2, idMateria);
			stmt.executeUpdate();
		}
	}	
	
	public int contarAlunos() throws SQLException {
	    String sql = "SELECT COUNT(*) AS count FROM aluno";

	    try (PreparedStatement stmt = con.prepareStatement(sql)) {
	        ResultSet resultSet = stmt.executeQuery();

	        if (resultSet.next()) {
	            return resultSet.getInt("count");
	        }
	    }

	    return 0; // Retorna 0 se não houver alunos encontrados
	}

	public int contarMaterias() throws SQLException {
	    String sql = "SELECT COUNT(*) AS count FROM materia";

	    try (PreparedStatement stmt = con.prepareStatement(sql)) {
	        ResultSet resultSet = stmt.executeQuery();

	        if (resultSet.next()) {
	            return resultSet.getInt("count");
	        }
	    }

	    return 0; // Retorna 0 se não houver matérias encontradas
	}

	
	public int contarProfessores() throws SQLException {
	    String sql = "SELECT COUNT(*) AS count FROM professor";

	    try (PreparedStatement stmt = con.prepareStatement(sql)) {
	        ResultSet resultSet = stmt.executeQuery();

	        if (resultSet.next()) {
	            return resultSet.getInt("count");
	        }
	    }

	    return 0; 
	}

	
	public void inserirNotificacao(int idAluno, int idMateria) throws SQLException {
	    String materia = obterNomeMateriaPorId(idMateria);
	    notificacoes.computeIfAbsent(idAluno, k -> new ArrayList<>()).add(materia);
	    String sql = "INSERT INTO notificacao (id_aluno, nome_materia) VALUES (?, ?)";
	    try (PreparedStatement stmt = con.prepareStatement(sql)) {
	        stmt.setInt(1, idAluno);
	        stmt.setString(2, materia);
	        stmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
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
		return null;
	}

	public String obterNotificacoesNaoLidas(int idAluno) {
        //List<String> notificacoesDoAluno = notificacoes.getOrDefault(idAluno, new ArrayList<>());
		String sql = "SELECT nome_materia FROM notificacao WHERE id_aluno = ?";
    
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, idAluno);
			ResultSet resultSet = stmt.executeQuery();
	
			if (resultSet.next()) {
				return resultSet.getString("nome_materia");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		// Retorna null se não encontrar nenhuma matéria para o id_aluno
		return null;
    }

	public Boolean notificacaoIsTrue(String nome) throws SQLException{
		int id = obterIdAluno(nome);
		String notificacoes = obterNotificacoesNaoLidas(id);
		if(notificacoes != null) {
			return true;
		}else{
			return false;
		}

	}
	public void resetar_notificacao(){
		String sql = "DELETE FROM notificacao WHERE id_aluno = ?";
    
		try (PreparedStatement stmt = con.prepareStatement(sql)) {
			stmt.setInt(1, idAluno);
			stmt.executeUpdate();
			System.out.println("Notificações deletadas para o id_aluno: " + idAluno);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
