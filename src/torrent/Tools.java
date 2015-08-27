package torrent;

import java.nio.ByteBuffer;

public class Tools {
    public static int transferAsMuchAsPossible(
            ByteBuffer dest, ByteBuffer src) {
        int nTransfer = Math.min(dest.remaining(), src.remaining());
        if (nTransfer > 0) {
            dest.put(src.array(),
                    src.arrayOffset() + src.position(),
                    nTransfer);
            src.position(src.position() + nTransfer);
        }
        return nTransfer;
    }

    public static String toBinary(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++)
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        return sb.toString();
    }
}
