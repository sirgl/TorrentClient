package torrent.communication;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class CommunicationService implements SendService, CommunicationController {

    @Override
    public void sendMessage(byte[] message, ByteBuffer peerId) {

    }


    /**
     * @implNote To avoid deadlock it must be used only by queue handler thread
     */
    @Override
    public void addNewPeer(InetAddress address) {

    }

    @Override
    public void deletePeer(long peerId) {

    }


}
