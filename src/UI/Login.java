package UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import DB.Conexao;

public class Login {

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;
	private Conexao conexao;
	private static Login instance;
	private MainWindow mainWindow;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Login() {
		initialize();
		instance = this;
		try {
			conexao = new Conexao();
			mainWindow = new MainWindow(conexao);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static void showWindow() {
        if (instance != null) {
            instance.showLoginWindow();
        }
    }

	private void showLoginWindow() {
		clearTextFields();
        frame.setVisible(true);
    }

	private void clearTextFields() {
        textField.setText(""); // Clear the login field
        passwordField.setText(""); // Clear the password field
    }

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().setBackground(Color.WHITE);

		ImageIcon icon = new ImageIcon("src\\login.png");
		JLabel lblImagem = new JLabel(icon);
		lblImagem.setBounds(0, 0, 220, 263);
		lblImagem.setBackground(new Color(0, 0, 0, 0));
		frame.getContentPane().add(lblImagem);

		try {
			File fontFileAreaLogin = new File(
					"src\\SF-Pro-Display-Regular.otf");
			Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaLogin);
			GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
			geAreaLogin.registerFont(customFontAreaLogin);

			Font customFont12 = customFontAreaLogin.deriveFont(20f);

			JLabel lblNewLabel = new JLabel("Área de login");
			lblNewLabel.setFont(customFont12);
			lblNewLabel.setBounds(280, 45, 110, 24);
			lblNewLabel.setForeground(new Color(29, 29, 31));
			frame.getContentPane().add(lblNewLabel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			File fontFileUsuarioSenha = new File(
					"src\\SFUIText-Light.otf");
			Font customFontUsuarioSenha = Font.createFont(Font.TRUETYPE_FONT, fontFileUsuarioSenha);
			GraphicsEnvironment geUsuarioSenha = GraphicsEnvironment.getLocalGraphicsEnvironment();
			geUsuarioSenha.registerFont(customFontUsuarioSenha);

			Font customFont14 = customFontUsuarioSenha.deriveFont(14f);

			JLabel lblNewLabel_1 = new JLabel("Login");
			lblNewLabel_1.setFont(customFont14);
			lblNewLabel_1.setBounds(230, 89, 50, 20);
			lblNewLabel_1.setForeground(new Color(29, 29, 31));
			frame.getContentPane().add(lblNewLabel_1);

			JLabel lblNewLabel_1_1 = new JLabel("Senha");
			lblNewLabel_1_1.setFont(customFont14);
			lblNewLabel_1_1.setBounds(230, 131, 60, 20);
			lblNewLabel_1_1.setForeground(new Color(29, 29, 31));
			frame.getContentPane().add(lblNewLabel_1_1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		textField = new JTextField();
		textField.setBounds(230, 111, 196, 19);
		textField.setForeground(new Color(29, 29, 31));
		frame.getContentPane().add(textField);

		passwordField = new JPasswordField();
		passwordField.setBounds(230, 151, 196, 19);
		frame.getContentPane().add(passwordField);

		MyButton btnNewButton = new MyButton();
		btnNewButton.setText("Logar");
		try {
			File fontFileAreaLogin = new File(
					"src\\SF-Pro-Display-Regular.otf");
			Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaLogin);
			GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
			geAreaLogin.registerFont(customFontAreaLogin);
			Font customFont12 = customFontAreaLogin.deriveFont(11f);
			btnNewButton.setFont(customFont12);
		} catch (Exception e) {
			e.printStackTrace();
		}
		btnNewButton.setBounds(230, 191, 196, 21);
		btnNewButton.setColor(new Color(0xe3e3e3));
		btnNewButton.setForeground(new Color(57, 149, 247));
		frame.getContentPane().add(btnNewButton);

		try {

			File fontFileUsuarioSenha = new File(
					"src\\SFUIText-Light.otf");
			Font customFontUsuarioSenha = Font.createFont(Font.TRUETYPE_FONT, fontFileUsuarioSenha);
			GraphicsEnvironment geUsuarioSenha = GraphicsEnvironment.getLocalGraphicsEnvironment();
			geUsuarioSenha.registerFont(customFontUsuarioSenha);

			Font customFont14 = customFontUsuarioSenha.deriveFont(9f);

			JLabel lblRegistrar = new JLabel("Não tem uma conta? Registre-se");
			lblRegistrar.setForeground(Color.black);
			lblRegistrar.setFont(customFont14);
			lblRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			lblRegistrar.setBounds(288, 171, 138, 20);
			frame.getContentPane().add(lblRegistrar);

			lblRegistrar.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					mostrarJanelaRegistro();
				}
			});

			JLabel lblErro = new JLabel("");
			lblErro.setForeground(Color.RED);
			lblErro.setFont(customFont14);
			lblErro.setBounds(388, 219, 38, 20);
			frame.getContentPane().add(lblErro);

			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String usuario = textField.getText();
					String senha = new String(passwordField.getPassword());

					try {
						if (autenticar(usuario, senha)) {
							System.out.println("Login bem-sucedido!");
						} else {
							exibirMensagemErro("Inválido.", lblErro);
						}
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// metodos
	private void mostrarJanelaRegistro() {
		RegisterWindow registerWindow = new RegisterWindow(conexao);
		registerWindow.getFrame().setVisible(true);
	}

	private boolean autenticar(String usuario, String senha) throws SQLException {
		boolean autenticado = conexao.autenticarUsuario(usuario, senha);
		if (autenticado) {
			// Se autenticado, crie a instância de MainWindow e mostre-a
			mainWindow = new MainWindow(conexao);
			mainWindow.setVisible(true);
			frame.dispose();
		}
		return autenticado;
	}

	private void exibirMensagemErro(String mensagem, JLabel lblErro) {
		lblErro.setText(mensagem);
		frame.revalidate();
		frame.repaint();
	}
}
