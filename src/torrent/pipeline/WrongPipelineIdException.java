package torrent.pipeline;

public class WrongPipelineIdException extends RuntimeException {
    public WrongPipelineIdException(long id) {
        super(String.valueOf(id));
    }
}
