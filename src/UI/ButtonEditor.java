package UI;

import javax.swing.*;

import DB.Conexao;

import java.awt.*;
import java.sql.SQLException;

public class ButtonEditor extends DefaultCellEditor {
    private static final long serialVersionUID = 1L;
    private JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private Conexao conexao;
    private int idSelectedMateria;

    public ButtonEditor(JTextField textField, JTable table, Conexao conexao, int idSelectedMateria) {
        super(textField);
        this.table = table;
        this.conexao = conexao;
        this.idSelectedMateria = idSelectedMateria;

        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if ("Media".equals(table.getValueAt(row, 0).toString())) {
            button.setText("");
            button.setEnabled(false);
            return new JLabel("");
        } else {
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
                PopupUtil.exibirPopup("Erro! Notas devem ser de 0 a 10!");
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