package quentin;

import java.io.ByteArrayInputStream;

public class StreamUtility {

    public static void provideInput(String data) {
        ByteArrayInputStream is = new ByteArrayInputStream(data.getBytes());
        System.setIn(is);
    }
}
