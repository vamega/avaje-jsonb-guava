package com.bitfiddling.avaje.guava;

import com.google.common.collect.ImmutableMultiset;
import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;
import io.avaje.jsonb.AdapterFactory;
import io.avaje.jsonb.CustomAdapter;
import io.avaje.jsonb.Jsonb;
import io.avaje.jsonb.Types;
import java.lang.reflect.Type;

@CustomAdapter
public final class ImmutableMultisetAdapter<T> implements JsonAdapter<ImmutableMultiset<T>> {

    public static final AdapterFactory FACTORY = (Type type, Jsonb jsonb) -> {
        if (Types.isGenericTypeOf(type, ImmutableMultiset.class)) {
            return new ImmutableMultisetAdapter<>(jsonb, Types.typeArguments(type));
        }
        return null;
    };

    private final JsonAdapter<T> genericType;

    public ImmutableMultisetAdapter(Jsonb jsonb, Type[] types) {
        this.genericType = jsonb.adapter(types[0]);
    }

    @Override
    public ImmutableMultiset<T> fromJson(JsonReader reader) {
        ImmutableMultiset.Builder<T> builder = ImmutableMultiset.builder();
        reader.beginArray();
        while (reader.hasNextElement()) {
            T element = genericType.fromJson(reader);
            builder.add(element);
        }
        reader.endArray();
        return builder.build();
    }

    @Override
    public void toJson(JsonWriter writer, ImmutableMultiset<T> value) {
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
