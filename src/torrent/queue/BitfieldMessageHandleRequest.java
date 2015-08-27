package torrent.queue;

import torrent.Peer;
import torrent.communication.PeerManager;

import java.nio.ByteBuffer;
import java.util.BitSet;

public class BitfieldMessageHandleRequest implements Runnable {
    private final byte[] bitfield;
    private final long sourcePeerId;
    private final PeerManager peerManager;

    public BitfieldMessageHandleRequest(byte[] bitfield, long sourcePeerId, PeerManager peerManager) {
        this.bitfield = bitfield;
        this.sourcePeerId = sourcePeerId;
        this.peerManager = peerManager;
    }


    @Override
    public void run() {
        Peer peer = peerManager.getPeer(sourcePeerId);
        BitSet bitSet = BitSet.valueOf(bitfield);
        peer.setBitfield(bitSet);
    }
}
