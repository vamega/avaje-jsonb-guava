package com.bitfiddling.avaje.guava;

import com.google.common.collect.HashMultiset;
import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;
import io.avaje.jsonb.AdapterFactory;
import io.avaje.jsonb.CustomAdapter;
import io.avaje.jsonb.Jsonb;
import io.avaje.jsonb.Types;
import java.lang.reflect.Type;

/**
 * JsonAdapter for Guava HashMultiset.
 *
 * @param <T> the element type
 */
@CustomAdapter
public final class HashMultisetAdapter<T> implements JsonAdapter<HashMultiset<T>> {

    /** Factory for creating HashMultisetAdapter instances. */
    public static final AdapterFactory FACTORY = (Type type, Jsonb jsonb) -> {
        if (Types.isGenericTypeOf(type, HashMultiset.class)) {
            return new HashMultisetAdapter<>(jsonb, Types.typeArguments(type));
        }
        return null;
    };

    private final JsonAdapter<T> genericType;

    /**
     * Creates a new HashMultisetAdapter.
     *
     * @param jsonb the Jsonb instance
     * @param types the generic type arguments
     */
    public HashMultisetAdapter(Jsonb jsonb, Type[] types) {
        this.genericType = jsonb.adapter(types[0]);
    }

    @Override
    public HashMultiset<T> fromJson(JsonReader reader) {
        HashMultiset<T> multiset = HashMultiset.create();
        reader.beginArray();
        while (reader.hasNextElement()) {
            T element = genericType.fromJson(reader);
            multiset.add(element);
        }
        reader.endArray();
        return multiset;
    }

    @Override
    public void toJson(JsonWriter writer, HashMultiset<T> value) {
        if (value == null) {
            writer.nullValue();
            return;
        }
        if (value.isEmpty()) {
            writer.emptyArray();
            return;
        }
        writer.beginArray();
        for (T element : value) {
            genericType.toJson(writer, element);
        }
        writer.endArray();
    }
}
