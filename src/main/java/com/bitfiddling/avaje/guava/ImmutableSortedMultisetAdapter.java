package com.bitfiddling.avaje.guava;

import com.google.common.collect.ImmutableSortedMultiset;
import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;
import io.avaje.jsonb.AdapterFactory;
import io.avaje.jsonb.CustomAdapter;
import io.avaje.jsonb.Jsonb;
import io.avaje.jsonb.Types;
import java.lang.reflect.Type;

/**
 * JsonAdapter for Guava ImmutableSortedMultiset.
 *
 * @param <T> the element type
 */
@CustomAdapter
public final class ImmutableSortedMultisetAdapter<T extends Comparable<T>>
        implements JsonAdapter<ImmutableSortedMultiset<T>> {

    /** Factory for creating ImmutableSortedMultisetAdapter instances. */
    public static final AdapterFactory FACTORY = (Type type, Jsonb jsonb) -> {
        if (Types.isGenericTypeOf(type, ImmutableSortedMultiset.class)) {
            return new ImmutableSortedMultisetAdapter<>(jsonb, Types.typeArguments(type));
        }
        return null;
    };

    private final JsonAdapter<T> genericType;

    /**
     * Creates a new ImmutableSortedMultisetAdapter.
     *
     * @param jsonb the Jsonb instance
     * @param types the generic type arguments
     */
    public ImmutableSortedMultisetAdapter(Jsonb jsonb, Type[] types) {
        this.genericType = jsonb.adapter(types[0]);
    }

    @Override
    public ImmutableSortedMultiset<T> fromJson(JsonReader reader) {
        ImmutableSortedMultiset.Builder<T> builder = ImmutableSortedMultiset.naturalOrder();
        reader.beginArray();
        while (reader.hasNextElement()) {
            T element = genericType.fromJson(reader);
            builder.add(element);
        }
        reader.endArray();
        return builder.build();
    }

    @Override
    public void toJson(JsonWriter writer, ImmutableSortedMultiset<T> value) {
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
