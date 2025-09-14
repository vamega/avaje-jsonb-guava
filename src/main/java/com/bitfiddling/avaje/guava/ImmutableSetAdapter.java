package com.bitfiddling.avaje.guava;

import com.google.common.collect.ImmutableSet;
import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;
import io.avaje.jsonb.AdapterFactory;
import io.avaje.jsonb.CustomAdapter;
import io.avaje.jsonb.Jsonb;
import io.avaje.jsonb.Types;
import java.lang.reflect.Type;

@CustomAdapter
public final class ImmutableSetAdapter<T> implements JsonAdapter<ImmutableSet<T>> {

    public static final AdapterFactory FACTORY = (Type type, Jsonb jsonb) -> {
        if (Types.isGenericTypeOf(type, ImmutableSet.class)) {
            return new ImmutableSetAdapter<>(jsonb, Types.typeArguments(type));
        }
        return null;
    };

    private final JsonAdapter<T> genericType;

    public ImmutableSetAdapter(Jsonb jsonb, Type[] types) {
        this.genericType = jsonb.adapter(types[0]);
    }

    @Override
    public ImmutableSet<T> fromJson(JsonReader reader) {
        ImmutableSet.Builder<T> builder = ImmutableSet.builder();
        reader.beginArray();
        while (reader.hasNextElement()) {
            T element = genericType.fromJson(reader);
            builder.add(element);
        }
        reader.endArray();
        return builder.build();
    }

    @Override
    public void toJson(JsonWriter writer, ImmutableSet<T> value) {
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
