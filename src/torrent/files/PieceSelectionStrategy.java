package torrent.files;

import java.util.OptionalInt;

public interface PieceSelectionStrategy {
    OptionalInt getNextPiece(int peerId);
}
