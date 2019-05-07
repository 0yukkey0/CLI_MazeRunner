
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpReceiveJSON {
    public String callGet() {

        //GASのURL
        String strPostUrl = "https://script.google.com/macros/s/AKfycbxJsJJcB3u5AMMbO2FvaY9gGCRYHFNnoNTFeVFT_MsT-YnIseDy/exec" ;


        HttpURLConnection con = null;
        StringBuffer result = new StringBuffer();
        try {

            URL url = new URL(strPostUrl);
            con = (HttpURLConnection) url.openConnection();
            // HTTPリクエストコード
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept-Language", "jp");
            con.connect();

            // HTTPレスポンスコード
            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                // 通信に成功した
                // テキストを取得する
                final InputStream in = con.getInputStream();
                String encoding = con.getContentEncoding();
                if (null == encoding) {
                    encoding = "UTF-8";
                }
                final InputStreamReader inReader = new InputStreamReader(in, encoding);
                final BufferedReader bufReader = new BufferedReader(inReader);
                String line = null;
                // 1行ずつテキストを読み込む
                while ((line = bufReader.readLine()) != null) {
                    result.append(line);
                }
                bufReader.close();
                inReader.close();
                in.close();
            } else {
                // 通信が失敗した場合のレスポンスコードを表示
                System.out.println(status);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (con != null) {
                // コネクションを切断
                con.disconnect();
            }
        }
        return result.toString();
    }
}
