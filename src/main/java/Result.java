import jline.console.ConsoleReader;
import org.apache.commons.lang3.StringUtils;

import javax.sound.sampled.Clip;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Result {
    private long secondTime;
    private String name = "";

    public Result(long time) {
        this.secondTime = time;
    }

    public void resultMenu() throws Exception{
        printRezult();

    }


    // 結果を表示するメソッド
    public void printRezult() throws Exception{
        Audio audio = new Audio();

        // 音声ファイルを非同期で開く
       // ExecutorService es = Executors.newWorkStealingPool();
        //CompletableFuture<Clip> cl = CompletableFuture.supplyAsync(() -> audio.createClip(new File("/Users/yuki-yoshii/Documents/Dungeon/src/main/resources/bgm_maoudamashii_8bit20.wav")), es);

        Clip clip = audio.createClip(new File("/Users/yuki-yoshii/Documents/Dungeon/src/main/resources/bgm_maoudamashii_8bit20.wav"));


        try {
            ConsoleReader console = new ConsoleReader();
            console.clearScreen();
            console.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        WhiteAsciiText wt = new WhiteAsciiText();
        String message = wt.generate("Congratulation!");
        String result = "Your time is " + String.valueOf(secondTime) + " seconds.";
        LineAsciiText lt = new LineAsciiText();
        result = lt.generate(result);

        System.out.println(message);
        System.out.println();
        System.out.println(result);

        //非同期で取得したデータを変数に格納
        //Clip clip = cl.get();
        clip.start();


        writeUserName();

        try {
            clip.close();
            Menu menu = new Menu();
            menu.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //プレイヤー名を入力してもらうメソッド
    public void writeUserName() {
        try {
            Thread.sleep(2000);
            clearScreen();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        LineAsciiText lt = new LineAsciiText();

        String announce1 = lt.generate("Write User Name");
        String announce2 = lt.generate("And Push 'Enter' ");
        System.out.println(announce1);
        System.out.println(announce2);
        Controller controller = new Controller();
        controller.controller();

        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            if (controller.getKey().equals("")) {
                continue;
            }
            if (controller.getKey().equals("Enter")) {
                if (StringUtils.isEmpty(name)) {
                    System.out.println(lt.generate("Please Write Your Name"));
                    controller.setKey("");
                    continue;
                }
                if (name.length() >= 9) {
                    name = name.substring(0,8);
                }
                controller.unregisterNativeHook();
                clearScreen();
                System.out.println(lt.generate("thank you "));
                System.out.println(lt.generate(name));
                System.out.println(lt.generate(" for playing to this game !!!"));

                postScore();

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                return;
            }
            if (controller.getKey().equals("Backspace")) {
                if (name.length() == 0) {
                    controller.setKey("");
                    continue;
                }
                int nameSize = name.length() - 1;
                name = name.substring(0, nameSize);
                clearScreen();
                controller.setKey("");
                System.out.println(announce1);
                System.out.println(announce2);
                System.out.println(name);
                continue;
            }
            if (controller.getKey().matches("[A-Z]")) {
                name += controller.getKey();
                clearScreen();
                controller.setKey("");
                System.out.println(announce1);
                System.out.println(announce2);
                System.out.println(name);
            }
        }
    }

    //入力された名前とスコアをpostリクエストで送る
    public void postScore() {
        String json = "{\"name\":\"" + name + "\", \"time\":\"" + secondTime + "\"}";
        HttpSendJSON httpSendJSON = new HttpSendJSON();
        httpSendJSON.callPost(json);

    }

    public void clearScreen() {
        try {
            ConsoleReader console = new ConsoleReader();
            console.clearScreen();
            console.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


}
