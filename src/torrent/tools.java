package torrent;

import java.nio.ByteBuffer;

public class tools {
    public static int transferAsMuchAsPossible(
            ByteBuffer dest, ByteBuffer src)
    {
        int nTransfer = Math.min(dest.remaining(), src.remaining());
        if (nTransfer > 0)
        {
            dest.put(src.array(),
                    src.arrayOffset() + src.position(),
                    nTransfer);
            src.position(src.position()+nTransfer);
        }
        return nTransfer;
    }
}
