package torrent.files;

import junit.framework.TestCase;
import org.junit.Assert;

import java.util.BitSet;
import java.util.List;

public class StatisticsTest extends TestCase {
    Statistics statistics;

    @Override
    public void setUp() throws Exception {
        statistics = new Statistics(10);
    }

    public void testStatisticsIncreases() throws Exception {
        statistics.increasePiecePopularity(2);
        statistics.increasePiecePopularity(2);
        statistics.increasePiecePopularity(2);
        statistics.increasePiecePopularity(2);
        statistics.decreasePiecePopularity(2);
        Assert.assertEquals(3, statistics.getPopularity(2));
    }

    public void testSetDownloaded() throws Exception {
        statistics.setDownloaded(2);
        Assert.assertTrue(statistics.isDownloaded(2));
    }


    public void testPutPeerStatistics() throws Exception {
        BitSet bitSet = new BitSet();
        bitSet.set(1);
        bitSet.set(2);
        statistics.putPeerStatistics(bitSet);
        statistics.putPeerStatistics(bitSet);
        Assert.assertEquals(2, statistics.getPopularity(1));
        Assert.assertEquals(2, statistics.getPopularity(2));
    }

    public void testRemovePeerStatistics() throws Exception {
        BitSet bitSet = new BitSet();
        bitSet.set(1);
        bitSet.set(2);
        statistics.putPeerStatistics(bitSet);
        statistics.putPeerStatistics(bitSet);
        BitSet bitSet1 = new BitSet();
        bitSet1.set(1);
        statistics.removePeerStatistics(bitSet1);
        Assert.assertEquals(1, statistics.getPopularity(1));
        Assert.assertEquals(2, statistics.getPopularity(2));
    }

    public void testGetSortedStatistics() throws Exception {
        BitSet bitSet = new BitSet();
        bitSet.set(1);
        bitSet.set(2);
        statistics.putPeerStatistics(bitSet);
        statistics.putPeerStatistics(bitSet);
        BitSet bitSet1 = new BitSet();
        bitSet1.set(1);
        statistics.removePeerStatistics(bitSet1);
        List<Statistics.Entry> sortedStatistics = statistics.getSortedStatistics();
        Assert.assertEquals(2, sortedStatistics.get(0).popularity);
        Assert.assertEquals(1, sortedStatistics.get(1).popularity);
        Assert.assertEquals(2, sortedStatistics.get(0).position);
        Assert.assertEquals(1, sortedStatistics.get(1).position);
    }
}