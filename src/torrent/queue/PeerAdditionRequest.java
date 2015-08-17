package torrent.queue;

import torrent.communication.SendServiceInterface;

import java.net.Inet4Address;

public class PeerAdditionRequest implements Runnable {
    private final Inet4Address address;
    private final SendServiceInterface sendService;

    public PeerAdditionRequest(Inet4Address address, SendServiceInterface sendService) {
        this.address = address;
        this.sendService = sendService;
    }

    @Override
    public void run() {
        sendService.addNewPeer(address);
    }
}
