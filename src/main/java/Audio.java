import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Audio {
    public Clip createClip(File path) {
        //指定されたURLのオーディオ入力ストリームを取得
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(path)) {

            //ファイルの形式取得
            AudioFormat af = ais.getFormat();

            //単一のオーディオ形式を含む指定した情報からデータラインの情報オブジェクトを構築
            DataLine.Info dataLine = new DataLine.Info(Clip.class, af);

            //指定された Line.Info オブジェクトの記述に一致するラインを取得
            Clip c = (Clip) AudioSystem.getLine(dataLine);

            //再生準備完了
            c.open(ais);

            return c;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        return null;
    }

    //ボリュームを半分にして再生するためのメソッド
    public Clip createClipHalf(File path){
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream in = AudioSystem.getAudioInputStream(path);
            clip.open(in);
            FloatControl control = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            controlByLinearScalar(control, 0.50); // 50%の音量で再生する
            //clip.start();
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void controlByLinearScalar(FloatControl control, double linearScalar) {
        control.setValue((float)Math.log10(linearScalar) * 20);
    }

}
