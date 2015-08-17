package torrent;

public class BadTorrentException extends Exception {
    public BadTorrentException(String message) {
        super(message);
    }
}
