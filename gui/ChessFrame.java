//Sawyer Enchura
//SER120

package ser120.chess.gui;

import ser120.chess.game.GameManager;
import ser120.chess.models.Color;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
        getContentPane().setBackground(new java.awt.Color(40, 40, 40));

        whiteCaptures = new SidebarPanel("White Captures");
        blackCaptures = new SidebarPanel("Black Captures");

        statusLabel = new JLabel("White's turn", SwingConstants.CENTER);
        statusLabel.setForeground(java.awt.Color.WHITE);
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
            statusLabel.setForeground(java.awt.Color.WHITE);
        });

        JMenuItem saveItem = new JMenuItem("Save Game...");
        saveItem.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                try {
                    gameManager.saveGame(f.getAbsolutePath());
                    JOptionPane.showMessageDialog(this, "Game saved to " + f.getName());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to save: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem loadItem = new JMenuItem("Load Game...");
        loadItem.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                try {
                    List<String> moves = gameManager.loadGame(f.getAbsolutePath());
                    whiteCaptures.clear();
                    blackCaptures.clear();
                    Color resumeTurn = (moves.size() % 2 == 0) ? Color.WHITE : Color.BLACK;
                    boardPanel.resetWithTurn(resumeTurn);
                    statusLabel.setText(resumeTurn + "'s turn");
                    statusLabel.setForeground(java.awt.Color.WHITE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to load: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gameMenu.add(newItem);
        gameMenu.addSeparator();
        gameMenu.add(saveItem);
        gameMenu.add(loadItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
