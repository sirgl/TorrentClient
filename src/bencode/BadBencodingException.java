package bencode;

public class BadBencodingException extends Exception {
    public BadBencodingException(String s) {
        super(s);
    }

    public BadBencodingException() {
    }
}
