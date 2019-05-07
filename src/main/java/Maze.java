import jline.console.ConsoleReader;

import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Maze implements Loading{
    public static final int mazeSize = 30;
//    public static final int mazeSize = 15;
    // 壁: true, 道: false
    private boolean[][] wall;
    private static int usrRow = Maze.mazeSize - 1;
    private static int usrCol = 1;
    private static int goalRow = 0;
    private static int goalCol = Maze.mazeSize - 2;

    @Override
    public void loading(int time) throws Exception {
        //迷路の初期シャッフルを実装、見た目だけ。

        ConsoleReader console = new ConsoleReader();
        Digger digger = new Digger(wall);
        LineAsciiText lt = new LineAsciiText();

        Random rnd = new Random();
        int ran = rnd.nextInt(15) + 15;

        for (int i = 0; i < ran; i++) {
            System.out.println(lt.generate("Shuffle Maze"));

            try {
                Thread.sleep(time);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            wall = digger.digDungeon();
            resetUsr();
            resetGoal();
            console.clearScreen();
            console.flush();
            printMaze();
        }

        System.out.println(lt.generate("Decided The Maze"));

    }


    public void run() throws Exception {
        Audio audio = new Audio();

        // 音声ファイルを非同期で開く
        //ExecutorService es = Executors.newWorkStealingPool();
        //CompletableFuture<Clip> cl = CompletableFuture.supplyAsync(() -> audio.createClip(new File("/Users/yuki-yoshii/Documents/Dungeon/src/main/resources/game_maoudamashii_6_dangeon04.wav")), es);

        Clip clip = audio.createClip(new File("/Users/yuki-yoshii/Documents/Dungeon/src/main/resources/game_maoudamashii_6_dangeon04.wav"));

        //非同期で取得したデータを変数に格納
        //Clip clip = cl.get();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();

        ConsoleReader console = new ConsoleReader();
        console.clearScreen();
        console.flush();

        wall = new boolean[mazeSize][mazeSize];

        Digger digger = new Digger(wall);
        Player player = new Player();
        Controller controller = new Controller();

        loading(50);

        long start = System.currentTimeMillis();
        long end;

        controller.setKey("");

        while (true) {
            switch (controller.getKey()) {
                case "":
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                    break;
                case "R":
                    resetUsr();
                    console.clearScreen();
                    console.flush();
                    printMaze();
                    controller.setKey("");
                    break;
                case "N":
                    wall = digger.digDungeon();
                    resetUsr();
                    resetGoal();
                    console.clearScreen();
                    console.flush();
                    printMaze();
                    controller.setKey("");
                    break;
                default:
                    player.moveUsr(controller.getKey(), wall);

                   // if (player.isMovePlayer()) {
                        console.clearScreen();
                        console.flush();
                        printMaze();
                    //}
                    controller.setKey("");
                    break;
            }
            if (usrRow == goalRow && usrCol == goalCol) {
                clip.close();
                controller.unregisterNativeHook();
                end = System.currentTimeMillis();
                Result result = new Result((end - start) / 1000);
                result.resultMenu();
                break;
            }
        }
    }

    public int getUsrRow() {
        return usrRow;
    }

    public int getUsrCol() {
        return usrCol;
    }


    public void setUsrRow(int usrRow) {
        Maze.usrRow = usrRow;
    }

    public void setUsrCol(int usrCol) {
        Maze.usrCol = usrCol;
    }


    // 迷路を表示するメソッド
    public void printMaze() {
        System.out.println();
        for (int i = 0; i < mazeSize; i++) {
            for (int j = 0; j < mazeSize; j++) {
                if (i == usrRow && j == usrCol) {
                    System.out.print("\u001b[5;44m");
                    System.out.print("**");
                    System.out.print("\u001b[00m");
                } else if (i == goalRow && j == goalCol) {
                    System.out.print("\u001b[1;41m");
                    System.out.print("GO");
                    System.out.print("\u001b[00m");
                } else if (wall[i][j]) {
                    System.out.print("\u001b[48;5;237m");
                    System.out.print("\u001b[38;5;237m");
                    System.out.print("[]");
                    System.out.print("\u001b[00m");
                } else {
                    System.out.print("\u001b[48;5;255m");
                    System.out.print("  ");
                    System.out.print("\u001b[00m");

                }
            }
            System.out.println();
        }
    }

    // ユーザを初期位置に動かすメソッド
    public void resetUsr() {
        usrRow = mazeSize - 1;
        usrCol = 1;

        while (true) {
            if (wall[usrRow - 1][usrCol]) {
                usrCol++;
            } else {
                break;
            }
        }

        wall[usrRow][usrCol] = false;
    }

    // ゴールを初期位置に動かすメソッド
    public void resetGoal() {
        goalRow = 0;
        goalCol = mazeSize - 2;

        while (true) {
            if (wall[goalRow + 1][goalCol]) {
                goalCol--;
            } else {
                break;
            }
        }

        wall[goalRow][goalCol] = false;
    }

}
