package UI;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

import DB.Conexao;

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
			boolean isProfessor = conexao.verificarTipoUsuario(conexao.loginAutenticado);

			if (isProfessor) {
				materias = conexao.obterMateriasPorProfessor(conexao.loginAutenticado);
				exibirInformacoesProfessor(materias);
			} else {
				materias = conexao.obterMateriasPorAluno(conexao.loginAutenticado);
				exibirInformacoesAluno(materias);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void exibirInformacoesProfessor(List<String> materias) {
		getContentPane().removeAll(); // Remove existing components
		panel = new JPanel();
		panel.setLayout(new GridLayout(materias.size() + 2, 2)); // Plus 2 for labels in the first two rows
		panel.setBounds(39, 50, 300, materias.size() * 30);
		getContentPane().add(panel);
	
		JLabel lblNewLabel = new JLabel("Bem-vindo, " + nomeUsuario + "!");
		panel.add(lblNewLabel);
	
		JLabel emptyLabel = new JLabel(); // Empty label for layout
		panel.add(emptyLabel);
	
		JLabel lblMateriaHeader = new JLabel("Materia");
		panel.add(lblMateriaHeader);
	
		JLabel lblAcaoHeader = new JLabel("Ação");
		panel.add(lblAcaoHeader);
	
		for (String materia : materias) {
			JLabel lblMateria = new JLabel(materia);
			panel.add(lblMateria);
	
			JButton btnView = new JButton("View");
			btnView.addActionListener(new ViewButtonActionListener(materia));
			panel.add(btnView);
		}
	
		revalidate();
		repaint();
	}

	private void displayStudentInformation(String materia) throws SQLException {
		selectedMateria = materia;
		idSelectedMateria = conexao.obteIdMateriaPorNome(selectedMateria);
	
		// Set title
		setTitle("Materia: " + materia);
	
		// Remove previous panel if exists
		if (panel != null) {
			getContentPane().remove(panel);
			revalidate();
			repaint();
		}
	
		// Create back button
		JButton btnBack = new JButton("Voltar");
		btnBack.setBounds(39, 50, 80, 20);
		btnBack.addActionListener(new BackButtonActionListener());
		getContentPane().add(btnBack);
	
		// Create table with headers
		String[] columnHeaders = {"ID", "Aluno", "Faltas", "1º Bimestre", "2º Bimestre", "3º Bimestre", "4º Bimestre", "Exame", "Editar"};
		tableModel = new DefaultTableModel(columnHeaders, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				// Allow editing all cells except the "Aluno" column
				return column != 0;
			}
		};
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(39, 100, 700, 400);
		getContentPane().add(scrollPane);
	
		// Populate table with student information
		try {
			List<Integer> alunos = conexao.obterAlunosPorMateria(materia);
			for (Integer alunoId : alunos) {
				List<Object> alunoInfo = conexao.obterAlunoInfoPorId(alunoId, idSelectedMateria);
	
				// Add student information to the table model
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
	
		// Add action listener for the "Save" button
		table.getColumn("Editar").setCellRenderer(new ButtonRenderer());
		table.getColumn("Editar").setCellEditor(new ButtonEditor(new JTextField()));
	}
	

	private void exibirInformacoesAluno(List<String> materias) {
		getContentPane().removeAll(); // Remove existing components

		// Create a welcome label
		JLabel lblWelcome = new JLabel("Bem-vindo, " + nomeUsuario + "!");
		lblWelcome.setBounds(39, 50, 300, 20);
		getContentPane().add(lblWelcome);
	
		// Create table with headers
		String[] columnHeaders = {"ID", "Matéria", "Faltas", "1º Bimestre", "2º Bimestre", "3º Bimestre", "4º Bimestre", "Exame"};
		tableModel = new DefaultTableModel(columnHeaders, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(39, 100, 700, 400);
		getContentPane().add(scrollPane);

		// Populate table with student information
		try {
			for (String materia : materias) {
				int idMateriaAtual = conexao.obteIdMateriaPorNome(materia);
				List<Object> alunoInfo = conexao.obterAlunoInfoPorId(conexao.idAluno, idMateriaAtual);

				// Add student information to the table model
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
	
		/* revalidate();
		repaint(); */
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
			getContentPane().remove(panel); // Remove the panel
			getContentPane().revalidate();
			getContentPane().repaint();
			try {
				exibirInformacoesProfessor(conexao.obterMateriasPorProfessor(conexao.loginAutenticado));
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private class ButtonRenderer extends JButton implements TableCellRenderer {
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
				// Handle button click
				int idAluno = (int) parseValue(table.getValueAt(table.getSelectedRow(), 0));
				int faltas = (int) parseValue(table.getValueAt(table.getSelectedRow(), 2));
				double grade1 = parseValue(table.getValueAt(table.getSelectedRow(), 3));
				double grade2 = parseValue(table.getValueAt(table.getSelectedRow(), 4));
				double grade3 = parseValue(table.getValueAt(table.getSelectedRow(), 5));
				double grade4 = parseValue(table.getValueAt(table.getSelectedRow(), 6));
				double finalExam = parseValue(table.getValueAt(table.getSelectedRow(), 7));
	
				try {
					conexao.atualizarInfoAluno(idAluno, idSelectedMateria, faltas, grade1, grade2, grade3, grade4, finalExam);
				} catch (SQLException e) {
					e.printStackTrace();
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
	

}