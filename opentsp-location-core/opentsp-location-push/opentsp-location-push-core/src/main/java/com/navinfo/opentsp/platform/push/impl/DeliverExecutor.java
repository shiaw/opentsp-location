package com.navinfo.opentsp.platform.push.impl;

import com.google.common.util.concurrent.Runnables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.Phased;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This code schedule execution of {@link com.navinfo.opentsp.platform.push.impl.DeliverWorker}.
 */
@Component
public class DeliverExecutor implements SmartLifecycle {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeliverExecutor.class);
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private final DeliverWorker worker;
    private final AtomicReference<ScheduledFuture<?>> scheduleRef = new AtomicReference<>();
    private final DeliveryProcessConfigurationBean configurationBean;

    @Autowired
    public DeliverExecutor(DeliverWorker worker, DeliveryProcessConfigurationBean configurationBean) {
        this.worker = worker;
        this.configurationBean = configurationBean;
    }

    @Override
    public void start() {
        long delay = (long)(configurationBean.getDelay() * 1000f);
        if(delay <= 0) {
            delay = 1000l;
        }
        this.scheduleRef.set(executorService.scheduleWithFixedDelay(this.worker.getWorker(), delay, delay, TimeUnit.MILLISECONDS));
    }

    @Override
    public void stop() {
        stop(Runnables.doNothing());
    }

    @Override
    public boolean isRunning() {
        return this.scheduleRef.get() != null;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        ScheduledFuture<?> local = this.scheduleRef.getAndSet(null);
        local.cancel(true);
        callback.run();
    }

    @Override
    public int getPhase() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
