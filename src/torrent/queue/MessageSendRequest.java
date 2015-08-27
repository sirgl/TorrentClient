package torrent.queue;

import torrent.communication.SendService;

public class MessageSendRequest implements Runnable {
    private final SendService sendService;
    private final byte[] message;
    private final long id;

    public MessageSendRequest(SendService sendService, byte[] message, long id) {
        this.sendService = sendService;
        this.message = message;
        this.id = id;
    }

    @Override
    public void run() {
           sendService.sendMessage(message, id);
    }
}
