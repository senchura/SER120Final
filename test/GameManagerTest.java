//Sawyer Enchura
//SER120

package ser120.chess.test;

import ser120.chess.game.GameManager;
import ser120.chess.game.GameSaver;
import ser120.chess.models.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GameManagerTest {

    private static int passed = 0;
    private static int failed = 0;

    private static void assertTrue(boolean condition, String name) {
        if (condition) {
            System.out.println("  PASS: " + name);
            passed++;
        } else {
            System.out.println("  FAIL: " + name);
            failed++;
        }
    }

    private static void assertFalse(boolean condition, String name) {
        assertTrue(!condition, name);
    }

    private static void assertEquals(Object expected, Object actual, String name) {
        assertTrue(expected.equals(actual), name);
    }

    static void testPawnMoveOneSquare() {
        GameManager gm = new GameManager();
        assertTrue(gm.isValidMove(6, 0, 5, 0, Color.WHITE),
                "White pawn moves one square forward (A2→A3)");
    }

    static void testPawnMoveTwoSquaresFromStart() {
        GameManager gm = new GameManager();
        assertTrue(gm.isValidMove(6, 0, 4, 0, Color.WHITE),
                "White pawn moves two squares from starting row (A2→A4)");
    }

    static void testPawnCannotMoveBackward() {
        GameManager gm = new GameManager();
        assertFalse(gm.isValidMove(6, 0, 7, 0, Color.WHITE),
                "White pawn cannot move backward");
    }

    static void testRookMovesHorizontally() {
        GameManager gm = new GameManager();
        CoolBoard board = gm.getBoard();
        board.grid[7][1] = null;
        board.grid[7][2] = null;
        board.grid[7][3] = null;
        assertTrue(gm.isValidMove(7, 0, 7, 4, Color.WHITE),
                "Rook moves horizontally on a clear path");
    }

    static void testRookBlockedByOwnPiece() {
        GameManager gm = new GameManager();
        assertFalse(gm.isValidMove(7, 0, 7, 3, Color.WHITE),
                "Rook is blocked by own piece");
    }

    static void testKnightJumpsOverPieces() {
        GameManager gm = new GameManager();
        assertTrue(gm.isValidMove(7, 1, 5, 0, Color.WHITE),
                "Knight jumps over pieces (B1→A3)");
    }

    static void testCannotMoveOpponentPiece() {
        GameManager gm = new GameManager();
        assertFalse(gm.isValidMove(1, 0, 2, 0, Color.WHITE),
                "White cannot move a black pawn");
    }

    static void testKingMovesOneSquare() {
        GameManager gm = new GameManager();
        CoolBoard board = gm.getBoard();
        board.grid[7][3] = null;
        assertTrue(gm.isValidMove(7, 4, 7, 3, Color.WHITE),
                "King moves one square sideways");
    }

    static void testSaveAndLoad() throws IOException {
        File tmp = File.createTempFile("chess_test", ".txt");
        tmp.deleteOnExit();

        List<String> moves = Arrays.asList("A2 A4", "A7 A5", "B2 B4");
        GameSaver.save(tmp.getAbsolutePath(), moves);
        List<String> loaded = GameSaver.load(tmp.getAbsolutePath());

        assertEquals(moves, loaded, "Save/load round-trip preserves move history");
    }

    static void testWinConditionKingCapture() {
        GameManager gm = new GameManager();
        CoolBoard board = gm.getBoard();
        board.grid[0][3] = null;
        board.grid[1][4] = new Piece(PieceType.QUEEN, Color.WHITE);
        assertTrue(gm.isValidMove(1, 4, 0, 4, Color.WHITE),
                "White queen can capture black king (win move is valid)");
    }

    public static void main(String[] args) throws IOException {
        System.out.println("=== Chess Tests ===\n");

        testPawnMoveOneSquare();
        testPawnMoveTwoSquaresFromStart();
        testPawnCannotMoveBackward();
        testRookMovesHorizontally();
        testRookBlockedByOwnPiece();
        testKnightJumpsOverPieces();
        testCannotMoveOpponentPiece();
        testKingMovesOneSquare();
        testSaveAndLoad();
        testWinConditionKingCapture();

        System.out.println("\n--- Results: " + passed + " passed, " + failed + " failed ---");
        if (failed > 0) System.exit(1);
    }
}
