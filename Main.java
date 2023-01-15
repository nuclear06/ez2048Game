import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var main = new GameRunner();
        try {
            int length = Integer.parseInt(args[0]);
            int width = Integer.parseInt(args[1]);
            System.out.printf("Game has been initialized:%dx%d%n", length, width);
            main.startGame(length, width);
        } catch (Exception e) {
            System.out.println("default initialized:4x4");
            main.startGame(4, 4);
        }
    }
}

class GameRunner {
    Game mainGame;
    Mover mover;
    private int totalSteps = 0;

    public void startGame(int length, int width) {
        mainGame = new Game(length, width);
        mainGame.createRandomBlock(2);
        mover = new Mover(mainGame);
        System.out.println("use wasd to move");
        while (!mainGame.isGameOver()) {
            gameThread();
        }
        System.out.println("======Game Over======");
        System.out.println("Your Final Score Is " + mainGame.getTotalScore());
        System.out.println("Your Total Steps Is " + totalSteps);
    }

    private void gameThread() {
        mainGame.drawGameBoard();
        var scanner = new Scanner(System.in);
        char command = scanner.next().charAt(0);
        Mover.direct x = switch (command) {
            case 'w', 'W' -> Mover.direct.UP;
            case 'a', 'A' -> Mover.direct.LEFT;
            case 's', 'S' -> Mover.direct.DOWN;
            case 'd', 'D' -> Mover.direct.RIGHT;
            default -> null;
        };
        if (x == null) {
            return;
        }
        mover.mergeBlocks(x);
        mover.arrangeBlocks(x);
        if (Game.getBoardChange()) {
            mainGame.createRandomBlock(1);
            totalSteps++;
        }
    }
}