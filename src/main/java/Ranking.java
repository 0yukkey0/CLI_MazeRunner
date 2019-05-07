import com.google.gson.Gson;
import jline.console.ConsoleReader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Ranking {
    public void Ranking() throws Exception {
        ConsoleReader console = new ConsoleReader();
        console.clearScreen();
        console.flush();

        printRanking();
        Controller controller = new Controller();

        while (true) {
            if (controller.getKey().equals("Q")) {
                controller.setKey("");
                Menu menu = new Menu();
                menu.run();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                break;
            }
        }
    }

    public void printRanking() throws Exception{
        HttpReceiveJSON httpReceiveJSON = new HttpReceiveJSON();

        // 単純な非同期実行
        ExecutorService esc = Executors.newWorkStealingPool();
        CompletableFuture<String> json = CompletableFuture.supplyAsync(() -> httpReceiveJSON.callGet(), esc);

        //ロード画面の表示
        LoadingScreen ld = new LoadingScreen();
        ld.loading(100);

        //非同期で取得したデータを変数に格納
        String data = json.get();

        // JSONからjavaオブジェクトへの変換
        Gson gson = new Gson();
        Score[] score = gson.fromJson(data, Score[].class);
        LineAsciiText lt = new LineAsciiText();
        System.out.println(lt.generate("Rank : Name : Time"));

        //全データの表示
        for (int i = 0; i < score.length; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                break;
            }
            System.out.println(lt.generate(i + 1 + " : " + score[i].name + " : " + String.valueOf(score[i].time) + " seconds"));
            System.out.println();

        }
        System.out.println();
        System.out.println();
        System.out.println(lt.generate("Return Menu : push[Q]"));
        return;
    }
}
