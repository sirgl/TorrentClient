package torrent.files;

import java.util.*;

public class Statistics {
    private final List<Entry> pieceStats;
    private final List<Entry> sortedStats;
    private final BitSet downloadedPieces;

    public Statistics(int pieceCount) {
        pieceStats = new ArrayList<>(pieceCount);
        sortedStats = new ArrayList<>(pieceCount);
        for (int i = 0; i < pieceCount; i++) {
            pieceStats.add(i, new Entry(i));
        }
        sortedStats.addAll(pieceStats);
        downloadedPieces = new BitSet(pieceCount);

    }

    public boolean isDownloaded(int pieceNumber) {
        return pieceStats.get(pieceNumber).downloaded;
    }

    public void setDownloaded(int pieceNumber) {
        pieceStats.get(pieceNumber).downloaded = true;
    }

    public void increasePiecePopularity(int pieceNumber) {
        pieceStats.get(pieceNumber).popularity++;
    }

    public void decreasePiecePopularity(int pieceNumber) {
        pieceStats.get(pieceNumber).popularity--;
    }

    public void putPeerStatistics(BitSet bitfield) {
        for (int i = 0; i < bitfield.length(); i++) {
            if(bitfield.get(i)) {
                pieceStats.get(i).popularity++;
            }
        }
    }

    public void removePeerStatistics(BitSet bitfield) {
        for (int i = 0; i < bitfield.length(); i++) {
            if(bitfield.get(i)) {
                pieceStats.get(i).popularity--;
            }
        }
    }

    public List<Entry> getSortedStatistics() {
        Collections.sort(sortedStats, (o1, o2) -> o2.popularity - o1.popularity);
        return sortedStats;
    }

    public int getPopularity(int pieceNumber) {
        return pieceStats.get(pieceNumber).popularity;
    }


    public static class Entry {
        public int position;
        public int popularity;
        public boolean downloaded;
        public boolean downloading;

        public Entry(int position) {
            this.position = position;
        }
    }
}
