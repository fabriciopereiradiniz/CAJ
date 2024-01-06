package UI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import DB.Conexao;

import java.util.List;

public class MainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	private Conexao conexao;

	public MainWindow(Conexao conexao) {
		this.conexao = conexao;
		initialize();
	}

	private void initialize() {
		this.setBounds(100, 100, 800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		String nomeUsuario = "";
		try {
			nomeUsuario = conexao.obterNomePorLogin(conexao.loginAutenticado);
		} catch (Exception e) {
			e.printStackTrace();
		}

		JLabel lblNewLabel = new JLabel("Bem-vindo, " + nomeUsuario + "!");
		lblNewLabel.setBounds(39, 25, 200, 13);
		getContentPane().add(lblNewLabel);

		try {
			boolean isProfessor = conexao.verificarTipoUsuario(conexao.loginAutenticado);

			if (isProfessor) {
				List<String> materias = conexao.obterMateriasPorProfessor(conexao.loginAutenticado);
				exibirInformacoesProfessor(materias);
			} else {
				List<String> materias = conexao.obterMateriasPorAluno(conexao.loginAutenticado);
				exibirInformacoesAluno(materias);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void exibirInformacoesProfessor(List<String> materias) {
		int yPos = 50; // Initial Y position for JLabels
		JLabel lblProfessor = new JLabel("Professor - Matérias:");
		lblProfessor.setBounds(39, yPos, 200, 13);
		getContentPane().add(lblProfessor);
		yPos += 20;

		for (String materia : materias) {
			JLabel lblMateria = new JLabel(materia);
			lblMateria.setBounds(39, yPos, 200, 13);
			getContentPane().add(lblMateria);
			yPos += 20;

			// Add code to display students associated with each subject, if needed
		}
	}

	private void exibirInformacoesAluno(List<String> materias) {
		int yPos = 50; // Initial Y position for JLabels
		JLabel lblAluno = new JLabel("Aluno - Matérias:");
		lblAluno.setBounds(39, yPos, 200, 13);
		getContentPane().add(lblAluno);
		yPos += 20;

		for (String materia : materias) {
			JLabel lblMateria = new JLabel(materia);
			lblMateria.setBounds(39, yPos, 200, 13);
			getContentPane().add(lblMateria);
			yPos += 20;
		}
	}
}
