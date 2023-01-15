import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Game {
    private int TotalScore = 0;
    final int LENGTH;
    final int WIDTH;
    protected Block[][] gameBoard;
    private static boolean boardChange = false;
    private int maxBlockStage = 1;
    private int emptyBlock;

    public void updateMaxStage(int maxStage) {
        if (maxStage > this.maxBlockStage) this.maxBlockStage = maxStage;
    }

    Random random;

    public static void setBoardChange() {
        Game.boardChange = true;
    }

    public static void clearBoardChange() {
        Game.boardChange = false;
    }

    public static boolean getBoardChange() {
        return Game.boardChange;
    }

    public int getEmptyBlock() {
        return emptyBlock;
    }

    public Game(int length, int width) {
        this.LENGTH = length;
        this.WIDTH = width;
        this.gameBoard = new Block[WIDTH][LENGTH];
        this.emptyBlock = length * width;
        this.random = new Random();
    }

    private Block.Point getXIndexYIndexByIndex(int index) throws Exception {
        if (index < 0 || index >= LENGTH * WIDTH) throw new Exception("index illegal");
        index++;
        int y = 0;
        while (index > LENGTH) {
            index -= LENGTH;
            ++y;
        }
        return new Block.Point(index - 1, y);
    }

    private int getBlockStage() {
        if (maxBlockStage < 5) return 1;
        int total = (int) (Math.pow(2, (maxBlockStage - 3) + 1) - 2);//2^n求和
        int randRes = random.nextInt(2, total + 1);
        var binary = Integer.toBinaryString(randRes);
        var res = binary.substring(0, binary.length() - 1).lastIndexOf("1") + 1;
        assert res >= 1;
        return res;
    }

    public int getTotalScore() {
        return TotalScore;
    }

    protected void createRandomBlock(int num) {
        if (getEmptyBlock() == 0) return;
        var emptyIndexs = new ArrayList<Integer>(getEmptyBlock());
        int index = 0;
        for (var row : gameBoard) {
            for (var block : row) {
                if (block == null) {
                    emptyIndexs.add(index);
                }
                index++;
            }
        }
        try {
            for (int i = 0; i < num; i++) {
                int randomInt = random.nextInt(getEmptyBlock());
                assert randomInt < emptyIndexs.size();
                int randomIndex = emptyIndexs.remove(randomInt);
                var randomPoint = getXIndexYIndexByIndex(randomIndex);
                boolean changeResult = createBlockAt(randomPoint.x(), randomPoint.y(), getBlockStage());
                assert changeResult;
            }
        } catch (Exception e) {
            System.out.printf(String.valueOf(e));
        }
    }

    public boolean isGameOver() {
        if (!getBoardChange() && getEmptyBlock() == 0) {
            return true;
        }
        Game.clearBoardChange();
        return false;
    }

    protected boolean createBlockAt(int xIndex, int yIndex, int stage) {
        assert xIndex < LENGTH && yIndex < WIDTH;
        assert stage >= 1;
        if (gameBoard[yIndex][xIndex] != null) return false;
        gameBoard[yIndex][xIndex] = new myBlock(xIndex, yIndex, stage);
        updateMaxStage(stage);
        emptyBlock--;
        return true;
    }

    protected void setBlockAt(int xIndex, int yIndex, int stage) {
        assert xIndex < LENGTH && yIndex < WIDTH;
        assert stage >= 1;
        if (gameBoard[yIndex][xIndex] != null) {
            updateMaxStage(stage);
            gameBoard[yIndex][xIndex] = new myBlock(xIndex, yIndex, stage);
        } else {
            createBlockAt(xIndex, yIndex, stage);
        }
    }

    protected void setBlockAt(Block atBlock, int stage) {
        int xIndex = atBlock.getPoint().x();
        int yIndex = atBlock.getPoint().y();
        setBlockAt(xIndex, yIndex, stage);
    }

    protected Block deleteBlockAt(int xIndex, int yIndex) {
        if (xIndex > LENGTH - 1 || yIndex > WIDTH - 1) throw new ArrayIndexOutOfBoundsException("坐标越界");
        var tmp = gameBoard[yIndex][xIndex];
        gameBoard[yIndex][xIndex] = null;
//        System.out.printf("(%d,%d)\n".formatted(xIndex, yIndex));
        emptyBlock++;
        return tmp;
    }

    protected void addTotalScore(int score) {
        TotalScore += score;
    }

    protected void setPointIsBlock(int x, int y, Block block) {
        gameBoard[y][x] = block;
        emptyBlock--;
    }

    protected void moveBlock(int formX, int fromY, int toX, int toY) {
        var tmp = gameBoard[fromY][formX];
        assert tmp != null;
        deleteBlockAt(tmp);
        tmp.setPoint(new Block.Point(toX, toY));
        setPointIsBlock(toX, toY, tmp);
    }

    protected Block deleteBlockAt(Block block) {
        int xIndex = block.getPoint().x();
        int yIndex = block.getPoint().y();
        return deleteBlockAt(xIndex, yIndex);
    }

    public void drawGameBoard() {
//        System.out.println(emptyBlock);
        System.out.println("--" + String.join("", Collections.nCopies(WIDTH, "--**--")));
        for (var row : gameBoard) {
            System.out.print("* ");
            for (var block : row) {
                if (block == null) {
                    System.out.print("      ");
                } else {
                    block.draw();
                }
            }
            System.out.println("*");
        }
        System.out.println("--" + String.join("", Collections.nCopies(WIDTH, "--**--")) + "Score:" + getTotalScore());
    }

    public static void main(String[] args) {
        var x = new Game(4, 4);
        x.maxBlockStage = 5;
        for (int i = 0; i < 10; i++) {
            System.out.print(x.getBlockStage());

        }
    }
}

class myBlock extends Block {
    public myBlock(int x, int y, int aStage) {
        super(x, y, aStage);
    }

    @Override
    public void draw() {
        var blankEnvelop = new Object() {
            String blankEnvelopStr(int blankLen, String str) {
                var blank = String.join("", Collections.nCopies(blankLen, " "));
                return blank + str + blank;
            }
        };
        var standard = "      ";
        var score = String.valueOf(getScore());
        int n = standard.length() - score.length();
        if (n % 2 == 0) {
            System.out.print(blankEnvelop.blankEnvelopStr(n / 2, score));
        } else {
            System.out.print(blankEnvelop.blankEnvelopStr(n / 2, score + " "));
        }
    }
}