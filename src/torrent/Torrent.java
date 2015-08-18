package torrent;

import com.sun.istack.internal.NotNull;

import java.util.List;

public class Torrent {
    private final String announce;
    //info
    private final int length;
    private final String name;
    private final int pieceLength;
    private final byte[] pieces;
    private List<String> announceList;
    private String comment;
    private String createdBy;
    private String creationDate;
    private byte[] md5;

    public Torrent(@NotNull String announce,
                   int length,
                   @NotNull String name,
                   int pieceLength,
                   @NotNull byte[] pieces) {
        this.announce = announce;
        this.length = length;
        this.name = name;
        this.pieceLength = pieceLength;
        this.pieces = pieces;
    }

    public void setAnnounceList(List<String> announceList) {
        this.announceList = announceList;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public void setMd5(byte[] md5) {
        this.md5 = md5;
    }

    public byte[] getInfoHash() {
        return new byte[20];
    }
}
