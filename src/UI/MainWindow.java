package UI;

import javax.swing.*;
import javax.swing.table.*;

import DB.Conexao;
import DB.Par;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private Conexao conexao;
    private String nomeUsuario;
    private List<String> materias;
    private String selectedMateria;
    private int idSelectedMateria;

    private JPanel panel;
    private DefaultTableModel tableModel;
    private JTable table;

    public MainWindow(Conexao conexao) {
        this.conexao = conexao;
        initialize();
    }

    private void initialize() {
		this.setBounds(100, 100, 800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setLayout(null);

		nomeUsuario = "";
		try {
			nomeUsuario = conexao.obterNomePorLogin(conexao.loginAutenticado);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			int tipoUsuario = conexao.verificarTipoUsuario(conexao.loginAutenticado);

			if (tipoUsuario == 3) {
				exibirInformacoesAdministrador();
			} else if (tipoUsuario == 2) {
				materias = conexao.obterMateriasPorProfessor(conexao.loginAutenticado);
				exibirInformacoesProfessor(materias);
			} else if (tipoUsuario == 1) {
				materias = conexao.obterMateriasPorAluno(conexao.loginAutenticado);
				exibirInformacoesAluno(materias);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    
	private void exibirInformacoesAdministrador() {
		getContentPane().removeAll();
	
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		getContentPane().add(panel);

        setTitle("Area do Administrador");
	
		JLabel lblInformacoes = new JLabel("Informacoes");
		lblInformacoes.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblInformacoes, BorderLayout.NORTH);
	
		JPanel buttonPanel = new JPanel(new FlowLayout());
	
		JButton btnAlunos = new JButton("Alunos");
		btnAlunos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarTabelaAlunos();
			}
		});
		buttonPanel.add(btnAlunos);
	
		JButton btnProfessores = new JButton("Professores");
		btnProfessores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarTabelaProfessores();
			}
		});
		buttonPanel.add(btnProfessores);
	
		panel.add(buttonPanel, BorderLayout.CENTER);

		this.setContentPane(panel);
	
		revalidate();
		repaint();
	}

	private void mostrarTabelaAlunos() {
        setTitle("Informacao dos Alunos");

		List<Par<Integer, String>> alunos = null;
		try {
			alunos = conexao.mostrarTodosAlunos();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		JPanel alunoPanel = new JPanel();
		alunoPanel.setLayout(new BorderLayout());
		alunoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton btnBack = new JButton("Voltar");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exibirInformacoesAdministrador();
			}
		});
		controlPanel.add(btnBack);
	
		JLabel lblAlunos = new JLabel("Lista dos alunos");
		controlPanel.add(lblAlunos);
	
		alunoPanel.add(controlPanel, BorderLayout.NORTH);
	
		JPanel tableHeaderPanel = new JPanel(new GridLayout(1, 3));
		tableHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		tableHeaderPanel.add(new JLabel("ID"));
		tableHeaderPanel.add(new JLabel("Nome"));
		tableHeaderPanel.add(new JLabel("Detalhes"));
	

		JPanel tableRowsPanel = new JPanel();
		tableRowsPanel.setLayout(new BoxLayout(tableRowsPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(tableRowsPanel);
		alunoPanel.add(scrollPane, BorderLayout.CENTER);

		tableRowsPanel.add(tableHeaderPanel);
	

		for (Par<Integer, String> aluno : alunos) {
			JPanel rowPanel = new JPanel(new GridLayout(1, 3));
			rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
			rowPanel.add(new JLabel(aluno.getFirst().toString()));
			rowPanel.add(new JLabel(aluno.getSecond()));
	

			JButton viewButton = new JButton("Ver");
			int alunoId = aluno.getFirst();
			String alunoName = aluno.getSecond();
			viewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						mostrarTabelaMateriasAluno(alunoId, alunoName);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			});
			rowPanel.add(viewButton);
			tableRowsPanel.add(rowPanel);
		}
	
		panel.removeAll();
		panel.add(alunoPanel);
		panel.revalidate();
		panel.repaint();
	}
	
	private void mostrarTabelaMateriasAluno(int alunoId, String alunoName) throws SQLException {
        setTitle("Atribuicao de Materias");

		List<String> materias = null;
		try {
			materias = conexao.obterTodasAsMaterias();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		List<String> materiasDoAluno = null;
		try {
			materiasDoAluno = conexao.obterMateriasPorIdAluno(alunoId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		JPanel materiaPanel = new JPanel();
		materiaPanel.setLayout(new BorderLayout());
		materiaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton btnBack = new JButton("Voltar");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarTabelaAlunos();
			}
		});
		controlPanel.add(btnBack);
		materiaPanel.add(controlPanel, BorderLayout.NORTH);

		JLabel lblAlunos = new JLabel("Materias do aluno: " + alunoName);
		controlPanel.add(lblAlunos);
	
		JPanel tableHeaderPanel = new JPanel(new GridLayout(1, 3));
		tableHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		tableHeaderPanel.add(new JLabel("ID"));
		tableHeaderPanel.add(new JLabel("Nome"));
		tableHeaderPanel.add(new JLabel("Operacao"));
	
		JPanel tableRowsPanel = new JPanel();
		tableRowsPanel.setLayout(new BoxLayout(tableRowsPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(tableRowsPanel);
		materiaPanel.add(scrollPane, BorderLayout.CENTER);

		tableRowsPanel.add(tableHeaderPanel);
	
		for (String materia : materias) {
			JPanel rowPanel = new JPanel(new GridLayout(1, 3));
			rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
			rowPanel.add(new JLabel(String.valueOf(conexao.obteIdMateriaPorNome(materia))));
			rowPanel.add(new JLabel(materia));
	

			JButton operationButton;
			if (materiasDoAluno.contains(materia)) {
				operationButton = new JButton("Remover");
			} else {
				operationButton = new JButton("Adicionar");
			}
	
			int materiaId = conexao.obteIdMateriaPorNome(materia);
			operationButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if (operationButton.getText().equals("Adicionar")) {
							conexao.adicionarRelacaoAlunoMateria(alunoId, materiaId);
						} else {
							conexao.removerRelacaoAlunoMateria(alunoId, materiaId);
						}
						mostrarTabelaMateriasAluno(alunoId, alunoName);
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			});
	
			rowPanel.add(operationButton);
			tableRowsPanel.add(rowPanel);
		}
	
		panel.removeAll();
		panel.add(materiaPanel);
		panel.revalidate();
		panel.repaint();
	}
	
	
	private void mostrarTabelaProfessores() {
        setTitle("Informacao dos Professores");

		List<Par<Integer, String>> professores = null;
		try {
			professores = conexao.mostrarTodosProfessores();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		JPanel professorPanel = new JPanel();
		professorPanel.setLayout(new BorderLayout());
		professorPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
	
		// Botao voltar
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Alinhamento para a esquerda
		JButton btnBack = new JButton("Voltar");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exibirInformacoesAdministrador();
			}
		});
		controlPanel.add(btnBack);
	
		// Label lista dos professores
		JLabel lblProfessores = new JLabel("Lista dos professores");
		controlPanel.add(lblProfessores);

		professorPanel.add(controlPanel, BorderLayout.NORTH);
	
		// Header da tabela
		JPanel tableHeaderPanel = new JPanel(new GridLayout(1, 3));
		tableHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Altura de cada linha
		tableHeaderPanel.add(new JLabel("ID"));
		tableHeaderPanel.add(new JLabel("Nome"));
		tableHeaderPanel.add(new JLabel("Detalhes"));
	
		// Painel da tabela
		JPanel tableRowsPanel = new JPanel();
		tableRowsPanel.setLayout(new BoxLayout(tableRowsPanel, BoxLayout.Y_AXIS)); // Alinhar linhas verticalmente
		JScrollPane scrollPane = new JScrollPane(tableRowsPanel); // Tabela fica scrollavel caso necessario
		professorPanel.add(scrollPane, BorderLayout.CENTER);

		tableRowsPanel.add(tableHeaderPanel);
	
		// Preencher a tabela com as informacoes
		for (Par<Integer, String> professor : professores) {
			JPanel rowPanel = new JPanel(new GridLayout(1, 3));
			rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
			rowPanel.add(new JLabel(professor.getFirst().toString()));
			rowPanel.add(new JLabel(professor.getSecond()));
	
			// Botao para cada linha de professor
			JButton viewButton = new JButton("Ver");
			int professorId = professor.getFirst();
			String professorName = professor.getSecond();
			viewButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// Logica de clicar no botao
					// Passa o ID e o nome do professor para a funcao mostrarTabelaMateriasProfesor()
					try {
						mostrarTabelaMateriasProfessor(professorId, professorName);
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			});
			rowPanel.add(viewButton);
			tableRowsPanel.add(rowPanel);
		}
	
		// Troca o painel anterior com este dos professores
		panel.removeAll();
		panel.add(professorPanel);
		panel.revalidate();
		panel.repaint();
	}

	private void mostrarTabelaMateriasProfessor(int professorId, String professorName) throws SQLException {
        setTitle("Atribuicao de Materias");

		// Pega todas as materias do banco
		List<String> materias = null;
		try {
			materias = conexao.obterTodasAsMaterias();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		// Pega todas as materias associadas com este professor do banco
		List<String> materiasDoProfessor = null;
		try {
			materiasDoProfessor = conexao.obterMateriasPorIdProfessor(professorId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		// Painel de informacoes das materias
		JPanel materiaPanel = new JPanel();
		materiaPanel.setLayout(new BorderLayout());
		materiaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
	
		// Botao voltar
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Alinhamento para esquerda
		JButton btnBack = new JButton("Voltar");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mostrarTabelaProfessores();
			}
		});
		controlPanel.add(btnBack);
		materiaPanel.add(controlPanel, BorderLayout.NORTH);

		// Label da lista das materias
		JLabel lblProfessor = new JLabel("Materias do aluno: " + professorName);
		controlPanel.add(lblProfessor);
	
		// Header da tabela
		JPanel tableHeaderPanel = new JPanel(new GridLayout(1, 3));
		tableHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		tableHeaderPanel.add(new JLabel("ID"));
		tableHeaderPanel.add(new JLabel("Nome"));
		tableHeaderPanel.add(new JLabel("Operacao"));
	
		// Painel da tabela
		JPanel tableRowsPanel = new JPanel();
		tableRowsPanel.setLayout(new BoxLayout(tableRowsPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(tableRowsPanel);
		materiaPanel.add(scrollPane, BorderLayout.CENTER);

		tableRowsPanel.add(tableHeaderPanel);
	
		// Preencher tabela com informacoes
		for (String materia : materias) {
			JPanel rowPanel = new JPanel(new GridLayout(1, 3));
			rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
			// Obter ID da materia do banco
			int materiaId = conexao.obteIdMateriaPorNome(materia);

			rowPanel.add(new JLabel(String.valueOf(materiaId)));
			rowPanel.add(new JLabel(materia));

			// Botao para cada materia dependendo se esta associada ou nao
			JButton operationButton;
			if (materiasDoProfessor.contains(materia)) {
				operationButton = new JButton("Remover");
			} else {
				operationButton = new JButton("Adicionar");
			}
	
			operationButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if (operationButton.getText().equals("Adicionar")) {
							conexao.adicionarRelacaoProfessorMateria(professorId, materiaId);
						} else {
							conexao.removerRelacaoProfessorMateria(professorId, materiaId);
						}
						// Atualizar a UI para mostrar mudancas
						mostrarTabelaMateriasAluno(professorId, professorName);
					} catch (SQLException ex) {
						ex.printStackTrace();
					}
				}
			});
	
			rowPanel.add(operationButton);
			tableRowsPanel.add(rowPanel);
		}
	
		// Troca o painel anterior com este das materias
		panel.removeAll();
		panel.add(materiaPanel);
		panel.revalidate();
		panel.repaint();
	}

    private void exibirInformacoesProfessor(List<String> materias) {
    	this.getContentPane().setBackground(Color.WHITE);
        int gridSize = materias.size();
        System.out.println(gridSize);

        setTitle("Area do Professor");

        if (gridSize < 2) {
            gridSize = 2;
        }
        getContentPane().removeAll();
        panel = new JPanel();
        panel.setLayout(new GridLayout(gridSize + 2, 2));
        panel.setBounds(39, 50, 300, gridSize * 30);
        getContentPane().add(panel);

        try {
        	File fontFileAreaAluno = new File("src\\SF-Pro-Display-Regular.otf");
            Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
            GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
            geAreaLogin.registerFont(customFontAreaLogin);

            Font customFont12 = customFontAreaLogin.deriveFont(12f); // Reduzindo o tamanho da fonte para 12 pixels

            JLabel lblWelcomeP = new JLabel("Bem-vindo, " + nomeUsuario + "!");
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
            btnView.addActionListener(new ViewButtonActionListener(materia));
            btnView.setSize(30,30);
            panel.add(btnView);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        revalidate();
        repaint();
    }

    private void displayStudentInformation(String materia) throws SQLException {
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
        btnBack.addActionListener(new BackButtonActionListener());
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
                System.out.println(rowData[0]);

                for (int i = 0; i <= 7; i++) {
                    rowData[i] = (alunoInfo.get(i) != null) ? alunoInfo.get(i) : "-";
                }
                rowData[8] = new JButton("Editar");
                tableModel.addRow(rowData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.getColumn("Editar").setCellRenderer(new ButtonRenderer());
        table.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField()));
    }

    private void exibirInformacoesAluno(List<String> materias) throws SQLException {
        getContentPane().removeAll();
		this.getContentPane().setBackground(Color.WHITE);
        
        
        setTitle("Area do Aluno");
        
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

            JLabel lblWelcome = new JLabel("Bem-vindo, " + nomeUsuario + "!");
            lblWelcome.setFont(customFont12);
            lblWelcome.setBounds(40, 65, 250, 24); // Ajustando a largura para acomodar o texto
            lblWelcome.setForeground(new Color(29, 29, 31));
            getContentPane().add(lblWelcome);
        } catch (Exception e) {
            e.printStackTrace();
        }

		try {
			File fontFileUsuarioSenha = new File(
					"src\\SF-Pro-Display-Regular.otf");
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
                System.out.println(rowData[0]);

                for (int i = 0; i <= 7; i++) {
                    rowData[i] = (alunoInfo.get(i) != null) ? alunoInfo.get(i) : "-";
                }
                rowData[1] = materia;
                tableModel.addRow(rowData);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		if (conexao.notificacaoIsTrue(nomeUsuario)) {
            System.out.println("Notificacoes on");
            int id = (conexao.obterIdAluno(nomeUsuario));
            String nome_materia = conexao.obterNotificacoesNaoLidas(id);
            exibirPopup("A Matéria de " + nome_materia + " foi alterada");
            conexao.resetar_notificacao();
        }
    }

    private class ViewButtonActionListener implements ActionListener {
        private String materia;

        public ViewButtonActionListener(String materia) {
            this.materia = materia;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                displayStudentInformation(materia);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    private class BackButtonActionListener implements ActionListener {
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
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
		private static final long serialVersionUID = 1L;

		public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Salvar");
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
		private static final long serialVersionUID = 1L;
		private JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JTextField textField) {
            super(textField);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(UIManager.getColor("Button.background"));
            }

            label = "Salvar";
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int idAluno = (int) parseValue(table.getValueAt(table.getSelectedRow(), 0));
                int faltas = (int) parseValue(table.getValueAt(table.getSelectedRow(), 2));
                double grade1 = parseValue(table.getValueAt(table.getSelectedRow(), 3));
                double grade2 = parseValue(table.getValueAt(table.getSelectedRow(), 4));
                double grade3 = parseValue(table.getValueAt(table.getSelectedRow(), 5));
                double grade4 = parseValue(table.getValueAt(table.getSelectedRow(), 6));
                double finalExam = parseValue(table.getValueAt(table.getSelectedRow(), 7));

                if(grade1<0 || grade1>10 || grade2<0 || grade2>10 || grade3<0 || grade3>10 || grade4<0 || grade4>10){
                    exibirPopup("Erro! Notas devem ser de 1 a 10!");
                }else{    
                    try {
                        conexao.atualizarInfoAluno(idAluno, idSelectedMateria, faltas, grade1, grade2, grade3, grade4, finalExam);
						conexao.inserirNotificacao(idAluno,idSelectedMateria);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                
            }
            isPushed = false;
            return label;
        }

        private double parseValue(Object value) {
            if (value instanceof String && ((String) value).equals("-")) {
                return 0;
            }
            return (value == null) ? Double.NaN : Double.parseDouble(value.toString());
        }
    }

    private void exibirPopup(String mensagem) {
        JFrame popupFrame = new JFrame();
        popupFrame.setUndecorated(true);  // Remove a barra de título e borda
        popupFrame.setBackground(new Color(0, 0, 0, 0));  // Define o fundo como transparente

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);  // Cor de fundo do painel flutuante
        panel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 5));  // Borda do painel
        panel.setPreferredSize(new Dimension(300, 150));  // Define o tamanho do painel
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(mensagem);
        label.setHorizontalAlignment(JLabel.CENTER);  // Centraliza o texto
        Font fontePersonalizada = new Font("Arial Black", Font.PLAIN, 10);
        label.setFont(fontePersonalizada);

        panel.add(label, BorderLayout.CENTER);
        popupFrame.add(panel);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - panel.getPreferredSize().width - 600;  // Ajuste o 20 conforme necessário
        int y = screenSize.height - panel.getPreferredSize().height - 200;  // Ajuste o 20 conforme necessário
        popupFrame.setLocation(x, y);

        popupFrame.setSize(panel.getPreferredSize());
        popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Defina um temporizador para fechar automaticamente após alguns segundos
        Timer timer = new Timer(5000, e -> popupFrame.dispose());  // 5000 milissegundos (5 segundos)
        timer.setRepeats(false);  // Execute apenas uma vez
        timer.start();

        popupFrame.setAlwaysOnTop(true);
        popupFrame.setVisible(true);
    }
}
