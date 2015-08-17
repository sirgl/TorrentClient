package torrent;

import java.nio.ByteBuffer;

public class Peer {
    private ByteBuffer name;

    public ByteBuffer getName() {
        return name;
    }

    public void setName(ByteBuffer name) {
        this.name = name;
    }
}
