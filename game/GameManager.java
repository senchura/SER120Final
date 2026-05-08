//Sawyer Enchura
//SER120

package ser120.chess.game;

import ser120.chess.models.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameManager {
    private CoolBoard board;
    private final Scanner scanner;
    private List<String> moveHistory = new ArrayList<>();

    public GameManager() {
        this.board = new CoolBoard(8, 8);
        this.scanner = new Scanner(System.in);
    }

    public void runGame() {
        System.out.println("Options: NEW, LOAD <file>, REPLAY <file>");
        System.out.print("> ");
        String choice = scanner.nextLine().trim().toUpperCase();

        if (choice.startsWith("LOAD ")) {
            String file = choice.substring(5).trim();
            resumeGame(file);
        } else if (choice.startsWith("REPLAY ")) {
            String file = choice.substring(7).trim();
            replayGame(file);
        } else {
            playGame(new ArrayList<>(), Color.WHITE, 0);
        }
    }

    private void playGame(List<String> existingMoves, Color startTurn, int startTurnNum) {
        List<String> moveHistory = new ArrayList<>(existingMoves);
        Color turn = startTurn;
        int turnNum = startTurnNum;

        while (true) {
            turnNum++;
            board.showBoard(new int[]{-1, -1}, String.format("%03d", turnNum));

            System.out.print(turn + " move (e.g., A2 A4), SAVE <file>, or EXIT: ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("EXIT")) break;

            if (input.startsWith("SAVE ")) {
                String file = input.substring(5).trim();
                handleSave(file, moveHistory);
                turnNum--;
                continue;
            }

            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid input.");
                turnNum--;
                continue;
            }

            int[] start = parseInput(parts[0]);
            int[] end   = parseInput(parts[1]);

            if (start == null || end == null) {
                System.out.println("Invalid coordinates.");
                turnNum--;
                continue;
            }

            if (!isValidMove(start[0], start[1], end[0], end[1], turn)) {
                System.out.println("Invalid move.");
                turnNum--;
                continue;
            }

            Color winner = checkWinByCapture(end[0], end[1]);

            move(start[0], start[1], end[0], end[1]);
            moveHistory.add(parts[0] + " " + parts[1]);

            if (winner != null) {
                board.showBoard(new int[]{-1, -1}, String.format("%03d", turnNum));
                System.out.println("\n*** " + winner + " wins by capturing the King! ***\n");
                break;
            }

            turn = (turn == Color.WHITE) ? Color.BLACK : Color.WHITE;
        }
    }

    private void handleSave(String file, List<String> moveHistory) {
        try {
            GameSaver.save(file, moveHistory);
            System.out.println("Game saved to: " + file);
        } catch (IOException e) {
            System.out.println("Failed to save: " + e.getMessage());
        }
    }

    private void resumeGame(String file) {
        List<String> moves;
        try {
            moves = GameSaver.load(file);
        } catch (IOException e) {
            System.out.println("Could not load file: " + e.getMessage());
            return;
        }

        Color turn = Color.WHITE;
        for (String moveStr : moves) {
            String[] parts = moveStr.toUpperCase().split(" ");
            int[] start = parseInput(parts[0]);
            int[] end   = parseInput(parts[1]);
            if (start != null && end != null) {
                move(start[0], start[1], end[0], end[1]);
                turn = (turn == Color.WHITE) ? Color.BLACK : Color.WHITE;
            }
        }

        System.out.println("Game loaded. " + moves.size() + " moves restored. Resuming...");
        playGame(moves, turn, moves.size());
    }

    public void replayGame(String file) {
        List<String> moves;
        try {
            moves = GameSaver.load(file);
        } catch (IOException e) {
            System.out.println("Could not load file: " + e.getMessage());
            return;
        }

        board = new CoolBoard(8, 8);
        Color turn = Color.WHITE;
        int turnNum = 0;

        System.out.println("Replaying " + moves.size() + " moves...\n");
        board.showBoard(new int[]{-1, -1}, "000");
        sleep(1000);

        for (String moveStr : moves) {
            turnNum++;
            String[] parts = moveStr.toUpperCase().split(" ");
            int[] start = parseInput(parts[0]);
            int[] end   = parseInput(parts[1]);
            if (start == null || end == null) continue;

            Color winner = checkWinByCapture(end[0], end[1]);
            move(start[0], start[1], end[0], end[1]);

            board.showBoard(new int[]{-1, -1}, String.format("%03d", turnNum));
            System.out.println("  Move: " + moveStr + " (" + turn + ")");

            if (winner != null) {
                System.out.println("\n*** " + winner + " wins by capturing the King! ***\n");
                break;
            }

            turn = (turn == Color.WHITE) ? Color.BLACK : Color.WHITE;
            sleep(1000);
        }

        System.out.println("Replay complete.");
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private Color checkWinByCapture(int er, int ec) {
        Piece target = board.grid[er][ec];
        if (target != null && target.type == PieceType.KING) {
            return (target.color == Color.WHITE) ? Color.BLACK : Color.WHITE;
        }
        return null;
    }

    private int[] parseInput(String input) {
        if (input == null || !input.matches("^[A-H][1-8]$")) return null;
        int col = input.charAt(0) - 'A';
        int row = 8 - Character.getNumericValue(input.charAt(1));
        return new int[]{row, col};
    }

    public boolean isValidMove(int sr, int sc, int er, int ec, Color turn) {
        Piece p = board.grid[sr][sc];
        if (p == null || p.color != turn) return false;

        Piece target = board.grid[er][ec];
        if (target != null && target.color == turn) return false;

        int dr = er - sr;
        int dc = ec - sc;

        switch (p.type) {
            case PAWN:
                int dir = (p.color == Color.WHITE) ? -1 : 1;
                if (dc == 0 && target == null) {
                    if (dr == dir) return true;
                    if ((sr == 6 || sr == 1) && dr == 2 * dir &&
                            board.grid[sr + dir][sc] == null) return true;
                }
                if (Math.abs(dc) == 1 && dr == dir && target != null) return true;
                return false;

            case ROOK:
                return (dr == 0 || dc == 0) && clearPath(sr, sc, er, ec);

            case BISHOP:
                return Math.abs(dr) == Math.abs(dc) && clearPath(sr, sc, er, ec);

            case QUEEN:
                return (dr == 0 || dc == 0 || Math.abs(dr) == Math.abs(dc))
                        && clearPath(sr, sc, er, ec);

            case KNIGHT:
                return (Math.abs(dr) == 2 && Math.abs(dc) == 1) ||
                       (Math.abs(dr) == 1 && Math.abs(dc) == 2);

            case KING:
                return Math.abs(dr) <= 1 && Math.abs(dc) <= 1;
        }
        return false;
    }

    private boolean clearPath(int sr, int sc, int er, int ec) {
        int dr = Integer.signum(er - sr);
        int dc = Integer.signum(ec - sc);
        int r = sr + dr;
        int c = sc + dc;
        while (r != er || c != ec) {
            if (board.grid[r][c] != null) return false;
            r += dr;
            c += dc;
        }
        return true;
    }

    public void move(int sr, int sc, int er, int ec) {
        String from = "" + (char)('A' + sc) + (8 - sr);
        String to   = "" + (char)('A' + ec) + (8 - er);
        moveHistory.add(from + " " + to);
        board.grid[er][ec] = board.grid[sr][sc];
        board.grid[sr][sc] = null;
    }

    public CoolBoard getBoard() { return board; }

    public void resetGame() {
        this.board = new CoolBoard(8, 8);
        this.moveHistory = new ArrayList<>();
    }

    public void saveGame(String filename) throws IOException {
        GameSaver.save(filename, moveHistory);
    }

    public List<String> loadGame(String filename) throws IOException {
        List<String> moves = GameSaver.load(filename);
        this.board = new CoolBoard(8, 8);
        this.moveHistory = new ArrayList<>();
        for (String moveStr : moves) {
            String[] parts = moveStr.toUpperCase().split(" ");
            int[] start = parseInput(parts[0]);
            int[] end   = parseInput(parts[1]);
            if (start != null && end != null) {
                board.grid[end[0]][end[1]] = board.grid[start[0]][start[1]];
                board.grid[start[0]][start[1]] = null;
                moveHistory.add(moveStr);
            }
        }
        return moves;
    }

    public List<String> loadMovesOnly(String filename) throws IOException {
        return GameSaver.load(filename);
    }

    public int[] parseMove(String notation) {
        String[] parts = notation.toUpperCase().split(" ");
        if (parts.length != 2) return null;
        int[] start = parseInput(parts[0]);
        int[] end   = parseInput(parts[1]);
        if (start == null || end == null) return null;
        return new int[]{start[0], start[1], end[0], end[1]};
    }
}
