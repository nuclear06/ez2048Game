import java.util.Objects;

public abstract class Block {
    private final int stage;
    private final int score;
    private Point point;

    public Block(int x, int y, int aStage) {
        stage = aStage;
        point = new Point(x, y);
        score = (int) Math.pow(2, stage);
    }

    public abstract void draw();

    public int getScore() {
        return score;
    }

    public int getStage() {
        return stage;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(int x, int y) {
        this.point = new Point(x, y);
    }

    public void setPoint(Point p) {
        this.point = p;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Block block = (Block) o;
        return stage == block.stage;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stage);
    }

    @Override
    public String toString() {
        return point + " score:" + score;
    }

    record Point(int x, int y) {
        @Override
        public String toString() {
            return "(%d,%d)".formatted(x, y);
        }
    }

}