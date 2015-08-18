package torrent.queue;

import torrent.communication.CommunicationController;
import torrent.communication.SendService;

import java.net.InetAddress;

public class PeerAdditionRequest implements Runnable {
    private final InetAddress address;
    private final CommunicationController communicationController;

    PeerAdditionRequest(InetAddress address, CommunicationController communicationController) {
        this.address = address;
        this.communicationController = communicationController;
    }

    @Override
    public void run() {
        communicationController.addNewPeer(address);
    }
}
