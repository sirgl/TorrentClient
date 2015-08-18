package torrent.queue;

import torrent.communication.CommunicationController;
import torrent.communication.SendService;

import java.net.InetAddress;

public class TaskBuilderImpl implements TaskBuilder {
    private final SendService sendService;
    private final CommunicationController communicationController;

    public TaskBuilderImpl(SendService sendService, CommunicationController communicationController) {
        this.sendService = sendService;
        this.communicationController = communicationController;
    }

    @Override
    public PeerAdditionRequest getPeerAdditionRequest(InetAddress address) {
        return new PeerAdditionRequest(address, communicationController);
    }

    @Override
    public PeerDeletionRequest getPeerDeletionRequest(long id, String reason) {
        return new PeerDeletionRequest();
    }
}
