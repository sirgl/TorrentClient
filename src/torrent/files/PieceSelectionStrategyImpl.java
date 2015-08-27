package torrent.files;

import torrent.Peer;
import torrent.communication.PeerManager;

import java.util.BitSet;
import java.util.List;
import java.util.OptionalInt;

public class PieceSelectionStrategyImpl implements PieceSelectionStrategy {
    static private final int PIECE_COUNT_BEFORE_RECALC = 20;

    private final PeerManager peerManager;
    private final Statistics statistics;
    private int iteration;
    private List<Statistics.Entry> entryList;

    public PieceSelectionStrategyImpl(PeerManager peerManager, Statistics statistics) {
        this.peerManager = peerManager;
        this.statistics = statistics;
        entryList = statistics.getSortedStatistics();
    }


    @Override
    public OptionalInt getNextPiece(int id) {
        if (iteration == PIECE_COUNT_BEFORE_RECALC) {
            iteration = 0;
            entryList = statistics.getSortedStatistics();
        }
        iteration++;

        Peer peer = peerManager.getPeer(id);
        BitSet bitfield = peer.getBitfield();
        for (Statistics.Entry entry : entryList) {
            if (!entry.downloaded && !entry.downloading && bitfield.get(entry.position)) {
                entry.downloading = true;
                return OptionalInt.of(entry.position);
            }
        }
        return OptionalInt.empty();
    }
}
