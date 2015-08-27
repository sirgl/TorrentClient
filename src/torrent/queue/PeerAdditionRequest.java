package torrent.queue;

import torrent.communication.CommunicationController;
import torrent.communication.SendService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * request from controller to queue to add a peer with known address
 */
public class PeerAdditionRequest implements Runnable {
    private final InetSocketAddress address;
    private final CommunicationController communicationController;

    PeerAdditionRequest(InetSocketAddress address, CommunicationController communicationController) {
        this.address = address;
        this.communicationController = communicationController;
    }

    @Override
    public void run() {
        try {
            communicationController.addNewPeer(address);
        } catch (IOException e) {
            //TODO addition peer fails policy
        }
    }
}
