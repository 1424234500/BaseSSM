package util.mode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import util.Lang;

public class Bean extends HashMap<Object, Object> {
	public static final String KEY_ID = "_PK_";
	private static final long serialVersionUID = -4862368161501105005L;

	public Bean() {
	}

	public Bean(String id) {
		this.setId(id);
	}

	public Bean(HashMap<Object, Object> values) {
		super(values);
	}

	public final String getId() {
		return this.getStr("_PK_");
	}

	public Bean setId(String id) {
		this.set("_PK_", id);
		return this;
	}

	public Bean set(Object key, Object obj) {
		this.put(key, obj);
		return this;
	}

	public final Object get(Object key, Object def) {
		return this.containsKey(key) ? this.get(key) : def;
	}

	public final String get(Object key, String def) {
		return Lang.to(this.get(key), def);
	}

	public final int get(Object key, int def) {
		return Lang.to(this.get(key), def);
	}

	public final long get(Object key, long def) {
		return Lang.to(this.get(key), def);
	}

	public final double get(Object key, double def) {
		return Lang.to(this.get(key), def);
	}

	public final boolean get(Object key, boolean def) {
		return Lang.to(this.get(key), def);
	}

	public final <T> List<T> get(Object key, List<T> def) {
		return this.contains(key) ? (List) this.get(key) : def;
	}

	public final <M, N> Map<M, N> get(Object key, Map<M, N> def) {
		return this.contains(key) ? (Map) this.get(key) : def;
	}

	public final <M, N> LinkedHashMap<M, N> get(Object key, LinkedHashMap<M, N> def) {
		return this.contains(key) ? (LinkedHashMap) this.get(key) : def;
	}

	public final String getStr(Object key) {
		return this.get(key, "");
	}

	public final int getInt(Object key) {
		return this.get(key, 0);
	}

	public final long getLong(Object key) {
		return this.get(key, 0L);
	}

	public final double getDouble(Object key) {
		return this.get(key, 0.0D);
	}

	public final boolean getBoolean(Object key) {
		return this.get(key, false);
	}

	public final Bean getBean(Object key) {
		return this.contains(key) ? (Bean) this.get(key) : new Bean();
	}

	public final <T> List<T> getList(Object key) {
		return (List) (this.contains(key) ? (List) this.get(key) : new ArrayList());
	}

	public final <T> T[] getArray(Class<T> type, Object key) {
      return (T[]) (this.contains(key)?(Object[])((Object[])this.get(key)):(Object[])((Object[])Array.newInstance(type, 0)));
   }

	public final <M, N> Map<M, N> getMap(Object key) {
		return (Map) (this.contains(key) ? (Map) this.get(key) : new HashMap());
	}

	public final <M, N> LinkedHashMap<M, N> getLinkedMap(Object key) {
		return this.contains(key) ? (LinkedHashMap) this.get(key) : new LinkedHashMap();
	}

	public final boolean contains(Object key) {
		return this.containsKey(key);
	}

	public final boolean isEmpty(Object key) {
		boolean bEmpty = true;
		if (this.contains(key)) {
			Object value = this.get(key);
			if (value != null) {
				if (value instanceof Number) {
					bEmpty = ((Number) value).intValue() == 0;
				} else if (value instanceof String) {
					bEmpty = ((String) value).isEmpty();
				} else {
					bEmpty = false;
				}
			}
		}
		return bEmpty;
	}

	public final boolean isNotEmpty(Object key) {
		return !this.isEmpty(key);
	}

	public Bean remove(Object key) {
		if (this.containsKey(key)) {
			super.remove(key);
		}
		return this;
	}

	public boolean equals(Bean obj) {
		return this.getId().equals(obj.getId());
	}

	public int hashCode() {
		return this.getId().hashCode();
	}

	public Bean copyOf() {
		return this.copyOf((Object[]) null);
	}

	public Bean copyOf(Object... keys) {
		Bean tar = new Bean();
		if (keys != null) {
			Object[] arg2 = keys;
			int arg3 = keys.length;
			for (int arg4 = 0; arg4 < arg3; ++arg4) {
				Object key = arg2[arg4];
				tar.set(key, this.get(key));
			}
		} else {
			tar.putAll(this);
		}
		return tar;
	}

	public void copyFrom(Bean bean) {
		this.copyFrom(bean, (Object[]) null);
	}

	public void copyFrom(Bean bean, Object... keys) {
		if (keys != null) {
			Object[] arg2 = keys;
			int arg3 = keys.length;
			for (int arg4 = 0; arg4 < arg3; ++arg4) {
				Object key = arg2[arg4];
				if (bean.contains(key)) {
					this.set(key, bean.get(key));
				}
			}
		} else {
			this.putAll(bean);
		}
	}
}
