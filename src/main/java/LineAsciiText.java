import com.github.lalyos.jfiglet.FigletFont;

import java.io.IOException;

public class LineAsciiText implements AsciiGenerate{
    @Override
    public String generate(String text) {

        try {
            text = FigletFont.convertOneLine(text);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }
}
