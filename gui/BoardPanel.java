//Sawyer Enchura
//SER120

package ser120.chess.gui;

import ser120.chess.game.GameManager;
import ser120.chess.models.Color;
import ser120.chess.models.Piece;
import ser120.chess.models.PieceType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {

    private static final int TILE = 72;
    private static final java.awt.Color LIGHT = new java.awt.Color(200, 175, 140);
    private static final java.awt.Color DARK  = new java.awt.Color(181, 136, 99);
    private static final java.awt.Color SELECT = new java.awt.Color(100, 200, 100, 160);
    private static final java.awt.Color ILLEGAL = new java.awt.Color(220, 60, 60, 180);

    private final GameManager gm;
    private final JLabel statusLabel;
    private final SidebarPanel whiteCaptures;
    private final SidebarPanel blackCaptures;

    private int selRow = -1;
    private int selCol = -1;
    private Color turn = Color.WHITE;
    private boolean gameOver = false;
    private boolean showIllegal = false;

    public BoardPanel(GameManager gm, JLabel statusLabel,
                      SidebarPanel whiteCaptures, SidebarPanel blackCaptures) {
        this.gm = gm;
        this.statusLabel = statusLabel;
        this.whiteCaptures = whiteCaptures;
        this.blackCaptures = blackCaptures;

        setPreferredSize(new Dimension(8 * TILE, 8 * TILE));
        setBackground(java.awt.Color.DARK_GRAY);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(e.getX() / TILE, e.getY() / TILE);
            }
        });
    }

    private void handleClick(int col, int row) {
        if (gameOver) return;
        showIllegal = false;

        if (selRow == -1) {
            Piece p = gm.getBoard().grid[row][col];
            if (p != null && p.color == turn) {
                selRow = row;
                selCol = col;
            }
        } else {
            if (row == selRow && col == selCol) {
                selRow = selCol = -1;
            } else {
                Piece target = gm.getBoard().grid[row][col];
                if (target != null && target.color == turn) {
                    selRow = row;
                    selCol = col;
                } else {
                    attemptMove(selRow, selCol, row, col);
                    selRow = selCol = -1;
                }
            }
        }
        repaint();
    }

    private void attemptMove(int sr, int sc, int er, int ec) {
        if (!gm.isValidMove(sr, sc, er, ec, turn)) {
            showIllegal = true;
            Timer t = new Timer(600, e -> { showIllegal = false; repaint(); });
            t.setRepeats(false);
            t.start();
            return;
        }

        Piece captured = gm.getBoard().grid[er][ec];
        boolean kingCaptured = captured != null && captured.type == PieceType.KING;

        if (captured != null) {
            if (turn == Color.WHITE) {
                whiteCaptures.addPiece(captured);
            } else {
                blackCaptures.addPiece(captured);
            }
        }

        gm.move(sr, sc, er, ec);

        if (kingCaptured) {
            gameOver = true;
            statusLabel.setText(turn + " wins by capturing the King!");
            statusLabel.setForeground(turn == Color.WHITE
                    ? new java.awt.Color(100, 220, 100)
                    : new java.awt.Color(220, 100, 100));
        } else {
            turn = (turn == Color.WHITE) ? Color.BLACK : Color.WHITE;
            statusLabel.setText(turn + "'s turn");
        }
    }

    public void reset() {
        resetWithTurn(Color.WHITE);
    }

    public void resetWithTurn(Color startTurn) {
        turn = startTurn;
        gameOver = false;
        showIllegal = false;
        selRow = selCol = -1;
        statusLabel.setForeground(java.awt.Color.WHITE);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                java.awt.Color bg = ((r + c) % 2 == 0) ? LIGHT : DARK;
                g2.setColor(bg);
                g2.fillRect(c * TILE, r * TILE, TILE, TILE);

                if (r == selRow && c == selCol) {
                    g2.setColor(SELECT);
                    g2.fillRect(c * TILE, r * TILE, TILE, TILE);
                }

                Piece p = gm.getBoard().grid[r][c];
                if (p != null) {
                    drawPiece(g2, p, c * TILE, r * TILE);
                }
            }
        }

        if (showIllegal) {
            g2.setColor(ILLEGAL);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(java.awt.Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 28));
            FontMetrics fm = g2.getFontMetrics();
            String msg = "Illegal move!";
            g2.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2,
                    getHeight() / 2 + fm.getAscent() / 2);
        }

        drawCoordinates(g2);
    }

    private void drawPiece(Graphics2D g2, Piece p, int x, int y) {
        String symbol = unicodeSymbol(p);
        g2.setFont(new Font("Serif", Font.PLAIN, TILE - 12));
        FontMetrics fm = g2.getFontMetrics();

        g2.setColor(p.color == Color.WHITE
                ? new java.awt.Color(230, 230, 230)
                : new java.awt.Color(30, 30, 30));
        g2.drawString(symbol,
                x + (TILE - fm.stringWidth(symbol)) / 2,
                y + TILE - (TILE - fm.getAscent()) / 2 - 4);
    }

    private String unicodeSymbol(Piece p) {
        if (p.color == Color.WHITE) {
            switch (p.type) {
                case KING:   return "\u2654";
                case QUEEN:  return "\u2655";
                case ROOK:   return "\u2656";
                case BISHOP: return "\u2657";
                case KNIGHT: return "\u2658";
                case PAWN:   return "\u2659";
            }
        } else {
            switch (p.type) {
                case KING:   return "\u265A";
                case QUEEN:  return "\u265B";
                case ROOK:   return "\u265C";
                case BISHOP: return "\u265D";
                case KNIGHT: return "\u265E";
                case PAWN:   return "\u265F";
            }
        }
        return "?";
    }

    private void drawCoordinates(Graphics2D g2) {
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        for (int i = 0; i < 8; i++) {
            String col = String.valueOf((char) ('A' + i));
            String row = String.valueOf(8 - i);
            java.awt.Color fg = (i % 2 == 0) ? DARK : LIGHT;
            g2.setColor(fg);
            g2.drawString(col, i * TILE + 3, 8 * TILE - 3);
            g2.drawString(row, 3, i * TILE + 13);
        }
    }
}
