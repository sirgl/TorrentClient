package torrent.queue;

import torrent.Peer;
import torrent.communication.PeerManager;

import java.util.BitSet;

public class HaveMessageHandleRequest implements Runnable {
    private final long sourcePeerId;
    private final int pieceNumber;
    private final PeerManager manager;

    public HaveMessageHandleRequest(long sourcePeerId, int pieceNumber, PeerManager manager) {
        this.sourcePeerId = sourcePeerId;
        this.pieceNumber = pieceNumber;
        this.manager = manager;
    }

    @Override
    public void run() {
        Peer peer = manager.getPeer(sourcePeerId);
        BitSet bitfield = peer.getBitfield();
        //TODO check id
        bitfield.set(pieceNumber);
    }
}
