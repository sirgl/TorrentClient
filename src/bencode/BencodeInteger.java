package bencode;

import bencode.BencodeItem;

import java.io.IOException;
import java.io.OutputStream;

public class BencodeInteger implements BencodeItem {
    private final int num;

    public BencodeInteger(int num) {
        this.num = num;
    }

    public int getInteger() {
        return num;
    }

    public void serialize(OutputStream outputStream) throws IOException {
        outputStream.write('i');
        String number = Integer.toString(this.num);
        for (int i = 0; i < number.length(); i++) {
            outputStream.write(number.charAt(i));
        }
        outputStream.write('e');
    }

}
