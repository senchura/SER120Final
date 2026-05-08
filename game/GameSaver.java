//Sawyer Enchura
//SER120

package ser120.chess.game;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameSaver {

    public static void save(String filename, List<String> moveHistory) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (String move : moveHistory) {
                writer.println(move);
            }
        }
    }

    public static List<String> load(String filename) throws IOException {
        List<String> moves = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    moves.add(line);
                }
            }
        }
        return moves;
    }
}
