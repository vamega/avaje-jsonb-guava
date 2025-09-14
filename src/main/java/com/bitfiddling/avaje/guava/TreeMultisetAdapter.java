package com.bitfiddling.avaje.guava;

import com.google.common.collect.TreeMultiset;
import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;
import io.avaje.jsonb.AdapterFactory;
import io.avaje.jsonb.CustomAdapter;
import io.avaje.jsonb.Jsonb;
import io.avaje.jsonb.Types;
import java.lang.reflect.Type;

@CustomAdapter
public final class TreeMultisetAdapter<T extends Comparable<T>> implements JsonAdapter<TreeMultiset<T>> {

    public static final AdapterFactory FACTORY = (Type type, Jsonb jsonb) -> {
        if (Types.isGenericTypeOf(type, TreeMultiset.class)) {
            return new TreeMultisetAdapter<>(jsonb, Types.typeArguments(type));
        }
        return null;
    };

    private final JsonAdapter<T> genericType;

    public TreeMultisetAdapter(Jsonb jsonb, Type[] types) {
        this.genericType = jsonb.adapter(types[0]);
    }

    @Override
    public TreeMultiset<T> fromJson(JsonReader reader) {
        TreeMultiset<T> multiset = TreeMultiset.create();
        reader.beginArray();
        while (reader.hasNextElement()) {
            T element = genericType.fromJson(reader);
            multiset.add(element);
        }
        reader.endArray();
        return multiset;
    }

    @Override
    public void toJson(JsonWriter writer, TreeMultiset<T> value) {
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
