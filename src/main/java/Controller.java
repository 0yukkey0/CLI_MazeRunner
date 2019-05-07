import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.logging.LogManager;


public class Controller implements NativeKeyListener {
    private static String key = "";

    public void controller() {
        LogManager.getLogManager().reset();
        //フックされていなかったら
        if (!GlobalScreen.isNativeHookRegistered()) {
            try {
                //フックを登録
                GlobalScreen.registerNativeHook();
            } catch (NativeHookException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        //キー・リスナを登録
        GlobalScreen.addNativeKeyListener(this);

    }

    public void unregisterNativeHook() {
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e1) {
            e1.printStackTrace();
        }
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    //キーを押したとき
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        //入力されたキーを保持する
        setKey(NativeKeyEvent.getKeyText(e.getKeyCode()));

        if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
            try {
                GlobalScreen.unregisterNativeHook();
            } catch (NativeHookException e1) {
                e1.printStackTrace();
            }
        }
    }

    //キーを離したとき
    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    //キーをタイプしたとき
    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }
}
