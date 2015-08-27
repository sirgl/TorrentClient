package torrent.files;

import java.io.IOException;

public interface FileManager {
    void putBlock(byte[] block, int position) throws IOException, OutOfBoundsException;
    byte[] getBlock(int offset, int blockSize) throws IOException, OutOfBoundsException;
}
