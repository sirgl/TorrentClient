package torrent.communication;

import java.net.Inet4Address;
import java.net.InetAddress;

public interface SendServiceInterface  {
    void sendMessage(byte[] message, String peerId);

    void addNewPeer(InetAddress address);
}
