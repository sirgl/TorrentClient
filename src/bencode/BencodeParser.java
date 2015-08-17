package bencode;

import java.io.IOException;
import java.io.InputStream;

public class BencodeParser {
    private static final int MAX_INT_LENGTH = 15;
    private final InputStream inputStream;

    public BencodeParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public BencodeItem parse() throws IOException, BadBencodingException {
        byte ch = (byte) inputStream.read();
        if (Character.isDigit(ch)) {
            return parseString((char) ch);
        } else if (ch == 'i') {
            return parseInteger();
        } else if (ch == 'l') {
            return parseBencodeList();
        } else if (ch == 'd') {
            return parseBencodeDictionary();
        } else if (ch == 'e') {
            return null;
        }
        throw new BadBencodingException();
    }

    private BencodeInteger parseInteger() throws BadBencodingException, IOException {
        int readChars = 0;
        StringBuilder number = new StringBuilder();
        char ch = (char) inputStream.read();
        while (ch != 'e') {
            number.append(ch);
            readChars++;
            if (readChars > MAX_INT_LENGTH) {
                throw new BadBencodingException("Too long int");
            }
            ch = (char) inputStream.read();
        }
        return new BencodeInteger(Integer.parseInt(number.toString()));
    }

    private BencodeString parseString(char ch) throws IOException, BadBencodingException {
        int length = parseLength(ch);
        byte[] buffer = new byte[length];
        int totalRead = 0;
        int read;
        while(totalRead != length) {
            read = inputStream.read(buffer, totalRead, length - totalRead);
            totalRead += read;
        }
        return new BencodeString(new String(buffer));
    }

    private int parseLength(char ch) throws IOException, BadBencodingException {
        int length;
        int readChars = 1;
        StringBuilder lenString = new StringBuilder();
        do {
            lenString.append(ch);
            ch = (char) inputStream.read();
            readChars++;
            if (readChars > MAX_INT_LENGTH) {
                throw new BadBencodingException("Too long int");
            }
        } while (ch != ':');
        length = Integer.parseInt(lenString.toString());
        return length;
    }

    private BencodeList parseBencodeList() throws IOException, BadBencodingException {
        BencodeList bencodeItems = new BencodeList();
        BencodeItem item = parse();
        while (item != null) {
            bencodeItems.addItem(item);
            item = parse();
        }
        return bencodeItems;
    }

    private BencodeDictionary parseBencodeDictionary() throws IOException, BadBencodingException {
        BencodeItem item = parse();
        BencodeDictionary dictionary = new BencodeDictionary();
        String key;
        while (item != null) {
            if (!(item instanceof BencodeString)) {
                throw new BadBencodingException("Expected string as a key in dictionary");
            }
            key = ((BencodeString) item).getString();
            item = parse();
            if (item == null) {
                throw new BadBencodingException("Expected value, but end of dictionary found");
            }
            dictionary.addItem(key, item);
            item = parse();
        }
        return dictionary;
    }
}
