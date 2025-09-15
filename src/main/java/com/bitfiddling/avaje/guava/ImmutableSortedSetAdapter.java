package com.bitfiddling.avaje.guava;

import com.google.common.collect.ImmutableSortedSet;
import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;
import io.avaje.jsonb.AdapterFactory;
import io.avaje.jsonb.CustomAdapter;
import io.avaje.jsonb.Jsonb;
import io.avaje.jsonb.Types;
import java.lang.reflect.Type;

/**
 * JsonAdapter for Guava ImmutableSortedSet.
 *
 * @param <T> the element type
 */
@CustomAdapter
public final class ImmutableSortedSetAdapter<T extends Comparable<T>> implements JsonAdapter<ImmutableSortedSet<T>> {

    /** Factory for creating ImmutableSortedSetAdapter instances. */
    public static final AdapterFactory FACTORY = (Type type, Jsonb jsonb) -> {
        if (Types.isGenericTypeOf(type, ImmutableSortedSet.class)) {
            return new ImmutableSortedSetAdapter<>(jsonb, Types.typeArguments(type));
        }
        return null;
    };

    private final JsonAdapter<T> genericType;

    /**
     * Creates a new ImmutableSortedSetAdapter.
     *
     * @param jsonb the Jsonb instance
     * @param types the generic type arguments
     */
    public ImmutableSortedSetAdapter(Jsonb jsonb, Type[] types) {
        this.genericType = jsonb.adapter(types[0]);
    }

    @Override
    public ImmutableSortedSet<T> fromJson(JsonReader reader) {
        ImmutableSortedSet.Builder<T> builder = ImmutableSortedSet.naturalOrder();
        reader.beginArray();
        while (reader.hasNextElement()) {
            T element = genericType.fromJson(reader);
            builder.add(element);
        }
        reader.endArray();
        return builder.build();
    }

    @Override
    public void toJson(JsonWriter writer, ImmutableSortedSet<T> value) {
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
