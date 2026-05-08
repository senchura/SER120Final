//Sawyer Enchura
//SER120

package ser120.chess.gui;

import ser120.chess.game.GameManager;

import javax.swing.*;
import java.awt.*;

public class ChessFrame extends JFrame {

    private final GameManager gameManager;
    private final BoardPanel boardPanel;
    private final SidebarPanel whiteCaptures;
    private final SidebarPanel blackCaptures;
    private final JLabel statusLabel;

    public ChessFrame() {
        super("Chess Time!");
        gameManager = new GameManager();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(new Color(40, 40, 40));

        whiteCaptures = new SidebarPanel("White Captures");
        blackCaptures = new SidebarPanel("Black Captures");

        statusLabel = new JLabel("White's turn", SwingConstants.CENTER);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        boardPanel = new BoardPanel(gameManager, statusLabel, whiteCaptures, blackCaptures);

        add(blackCaptures, BorderLayout.WEST);
        add(boardPanel, BorderLayout.CENTER);
        add(whiteCaptures, BorderLayout.EAST);
        add(statusLabel, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newItem = new JMenuItem("New Game");
        newItem.addActionListener(e -> {
            gameManager.resetGame();
            boardPanel.reset();
            whiteCaptures.clear();
            blackCaptures.clear();
            statusLabel.setText("White's turn");
        });
        gameMenu.add(newItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
