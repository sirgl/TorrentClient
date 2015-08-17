package bencode;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class BencodeTest {
    @Test
    public void serializingIntegerShouldCorrespondSpecification() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        BencodeInteger bencodeInteger = new BencodeInteger(123);
        bencodeInteger.serialize(byteArrayOutputStream);
        String s = byteArrayOutputStream.toString();
        Assert.assertEquals(s, "i123e");
    }

    @Test
    public void serializingNegativeIntegerShouldCorrespondSpecification() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        BencodeInteger bencodeInteger = new BencodeInteger(-10);
        bencodeInteger.serialize(byteArrayOutputStream);
        String s = byteArrayOutputStream.toString();
        Assert.assertEquals("i-10e", s);
    }

    @Test
    public void serializingStringShouldCorrespondSpecification() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        BencodeString bencodeString = new BencodeString("Hello");
        bencodeString.serialize(byteArrayOutputStream);
        String s = byteArrayOutputStream.toString();
        Assert.assertEquals("5:Hello", s);
    }

    @Test
    public void serializingEmptyStringShouldBeCorrect() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        BencodeString bencodeString = new BencodeString("");
        bencodeString.serialize(byteArrayOutputStream);
        String s = byteArrayOutputStream.toString();
        Assert.assertEquals("0:", s);
    }

    @Test
    public void serializingListShouldBeCorrect() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        BencodeString bencodeString = new BencodeString("123");
        BencodeInteger bencodeInteger = new BencodeInteger(5);
        BencodeList bencodeList = new BencodeList();
        bencodeList.addItem(bencodeInteger);
        bencodeList.addItem(bencodeString);
        bencodeList.serialize(byteArrayOutputStream);
        String s = byteArrayOutputStream.toString();
        Assert.assertEquals("li5e3:123e", s);
    }

    @Test
    public void serializingEmptyListShouldBeCorrect() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        BencodeList bencodeList = new BencodeList();
        bencodeList.serialize(byteArrayOutputStream);
        String s = byteArrayOutputStream.toString();
        Assert.assertEquals("le", s);
    }

    @Test
    public void serializingEmptyDictionaryShouldBeCorrect() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        BencodeDictionary bencodeDictionary = new BencodeDictionary();
        bencodeDictionary.serialize(byteArrayOutputStream);
        String s = byteArrayOutputStream.toString();
        Assert.assertEquals("de", s);
    }

    @Test
    public void serializingDictionaryShouldBeCorrect() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        BencodeDictionary bencodeDictionary = new BencodeDictionary();
        bencodeDictionary.addItem("key1", new BencodeInteger(5));
        bencodeDictionary.addItem("key2", new BencodeString("hello"));
        bencodeDictionary.serialize(byteArrayOutputStream);
        String s = byteArrayOutputStream.toString();
        Assert.assertEquals("d4:key1i5e4:key25:helloe", s);
    }

    @Test
    public void serializingNestedListInDictionaryShouldBeCorrect() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(100);
        BencodeDictionary bencodeDictionary = new BencodeDictionary();
        BencodeList bencodeList = new BencodeList();
        bencodeList.addItem(new BencodeInteger(5));
        bencodeList.addItem(new BencodeInteger(2));
        bencodeDictionary.addItem("key", bencodeList);
        bencodeDictionary.serialize(byteArrayOutputStream);
        String s = byteArrayOutputStream.toString();
        Assert.assertEquals("d3:keyli5ei2eee", s);
    }

    //Parsing test

    @Test
    public void parsingIntShouldBeCorrect() throws IOException, BadBencodingException {
        byte[] buffer;
        buffer = "i-2525e".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        BencodeParser parser = new BencodeParser(byteArrayInputStream);
        BencodeInteger integer = (BencodeInteger) parser.parse();
        Assert.assertEquals(-2525, integer.getInteger());
    }

    @Test
    public void parsingStringShouldBeCorrect() throws IOException, BadBencodingException {
        byte[] buffer;
        buffer = "5:Hello".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        BencodeParser parser = new BencodeParser(byteArrayInputStream);
        BencodeString str = (BencodeString) parser.parse();
        Assert.assertEquals("Hello", str.getString());
    }

    @Test
    public void parsingStringShouldReadUnicodeLettersBeCorrect() throws IOException, BadBencodingException {
        byte[] buffer;
        buffer = "12:Привет".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        BencodeParser parser = new BencodeParser(byteArrayInputStream);
        BencodeString str = (BencodeString) parser.parse();
        Assert.assertEquals("Привет", str.getString());
    }

    @Test
    public void parsingEmptyStringShouldBeCorrect() throws IOException, BadBencodingException {
        byte[] buffer;
        buffer = "0:".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        BencodeParser parser = new BencodeParser(byteArrayInputStream);
        BencodeString str = (BencodeString) parser.parse();
        Assert.assertEquals("", str.getString());
    }

    @Test
    public void parsingListShouldBeCorrect() throws IOException, BadBencodingException {
        byte[] buffer;
        buffer = "li5e3:123e".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        BencodeParser parser = new BencodeParser(byteArrayInputStream);
        BencodeList list = (BencodeList) parser.parse();
        for (BencodeItem item : list) {
            if (item instanceof BencodeInteger) {
                Assert.assertEquals(5, ((BencodeInteger) item).getInteger());
                return;
            }
            if (item instanceof BencodeString) {
                Assert.assertEquals("123", ((BencodeString) item).getString());
                return;
            }
        }
    }

    @Test
    public void parsingDictionaryShouldBeCorrect() throws IOException, BadBencodingException {
        byte[] buffer;
        buffer = "d4:key15:Hello4:key2i5ee".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        BencodeParser parser = new BencodeParser(byteArrayInputStream);
        BencodeDictionary dictionary = (BencodeDictionary) parser.parse();
        Map<String, BencodeItem> map = dictionary.getMap();
        Assert.assertNotNull(map.get("key1"));
        Assert.assertNotNull(map.get("key2"));
        Assert.assertEquals("Hello", ((BencodeString) map.get("key1")).getString());
        Assert.assertEquals(5, ((BencodeInteger) map.get("key2")).getInteger());
    }

    @Test(expected = BadBencodingException.class)
    public void parsingDictionaryWithoutValueShouldThrowException() throws IOException, BadBencodingException {
        byte[] buffer;
        buffer = "d4:key1e".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        BencodeParser parser = new BencodeParser(byteArrayInputStream);
        parser.parse();
    }

    @Test(expected = BadBencodingException.class)
    public void parsingDictionaryWithWrongKeyClassShouldThrowException() throws IOException, BadBencodingException {
        byte[] buffer;
        buffer = "di5e5:valuee".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        BencodeParser parser = new BencodeParser(byteArrayInputStream);
        parser.parse();
    }

    @Test(expected = BadBencodingException.class)
    public void parsingWrongBencodeShouldThrowException() throws IOException, BadBencodingException {
        byte[] buffer;
        buffer = "sdfsd:".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        BencodeParser parser = new BencodeParser(byteArrayInputStream);
        parser.parse();
    }

    @Test(expected = BadBencodingException.class)
    public void parsingTooLongDecimalShouldThrowException() throws IOException, BadBencodingException {
        byte[] buffer;
        buffer = "i457839457893458349573489573495734859357e".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        BencodeParser parser = new BencodeParser(byteArrayInputStream);
        parser.parse();
    }

    @Test(expected = BadBencodingException.class)
    public void parsingTooLongDecimalAsStringLengthShouldThrowException() throws IOException, BadBencodingException {
        byte[] buffer;
        buffer = "457839457893458349573489573495734859357:234234".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
        BencodeParser parser = new BencodeParser(byteArrayInputStream);
        parser.parse();
    }
}

