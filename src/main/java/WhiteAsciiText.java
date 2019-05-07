import com.indvd00m.ascii.render.Render;
import com.indvd00m.ascii.render.api.ICanvas;
import com.indvd00m.ascii.render.api.IContextBuilder;
import com.indvd00m.ascii.render.api.IRender;
import com.indvd00m.ascii.render.elements.PseudoText;
import jline.Terminal;
import jline.TerminalFactory;

public class WhiteAsciiText implements AsciiGenerate {
    @Override
    public String generate(String text) {
        Terminal terminal = TerminalFactory.get();
        int height = terminal.getHeight();
        int width = terminal.getWidth();

        IRender titleReder = new Render();
        IContextBuilder titleBuilder = titleReder.newBuilder();
        titleBuilder.width(width / 6 * 5).height(height / 3);

        titleBuilder.element(new PseudoText(text));
        ICanvas titleCanvas = titleReder.render(titleBuilder.build());
        text = titleCanvas.getText();

        return text;
    }
}
