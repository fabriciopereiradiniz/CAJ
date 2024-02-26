package UI;

import javax.swing.*;

import DB.Conexao;
import entities.*;

import java.awt.*;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private Conexao conexao;
	private Pessoa usuario;

    public MainWindow(Conexao conexao) {
        this.conexao = conexao;
        initialize();
    }

    private void initialize() {
		this.setBounds(100, 100, 800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setResizable(false);

		String nomeUsuario = "";
		try {
			nomeUsuario = conexao.obterNomePorLogin(conexao.loginAutenticado);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			int tipoUsuario = conexao.verificarTipoUsuario(conexao.loginAutenticado);

			if (tipoUsuario == 3) {
				usuario = new Administrador(nomeUsuario);
				this.setBackground(Color.WHITE);
				exibirInformacoesAdministrador();
			} else if (tipoUsuario == 2) {
				usuario = new Professor(nomeUsuario);
				usuario.setMaterias(conexao.obterMateriasPorProfessor(conexao.loginAutenticado));
				exibirInformacoesProfessor();
			} else if (tipoUsuario == 1) {
				usuario = new Aluno(nomeUsuario);
				usuario.setMaterias(conexao.obterMateriasPorAluno(conexao.loginAutenticado));
				exibirInformacoesAluno();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void exibirInformacoesAdministrador() {
		SwingUtilities.invokeLater(() -> {
			AreaAdmin areaAdministrador = new AreaAdmin(conexao, (Administrador) usuario);
			areaAdministrador.setVisible(true);
			MainWindow.this.dispose();
		});
	}

	private void exibirInformacoesProfessor() {
		SwingUtilities.invokeLater(() -> {
			AreaProfessor areaProfessor = new AreaProfessor(conexao, (Professor) usuario);
			areaProfessor.setVisible(true);
			MainWindow.this.dispose();
		});
	}

	private void exibirInformacoesAluno() {
		SwingUtilities.invokeLater(() -> {
			AreaAluno areaAluno = new AreaAluno(conexao, (Aluno) usuario);
			areaAluno.setVisible(true);
			MainWindow.this.dispose();
		});
	}
}
