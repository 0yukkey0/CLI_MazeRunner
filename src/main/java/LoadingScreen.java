import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import com.indvd00m.ascii.render.elements.Ellipse;
import com.indvd00m.ascii.render.elements.Text;
import jline.console.ConsoleReader;

import java.util.Random;

public class LoadingScreen implements Loading {
    @Override
    public void loading(int time) throws Exception {
        //ローディング画面っぽい画面を作る
        IRender render = new Render();
        IContextBuilder builder = render.newBuilder();
        builder.width(100).height(40);
        builder.element(new Ellipse(40, 20, 50, 30));

        ConsoleReader console = new ConsoleReader();
        LineAsciiText lt = new LineAsciiText();

        for (int i = 1; i < 101; i++) {
            console.clearScreen();
            console.flush();

            System.out.println(lt.generate("Now Loading..."));


            String number = lt.generate(String.valueOf(i));

            builder.element(new Text(number, 33, 18, 40, 20));
            ICanvas canvas = render.render(builder.build());
            String progress = canvas.getText();
            System.out.println(progress);
            Thread.sleep(time);

            Random rnd = new Random();
            int ran = rnd.nextInt(10);
            i += ran;
            if (i > 100) {
                i = 99;
            }

        }

        System.out.println();
        System.out.println(lt.generate("Complete!!"));

        Thread.sleep(500);

    }
}
