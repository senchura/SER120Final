//Sawyer Enchura
//SER120

package ser120.chess.models;

public class Board {
    protected final int rows;
    protected final int cols;
    public Piece[][] grid;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        initialize();
    }

    private void initialize() {
        grid = new Piece[rows][cols];

        // Pawns
        for (int c = 0; c < cols; c++) {
            grid[1][c] = new Piece(PieceType.PAWN, Color.BLACK);
            grid[6][c] = new Piece(PieceType.PAWN, Color.WHITE);
        }

        // Rooks
        grid[0][0] = grid[0][7] = new Piece(PieceType.ROOK, Color.BLACK);
        grid[7][0] = grid[7][7] = new Piece(PieceType.ROOK, Color.WHITE);

        // Knights
        grid[0][1] = grid[0][6] = new Piece(PieceType.KNIGHT, Color.BLACK);
        grid[7][1] = grid[7][6] = new Piece(PieceType.KNIGHT, Color.WHITE);

        // Bishops
        grid[0][2] = grid[0][5] = new Piece(PieceType.BISHOP, Color.BLACK);
        grid[7][2] = grid[7][5] = new Piece(PieceType.BISHOP, Color.WHITE);

        // Queens
        grid[0][3] = new Piece(PieceType.QUEEN, Color.BLACK);
        grid[7][3] = new Piece(PieceType.QUEEN, Color.WHITE);

        // Kings
        grid[0][4] = new Piece(PieceType.KING, Color.BLACK);
        grid[7][4] = new Piece(PieceType.KING, Color.WHITE);
    }
    
    public void showBoard(int[] coords, String turn) {
    
	}
}
