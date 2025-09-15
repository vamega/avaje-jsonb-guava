package com.bitfiddling.avaje.guava;

import com.google.common.collect.ImmutableMap;
import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;
import io.avaje.jsonb.AdapterFactory;
import io.avaje.jsonb.CustomAdapter;
import io.avaje.jsonb.Jsonb;
import io.avaje.jsonb.Types;
import java.lang.reflect.Type;

/**
 * JsonAdapter for Guava ImmutableMap.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
@CustomAdapter
public final class ImmutableMapAdapter<K, V> implements JsonAdapter<ImmutableMap<K, V>> {

    /** Factory for creating ImmutableMapAdapter instances. */
    public static final AdapterFactory FACTORY = (Type type, Jsonb jsonb) -> {
        if (Types.isGenericTypeOf(type, ImmutableMap.class)) {
            return new ImmutableMapAdapter<>(jsonb, Types.typeArguments(type));
        }
        return null;
    };

    private final JsonAdapter<V> valueAdapter;

    /**
     * Creates a new ImmutableMapAdapter.
     *
     * @param jsonb the Jsonb instance
     * @param types the generic type arguments
     */
    public ImmutableMapAdapter(Jsonb jsonb, Type[] types) {
        this.valueAdapter = jsonb.adapter(types[1]);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ImmutableMap<K, V> fromJson(JsonReader reader) {
        ImmutableMap.Builder<K, V> builder = ImmutableMap.builder();
        reader.beginObject();
        while (reader.hasNextField()) {
            String keyString = reader.nextField();
            K key = (K) keyString; // Assume String keys for JSON objects
            V value = valueAdapter.fromJson(reader);
            builder.put(key, value);
        }
        reader.endObject();
        return builder.build();
    }

    @Override
    public void toJson(JsonWriter writer, ImmutableMap<K, V> value) {
        if (value == null) {
            writer.nullValue();
            return;
        }
        if (value.isEmpty()) {
            writer.beginObject();
            writer.endObject();
            return;
        }
        writer.beginObject();
        for (var entry : value.entrySet()) {
            writer.name(entry.getKey().toString());
            valueAdapter.toJson(writer, entry.getValue());
        }
        writer.endObject();
    }
}
