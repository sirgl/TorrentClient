package bencode;

import java.io.IOException;
import java.io.OutputStream;

public interface BencodeItem {
    void serialize(OutputStream outputStream) throws IOException;
}
