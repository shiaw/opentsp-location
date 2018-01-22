package com.navinfo.opentsp.platform.push.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.DeliveringState;
import com.navinfo.opentsp.platform.push.api.ScheduledTasksStorage;
import com.navinfo.opentsp.platform.push.api.StoredScheduledTask;
import com.navinfo.opentsp.platform.util.RedisJacksonSerializer;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * In memory schedule storage. It use redis.
 */
public class InMemoryScheduleStorage implements ScheduledTasksStorage {
    /**
     * Prefix is must be an unique per data group. It need for isolation keys between different data groups (like sql database tables).
     */
    private static final String _PREFIX = InMemoryScheduleStorage.class.getName() + ":";
    private static final String KEY_TASK = "task";
    private static final String KEY_STORED_TIME = "storedTime";
    public static final String KEY_DELIVERING_PREFIX = "delivering.";
    private final RedisTemplate<String, Object> redis;
    private final ScheduleStateWatcher watcher;

    public InMemoryScheduleStorage(RedisConnectionFactory redisConnectionFactory, ObjectMapper objectMapper, ScheduleStateWatcher watcher) {
        this.watcher = watcher;

        redis = new RedisTemplate<>();
        redis.setConnectionFactory(redisConnectionFactory);
        redis.setKeySerializer(new StringRedisSerializer(StandardCharsets.UTF_8));
        redis.setHashValueSerializer(new RedisJacksonSerializer(objectMapper, new RedisJacksonSerializer.TypeAsIndexMapper(
                ScheduledTask.class, DeliveringState.class, Long.class)));
        redis.afterPropertiesSet();
    }

    @Override
    public void store(final StoredScheduledTask schedule) {
        final String id = schedule.getTask().getId();
        final Object key = asKey(id);
        redis.execute(new SessionCallback<Boolean>() {
            @Override
            public <K, V> Boolean execute(RedisOperations<K, V> operations) throws DataAccessException {
                HashOperations hops = redis.opsForHash();
                Boolean ok = hops.putIfAbsent(key, KEY_TASK, schedule.getTask());
                if (!ok) {
                    throw new RuntimeException("Already has " + id + " task.");
                }
                Map<String, Object> map = new HashMap<>();
                map.put(KEY_STORED_TIME, schedule.getStoredTime().getTime());
                for (DeliveringState status : schedule.getDeliveringStates()) {
                    map.put(getStateKey(status), status);
                }
                hops.putAll(key, map);
                operations.expire((K) key, schedule.getTask().getTtl(), TimeUnit.SECONDS);
                return true;
            }
        });
        watcher.notifyTaskState(schedule);
    }

    private String asKey(String id) {
        return _PREFIX + id;
    }


    @Override
    public Collection<String> getAllSchedules() {
        // we not use KEYS command, due redis doc prevent us from this
//        final ScanOptions options = ScanOptions.scanOptions()
//                .match(_PREFIX + "*")
//                .count(1000)// count note server that we expect batches with specified size
//                .build();
//        final RedisSerializer<?> keySerializer = redis.getKeySerializer();
//        return redis.execute(new RedisCallback<Set<String>>() {
//
//            public Set<String> doInRedis(RedisConnection connection) {
//                Cursor<byte[]> cursor = connection.scan(options);
//                Set<String> keys = new HashSet<>();
//                while (cursor.hasNext()) {
//                    Object key = keySerializer.deserialize(cursor.next());
//                    keys.add(key.toString().substring(_PREFIX.length()));
//                }
//                return Collections.unmodifiableSet(keys);
//            }
//        });
      return   redis.keys(_PREFIX + "*");
    }

    @Override
    public StoredScheduledTask getSchedule(String id) {
        final String key = asKey(id);
        final HashOperations<String, String, Object> hops = redis.opsForHash();
        final Set<String> keys = hops.keys(key);
        if (CollectionUtils.isEmpty(keys)) {
            return null;
        }
        StoredScheduledTask sst = new StoredScheduledTask();
        sst.setTask((ScheduledTask) hops.get(key, KEY_TASK));
        List<DeliveringState> states = new ArrayList<>();
        for (Object entryKey : keys) {
            if (!entryKey.toString().startsWith(KEY_DELIVERING_PREFIX)) {
                continue;
            }
            DeliveringState state = (DeliveringState) hops.get(key, entryKey);
            states.add(state);
        }
        sst.setDeliveringStates(states);
        sst.setStoredTime(new Date((Long) hops.get(key, KEY_STORED_TIME)));
        return sst;
    }

    @Override
    public boolean updateDelivering(final String id, final DeliveringState oldState, final DeliveringState newState) {
        final HashOperations<String, Object, Object> hops = redis.opsForHash();
        final String key = asKey(id);
        final Object stateKey = getStateKey(oldState);
        return redis.execute(new SessionCallback<Boolean>() {
            @Override
            public <K, V> Boolean execute(RedisOperations<K, V> operations) throws DataAccessException {
                //TODO potentially below code is unsafe, and we need more deep investigation of redis behavior in this case
                // we need to resolve: guarantee 'redis.execute(' concurrency safe or not. If not then we need to rewrite code for safe.
                DeliveringState state = (DeliveringState) hops.get(key, stateKey);
                if (state == null) {
                    return false;
                }
                if (!state.equals(oldState)) {
                    return false;
                }
                hops.put(key, stateKey, newState);
                watcher.updateState(new StoredScheduledTaskSupplier(id), newState);
                return true;
            }
        });
    }

    @Override
    public void endSchedule(StoredScheduledTask task) {
        final String key = asKey(task.getTask().getId());
        watcher.notifyTaskState(task);
        redis.delete(key);
    }

    private String getStateKey(DeliveringState state) {
        return KEY_DELIVERING_PREFIX + state.getId();
    }

    private class StoredScheduledTaskSupplier implements Supplier<StoredScheduledTask> {
        private final String id;

        StoredScheduledTaskSupplier(String id) {
            this.id = id;
        }

        @Override
        public StoredScheduledTask get() {
            return getSchedule(id);
        }
    }
}
