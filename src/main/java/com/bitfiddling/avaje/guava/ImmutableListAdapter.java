package com.bitfiddling.avaje.guava;

import com.google.common.collect.ImmutableList;
import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;
import io.avaje.jsonb.AdapterFactory;
import io.avaje.jsonb.CustomAdapter;
import io.avaje.jsonb.Jsonb;
import io.avaje.jsonb.Types;
import java.lang.reflect.Type;

/**
 * Custom JsonAdapter for Guava ImmutableList that implements ViewBuilderAware.
 * This adapter enables deserialization of ImmutableList fields in records that use avaje-jsonb.
 */
@CustomAdapter
public final class ImmutableListAdapter<T> implements JsonAdapter<ImmutableList<T>> {

    public static final AdapterFactory FACTORY = (Type type, Jsonb jsonb) -> {
        if (Types.isGenericTypeOf(type, ImmutableList.class)) {
            return new ImmutableListAdapter<>(jsonb, Types.typeArguments(type));
        }
        return null;
    };

    private final JsonAdapter<T> genericType;

    public ImmutableListAdapter(Jsonb jsonb, Type[] types) {
        this.genericType = jsonb.adapter(types[0]);
    }

    @Override
    public ImmutableList<T> fromJson(JsonReader reader) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();
        reader.beginArray();
        while (reader.hasNextElement()) {
            T element = genericType.fromJson(reader);
            builder.add(element);
        }
        reader.endArray();
        return builder.build();
    }

    @Override
    public void toJson(JsonWriter writer, ImmutableList<T> value) {
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
