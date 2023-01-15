import java.util.Objects;

public class Mover {
    final int LENGTH;
    final int WIDTH;

    enum direct {UP, DOWN, LEFT, RIGHT}

    Block[][] gameBoard;
    Game game;

    Mover(Game g) {
        this.LENGTH = g.LENGTH;
        this.WIDTH = g.WIDTH;
        this.gameBoard = g.gameBoard;
        this.game = g;
    }

    public void mergeBlocks(direct dir) {
        try {
            class mergeHelper {
                private void mergeRowOrCol(int x, int y, direct dir) {
                    if (x < 0 || y < 0 || x > LENGTH || y > WIDTH) return;

                    Block a = null, b = null;
                    switch (dir) {
                        case UP -> {
                            while (y < WIDTH) {
                                var tmp = gameBoard[y++][x];
                                if (tmp != null) {
                                    a = tmp;
                                    break;
                                }
                            }
                            while (y < WIDTH) {
                                var tmp = gameBoard[y++][x];
                                if (tmp != null) {
                                    b = tmp;
                                    break;
                                }
                            }
                            if (b == null) {
                                return;
                            }
                            if (Objects.equals(a, b)) {
                                game.setBlockAt(b, b.getStage() + 1);
                                game.deleteBlockAt(a);
                                game.addTotalScore(b.getScore() * 2);
                                Game.setBoardChange();
                                mergeRowOrCol(x, y, direct.UP);
                            } else {
                                mergeRowOrCol(x, y - 1, direct.UP);
                            }
                        }
                        case DOWN -> {
                            while (y >= 0) {
                                var tmp = gameBoard[y--][x];
                                if (tmp != null) {
                                    a = tmp;
                                    break;
                                }
                            }
                            while (y >= 0) {
                                var tmp = gameBoard[y--][x];
                                if (tmp != null) {
                                    b = tmp;
                                    break;
                                }
                            }
                            if (b == null) {
                                return;
                            }
                            if (Objects.equals(a, b)) {
                                game.setBlockAt(b, b.getStage() + 1);
                                game.deleteBlockAt(a);
                                game.addTotalScore(b.getScore() * 2);
                                Game.setBoardChange();
                                mergeRowOrCol(x, y, direct.DOWN);
                            } else {
                                mergeRowOrCol(x, y + 1, direct.DOWN);
                            }
                        }
                        case LEFT -> {
                            while (x < LENGTH) {
                                var tmp = gameBoard[y][x++];
                                if (tmp != null) {
                                    a = tmp;
                                    break;
                                }
                            }
                            while (x < LENGTH) {
                                var tmp = gameBoard[y][x++];
                                if (tmp != null) {
                                    b = tmp;
                                    break;
                                }
                            }
                            if (b == null) {
                                return;
                            }
                            if (Objects.equals(a, b)) {
                                game.setBlockAt(b, b.getStage() + 1);
                                game.deleteBlockAt(a);
                                game.addTotalScore(b.getScore() * 2);
                                Game.setBoardChange();
                                mergeRowOrCol(x, y, direct.LEFT);
                            } else {
                                mergeRowOrCol(x - 1, y, direct.LEFT);
                            }
                        }
                        case RIGHT -> {
                            while (x >= 0) {
                                var tmp = gameBoard[y][x--];
                                if (tmp != null) {
                                    a = tmp;
                                    break;
                                }
                            }
                            while (x >= 0) {
                                var tmp = gameBoard[y][x--];
                                if (tmp != null) {
                                    b = tmp;
                                    break;
                                }
                            }
                            if (b == null) {
                                return;
                            }
                            if (Objects.equals(a, b)) {
                                game.setBlockAt(b, b.getStage() + 1);
                                game.deleteBlockAt(a);
                                game.addTotalScore(b.getScore() * 2);
                                Game.setBoardChange();
                                mergeRowOrCol(x, y, direct.RIGHT);
                            } else {
                                mergeRowOrCol(x + 1, y, direct.RIGHT);
                            }
                        }
                    }
                }
            }
            var Helper = new mergeHelper();
            switch (dir) {
                case UP -> {
                    for (int col = 0; col < LENGTH; col++) {
                        Helper.mergeRowOrCol(col, 0, direct.UP);
                    }
                }
                case DOWN -> {
                    for (int col = 0; col < LENGTH; col++) {
                        Helper.mergeRowOrCol(col, WIDTH - 1, direct.DOWN);
                    }
                }
                case LEFT -> {
                    for (int row = 0; row < WIDTH; row++) {
                        Helper.mergeRowOrCol(0, row, direct.LEFT);
                    }
                }
                case RIGHT -> {
                    for (int row = 0; row < WIDTH; row++) {
                        Helper.mergeRowOrCol(LENGTH - 1, row, direct.RIGHT);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void arrangeBlocks(direct direct) {
        class arrangeHelper {
            void arrangeRowOrCol(int x, int y, direct direct) {
                assert x >= 0 && y >= 0 && x < LENGTH && y < WIDTH;

                int index;
                switch (direct) {
                    case UP -> {
                        index = 0;
                        for (int row = 0; row < WIDTH; row++) {
                            if (gameBoard[row][x] != null) {
                                if (index == row) {
                                    index++;
                                } else {
                                    Game.setBoardChange();
                                    game.moveBlock(x, row, x, index++);
                                }
                            }
                        }
                    }
                    case DOWN -> {
                        index = WIDTH - 1;
                        for (int row = WIDTH - 1; row >= 0; row--) {
                            if (gameBoard[row][x] != null) {
                                if (index == row) {
                                    index--;
                                } else {
                                    Game.setBoardChange();
                                    game.moveBlock(x, row, x, index--);
                                }
                            }
                        }
                    }
                    case LEFT -> {
                        index = 0;
                        for (int col = 0; col < LENGTH; col++) {
                            if (gameBoard[y][col] != null) {
                                if (index == col) {
                                    index++;
                                } else {
                                    Game.setBoardChange();
                                    game.moveBlock(col, y, index++, y);
                                }
                            }
                        }
                    }
                    case RIGHT -> {
                        index = LENGTH - 1;
                        for (int col = LENGTH - 1; col >= 0; col--) {
                            if (gameBoard[y][col] != null) {
                                if (index == col) {
                                    index--;
                                } else {
                                    Game.setBoardChange();
                                    game.moveBlock(col, y, index--, y);

                                }
                            }
                        }
                    }
                }
            }
        }

        var Helper = new arrangeHelper();
        switch (direct) {
            case UP -> {
                for (int col = 0; col < LENGTH; col++) {
                    Helper.arrangeRowOrCol(col, WIDTH - 1, Mover.direct.UP);
                }
            }
            case DOWN -> {
                for (int col = 0; col < LENGTH; col++) {
                    Helper.arrangeRowOrCol(col, 0, Mover.direct.DOWN);
                }
            }
            case LEFT -> {
                for (int row = 0; row < WIDTH; row++) {
                    Helper.arrangeRowOrCol(LENGTH - 1, row, Mover.direct.LEFT);
                }
            }
            case RIGHT -> {
                for (int row = 0; row < WIDTH; row++) {
                    Helper.arrangeRowOrCol(0, row, Mover.direct.RIGHT);
                }
            }
        }
    }
}
