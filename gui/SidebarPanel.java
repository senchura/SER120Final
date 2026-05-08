//Sawyer Enchura
//SER120

package ser120.chess.gui;

import ser120.chess.models.Color;
import ser120.chess.models.Piece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SidebarPanel extends JPanel {

    private final String title;
    private final List<Piece> captured = new ArrayList<>();

    public SidebarPanel(String title) {
        this.title = title;
        setPreferredSize(new Dimension(96, 8 * 72));
        setBackground(new java.awt.Color(30, 30, 30));
    }

    public void addPiece(Piece p) {
        captured.add(p);
        repaint();
    }

    public void clear() {
        captured.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(new java.awt.Color(180, 180, 180));
        g2.setFont(new Font("SansSerif", Font.BOLD, 11));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, 16);

        g2.setFont(new Font("Serif", Font.PLAIN, 26));
        fm = g2.getFontMetrics();
        int cellSize = 32;
        int cols = Math.max(1, getWidth() / cellSize);
        for (int i = 0; i < captured.size(); i++) {
            int row = i / cols;
            int col = i % cols;
            int rowCount = Math.min(cols, captured.size() - row * cols);
            int rowOffset = (getWidth() - rowCount * cellSize) / 2;
            int x = rowOffset + col * cellSize;
            int y = 36 + row * 36;
            Piece p = captured.get(i);
            String sym = unicodeSymbol(p);
            g2.setColor(p.color == Color.WHITE
                    ? new java.awt.Color(220, 220, 220)
                    : new java.awt.Color(80, 80, 80));
            g2.drawString(sym, x, y + fm.getAscent());
        }
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
}
