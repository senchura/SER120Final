//Sawyer Enchura
//SER120
//test comment

package ser120.chess;

import ser120.chess.game.GameManager;

public class App {
    public static void main(String[] args) {
        System.out.println("~~~ Welcome to Chess Time! ~~~");

        new GameManager().runGame();

        System.out.println("~~~ Thanks for playing! ~~~");
    }
}
