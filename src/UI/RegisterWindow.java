package UI;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import DB.Conexao;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;

public class RegisterWindow {

	private JFrame frame;
	private JTextField textFieldLogin;
	private JPasswordField passwordField;
	private JTextField textFieldNome;
	private JCheckBox checkBoxIsProfessor;

	public RegisterWindow(Conexao conexao) {
		initialize();
		try {
			File fontFileUsuarioSenha = new File("src\\SFUIText-Light.otf");
			Font customFontUsuarioSenha = Font.createFont(Font.TRUETYPE_FONT, fontFileUsuarioSenha);
			GraphicsEnvironment geUsuarioSenha = GraphicsEnvironment.getLocalGraphicsEnvironment();
			geUsuarioSenha.registerFont(customFontUsuarioSenha);

			Font customFont14 = customFontUsuarioSenha.deriveFont(14f);

			MyButton btnRegistrar = new MyButton();
			btnRegistrar.setText("Registrar");
			btnRegistrar.setFont(customFont14);
			btnRegistrar.setBounds(150, 210, 151, 23);
			btnRegistrar.setColor(new Color(0xe3e3e3));
			btnRegistrar.setForeground(new Color(57, 149, 247));

			btnRegistrar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					registrarUsuario(conexao);
				}
			});
			frame.getContentPane().add(btnRegistrar);

			JLabel lblNome = new JLabel("Nome");
			lblNome.setFont(customFont14);
			lblNome.setBounds(150, 46, 46, 14);
			frame.getContentPane().add(lblNome);

			JLabel lblLogin = new JLabel("Login");
			lblLogin.setFont(customFont14);
			lblLogin.setBounds(150, 87, 46, 14);
			frame.getContentPane().add(lblLogin);

			JLabel lblSenha = new JLabel("Senha");
			lblSenha.setFont(customFont14);
			lblSenha.setBounds(150, 127, 46, 14);
			frame.getContentPane().add(lblSenha);

			textFieldNome = new JTextField();
			textFieldNome.setBounds(150, 60, 151, 20);
			frame.getContentPane().add(textFieldNome);
			textFieldNome.setColumns(10);

			textFieldLogin = new JTextField();
			textFieldLogin.setBounds(150, 100, 151, 20);
			frame.getContentPane().add(textFieldLogin);
			textFieldLogin.setColumns(10);

			passwordField = new JPasswordField();
			passwordField.setBounds(150, 140, 151, 20);
			frame.getContentPane().add(passwordField);

			checkBoxIsProfessor = new JCheckBox("Sou professor");
			checkBoxIsProfessor.setFont(customFont14);
			checkBoxIsProfessor.setBounds(150, 170, 151, 23);

			// Configurando a cor de fundo da JCheckBox
			checkBoxIsProfessor.setBackground(new Color(0xe3e3e3));

			frame.getContentPane().add(checkBoxIsProfessor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.WHITE);
	}

	private void registrarUsuario(Conexao conexao) {
		String login = textFieldLogin.getText();
		String senha = new String(passwordField.getPassword());
		String nome = textFieldNome.getText();
		boolean isProfessor = checkBoxIsProfessor.isSelected();

		if (senha.length() < 8) {
			JOptionPane.showMessageDialog(frame, "A senha deve ter no mínimo 8 caracteres.");
		} else {
			try {
				if (conexao.verificarLoginExistente(login)) {
					JOptionPane.showMessageDialog(frame, "O login já está em uso. Escolha outro.");
				} else {
					try {
						if (isProfessor) {
							conexao.registrarProfessor(login, senha, nome);
						} else {
							conexao.registrarAluno(login, senha, nome);
						}
						JOptionPane.showMessageDialog(frame, "Registro bem-sucedido!");
						frame.dispose();
					} catch (SQLException ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(frame,
								"Erro ao registrar. Verifique os dados e tente novamente.");
					}
				}
			} catch (HeadlessException | SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	public JFrame getFrame() {
		return frame;
	}
}
