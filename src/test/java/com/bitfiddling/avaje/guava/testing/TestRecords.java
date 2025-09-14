package com.bitfiddling.avaje.guava.testing;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMultiset;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.TreeMultiset;
import io.avaje.jsonb.Json;

/**
 * Test record classes for verifying Guava collection adapters.
 * These are test-only classes and will be excluded from the published JAR.
 */
public class TestRecords {

    @Json
    public record TestDataWithImmutableList(String name, ImmutableList<String> items) {}

    @Json
    public record TestDataWithImmutableSet(String name, ImmutableSet<Integer> numbers) {}

    @Json
    public record TestDataWithImmutableMap(String name, ImmutableMap<String, String> properties) {}

    @Json
    public record TestPerson(String name, int age) {}

    // Additional test records for all Guava collection types
    @Json
    public record TestDataWithImmutableSortedSet(String name, ImmutableSortedSet<String> sortedItems) {}

    @Json
    public record TestDataWithImmutableBiMap(String name, ImmutableBiMap<String, Integer> biMapping) {}

    @Json
    public record TestDataWithImmutableMultiset(String name, ImmutableMultiset<String> multiItems) {}

    @Json
    public record TestDataWithImmutableSortedMultiset(String name, ImmutableSortedMultiset<String> sortedMultiItems) {}

    @Json
    public record TestDataWithHashMultiset(String name, HashMultiset<String> hashMultiItems) {}

    @Json
    public record TestDataWithLinkedHashMultiset(String name, LinkedHashMultiset<String> linkedMultiItems) {}

    @Json
    public record TestDataWithTreeMultiset(String name, TreeMultiset<String> treeMultiItems) {}
}
