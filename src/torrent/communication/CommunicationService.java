package torrent.communication;

import java.net.InetAddress;

public class CommunicationService implements SendServiceInterface {

    @Override
    public void sendMessage(byte[] message, String peerId) {

    }


    /**
     * @implNote To avoid deadlock it must be used only by queue handler thread
     */
    @Override
    public void addNewPeer(InetAddress address) {

    }


}
