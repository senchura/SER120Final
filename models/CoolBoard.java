//Sawyer Enchura
//SER120

package ser120.chess.models;

public class CoolBoard extends Board {

    public static final String RESET = "\u001B[0m";
    public static final String LIGHT = "\u001B[100m";
    public static final String DARK = "\u001B[40m";
    public static final String TEXT = "\u001B[37m";
    public static final String HIGHLIGHT = "\u001B[32m";
    public static final String WHITE_PIECE = "\u001B[36m"; // cyan
	public static final String BLACK_PIECE = "\u001B[31m"; // red

    public CoolBoard(int rows, int cols) {
        super(rows, cols);
    }

    @Override
    public void showBoard(int[] coords, String turn) {
        System.out.println(HIGHLIGHT + "\n        --- TURN " + turn + " ---" + RESET);

        // Column labels (top)
        System.out.print("    ");
        for (int c = 0; c < cols; c++) {
            System.out.print(" " + (char) ('A' + c) + " ");
        }
        System.out.println();

        for (int r = 0; r < rows; r++) {
            int displayRow = rows - r;

            System.out.printf(" %d  ", displayRow);

            for (int c = 0; c < cols; c++) {
                String bg = ((r + c) % 2 == 0) ? LIGHT : DARK;

                Piece piece = grid[r][c];

String content;
String pieceColor = TEXT;

if (coords[0] == r && coords[1] == c) {
    content = " X ";
    pieceColor = HIGHLIGHT;
} else if (piece != null) {
    content = " " + piece.getSymbol() + " ";
    pieceColor = (piece.color == Color.WHITE) ? WHITE_PIECE : BLACK_PIECE;
} else {
    content = "   ";
}

System.out.print(bg + pieceColor + content + RESET);
            }

            System.out.printf(" %d", displayRow);
            System.out.println();
        }

        // Column labels (bottom)
        System.out.print("    ");
        for (int c = 0; c < cols; c++) {
            System.out.print(" " + (char) ('A' + c) + " ");
        }
        System.out.println("\n");
    }
}
