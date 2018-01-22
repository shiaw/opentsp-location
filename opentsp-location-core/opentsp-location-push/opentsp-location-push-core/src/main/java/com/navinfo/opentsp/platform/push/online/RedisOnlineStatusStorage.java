package com.navinfo.opentsp.platform.push.online;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.MoreObjects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 */
@Component
public class RedisOnlineStatusStorage implements OnlineStatusStorage {

    private static final String PREFIX = RedisOnlineStatusStorage.class.getName();
    private final RedisTemplate<String, DeviceRegistration> template;

    @Autowired
    public RedisOnlineStatusStorage(ObjectMapper objectMapper, RedisConnectionFactory factory) {
        this.template = new RedisTemplate<>();
        this.template.setConnectionFactory(factory);
        this.template.setKeySerializer(new StringRedisSerializer());
        this.template.setValueSerializer(new Jackson2JsonRedisSerializer<>(DeviceRegistration.class));
        this.template.afterPropertiesSet();
    }

    private String asKey(String device) {
        return PREFIX + ":" + device;
    }

    @Override
    public void register(DeviceRegistration dr) {
        String device = dr.getDevice();
        BoundValueOperations<String, DeviceRegistration> ops = template.boundValueOps(asKey(device));
        ops.set(dr);
      //  updateExpire(ops, dr);
    }

    @Override
    public void heartbeat(String userDeviceId) {
        BoundValueOperations<String, DeviceRegistration> ops = template.boundValueOps(asKey(userDeviceId));
        DeviceRegistration dr = ops.get();
        updateExpire(ops, dr);

    }

    private void updateExpire(BoundValueOperations<String, DeviceRegistration> ops, DeviceRegistration dr) {
        // note that in cluster time in current node and redis storage node may differ, therefore you don't set small ttl
        ops.expire(dr.getTtl(), TimeUnit.MILLISECONDS);
    }

    @Override
    public DeviceRegistration get(String userDeviceId) {
        BoundValueOperations<String, DeviceRegistration> ops = template.boundValueOps(asKey(userDeviceId));
        DeviceRegistration dr = ops.get();
        return dr;
    }

    @Override
    public void unregister(String userDeviceId) {
        template.delete(asKey(userDeviceId));
    }


    public void enumerate(String deviceId, final Consumer<DeviceChooserContext> callback) {
        // we not use KEYS command, due redis doc prevent us from this
        final String pattern = asKey(deviceId);
        final ScanOptions options = ScanOptions.scanOptions()
                .match(pattern)
                .count(1000)// count note server that we expect batches with specified size
                .build();
        template.execute(new RedisCallback<Set<String>>() {

            public Set<String> doInRedis(RedisConnection connection) {
                DCC dcc = new DCC(connection.scan(options), pattern.length() - 1);
                while (dcc.hasNext()) {
                    dcc.next();
                    callback.accept(dcc);
                }
                return null;
            }
        });
    }

    private class DCC implements DeviceChooserContext {

        private final Cursor<byte[]> cursor;
        private final RedisSerializer<?> keySerializer;
        private final ValueOperations<String, DeviceRegistration> valueOps;
        private final int prefixLen;
        private boolean end = false;
        private String id;
        private Optional<DeviceRegistration> deviceRegistration;

        public DCC(Cursor<byte[]> cursor, int prefixLen) {
            this.keySerializer = template.getKeySerializer();
            this.valueOps = template.opsForValue();
            this.cursor = cursor;
            this.prefixLen = prefixLen;
        }

        void next() {
            byte[] serializedId = cursor.next();
            this.id = (String) keySerializer.deserialize(serializedId);
            this.deviceRegistration = null;
        }

        @Override
        public DeviceRegistration getDevice() {
            if (deviceRegistration == null) {
                this.deviceRegistration = Optional.ofNullable(valueOps.get(id));
            }
            // below strange code allow us to cache null values
            return deviceRegistration.orElse(null);
        }

        @Override
        public String getDeviceId() {
            return id == null ? null : id.substring(prefixLen);
        }

        @Override
        public void end() {
            end = true;
        }

        public boolean hasNext() {
            return !end && cursor.hasNext();
        }
    }
}
