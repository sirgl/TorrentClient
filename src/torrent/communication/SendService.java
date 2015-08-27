package torrent.communication;

public interface SendService {
    void sendMessage(byte[] message, long peerId);
}
