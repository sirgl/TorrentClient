package torrent;

public interface FileManager {
    void putBlock(byte[] block, int piecePosition, int blockPosition);
    byte[] getBlock(int piecePosition, int blockPosition);
}
