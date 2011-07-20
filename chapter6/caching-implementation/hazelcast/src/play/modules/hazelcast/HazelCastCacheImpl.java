package play.modules.hazelcast;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import play.Logger;
import play.Play;
import play.cache.CacheImpl;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.IMap;

public class HazelCastCacheImpl implements CacheImpl {

	private HazelcastClient client;
	private static HazelCastCacheImpl instance;
	
	public static HazelCastCacheImpl getInstance() {
		if (instance == null) {
			instance = new HazelCastCacheImpl();
		}
		return instance;
	}
	
	private HazelCastCacheImpl() {
		String groupName = Play.configuration.getProperty("hazelcast.groupname", "dev");
		String groupPass = Play.configuration.getProperty("hazelcast.grouppass", "dev-pass");
		String[] addresses = Play.configuration.getProperty("hazelcast.addresses", "127.0.0.1:5701").replaceAll(" ", "").split(",");
		client = HazelcastClient.newHazelcastClient(groupName, groupPass, addresses);
	}
	
	private IMap getMap() {
		return client.getMap("default");
	}
	
	@Override
	public void add(String key, Object value, int expiration) {
		if (!getMap().containsKey(key)) {
			getMap().put(key, value, expiration, TimeUnit.SECONDS);
		}
	}

	@Override
	public boolean safeAdd(String key, Object value, int expiration) {
		getMap().putIfAbsent(key, value, expiration, TimeUnit.SECONDS);
		return getMap().get(key).equals(value);
	}

	@Override
	public void set(String key, Object value, int expiration) {
		getMap().put(key, value, expiration, TimeUnit.SECONDS);
	}

	@Override
	public boolean safeSet(String key, Object value, int expiration) {
		set(key, value, expiration);
		return true;
	}

	@Override
	public void replace(String key, Object value, int expiration) {
		if (getMap().containsKey(key)) {
			getMap().replace(key, value);
		}
	}

	@Override
	public boolean safeReplace(String key, Object value, int expiration) {
		if (getMap().containsKey(key)) {
			getMap().replace(key, value);
			return true;
		}
		return false;
	}

	@Override
	public Object get(String key) {
		return getMap().get(key);
	}

	@Override
	public Map<String, Object> get(String[] keys) {
		Map<String, Object> map = new HashMap(keys.length);
		for (String key : keys) {
			map.put(key, getMap().get(key));
		}
		return map;
	}

	@Override
	public long incr(String key, int by) {
		if (getMap().containsKey(key)) {
			getMap().lock(key);
			Object obj = getMap().get(key);
			if (obj instanceof Long) {
				Long number = (Long) obj;
				number += by;
				getMap().put(key, number);
			}
			getMap().unlock(key);
			return (Long) getMap().get(key);
		}
		return 0;
	}

	@Override
	public long decr(String key, int by) {
		return incr(key, -by);
	}

	@Override
	public void clear() {
		getMap().clear();
	}

	@Override
	public void delete(String key) {
		getMap().remove(key);
	}

	@Override
	public boolean safeDelete(String key) {
		if (getMap().containsKey(key)) {
			getMap().remove(key);
			return true;
		}
		return false;
	}

	@Override
	public void stop() {
		client.shutdown();
	}
}
