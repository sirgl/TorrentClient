package torrent;

import java.io.IOException;

public interface TorrentProviderInteface {
    Torrent getTorrent() throws IOException, BadTorrentException;
}
