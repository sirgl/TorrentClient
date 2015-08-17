package torrent;

import bencode.*;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TorrentParser implements TorrentProviderInteface {
    private final BencodeParser parser;

    public TorrentParser(String torrentPath) throws FileNotFoundException {
        parser = new BencodeParser(new FileInputStream(torrentPath));
    }

    @Override
    @NotNull
    public Torrent getTorrent() throws IOException, BadTorrentException {
        try {
            BencodeDictionary dictionary = (BencodeDictionary) parser.parse();
            Map<String, BencodeItem> map = dictionary.getMap();

            String announce = getDictionaryString(map, "announce", false);
            List<String> announceList = getAnnounceList(map);
            String comment = getDictionaryString(map, "comment", true);
            String createdBy = getDictionaryString(map, "created by", true);
            String creationDate = getDictionaryString(map, "creation date", true);
            Map<String, BencodeItem> infoMap = getInfoMap(map);

            //info dictionary
            BencodeItem bencodeFilesItem = map.get("files");
            if(bencodeFilesItem != null) {
                //TODO multiple files handling
                throw new NotImplementedException();
            } else {
                int length = getDictionaryInt(infoMap, "length");
                String md5Sum = getDictionaryString(infoMap, "md5sum", true);
                String name = getDictionaryString(infoMap, "name", false);
                int pieceLength = getDictionaryInt(infoMap, "piece length");
                String piecesStr = getDictionaryString(infoMap, "pieces", false);
                byte[] pieces = piecesStr.getBytes();
            }
        } catch (BadBencodingException e) {
            throw new BadTorrentException("Bad file encoding: " + e.getMessage());
        }
        return null;
    }

    private Map<String, BencodeItem> getInfoMap(Map<String, BencodeItem> map) throws BadTorrentException {
        BencodeItem bencodeInfoMap = map.get("info");
        if(bencodeInfoMap == null) {
            throw new BadTorrentException("'info' key not found in file");
        }
        return ((BencodeDictionary) bencodeInfoMap).getMap();
    }

    @Nullable
    private List<String> getAnnounceList(@NotNull Map<String, BencodeItem> map) {
        List<String> announceList = null;
        BencodeItem bencodeAnnounceList = map.get("announce-list");
        if (bencodeAnnounceList != null) {
            announceList = new ArrayList<>();
            for (BencodeItem item : ((BencodeList) bencodeAnnounceList)) {
                announceList.add(((BencodeString) item).getString());
            }
        }
        return announceList;
    }

    @Nullable
    private String getDictionaryString(
            @NotNull Map<String, BencodeItem> map,
            @NotNull String key,
            boolean optional) throws BadTorrentException {
        BencodeItem item = map.get(key);
        if (item == null) {
            if (!optional) {
                throw new BadTorrentException("'" + key + "' key not found in file");
            }
            return null;
        }
        return ((BencodeString) item).getString();
    }

    private int getDictionaryInt(
            @NotNull Map<String, BencodeItem> map,
            @NotNull String key) throws BadTorrentException {
        BencodeItem item = map.get(key);
        if (item == null) {
            throw new BadTorrentException("'" + key + "' key not found in file");
        }
        return ((BencodeInteger) item).getInteger();
    }
}
