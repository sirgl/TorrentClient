package torrent.queue;

import torrent.communication.CommunicationController;
import torrent.communication.PeerManager;
import torrent.communication.SendService;

import java.net.InetSocketAddress;

public class TaskBuilderImpl implements TaskBuilder {
    private final PeerManager peerManager;
    private SendService sendService;
    private CommunicationController communicationController;

    public TaskBuilderImpl(PeerManager peerManager) {
        this.peerManager = peerManager;
    }

    public void setCommunicationController(CommunicationController communicationController) {
        this.communicationController = communicationController;
    }

    public void setSendService(SendService sendService) {
        this.sendService = sendService;
    }

    @Override
    public PeerAdditionRequest getPeerAdditionRequest(InetSocketAddress address) {
        return new PeerAdditionRequest(address, communicationController);
    }

    @Override
    public PeerDeletionRequest getPeerDeletionRequest(long id, String reason) {
        return new PeerDeletionRequest(communicationController, reason, id, peerManager);
    }

    @Override
    public PeerRegistrationRequest getPeerRegistrationRequest(long id, byte[] name) {
        return new PeerRegistrationRequest(id, name, peerManager);
    }

    @Override
    public PeerCreationRequest getPeerCreationRequest(long id) {
        return new PeerCreationRequest(id, peerManager);
    }

    @Override
    public MessageSendRequest getMessageSendRequest(long id, byte[] message) {
        return new MessageSendRequest(sendService, message, id);
    }
}
