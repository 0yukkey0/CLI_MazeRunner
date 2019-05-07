import javax.sound.sampled.Clip;
import java.io.File;

public class Player {
    // ユーザを動かすメソッド
    private boolean movePlayerFlag;

    public void moveUsr(String key, boolean[][] wall) {
        Maze maze = new Maze();
        movePlayerFlag = false;


        String errMes = "You can not move there.";
        int exUsrRow = maze.getUsrRow();
        int exUsrCol = maze.getUsrCol();

        switch (key) {
            case "W":    // 上
                exUsrRow--;
                break;
            case "上":    // 上
                exUsrRow--;
                break;
            case "S":    // 下
                exUsrRow++;
                break;
            case "下":    // 下
                exUsrRow++;
                break;
            case "A":    // 左
                exUsrCol--;
                break;
            case "左":    // 左
                exUsrCol--;
                break;
            case "D":    // 右
                exUsrCol++;
                break;
            case "右":    // 右
                exUsrCol++;
                break;
            case "Esc":
                System.exit(0);
            case "":
                break;
            default:
                break;
        }
        //進めないところだった場合
        if (exUsrRow > Maze.mazeSize - 1 || wall[exUsrRow][exUsrCol]) {
            System.out.println(errMes);
            return;
        }
        //進める場合
        if (exUsrCol != maze.getUsrCol() || exUsrRow != maze.getUsrRow()) {
            Audio audio = new Audio();
            Clip clip = audio.createClip(new File("/Users/yuki-yoshii/Documents/Dungeon/src/main/resources/Footstep-high.wav"));
            clip.start();

            maze.setUsrRow(exUsrRow);
            maze.setUsrCol(exUsrCol);
            movePlayerFlag = true;

        }

    }

    public boolean isMovePlayer() {
        return movePlayerFlag;
    }

}
