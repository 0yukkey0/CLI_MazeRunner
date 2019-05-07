import java.util.Random;
import java.util.Stack;

public class Digger {
    private static int row;
    private static int col;
    private static Stack<Integer> rowStack = new Stack<Integer>();
    private static Stack<Integer> colStack = new Stack<Integer>();

    private boolean wall[][];

    public Digger(boolean wall[][]) {
        this.wall = wall;
    }

    public boolean[][] digDungeon() {
        // 初期化
        for (int i = 0; i < Maze.mazeSize; i++) {
            for (int j = 0; j < Maze.mazeSize; j++) {
                wall[i][j] = true;
            }
        }

        // ランダムに開始位置を選ぶ（1 〜 mazeSize - 2）
        Random rnd = new Random();
        row = rnd.nextInt(Maze.mazeSize - 2) + 1;
        col = rnd.nextInt(Maze.mazeSize - 2) + 1;
        wall[row][col] = false;
        rowStack.push(row);
        colStack.push(col);

        boolean continueFlag = true;

        // 以下、wall[][]全体を埋めるまで繰り返し
        while (continueFlag) {

            // 上下左右のいずれかに限界まで道を伸ばす
            extendPath();

            // 既にある道から次の開始位置を選ぶ（0 〜 mazeSize - 1（かつ 偶数？））
            continueFlag = false;

            while (!rowStack.empty() && !colStack.empty()) {
                row = rowStack.pop();
                col = colStack.pop();

                if (canExtendPath()) {
                    continueFlag = true;
                    break;
                }
            }
        }
        return wall;
    }


    // 道を拡張するメソッド
    public void extendPath() {
        boolean extendFlag = true;

        while (extendFlag) {
            extendFlag = extendPathSub();
        }
    }

    // 道の拡張に成功したらtrue、失敗したらfalseを返すメソッド
    public boolean extendPathSub() {
        Random rmd = new Random();
        // 上: 0, 下: 1, 左: 2, 右: 3
        int direction = rmd.nextInt(4);

        for (int i = 0; i < 4; i++) {
            direction = (direction + i) % 4;
            if (canExtendPathWithDir(direction)) {
                movePoint(direction);
                return true;
            }
        }

        return false;
    }

    // 指定した方向へ拡張可能ならばtrue、不可能ならばfalseを返すメソッド
    public boolean canExtendPathWithDir(int direction) {
        int exRow = row;
        int exCol = col;

        switch (direction) {
            case 0:    // 上
                exRow--;
                break;

            case 1:    // 下
                exRow++;
                break;

            case 2:    // 左
                exCol--;
                break;

            case 3:    // 右
                exCol++;
                break;
        }

        if (countSurroundingPath(exRow, exCol) > 1) {
            return false;
        }

        return true;
    }

    // 周囲1マスにある道の数を数えるメソッド
    public int countSurroundingPath(int row, int col) {
        int num = 0;

        if (row - 1 < 0 || !wall[row - 1][col]) {
            num++;
        }
        if (row + 1 > Maze.mazeSize - 1 || !wall[row + 1][col]) {
            num++;
        }
        if (col - 1 < 0 || !wall[row][col - 1]) {
            num++;
        }
        if (col + 1 > Maze.mazeSize - 1 || !wall[row][col + 1]) {
            num++;
        }

        return num;
    }

    // 指定した方向へ1マスrowとcolを移動させるメソッド
    public void movePoint(int direction) {
        switch (direction) {
            case 0:    // 上
                row--;
                break;

            case 1:    // 下
                row++;
                break;

            case 2:    // 左
                col--;
                break;

            case 3:    // 右
                col++;
                break;
        }

        wall[row][col] = false;
        rowStack.push(row);
        colStack.push(col);
    }

    // 上下左右いずれかの方向へ移動できるならtrue、できないならfalseを返すメソッド
    public boolean canExtendPath() {
        return (canExtendPathWithDir(0) || canExtendPathWithDir(1) || canExtendPathWithDir(2) || canExtendPathWithDir(3));
    }
}