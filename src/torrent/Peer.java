package torrent;

import java.util.BitSet;

public class Peer {
    private byte[] name;
    private BitSet bitfield;

    public byte[] getName() {
        return name;
    }

    public void setName(byte[] name) {
        this.name = name;
    }

    public BitSet getBitfield() {
        return bitfield;
    }

    public void setBitfield(BitSet bitfield) {
        this.bitfield = bitfield;
    }
}
