package torrent.files;

import com.sun.istack.internal.NotNull;
import torrent.Torrent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManagerImpl implements FileManager {
    private final RandomAccessFile file;
    private final int size;

    public FileManagerImpl(@NotNull Torrent torrent) throws IOException {
        String fileName = torrent.getName();
        size = torrent.getLength();
        Path filePath = Paths.get(fileName);
        try {
            Files.createFile(filePath);
        } catch (IOException e) {
            System.out.println("file exists");
            try {
                Files.delete(filePath);
                Files.createFile(filePath);
            } catch (IOException ignored) {
            }
        }
        try {
            file = new RandomAccessFile(fileName, "rw");
            System.out.println(file.length());
            file.setLength(size);
            System.out.println(file.length());
        } catch (FileNotFoundException e) {
            //TODO remake it, as it will never happen
            throw new RuntimeException(e);
        }
    }

    @Override
    public void putBlock(byte[] block, int position) throws IOException, OutOfBoundsException {
        if(block.length + position > size) {
            throw new OutOfBoundsException();
        }
        file.seek(position);
        file.write(block);
    }

    @Override
    public byte[] getBlock(int offset, int blockSize) throws IOException, OutOfBoundsException {
        if(offset + blockSize > size) {
            throw new OutOfBoundsException();
        }
        byte[] block = new byte[blockSize];
        file.seek(offset);
        file.read(block);
        return block;
    }
}
