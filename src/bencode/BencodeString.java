package bencode;

import bencode.BencodeItem;

import java.io.IOException;
import java.io.OutputStream;

public class BencodeString implements BencodeItem {
    private final String str;

    public BencodeString(String str) {
        this.str = str;
    }

    public String getString() {
        return str;
    }

    public void serialize(OutputStream outputStream) throws IOException {
        byte[] bytes = str.getBytes();
        String length = Integer.toString(bytes.length);
        for (int i = 0; i < length.length(); i++) {
            outputStream.write(length.charAt(i));
        }
        outputStream.write(':');
        for (byte b : bytes) {
            outputStream.write(b);
        }
    }
}
