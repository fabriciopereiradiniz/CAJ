package UI;

import javax.swing.*;
import javax.swing.table.*;

import java.awt.*;

public class ButtonRenderer extends JButton implements TableCellRenderer {
    private static final long serialVersionUID = 1L;

    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if ("Media".equals(table.getValueAt(row, 0).toString())) {
            return new JLabel("");
        } else {
            setText("Salvar");
            return this;
        }
    }
}
