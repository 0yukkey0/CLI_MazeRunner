import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import com.indvd00m.ascii.render.elements.Rectangle;
import com.indvd00m.ascii.render.elements.Text;

import jline.Terminal;
import jline.TerminalFactory;
import jline.console.ConsoleReader;

import javax.sound.sampled.Clip;
import java.io.File;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Menu {

    public void run() throws Exception {
        Audio audio = new Audio();
        // 音声ファイルを非同期で開く
        //ExecutorService esi = Executors.newWorkStealingPool();
        //CompletableFuture<Clip> cl = CompletableFuture.supplyAsync(() -> audio.createClip(new File("/Users/yuki-yoshii/Documents/Dungeon/src/main/resources/game_maoudamashii_6_dangeon23.wav")), esi);

        Clip clip = audio.createClip(new File("/Users/yuki-yoshii/Documents/Dungeon/src/main/resources/game_maoudamashii_6_dangeon23.wav"));


        //ローディング画面を呼ぶ、画面遷移の速度を引数に
        LoadingScreen ld = new LoadingScreen();
        ld.loading(120);


        //非同期で取得したデータを変数に格納
        //Clip clip = cl.get();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();

        ConsoleReader console = new ConsoleReader();
        console.clearScreen();
        console.flush();

        printMainMenu();
        Controller controller = new Controller();
        controller.controller();

        boolean menuFlag = true;

        //メニュー画面で指定されたキー入力があった場合、その処理を実行しwhileを抜ける
        while (menuFlag) {
            switch (controller.getKey()) {
                case "S":
                    try {
                        clip.close();
                        Maze maze = new Maze();
                        maze.run();
                        menuFlag = false;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                case "R":
                    clip.close();
                    Ranking rank = new Ranking();
                    rank.Ranking();
                    menuFlag = false;
                    break;
                case "Q":
                    System.exit(0);
                default:
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                        break;
                    }
                    break;
            }
        }

    }

    public void printMainMenu() {
        /** terminalのサイズ取得 */
        Terminal terminal = TerminalFactory.get();
        int height = terminal.getHeight();
        int width = terminal.getWidth();

        WhiteAsciiText wt = new WhiteAsciiText();
        LineAsciiText lt = new LineAsciiText();

        String title = wt.generate("Maze Runner 2019");
        String start = lt.generate("Start : push[ S ]");
        String ranking = lt.generate("Ranking : push[ R ]");
        String exit = lt.generate("exit : push[ Q ]");


        IRender render = new Render();
        IContextBuilder builder = render.newBuilder();
        builder.width(width / 6 * 5 + 5).height(height - 1);
        builder.element(new Rectangle());
        builder.element(new Text(title, 2, 1, width / 5 * 4, height / 3));
        builder.element(new Text(start, 30, 25, 100, 30));
        builder.element(new Text(ranking, 30, 35, 110, 30));
        builder.element(new Text(exit, 30, 45, 100, 30));

        ICanvas canvas = render.render(builder.build());
        String menu = canvas.getText();
        System.out.println(menu);

    }

}

