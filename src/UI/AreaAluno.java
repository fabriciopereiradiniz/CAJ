package UI;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.sql.SQLException;

import java.util.Arrays;
import java.util.List;

import DB.Conexao;
import entities.Aluno;

public class AreaAluno extends JFrame {

    private Conexao conexao;
    private Aluno aluno;
    private JTable table;
    private DefaultTableModel tableModel;

    public AreaAluno(Conexao conexao, Aluno aluno) {
        this.conexao = conexao;
        this.aluno = aluno;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Area do Aluno - " + aluno.getNome());
        this.setBounds(100, 100, 800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setResizable(false);

        try {
            exibirInformacoesAluno(aluno.getMaterias());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exibirInformacoesAluno(List<String> materias) throws SQLException {
        getContentPane().removeAll();
		this.getContentPane().setBackground(Color.WHITE);

        setTitle("Area do Aluno - " + aluno.getNome());
        
        
        String[] columnHeaders = {"ID", "Matéria", "Faltas", "1º Bimestre", "2º Bimestre", "3º Bimestre", "4º Bimestre", "Exame"};
        tableModel = new DefaultTableModel(columnHeaders, 0) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        try {
            File fontFileAreaAluno = new File("src\\SF-Pro-Display-Regular.otf");
            Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
            GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
            geAreaLogin.registerFont(customFontAreaLogin);

            Font customFont12 = customFontAreaLogin.deriveFont(16f); // Reduzindo o tamanho da fonte para 16 pixels

            JLabel lblWelcome = new JLabel("Bem-vindo, " + aluno.getNome() + "!");
            lblWelcome.setFont(customFont12);
            lblWelcome.setBounds(40, 65, 250, 24); // Ajustando a largura para acomodar o texto
            lblWelcome.setForeground(new Color(29, 29, 31));
            getContentPane().add(lblWelcome);
        } catch (Exception e) {
            e.printStackTrace();
        }

		try {
			File fontFileUsuarioSenha = new File("src\\SF-Pro-Display-Regular.otf");
			Font customFontUsuarioSenha = Font.createFont(Font.TRUETYPE_FONT, fontFileUsuarioSenha);
			GraphicsEnvironment geUsuarioSenha = GraphicsEnvironment.getLocalGraphicsEnvironment();
			geUsuarioSenha.registerFont(customFontUsuarioSenha);

			Font customFont14 = customFontUsuarioSenha.deriveFont(14f);

			table.setFont(customFont14);

		} catch (Exception e) {
			e.printStackTrace();
		}
        
        try {
            File fontFileAreaAluno = new File("src\\SFUIText-Light.otf");
            Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
            GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
            geAreaLogin.registerFont(customFontAreaLogin);

            Font customFont12 = customFontAreaLogin.deriveFont(16f); // Reduzindo o tamanho da fonte para 16 pixels

            // Criar uma fonte personalizada para os cabeçalhos
            Font fonteCabecalho = customFont12;

            // Criar um renderizador para os cabeçalhos da tabela
            DefaultTableCellRenderer rendererCabecalho = new DefaultTableCellRenderer();
            rendererCabecalho.setFont(fonteCabecalho);

            // Definir o renderizador para os cabeçalhos de todas as colunas
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getTableHeader().getColumnModel().getColumn(i).setHeaderRenderer(rendererCabecalho);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(39, 100, 700, 400);
        getContentPane().add(scrollPane);

        try {
            for (String materia : materias) {
                int idMateriaAtual = conexao.obteIdMateriaPorNome(materia);
                List<Object> alunoInfo = conexao.obterAlunoInfoPorId(conexao.idAluno, idMateriaAtual);

                Object[] rowData = new Object[8];
				rowData[0] = idMateriaAtual;

                for (int i = 1; i <= 7; i++) {
                    Object value = (alunoInfo.get(i) != null) ? alunoInfo.get(i) : "-";
                    rowData[i] = value;

                    if ((i == 3 || i == 4 || i == 5 || i == 6) && value instanceof Number && ((Number) value).doubleValue() < 70.0) {
                        // Se a nota for menor que 7.0 e estiver nos índices 3, 4, 5 ou 6, definimos a cor vermelha
                        table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
							private static final long serialVersionUID = 1L;

							@Override
                            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                                c.setForeground(Color.BLACK);
                                return c;
                            }
                        });
                    }
                }

                rowData[1] = materia;
                tableModel.addRow(rowData);

				double bimestreAverage = 0;
				for (int i = 3; i < 7; i++) {
					bimestreAverage += (double) alunoInfo.get(i);
				}
				bimestreAverage /= 4;

				if (bimestreAverage < 7) {
					double requiredExamScore = 12 - bimestreAverage;
					Object[] messageRow = new Object[columnHeaders.length];
					Arrays.fill(messageRow, null);
					messageRow[1] = "<html><font color='red'>Você ficou</font></html>";
					messageRow[2] = "<html><font color='red'>de exame.</font></html>";
					messageRow[3] = "<html><font color='red'>Nota</font></html>";
					messageRow[4] = "<html><font color='red'>necessaria</font></html>";
					messageRow[5] = "<html><font color='red'>no exame: </font></html>";
					messageRow[6] = "<html><font color='red'> " + String.format("%.2f", requiredExamScore) + "</font></html>";
					tableModel.addRow(messageRow);
				}
						}
        } catch (Exception e) {
            e.printStackTrace();
        }

		MyButton btnSairAluno = new MyButton();
		btnSairAluno.setText("Sair");
		btnSairAluno.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				voltarParaTelaDeLogin();
			}
		});
		btnSairAluno.setBounds(39, 510, 80, 20);
		getContentPane().add(btnSairAluno);


		if (conexao.notificacaoIsTrue(aluno.getNome())) {
            System.out.println("Notificacoes on");
            int id = (conexao.obterIdAluno(aluno.getNome()));
            String nome_materia = conexao.obterNotificacoesNaoLidas(id);
            PopupUtil.exibirPopup("Alterações em " + nome_materia + ".");
            conexao.resetar_notificacao();
        }
    }

    private void voltarParaTelaDeLogin() {
        this.dispose();
        Login.showWindow();
    }
}