package UI;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class PopupUtil {
    public static void exibirPopup(String mensagem) {
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

            // Fonte personalizada para os cabeçalhos
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
        int x = screenSize.width - panel.getPreferredSize().width - 600;
        int y = screenSize.height - panel.getPreferredSize().height - 200;
        popupFrame.setLocation(x, y);

        popupFrame.setSize(panel.getPreferredSize());
        popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Temporizador para fechar automaticamente após alguns segundos
        Timer timer = new Timer(5000, e -> popupFrame.dispose());  // 5000 milissegundos (5 segundos)
        timer.setRepeats(false);
        timer.start();

        popupFrame.setAlwaysOnTop(true);
        popupFrame.setVisible(true);
    }
}
