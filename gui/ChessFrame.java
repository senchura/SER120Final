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
                    String path = f.getAbsolutePath();
                    if (!path.endsWith(".chs")) path += ".chs";
                    gameManager.saveGame(path);
                    JOptionPane.showMessageDialog(this, "Game saved to " + new File(path).getName());
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

        JMenuItem replayItem = new JMenuItem("Replay Game...");
        replayItem.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File f = fc.getSelectedFile();
                List<String> moves;
                try {
                    moves = gameManager.loadMovesOnly(f.getAbsolutePath());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Failed to load: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                gameManager.resetGame();
                boardPanel.reset();
                whiteCaptures.clear();
                blackCaptures.clear();
                statusLabel.setText("Replaying...");
                statusLabel.setForeground(java.awt.Color.WHITE);

                final int[] index = {0};
                final Color[] turn = {Color.WHITE};
                final javax.swing.Timer[] timerHolder = {null};

                javax.swing.Timer replayTimer = new javax.swing.Timer(1000, null);
                timerHolder[0] = replayTimer;
                replayTimer.addActionListener(step -> {
                    if (index[0] >= moves.size()) {
                        timerHolder[0].stop();
                        statusLabel.setText("Replay complete.");
                        return;
                    }
                    int[] coords = gameManager.parseMove(moves.get(index[0]));
                    if (coords != null) {
                        ser120.chess.models.Piece captured = gameManager.getBoard().grid[coords[2]][coords[3]];
                        if (captured != null) {
                            if (turn[0] == Color.WHITE) whiteCaptures.addPiece(captured);
                            else blackCaptures.addPiece(captured);
                        }
                        gameManager.move(coords[0], coords[1], coords[2], coords[3]);
                        boardPanel.repaint();
                        turn[0] = (turn[0] == Color.WHITE) ? Color.BLACK : Color.WHITE;
                        statusLabel.setText("Replay: move " + (index[0] + 1) + " of " + moves.size());
                    }
                    index[0]++;
                });
                replayTimer.setInitialDelay(0);
                replayTimer.start();
            }
        });

        gameMenu.add(newItem);
        gameMenu.addSeparator();
        gameMenu.add(saveItem);
        gameMenu.add(loadItem);
        gameMenu.addSeparator();
        gameMenu.add(replayItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
