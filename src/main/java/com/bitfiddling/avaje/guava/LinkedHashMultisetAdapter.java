package com.bitfiddling.avaje.guava;

import com.google.common.collect.LinkedHashMultiset;
import io.avaje.json.JsonAdapter;
import io.avaje.json.JsonReader;
import io.avaje.json.JsonWriter;
import io.avaje.jsonb.AdapterFactory;
import io.avaje.jsonb.CustomAdapter;
import io.avaje.jsonb.Jsonb;
import io.avaje.jsonb.Types;
import java.lang.reflect.Type;

@CustomAdapter
public final class LinkedHashMultisetAdapter<T> implements JsonAdapter<LinkedHashMultiset<T>> {

    public static final AdapterFactory FACTORY = (Type type, Jsonb jsonb) -> {
        if (Types.isGenericTypeOf(type, LinkedHashMultiset.class)) {
            return new LinkedHashMultisetAdapter<>(jsonb, Types.typeArguments(type));
        }
        return null;
    };

    private final JsonAdapter<T> genericType;

    public LinkedHashMultisetAdapter(Jsonb jsonb, Type[] types) {
        this.genericType = jsonb.adapter(types[0]);
    }

    @Override
    public LinkedHashMultiset<T> fromJson(JsonReader reader) {
        LinkedHashMultiset<T> multiset = LinkedHashMultiset.create();
        reader.beginArray();
        while (reader.hasNextElement()) {
            T element = genericType.fromJson(reader);
            multiset.add(element);
        }
        reader.endArray();
        return multiset;
    }

    @Override
    public void toJson(JsonWriter writer, LinkedHashMultiset<T> value) {
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
