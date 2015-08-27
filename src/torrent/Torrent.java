package torrent;

import com.sun.istack.internal.NotNull;

import java.util.List;
import java.util.Optional;

public class Torrent {
    private final String announce;
    //info
    private final int length;
    private final String name;
    private final int pieceLength;
    private final byte[] pieces;

    private final Optional<String> comment;
    private final Optional<String> createdBy;
    private final Optional<String> creationDate;
    private final Optional<byte[]> md5;
    private final Optional<List<String>> announceList;

    /**
     * @param announce tracker URL
     * @param length length of the file in bytes
     * @param name name of the file
     * @param pieceLength length of each piece in bytes
     * @param pieces SHA hashes of pieces
     * @param comment
     * @param createdBy
     * @param creationDate
     * @param md5
     * @param announceList
     */
    public Torrent(@NotNull String announce,
                   int length,
                   @NotNull String name,
                   int pieceLength,
                   @NotNull byte[] pieces,
                   @NotNull Optional<String> comment,
                   @NotNull Optional<String> createdBy,
                   @NotNull Optional<String> creationDate,
                   @NotNull Optional<byte[]> md5,
                   @NotNull Optional<List<String>> announceList) {
        this.announce = announce;
        this.length = length;
        this.name = name;
        this.pieceLength = pieceLength;
        this.pieces = pieces;
        this.comment = comment;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.md5 = md5;
        this.announceList = announceList;
    }

    public String getAnnounce() {
        return announce;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public int getPieceLength() {
        return pieceLength;
    }

    public byte[] getPieces() {
        return pieces;
    }

    public Optional<String> getComment() {
        return comment;
    }

    public Optional<String> getCreatedBy() {
        return createdBy;
    }

    public Optional<String> getCreationDate() {
        return creationDate;
    }

    public Optional<byte[]> getMd5() {
        return md5;
    }

    public Optional<List<String>> getAnnounceList() {
        return announceList;
    }

    public byte[] getInfoHash() {
        return new byte[20];
    }
}
