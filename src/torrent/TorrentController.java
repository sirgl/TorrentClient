package torrent;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class TorrentController {
    private final List<Peer> peers;

    public TorrentController() {
        peers = new ArrayList<>();
    }

    public void addPeer(SocketChannel channel) {
//        peers.add(new Peer(channel));
    }
}
