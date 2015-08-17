package bencode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BencodeList implements BencodeItem, Iterable<BencodeItem> {
    private final List<BencodeItem> list;

    public BencodeList() {
        this.list = new ArrayList<>();
    }

    public void addItem(BencodeItem item) {
        list.add(item);
    }

    @Override
    public void serialize(OutputStream outputStream) throws IOException {
        outputStream.write('l');
        for (BencodeItem item : list) {
            item.serialize(outputStream);
        }
        outputStream.write('e');
    }

    @Override
    public Iterator<BencodeItem> iterator() {
        return list.iterator();
    }
}
