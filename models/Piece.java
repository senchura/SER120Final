//Sawyer Enchura
//SER120

package ser120.chess.models;

public class Piece {
    public final PieceType type;
    public final Color color;

    public Piece(PieceType type, Color color) {
        this.type = type;
        this.color = color;
    }

    public String getSymbol() {
    char symbol;

    switch (type) {
        case KING:   symbol = 'K'; break;
        case QUEEN:  symbol = 'Q'; break;
        case ROOK:   symbol = 'R'; break;
        case BISHOP: symbol = 'B'; break;
        case KNIGHT: symbol = 'N'; break;
        case PAWN:   symbol = 'P'; break;
        default:     symbol = '?';
    }

    return color == Color.WHITE
        ? String.valueOf(symbol)
        : String.valueOf(Character.toLowerCase(symbol));
}
}
