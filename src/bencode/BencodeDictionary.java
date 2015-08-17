package bencode;

import bencode.BencodeItem;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class BencodeDictionary implements BencodeItem {
    private final Map<String, BencodeItem> dictionary;

    public BencodeDictionary() {
        dictionary = new TreeMap<>();
    }

    public void addItem(String key, BencodeItem value) {
        dictionary.put(key, value);
    }

    public Map<String, BencodeItem> getMap() {
        return dictionary;
    }

    @Override
    public void serialize(OutputStream outputStream) throws IOException {
        outputStream.write('d');
        for (Map.Entry<String, BencodeItem> itemEntry : dictionary.entrySet()) {
            new BencodeString(itemEntry.getKey()).serialize(outputStream);
            itemEntry.getValue().serialize(outputStream);
        }
        outputStream.write('e');
    }

}
