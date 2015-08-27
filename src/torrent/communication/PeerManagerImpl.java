package torrent.communication;

import torrent.Peer;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class PeerManagerImpl implements PeerManager {
    //TODO synchronization???
    Map<Long, Peer> idPeerMap = new HashMap<>();
    Map<byte[], Long> nameMap = new HashMap<>();

    @Override
    public Peer getPeer(long id) {
        return idPeerMap.get(id);
    }

    @Override
    public long getPeerId(ByteBuffer name) {
        return nameMap.get(name);
    }

    @Override
    public void addPeer(long id) {
        idPeerMap.put(id, new Peer());
    }

    //TODO synchronization????
    @Override
    public void registerPeer(long id, byte[] name) {
        Peer peer = idPeerMap.get(id);
        if (peer == null) {
            //TODO
        }
        peer.setName(name);
        nameMap.put(name, id);
    }

    @Override
    public void removePeer(long id) {
        Peer peer = idPeerMap.get(id);
        idPeerMap.remove(id);
        nameMap.remove(peer.getName());
    }

    @Override
    public boolean hasPeer(long id) {
        return idPeerMap.containsKey(id);
    }
}
