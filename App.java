//Sawyer Enchura
//SER120

package ser120.chess;

import ser120.chess.gui.ChessFrame;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChessFrame::new);
    }
}
