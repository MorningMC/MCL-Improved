package morningmc.foliage.launcher.listener;

import java.io.IOException;
import java.io.InputStream;

public class StreamPump implements Runnable {

    private InputStream in;

    public StreamPump(InputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                if (in.read() == -1) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
