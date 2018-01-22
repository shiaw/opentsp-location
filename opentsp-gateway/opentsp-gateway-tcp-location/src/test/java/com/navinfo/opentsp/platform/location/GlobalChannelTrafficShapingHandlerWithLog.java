package com.navinfo.opentsp.platform.location;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import io.netty.handler.traffic.GlobalChannelTrafficShapingHandler;
import io.netty.handler.traffic.TrafficCounter;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

/**
 * User: zhanhk
 * Date: 16/7/18
 * Time: 下午1:55
 */
public class GlobalChannelTrafficShapingHandlerWithLog extends GlobalChannelTrafficShapingHandler {
    private static final InternalLogger logger =
            InternalLoggerFactory.getInstance(GlobalChannelTrafficShapingHandlerWithLog.class);

    private final List<Long> cumulativeWrittenBytes = new LinkedList<Long>();
    private final List<Long> cumulativeReadBytes = new LinkedList<Long>();
    private final List<Long> throughputWrittenBytes = new LinkedList<Long>();
    private final List<Long> throughputReadBytes = new LinkedList<Long>();

    public GlobalChannelTrafficShapingHandlerWithLog(ScheduledExecutorService executor, long writeLimit,
                                                     long readLimit, long writeChannel, long readChannel, long checkInterval, long maxTime) {
        super(executor, writeLimit, readLimit, writeChannel, readChannel, checkInterval, maxTime);
    }

    public GlobalChannelTrafficShapingHandlerWithLog(ScheduledExecutorService executor, long writeLimit,
                                                     long readLimit, long writeChannel, long readChannel, long checkInterval) {
        super(executor, writeLimit, readLimit, writeChannel, readChannel, checkInterval);
    }

    public GlobalChannelTrafficShapingHandlerWithLog(ScheduledExecutorService executor, long writeLimit,
                                                     long readLimit, long writeChannel, long readChannel) {
        super(executor, writeLimit, readLimit, writeChannel, readChannel);
    }

    public GlobalChannelTrafficShapingHandlerWithLog(ScheduledExecutorService executor, long checkInterval) {
        super(executor, checkInterval);
    }

    public GlobalChannelTrafficShapingHandlerWithLog(EventExecutor executor) {
        super(executor);
    }

    /**
     * Override to compute average of bandwidth between all channels
     */
    @Override
    protected void doAccounting(TrafficCounter counter) {
        long maxWrittenNonZero = this.maximumCumulativeWrittenBytes();
        if (maxWrittenNonZero == 0) {
            maxWrittenNonZero = 1;
        }
        long maxReadNonZero = this.maximumCumulativeReadBytes();
        if (maxReadNonZero == 0) {
            maxReadNonZero = 1;
        }
        for (TrafficCounter tc : this.channelTrafficCounters()) {
            long cumulativeWritten = tc.cumulativeWrittenBytes();
            if (cumulativeWritten > maxWrittenNonZero) {
                cumulativeWritten = maxWrittenNonZero;
            }
            cumulativeWrittenBytes.add((maxWrittenNonZero - cumulativeWritten) * 100 / maxWrittenNonZero);
            throughputWrittenBytes.add(tc.getRealWriteThroughput() >> 10);
            long cumulativeRead = tc.cumulativeReadBytes();
            if (cumulativeRead > maxReadNonZero) {
                cumulativeRead = maxReadNonZero;
            }
            cumulativeReadBytes.add((maxReadNonZero - cumulativeRead) * 100 / maxReadNonZero);
            throughputReadBytes.add(tc.lastReadThroughput() >> 10);
        }
        logger.info(this.toString() + " QueuesSize: " + queuesSize()
                + "\nWrittenBytesPercentage: " + cumulativeWrittenBytes
                + "\nWrittenThroughputBytes: " + throughputWrittenBytes
                + "\nReadBytesPercentage:    " + cumulativeReadBytes
                + "\nReadThroughputBytes:    " + throughputReadBytes);
        cumulativeWrittenBytes.clear();
        cumulativeReadBytes.clear();
        throughputWrittenBytes.clear();
        throughputReadBytes.clear();
        super.doAccounting(counter);
    }
}
