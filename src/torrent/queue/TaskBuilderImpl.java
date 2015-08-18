package torrent.queue;

import torrent.communication.CommunicationController;
import torrent.communication.PeerManager;
import torrent.communication.SendService;

import java.net.InetAddress;
import java.nio.ByteBuffer;

public class TaskBuilderImpl implements TaskBuilder {
    private final SendService sendService;
    private final CommunicationController communicationController;
    private final PeerManager peerManager;

    public TaskBuilderImpl(SendService sendService, CommunicationController communicationController, PeerManager peerManager) {
        this.sendService = sendService;
        this.communicationController = communicationController;
        this.peerManager = peerManager;
    }

    @Override
    public PeerAdditionRequest getPeerAdditionRequest(InetAddress address) {
        return new PeerAdditionRequest(address, communicationController);
    }

    @Override
    public PeerDeletionRequest getPeerDeletionRequest(long id, String reason) {
        return new PeerDeletionRequest(reason);
    }

    @Override
    public PeerRegistrationRequest getPeerRegistrationRequest(long id, ByteBuffer name) {
        return new PeerRegistrationRequest(id, name, peerManager);
    }
}
