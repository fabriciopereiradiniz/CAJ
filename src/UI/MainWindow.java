package UI;

import javax.swing.*;
import javax.swing.table.*;

import DB.Conexao;
import DB.Par;
import entities.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private Conexao conexao;
	private Pessoa usuario;
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
				exibirInformacoesProfessor(usuario.getMaterias());
			} else if (tipoUsuario == 1) {
				usuario = new Aluno(nomeUsuario);
				usuario.setMaterias(conexao.obterMateriasPorAluno(conexao.loginAutenticado));
				exibirInformacoesAluno(usuario.getMaterias());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    
    private void exibirInformacoesAdministrador() {
        getContentPane().removeAll();
        this.getContentPane().setBackground(Color.WHITE);
        this.setBackground(Color.WHITE); // teste
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        getContentPane().add(panel);

        setTitle("Area do Administrador");
        try {
            File fontFileAreaAluno = new File("src\\\\SF-Pro-Display-Regular.otf");
            Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
            GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
            geAreaLogin.registerFont(customFontAreaLogin);

            Font customFont12 = customFontAreaLogin.deriveFont(16f);

            Font fonteSetter = customFont12;
            JLabel lblInformacoes = new JLabel("Ações de administrador");
            Font customFont50 = customFontAreaLogin.deriveFont(128f); 
            lblInformacoes.setFont(fonteSetter);
            lblInformacoes.setForeground(Color.WHITE);
            lblInformacoes.setHorizontalAlignment(SwingConstants.CENTER);
            panel.add(lblInformacoes, BorderLayout.NORTH);

        panel.setBackground(Color.decode("#3995f7"));

        // Placeholders de contador
        JPanel counterPanel = new JPanel(new GridLayout(1, 3, 10, 0)); 
        counterPanel.setBackground(Color.WHITE);
        counterPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); 

        RoundedPanel professorCounter = new RoundedPanel(10); 
        professorCounter.setPreferredSize(new Dimension(100, 100)); 
        professorCounter.setLayout(new GridLayout(2, 1)); 
        professorCounter.setBackground(Color.decode("#e3e3e3"));
        professorCounter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10), 
                BorderFactory.createLineBorder(Color.WHITE, 1) 
        ));
        JLabel lblProfessores = new JLabel();
        lblProfessores.setText(String.valueOf(conexao.contarProfessores()));
        lblProfessores.setFont(customFont50);
        lblProfessores.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblProfessoresText = new JLabel("Professores registrados");
        lblProfessoresText.setHorizontalAlignment(SwingConstants.CENTER);
        professorCounter.add(lblProfessores);
        professorCounter.add(lblProfessoresText);
        counterPanel.add(professorCounter);

        // Alunos registrados
        RoundedPanel alunoCounter =  new RoundedPanel(10);
        alunoCounter.setPreferredSize(new Dimension(100, 100)); 
        alunoCounter.setLayout(new GridLayout(2, 1)); 
        alunoCounter.setBackground(Color.decode("#e3e3e3"));
        alunoCounter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10), 
                BorderFactory.createLineBorder(Color.WHITE, 1) 
        ));
        JLabel lblAlunos = new JLabel();
        lblAlunos.setText(String.valueOf(conexao.contarAlunos()));
        lblAlunos.setFont(customFont50);
        lblAlunos.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblAlunosText = new JLabel("Alunos registrados");
        lblAlunosText.setHorizontalAlignment(SwingConstants.CENTER);
        alunoCounter.add(lblAlunos);
        alunoCounter.add(lblAlunosText);
        counterPanel.add(alunoCounter);

        RoundedPanel materiaCounter =  new RoundedPanel(10);
        materiaCounter.setPreferredSize(new Dimension(100, 100)); 
        materiaCounter.setLayout(new GridLayout(2, 1)); 
        materiaCounter.setBackground(Color.decode("#e3e3e3"));
        materiaCounter.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 10, 5, 10), 
                BorderFactory.createLineBorder(Color.WHITE, 1) 
        ));
        JLabel lblMaterias = new JLabel();
        lblMaterias.setText(String.valueOf(conexao.contarMaterias()));
        lblMaterias.setFont(customFont50);
        lblMaterias.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel lblMateriasText = new JLabel("Matérias registradas");
        lblMateriasText.setHorizontalAlignment(SwingConstants.CENTER);
        materiaCounter.add(lblMaterias);
        materiaCounter.add(lblMateriasText);
        counterPanel.add(materiaCounter);

        panel.add(counterPanel, BorderLayout.CENTER);





        panel.add(counterPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);

        MyButton btnAlunos = new MyButton();
        btnAlunos.setText("Alunos");
        btnAlunos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTabelaAlunos();
            }
        });
        buttonPanel.add(btnAlunos);

        MyButton btnProfessores = new MyButton();
        btnProfessores.setText("Professores");
        btnProfessores.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTabelaProfessores();
            }
        });
        buttonPanel.add(btnProfessores);

        MyButton btnMaterias = new MyButton();
        btnMaterias.setText("Matérias");
        btnMaterias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarTabelaMaterias();
            }
        });
        buttonPanel.add(btnMaterias);

        MyButton btnSair = new MyButton();
        btnSair.setText("Sair");
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                voltarParaTelaDeLogin();
            }
        });
        buttonPanel.add(btnSair);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG");
        }
        this.setContentPane(panel);
        revalidate();
        repaint();
    }



	private void voltarParaTelaDeLogin() {
		this.dispose();
		Login.showWindow();
	}

	private void mostrarTabelaAlunos() {
        setTitle("Informacao dos Alunos");
		this.getContentPane().setBackground(Color.WHITE);
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
		this.setBackground(Color.WHITE); // teste
		controlPanel.setBackground(Color.WHITE);
		MyButton btnBack = new MyButton();
		btnBack.setText("Voltar");
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exibirInformacoesAdministrador();
			}
		});
		controlPanel.add(btnBack);
		JLabel lblAlunos = new JLabel("Lista dos alunos");
		controlPanel.add(lblAlunos);
		controlPanel.setBackground(Color.WHITE);
		alunoPanel.add(controlPanel, BorderLayout.NORTH);
		alunoPanel.setBackground(Color.WHITE);
		JPanel tableHeaderPanel = new JPanel(new GridLayout(1, 3));
		tableHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        try {
            File fontFileAreaAluno = new File("src\\\\SF-Pro-Display-Regular.otf");
            Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
            GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
            geAreaLogin.registerFont(customFontAreaLogin);

            Font customFont12 = customFontAreaLogin.deriveFont(16f); // Reduzindo o tamanho da fonte para 16 pixels

            // Criar uma fonte personalizada para os cabeçalhos
            Font fonteSetter = customFont12;
            lblAlunos.setFont(fonteSetter);
         // Criação das labels para os cabeçalhos da tabela
            JLabel labelID = new JLabel("ID");
            JLabel labelNome = new JLabel("Nome");
            JLabel labelDetalhes = new JLabel("Detalhes");

            // Adição das labels ao tableHeaderPanel
            tableHeaderPanel.add(labelID);
            tableHeaderPanel.add(labelNome);
            tableHeaderPanel.add(labelDetalhes);

            // Definição da fonte personalizada para as labels
            for (Component component : tableHeaderPanel.getComponents()) {
                if (component instanceof JLabel) {
                    ((JLabel) component).setFont(fonteSetter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG");
        }

		

		tableHeaderPanel.setBackground(Color.WHITE);

		JPanel tableRowsPanel = new JPanel();
		
		tableRowsPanel.setLayout(new BoxLayout(tableRowsPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(tableRowsPanel);
		alunoPanel.add(scrollPane, BorderLayout.CENTER);

		tableRowsPanel.add(tableHeaderPanel);
	
        try {
            File fontFileAreaAluno = new File("src\\SFUIText-Light.otf");
            Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
            GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
            geAreaLogin.registerFont(customFontAreaLogin);

            Font customFont12 = customFontAreaLogin.deriveFont(16f); // Reduzindo o tamanho da fonte para 16 pixels

            // Criar uma fonte personalizada para os cabeçalhos
            Font fonteSetter = customFont12;
		for (Par<Integer, String> aluno : alunos) {
		    JPanel rowPanel = new JPanel(new GridLayout(1, 3));
		    rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		    
		    // Criação das labels para o ID e nome do aluno
		    JLabel labelID = new JLabel(aluno.getFirst().toString());
		    JLabel labelNome = new JLabel(aluno.getSecond());
		    
		    // Definição da fonte personalizada para as labels
		    labelID.setFont(fonteSetter);
		    labelNome.setFont(fonteSetter);
		    
		    // Adição das labels e botão ao rowPanel
		    rowPanel.add(labelID);
		    rowPanel.add(labelNome);

		    JButton viewButton = new JButton("Ver");
		    int alunoId = aluno.getFirst();
		    String alunoName = aluno.getSecond();
		    viewButton.setFont(fonteSetter);
		    viewButton.setBackground(Color.WHITE);
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
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG");
        }
		panel.setBackground(Color.WHITE);
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
		MyButton btnBack = new MyButton();
		btnBack.setText("Voltar");
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
		controlPanel.setBackground(Color.WHITE);
		materiaPanel.setBackground(Color.WHITE);
		JPanel tableHeaderPanel = new JPanel(new GridLayout(1, 3));
		tableHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		
        try {
            File fontFileAreaAluno = new File("src\\\\SF-Pro-Display-Regular.otf");
            Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
            GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
            geAreaLogin.registerFont(customFontAreaLogin);

            Font customFont12 = customFontAreaLogin.deriveFont(16f); // Reduzindo o tamanho da fonte para 16 pixels

            // Criar uma fonte personalizada para os cabeçalhos
            Font fonteSetter = customFont12;
            lblAlunos.setFont(fonteSetter);
         // Criação das labels para os cabeçalhos da tabela
            JLabel labelID = new JLabel("ID");
            JLabel labelNome = new JLabel("Nome");
            JLabel labelOperacao = new JLabel("Operacao");

            // Adição das labels ao tableHeaderPanel
            tableHeaderPanel.add(labelID);
            tableHeaderPanel.add(labelNome);
            tableHeaderPanel.add(labelOperacao);

            // Definição da fonte personalizada para as labels
            for (Component component : tableHeaderPanel.getComponents()) {
                if (component instanceof JLabel) {
                    ((JLabel) component).setFont(fonteSetter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG");
        }
		
		

		tableHeaderPanel.setBackground(Color.WHITE);
		JPanel tableRowsPanel = new JPanel();
		tableRowsPanel.setLayout(new BoxLayout(tableRowsPanel, BoxLayout.Y_AXIS));
		JScrollPane scrollPane = new JScrollPane(tableRowsPanel);
		materiaPanel.add(scrollPane, BorderLayout.CENTER);

		tableRowsPanel.add(tableHeaderPanel);
	
		try {
		    File fontFileAreaAluno = new File("src\\\\SFUIText-Light.otf");
		    Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
		    GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    geAreaLogin.registerFont(customFontAreaLogin);

		    Font customFont12 = customFontAreaLogin.deriveFont(16f); 


		    Font fonteSetter = customFont12;

		    for (String materia : materias) {
		        JPanel rowPanel = new JPanel(new GridLayout(1, 3));
		        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));


		        JLabel labelMateriaId = new JLabel(String.valueOf(conexao.obteIdMateriaPorNome(materia)));
		        labelMateriaId.setFont(fonteSetter);
		        rowPanel.add(labelMateriaId);

		        JLabel labelMateriaNome = new JLabel(materia);
		        labelMateriaNome.setFont(fonteSetter);
		        rowPanel.add(labelMateriaNome);

		        JButton operationButton;
		        if (materiasDoAluno.contains(materia)) {
		            operationButton = new JButton("Remover");
		        } else {
		            operationButton = new JButton("Adicionar");
		        }

		        operationButton.setFont(fonteSetter);
		        operationButton.setBackground(Color.WHITE);
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
		} catch (Exception e) {
		    e.printStackTrace();
		    System.out.println("DEBUG");
		}

	
		panel.removeAll();
		panel.add(materiaPanel);
		panel.revalidate();
		panel.repaint();
	}
	
	
	private void mostrarTabelaProfessores() {
        setTitle("Informacao dos Professores");
		this.getContentPane().setBackground(Color.WHITE);
		List<Par<Integer, String>> professores = null;
		try {
			professores = conexao.mostrarTodosProfessores();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		JPanel professorPanel = new JPanel();
		professorPanel.setLayout(new BorderLayout());
		professorPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding
		professorPanel.setBackground(Color.WHITE);
		// Botao voltar
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Alinhamento para a esquerda
		MyButton btnBack = new MyButton();
		btnBack.setText("Voltar");
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
		controlPanel.setBackground(Color.WHITE);
		professorPanel.add(controlPanel, BorderLayout.NORTH);
	
		// Header da tabela
		
		JPanel tableHeaderPanel = new JPanel(new GridLayout(1, 3));
		tableHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Altura de cada linha
		tableHeaderPanel.setBackground(Color.WHITE);
        try {
            File fontFileAreaAluno = new File("src\\\\SF-Pro-Display-Regular.otf");
            Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
            GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
            geAreaLogin.registerFont(customFontAreaLogin);

            Font customFont12 = customFontAreaLogin.deriveFont(16f); // Reduzindo o tamanho da fonte para 16 pixels

            // Criar uma fonte personalizada para os cabeçalhos
            Font fonteSetter = customFont12;
            lblProfessores.setFont(fonteSetter);
         // Criação das labels para os cabeçalhos da tabela
            JLabel labelID = new JLabel("ID");
            JLabel labelNome = new JLabel("Nome");
            JLabel labelDetalhes = new JLabel("Detalhes");

            // Adição das labels ao tableHeaderPanel
            tableHeaderPanel.add(labelID);
            tableHeaderPanel.add(labelNome);
            tableHeaderPanel.add(labelDetalhes);

            // Definição da fonte personalizada para as labels
            for (Component component : tableHeaderPanel.getComponents()) {
                if (component instanceof JLabel) {
                    ((JLabel) component).setFont(fonteSetter);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG");
        }
	
		// Painel da tabela
		JPanel tableRowsPanel = new JPanel();
		tableRowsPanel.setLayout(new BoxLayout(tableRowsPanel, BoxLayout.Y_AXIS)); // Alinhar linhas verticalmente
		JScrollPane scrollPane = new JScrollPane(tableRowsPanel); // Tabela fica scrollavel caso necessario
		professorPanel.add(scrollPane, BorderLayout.CENTER);

		tableRowsPanel.add(tableHeaderPanel);
	
		// Preencher a tabela com as informacoes
		try {
		    File fontFileAreaAluno = new File("src\\SFUIText-Light.otf");
		    Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
		    GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    geAreaLogin.registerFont(customFontAreaLogin);

		    Font customFont12 = customFontAreaLogin.deriveFont(16f); // Reduzindo o tamanho da fonte para 16 pixels

		    // Criar uma fonte personalizada para os cabeçalhos
		    Font fonteSetter = customFont12;

		    for (Par<Integer, String> professor : professores) {
		        JPanel rowPanel = new JPanel(new GridLayout(1, 3));
		        rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		        
		        // Criação das labels para o ID e nome do professor
		        JLabel labelID = new JLabel(professor.getFirst().toString());
		        JLabel labelNome = new JLabel(professor.getSecond());
		        
		        // Definição da fonte personalizada para as labels
		        labelID.setFont(fonteSetter);
		        labelNome.setFont(fonteSetter);
		        
		        // Adição das labels e botão ao rowPanel
		        rowPanel.add(labelID);
		        rowPanel.add(labelNome);

		        JButton viewButton = new JButton("Ver");
		        int professorId = professor.getFirst();
		        String professorName = professor.getSecond();
		        viewButton.setBackground(Color.WHITE);
		        viewButton.setFont(fonteSetter);
		        viewButton.addActionListener(new ActionListener() {
		            @Override
		            public void actionPerformed(ActionEvent e) {
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
		} catch (Exception e) {
		    e.printStackTrace();
		    System.out.println("DEBUG");
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
	    MyButton btnBack = new MyButton();
	    btnBack.setText("Voltar");
	    btnBack.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            mostrarTabelaProfessores();
	        }
	    });
	    controlPanel.add(btnBack);
	    materiaPanel.add(controlPanel, BorderLayout.NORTH);
	    materiaPanel.setBackground(Color.WHITE);
	    controlPanel.setBackground(Color.WHITE);

	    // Carregamento e registro da fonte
	    Font customFont = null;
	    Font customFont2 = null;
	    try {
	        File fontFile = new File("src\\SFUIText-Light.otf");
	        customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(16f);
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge.registerFont(customFont);
	        
	        File fontFile2 = new File("src\\SF-Pro-Display-Regular.otf");
	        customFont2 = Font.createFont(Font.TRUETYPE_FONT, fontFile2).deriveFont(16f);
	        GraphicsEnvironment ge2 = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge2.registerFont(customFont2);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    // Label da lista das materias
	    JLabel lblProfessor = new JLabel("Materias do professor: " + professorName);
	    lblProfessor.setFont(customFont2);
	    controlPanel.add(lblProfessor);

	    // Header da tabela
	    JPanel tableHeaderPanel = new JPanel(new GridLayout(1, 3));
	    tableHeaderPanel.setBackground(Color.WHITE);
	    tableHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	    JLabel labelID = new JLabel("ID");
	    JLabel labelNome = new JLabel("Nome");
	    JLabel labelOperacao = new JLabel("Operacao");
	    labelID.setFont(customFont2);
	    labelNome.setFont(customFont2);
	    labelOperacao.setFont(customFont2);
	    tableHeaderPanel.add(labelID);
	    tableHeaderPanel.add(labelNome);
	    tableHeaderPanel.add(labelOperacao);

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

	        JLabel labelMateriaId = new JLabel(String.valueOf(materiaId));
	        labelMateriaId.setFont(customFont);
	        JLabel labelMateriaNome = new JLabel(materia);
	        labelMateriaNome.setFont(customFont);

	        rowPanel.add(labelMateriaId);
	        rowPanel.add(labelMateriaNome);

	        // Botao para cada materia dependendo se esta associada ou nao
	        JButton operationButton;

	        if (materiasDoProfessor.contains(materia)) {
	            operationButton = new JButton("Remover");
	        } else {
	            operationButton = new JButton("Adicionar");
	        }
	        operationButton.setBackground(Color.WHITE);
	        operationButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                try {
	                    if (operationButton.getText().equals("Adicionar")) {
	                        conexao.adicionarRelacaoProfessorMateria(professorId, materiaId);
	                    } else {
	                        conexao.removerRelacaoProfessorMateria(professorId, materiaId);
	                    }
	                    mostrarTabelaMateriasProfessor(professorId, professorName);
	                } catch (SQLException ex) {
	                    ex.printStackTrace();
	                }
	            }
	        });

	        operationButton.setFont(customFont);

	        rowPanel.add(operationButton);
	        tableRowsPanel.add(rowPanel);
	    }

	    // Troca o painel anterior com este das materias
	    panel.removeAll();
	    panel.add(materiaPanel);
	    panel.revalidate();
	    panel.repaint();
	}



	private void mostrarTabelaMaterias() {
	    setTitle("Informação das Matérias");

	    List<Par<Integer, String>> materias = null;
	    try {
	        materias = conexao.obterTodasAsMateriasComId();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    JPanel materiaPanel = new JPanel();
	    materiaPanel.setLayout(new BorderLayout());
	    materiaPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    materiaPanel.setBackground(Color.WHITE);
	    JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	    MyButton btnBack = new MyButton();
	    btnBack.setText("Voltar");
	    btnBack.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            exibirInformacoesAdministrador();
	        }
	    });
	    controlPanel.add(btnBack);

	    JLabel lblMaterias = new JLabel("Lista das matérias");
	    controlPanel.add(lblMaterias);
	    controlPanel.setBackground(Color.WHITE);

	    materiaPanel.add(controlPanel, BorderLayout.NORTH);

	    JPanel tableHeaderPanel = new JPanel(new GridLayout(1, 3));
	    tableHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	    tableHeaderPanel.setBackground(Color.WHITE);
	    try {
	        File fontFile = new File("src\\SF-Pro-Display-Regular.otf");
	        Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge.registerFont(customFont);

	        Font customFont12 = customFont.deriveFont(16f); // Reduzindo o tamanho da fonte para 16 pixels

	        // Criar uma fonte personalizada
	        Font fonteSetter = customFont12;

	        // Definir a fonte para o label "Lista das Matérias"
	        lblMaterias.setFont(fonteSetter);

	        // Criação das labels para os cabeçalhos da tabela
	        JLabel labelID = new JLabel("ID");
	        JLabel labelNome = new JLabel("Nome");
	        JLabel labelOperacao = new JLabel("Operação");

	        // Definir a fonte personalizada para as labels do cabeçalho
	        labelID.setFont(fonteSetter);
	        labelNome.setFont(fonteSetter);
	        labelOperacao.setFont(fonteSetter);

	        // Adicionar as labels ao tableHeaderPanel
	        tableHeaderPanel.add(labelID);
	        tableHeaderPanel.add(labelNome);
	        tableHeaderPanel.add(labelOperacao);

	        // Definir a fonte personalizada para as labels de cada linha da tabela
	        for (Component component : tableHeaderPanel.getComponents()) {
	            if (component instanceof JLabel) {
	                ((JLabel) component).setFont(fonteSetter);
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("DEBUG");
	    }

	    JPanel tableRowsPanel = new JPanel();
	    tableRowsPanel.setLayout(new BoxLayout(tableRowsPanel, BoxLayout.Y_AXIS));
	    JScrollPane scrollPane = new JScrollPane(tableRowsPanel);
	    materiaPanel.add(scrollPane, BorderLayout.CENTER);

	    tableRowsPanel.add(tableHeaderPanel);
	    
	    
	    try {
	        File fontFileAreaAluno = new File("src\\SFUIText-Light.otf");
	        Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
	        GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        geAreaLogin.registerFont(customFontAreaLogin);

	        Font customFont12 = customFontAreaLogin.deriveFont(16f); // Reduzindo o tamanho da fonte para 16 pixels

	        // Criar uma fonte personalizada para os cabeçalhos
	        Font fonteSetter = customFont12;


	        for (Par<Integer, String> materia : materias) {
	            JPanel rowPanel = new JPanel(new GridLayout(1, 3));
	            rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	            
	            JLabel idLabel = new JLabel(materia.getFirst().toString()); // JLabel para o ID da matéria
	            idLabel.setFont(fonteSetter); // Aplicando a fonte personalizada
	            
	            JLabel materiaLabel = new JLabel(materia.getSecond());
	            
	            materiaLabel.setFont(fonteSetter); // Aplicando a fonte personalizada para o nome da matéria
	            
	            rowPanel.add(idLabel); // Adicionando o JLabel do ID
	            rowPanel.add(materiaLabel); // Adicionando o JLabel do nome da matéria

	            JButton deleteButton = new JButton("Deletar");
	            deleteButton.setFont(customFont12);
	            deleteButton.setBackground(Color.WHITE);
	            deleteButton.addActionListener(new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    int response = JOptionPane.showConfirmDialog(null, "Você tem certeza que quer deletar a matéria " + materia.getSecond() + "?", "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	                    if (response == JOptionPane.YES_OPTION) {
	                        try {
	                            conexao.deletarMateria(materia.getFirst());
	                            // Opcionalmente, atualize a lista de matérias aqui
	                            mostrarTabelaMaterias();
	                        } catch (SQLException ex) {
	                            ex.printStackTrace();
	                            JOptionPane.showMessageDialog(null, "Erro deletando a matéria: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
	                        }
	                    }
	                }
	            });
	            rowPanel.add(deleteButton);

	            tableRowsPanel.add(rowPanel);
	        }

	        
			JPanel rowPanel = new JPanel(new GridLayout(1, 3));
			rowPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

			rowPanel.add(new JLabel(""));

			JTextField newMateriaName = new JTextField();
			rowPanel.add(newMateriaName);

			JButton createButton = new JButton("Criar");
			createButton.setFont(fonteSetter);
			createButton.setBackground(Color.WHITE);
			createButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String nomeMateria = newMateriaName.getText();
					if (!nomeMateria.trim().isEmpty()) {
						try {
							boolean created = conexao.criarMateria(nomeMateria);
							if (created) {
								mostrarTabelaMaterias();
							} else {
								JOptionPane.showMessageDialog(null, "Nome da matéria já existe!", "Erro", JOptionPane.ERROR_MESSAGE);
							}
						} catch (SQLException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(null, "Erro ao criar a matéria: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Nome da matéria não pode ser vazio!", "Erro", JOptionPane.WARNING_MESSAGE);
					}
				}
			});
			rowPanel.add(createButton);

			tableRowsPanel.add(rowPanel);
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("DEBUG");
	    }


	
	


	
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
        panel.setLayout(new GridLayout(gridSize + 3, 2));
        panel.setBounds(39, 50, 300, gridSize * 30);
        getContentPane().add(panel);

        try {
        	File fontFileAreaAluno = new File("src\\SF-Pro-Display-Regular.otf");
            Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
            GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
            geAreaLogin.registerFont(customFontAreaLogin);

            Font customFont12 = customFontAreaLogin.deriveFont(12f); // Reduzindo o tamanho da fonte para 12 pixels

            JLabel lblWelcomeP = new JLabel("Bem-vindo, " + usuario.getNome() + "!");
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

                for (int i = 0; i <= 7; i++) {
                    Object value = (alunoInfo.get(i) != null) ? alunoInfo.get(i) : "-";
                    rowData[i] = value;

                    if ((i == 3 || i == 4 || i == 5 || i == 6) && value instanceof Number && ((Number) value).doubleValue() < 70.0) {
                        // Se a nota for menor que 7.0 e estiver nos índices 3, 4, 5 ou 6, definimos a cor vermelha
                        table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                            /**
							 * 
							 */
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
        table.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField()));

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

            JLabel lblWelcome = new JLabel("Bem-vindo, " + usuario.getNome() + "!");
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
				rowData[0] = idMateriaAtual; // Sets the id_materia in the ID column

                for (int i = 1; i <= 7; i++) {
                    Object value = (alunoInfo.get(i) != null) ? alunoInfo.get(i) : "-";
                    rowData[i] = value;

                    if ((i == 3 || i == 4 || i == 5 || i == 6) && value instanceof Number && ((Number) value).doubleValue() < 70.0) {
                        // Se a nota for menor que 7.0 e estiver nos índices 3, 4, 5 ou 6, definimos a cor vermelha
                        table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                            /**
							 * 
							 */
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
					// Set your message in a specific cell, e.g., the second cell
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
		btnSairAluno.setBounds(39, 510, 80, 20); // Adjust position and size as needed
		getContentPane().add(btnSairAluno);


		if (conexao.notificacaoIsTrue(usuario.getNome())) {
            System.out.println("Notificacoes on");
            int id = (conexao.obterIdAluno(usuario.getNome()));
            String nome_materia = conexao.obterNotificacoesNaoLidas(id);
            exibirPopup("Alterações em " + nome_materia + ".");
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
	    this.setBackground(Color.WHITE);
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
                    exibirPopup("Erro! Notas devem ser de 0 a 10!");
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
        panel.setBackground(Color.RED);  // Cor de fundo do painel flutuante
        panel.setPreferredSize(new Dimension(300, 150));  // Define o tamanho do painel
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(mensagem);
        label.setHorizontalAlignment(JLabel.CENTER);  // Centraliza o texto
        
        try {
            File fontFileAreaAluno = new File("src\\\\SF-Pro-Display-Regular.otf");
            Font customFontAreaLogin = Font.createFont(Font.TRUETYPE_FONT, fontFileAreaAluno);
            GraphicsEnvironment geAreaLogin = GraphicsEnvironment.getLocalGraphicsEnvironment();
            geAreaLogin.registerFont(customFontAreaLogin);

            Font customFont12 = customFontAreaLogin.deriveFont(12f); // Reduzindo o tamanho da fonte para 16 pixels

            // Criar uma fonte personalizada para os cabeçalhos
            Font fonteSetter = customFont12;
            label.setFont(fonteSetter);
            label.setBackground(Color.WHITE);
            label.setForeground(Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("DEBUG");
        }
        

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
