import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TimedCache<String, String> cache = new TimedCache<>(5000);
        cache.put("key1", "value1");
        System.out.println("key1: " + cache.get("key1"));
        Thread.sleep(6000);
        System.out.println("key1 after 6 seconds: " + cache.get("key1"));
        cache.put("key2", "value2");
        Thread.sleep(3000);
        System.out.println("key2 after 3 seconds: " + cache.get("key2"));
        Thread.sleep(3000);
        System.out.println("key2 after 6 seconds: " + cache.get("key2"));
    }
}

class TimedCache<K, V> {
    private final HashMap<K, ValueWithTimestamp<V>> cache = new HashMap<>();
    private final long lifespanInMillis;

    public TimedCache(long lifespanInMillis) {
        this.lifespanInMillis = lifespanInMillis;
    }

    public void put(K key, V value) {
        cache.put(key, new ValueWithTimestamp<>(value, System.currentTimeMillis()));
    }

    public V get(K key) {
        ValueWithTimestamp<V> item = cache.get(key);
        if (item != null && (System.currentTimeMillis() - item.getTimestamp()) < lifespanInMillis) {
            return item.getValue();
        }
        cache.remove(key);
        return null;
    }

    private static class ValueWithTimestamp<V> {
        private final V value;
        private final long timestamp;

        public ValueWithTimestamp(V value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }

        public V getValue() {
            return value;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
