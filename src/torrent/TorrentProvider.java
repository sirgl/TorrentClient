package torrent;

import java.io.IOException;

public interface TorrentProvider {
    Torrent getTorrent() throws IOException, BadTorrentException;
}
