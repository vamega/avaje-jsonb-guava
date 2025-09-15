package com.bitfiddling.avaje.guava;

import com.google.common.collect.ImmutableBiMap;
import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;
import io.avaje.jsonb.AdapterFactory;
import io.avaje.jsonb.CustomAdapter;
import io.avaje.jsonb.Jsonb;
import io.avaje.jsonb.Types;
import java.lang.reflect.Type;

/**
 * JsonAdapter for Guava ImmutableBiMap.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
@CustomAdapter
public final class ImmutableBiMapAdapter<K, V> implements JsonAdapter<ImmutableBiMap<K, V>> {

    /** Factory for creating ImmutableBiMapAdapter instances. */
    public static final AdapterFactory FACTORY = (Type type, Jsonb jsonb) -> {
        if (Types.isGenericTypeOf(type, ImmutableBiMap.class)) {
            return new ImmutableBiMapAdapter<>(jsonb, Types.typeArguments(type));
        }
        return null;
    };

    private final JsonAdapter<V> valueAdapter;

    /**
     * Creates a new ImmutableBiMapAdapter.
     *
     * @param jsonb the Jsonb instance
     * @param types the generic type arguments
     */
    public ImmutableBiMapAdapter(Jsonb jsonb, Type[] types) {
        this.valueAdapter = jsonb.adapter(types[1]);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ImmutableBiMap<K, V> fromJson(JsonReader reader) {
        ImmutableBiMap.Builder<K, V> builder = ImmutableBiMap.builder();
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
    public void toJson(JsonWriter writer, ImmutableBiMap<K, V> value) {
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
