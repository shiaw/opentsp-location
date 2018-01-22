package com.navinfo.opentsp.platform.push.impl;

import com.navinfo.opentsp.platform.push.api.LocalMessageEvent;
import com.navinfo.opentsp.platform.push.api.LocalMessageListener;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Hub for message schedule events.
 */
@Component
public class LocalMessageEventHub implements LocalMessageListener {

    private final CopyOnWriteArrayList<LocalMessageListener> consumers = new CopyOnWriteArrayList<>();
    private final AtomicReference<ObjectFactory<Collection<LocalMessageListener>>> ref = new AtomicReference<>();

    /**
     * Create hub with predefined listeners, allow null.
     * @param consumers
     */
    @Autowired(required = false)
    public LocalMessageEventHub(ObjectFactory<Collection<LocalMessageListener>> consumers) {
        ref.set(consumers);
    }

    public void addConsumer(LocalMessageListener consumer) {
        this.consumers.addIfAbsent(consumer);
    }

    public void removeConsumer(LocalMessageListener consumer) {
        this.consumers.remove(consumer);
    }

    @Override
    public void accept(LocalMessageEvent event) {
        lazyInit();
        for(LocalMessageListener consumer: consumers) {
            consumer.accept(event);
        }
    }

    private void lazyInit() {
        // we use simply get because it potentially more fast then getAndSet
        ObjectFactory<Collection<LocalMessageListener>> factory = ref.get();
        if(factory == null) {
            return;
        }
        if(!ref.compareAndSet(factory, null)) {
            return;
        }
        try {
            Collection<LocalMessageListener> consumers = factory.getObject();
            this.consumers.addAll(consumers);
        } catch (NoSuchBeanDefinitionException e) {
            // we support cases when no consumers, but must log this error
            LoggerFactory.getLogger(getClass()).error("Error on setup core listeners", e);
        }
    }
}
