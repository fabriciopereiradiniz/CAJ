package UI;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import DB.Conexao;
import DB.Par;
import entities.Administrador;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class AreaAdmin extends JFrame {

    private JPanel panel;
    private Conexao conexao;
    private Administrador administrador;

    public AreaAdmin(Conexao conexao, Administrador administrador) {
        this.conexao = conexao;
        this.administrador = administrador;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Area do Administrador - " + administrador.getNome());
        setBounds(100, 100, 800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        panel = new JPanel(new BorderLayout());
        getContentPane().add(panel, BorderLayout.CENTER);


        exibirInformacoesAdministrador();
    }

    private void voltarParaTelaDeLogin() {
		this.dispose();
		Login.showWindow();
	}
    
    private void exibirInformacoesAdministrador() {
        getContentPane().removeAll();
        this.getContentPane().setBackground(Color.WHITE);
        this.setBackground(Color.WHITE);
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        getContentPane().add(panel);

        setTitle("Area do Administrador - " + administrador.getNome());

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
}
