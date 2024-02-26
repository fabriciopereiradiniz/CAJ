package UI;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import DB.Conexao;
import entities.Professor;

public class AreaProfessor extends JFrame {

    private Conexao conexao;
    private Professor professor;
    private JPanel panel;
    private JTable table;
    private DefaultTableModel tableModel;
    private String selectedMateria;
    private int idSelectedMateria;

    public AreaProfessor(Conexao conexao, Professor professor) {
        this.conexao = conexao;
        this.professor = professor;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Area do Professor - " + professor.getNome());
        this.setBounds(100, 100, 800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setResizable(false);

        exibirInformacoesProfessor(professor.getMaterias());
    }

    private void exibirInformacoesProfessor(List<String> materias) {
        this.getContentPane().setBackground(Color.WHITE);
        int gridSize = materias.size();
        System.out.println(gridSize);

        if (gridSize < 2) {
            gridSize = 2;
        }
        getContentPane().removeAll();
        panel = new JPanel();
        panel.setLayout(new GridLayout(gridSize + 3, 2));
        panel.setBounds(39, 50, 300, gridSize * 30);
        getContentPane().add(panel);

        setTitle("Area do Professor - " + professor.getNome());

        try {
        	File fontFileAreaAluno = new File("src\\SF-Pro-Display-Regular.otf");
            Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
            GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
            geAreaLogin.registerFont(customFontAreaLogin);

            Font customFont12 = customFontAreaLogin.deriveFont(12f); // Reduzindo o tamanho da fonte para 12 pixels

            JLabel lblWelcomeP = new JLabel("Bem-vindo, " + professor.getNome() + "!");
            lblWelcomeP.setFont(customFont12);
            // Ajustando a largura do JLabel para acomodar o texto completamente
            lblWelcomeP.setSize(lblWelcomeP.getPreferredSize());
            lblWelcomeP.setForeground(new Color(29, 29, 31));
            panel.add(lblWelcomeP);

			JLabel emptyLabel = new JLabel();
			panel.add(emptyLabel);
			panel.setBounds(80, 60, 630	, 330);
			panel.setBackground(Color.white);
			if (materias.size() > 0) {
				JLabel lblMateriaHeader = new JLabel("Matéria");
				lblMateriaHeader.setFont(customFont12);
				panel.add(lblMateriaHeader);

				JLabel blankDebugger = new JLabel("");
				panel.add(blankDebugger);
			} else {
				JLabel lblNoMateriaHeader = new JLabel("Professor sem materias no sistema");
				lblNoMateriaHeader.setFont(customFont12);
				panel.add(lblNoMateriaHeader);
			}

			for (String materia : materias) {
				JLabel lblMateria = new JLabel(materia);
				lblMateria.setFont(customFont12);
				panel.add(lblMateria);
				
				MyButton btnView = new MyButton();
				btnView.setFont(customFont12);
				btnView.setBackground(new Color(227, 227, 227));
				btnView.setColorOver(new Color (217, 217, 217));
				btnView.setText("Ver");
				btnView.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							mostrarInfoEstudante(materia);
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				});

				btnView.setSize(30,30);
				panel.add(btnView);
			}

			for (int i = materias.size(); i < gridSize; i++) {
				panel.add(new JLabel(""));
				panel.add(new JLabel(""));
			}

			MyButton btnSair = new MyButton();
			btnSair.setText("Sair");
			btnSair.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					voltarParaTelaDeLogin();
				}
			});
			btnSair.setFont(customFont12);
			panel.add(new JLabel(""));
			panel.add(btnSair);

        } catch (Exception e) {
            e.printStackTrace();
        }

        revalidate();
        repaint();
    }

    private void mostrarInfoEstudante(String materia) throws SQLException {
        selectedMateria = materia;
        idSelectedMateria = conexao.obteIdMateriaPorNome(selectedMateria);

        setTitle("Materia: " + materia);

        if (panel != null) {
            getContentPane().remove(panel);
            revalidate();
            repaint();
        }

        MyButton btnBack = new MyButton();
        btnBack.setBackground(new Color(227, 227, 227));
        btnBack.setColorOver(new Color (217, 217, 217));

        btnBack.setText("Voltar");
        btnBack.setBounds(39, 50, 80, 20);
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getContentPane().remove(table);
				getContentPane().remove((JButton) e.getSource());
				getContentPane().remove(panel);
				getContentPane().revalidate();
				getContentPane().repaint();
				try {
					exibirInformacoesProfessor(conexao.obterMateriasPorProfessor(conexao.loginAutenticado));
					getContentPane().setBackground(Color.WHITE);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});

        getContentPane().add(btnBack);

        String[] columnHeaders = {"ID", "Aluno", "Faltas", "1º Bimestre", "2º Bimestre", "3º Bimestre", "4º Bimestre", "Exame", "Editar"};
        tableModel = new DefaultTableModel(columnHeaders, 0) {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        table = new JTable(tableModel);
        table.setBackground(Color.WHITE);
        
        
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
            List<Integer> alunos = conexao.obterAlunosPorMateria(materia);
            for (Integer alunoId : alunos) {
                List<Object> alunoInfo = conexao.obterAlunoInfoPorId(alunoId, idSelectedMateria);

                Object[] rowData = new Object[9];

                for (int i = 0; i <= 7; i++) {
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

                rowData[8] = new JButton("Editar");
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		table.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField(), table, conexao, idSelectedMateria));


		double[] averages = new double[6];
		int studentCount = tableModel.getRowCount();

		if (studentCount > 0) {
			for (int row = 0; row < studentCount; row++) {
				for (int col = 2; col <= 7; col++) {
					Object value = tableModel.getValueAt(row, col);
					double numericValue = value instanceof Number ? ((Number) value).doubleValue() : 0;
					averages[col - 2] += numericValue;
				}
			}

			for (int i = 0; i < averages.length; i++) {
				averages[i] /= studentCount;
			}

			Object[] averageRow = new Object[9];
			averageRow[0] = "Media";
			averageRow[1] = "Turma";
			for (int i = 0; i < averages.length; i++) {
				averageRow[i + 2] = String.format("%.2f", averages[i]);
			}
			averageRow[8] = "";

			tableModel.addRow(averageRow);
		}
    }

    private void voltarParaTelaDeLogin() {
        this.dispose();
        Login.showWindow();
    }
}
