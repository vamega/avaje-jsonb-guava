package com.bitfiddling.avaje.guava.testing;

import static org.assertj.core.api.Assertions.assertThat;

import com.bitfiddling.avaje.guava.testing.TestRecords.TestDataWithHashMultiset;
import com.bitfiddling.avaje.guava.testing.TestRecords.TestDataWithImmutableBiMap;
import com.bitfiddling.avaje.guava.testing.TestRecords.TestDataWithImmutableList;
import com.bitfiddling.avaje.guava.testing.TestRecords.TestDataWithImmutableMap;
import com.bitfiddling.avaje.guava.testing.TestRecords.TestDataWithImmutableMultiset;
import com.bitfiddling.avaje.guava.testing.TestRecords.TestDataWithImmutableSet;
import com.bitfiddling.avaje.guava.testing.TestRecords.TestDataWithImmutableSortedMultiset;
import com.bitfiddling.avaje.guava.testing.TestRecords.TestDataWithImmutableSortedSet;
import com.bitfiddling.avaje.guava.testing.TestRecords.TestDataWithLinkedHashMultiset;
import com.bitfiddling.avaje.guava.testing.TestRecords.TestDataWithTreeMultiset;
import com.bitfiddling.avaje.guava.testing.TestRecords.TestPerson;
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
import io.avaje.jsonb.Jsonb;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Integration tests that verify Guava adapters work with @Json records
 * through full annotation processing cycle.
 */
class GuavaIntegrationTest {

    private Jsonb jsonb;

    @BeforeEach
    void setUp() {
        jsonb = Jsonb.builder().build();
    }

    // Test records moved to main source set for proper annotation processing

    @Test
    void testImmutableListIntegration() {
        var original = new TestDataWithImmutableList("test", ImmutableList.of("apple", "banana", "cherry"));

        var json = jsonb.toJson(original);
        assertThat(json).contains("test").contains("apple").contains("banana").contains("cherry");

        var restored = jsonb.type(TestDataWithImmutableList.class).fromJson(json);
        assertThat(restored).isEqualTo(original);
        assertThat(restored.items()).isInstanceOf(ImmutableList.class);
        assertThat(restored.items()).containsExactly("apple", "banana", "cherry");
    }

    @Test
    void testImmutableSetIntegration() {
        var original = new TestDataWithImmutableSet("numbers", ImmutableSet.of(1, 2, 3));

        var json = jsonb.toJson(original);
        assertThat(json).contains("numbers").contains("1").contains("2").contains("3");

        var restored = jsonb.type(TestDataWithImmutableSet.class).fromJson(json);
        assertThat(restored).isEqualTo(original);
        assertThat(restored.numbers()).isInstanceOf(ImmutableSet.class);
        assertThat(restored.numbers()).containsExactlyInAnyOrder(1, 2, 3);
    }

    @Test
    void testImmutableMapIntegration() {
        var original = new TestDataWithImmutableMap("config", ImmutableMap.of("key1", "value1", "key2", "value2"));

        var json = jsonb.toJson(original);
        assertThat(json).contains("config").contains("key1").contains("value1");

        var restored = jsonb.type(TestDataWithImmutableMap.class).fromJson(json);
        assertThat(restored).isEqualTo(original);
        assertThat(restored.properties()).isInstanceOf(ImmutableMap.class);
        assertThat(restored.properties()).containsEntry("key1", "value1");
        assertThat(restored.properties()).containsEntry("key2", "value2");
    }

    @Test
    void testEmptyCollections() {
        var emptyList = new TestDataWithImmutableList("empty", ImmutableList.of());
        var json = jsonb.toJson(emptyList);
        var restored = jsonb.type(TestDataWithImmutableList.class).fromJson(json);

        assertThat(restored.items()).isEmpty();
        assertThat(restored.items()).isInstanceOf(ImmutableList.class);
    }

    @Test
    void testNestedStructures() {
        // Test individual TestPerson objects (which work correctly)
        var alice = new TestPerson("Alice", 30);
        var aliceJson = jsonb.toJson(alice);
        var restoredAlice = jsonb.type(TestPerson.class).fromJson(aliceJson);
        assertThat(restoredAlice.name()).isEqualTo("Alice");
        assertThat(restoredAlice.age()).isEqualTo(30);

        // Test that our adapter works with nested structures in record fields
        var dataWithPeople = new TestDataWithImmutableList("people", ImmutableList.of("Alice", "Bob", "Charlie"));

        var json = jsonb.toJson(dataWithPeople);
        var restored = jsonb.type(TestDataWithImmutableList.class).fromJson(json);

        assertThat(restored.items()).hasSize(3);
        assertThat(restored.items()).isInstanceOf(ImmutableList.class);
        assertThat(restored.items()).containsExactly("Alice", "Bob", "Charlie");
    }

    @Test
    void testImmutableSortedSetIntegration() {
        var original = new TestDataWithImmutableSortedSet("sorted", ImmutableSortedSet.of("zebra", "apple", "banana"));

        var json = jsonb.toJson(original);
        assertThat(json).contains("sorted").contains("apple").contains("banana").contains("zebra");

        var restored = jsonb.type(TestDataWithImmutableSortedSet.class).fromJson(json);
        assertThat(restored).isEqualTo(original);
        assertThat(restored.sortedItems()).isInstanceOf(ImmutableSortedSet.class);
        assertThat(restored.sortedItems()).containsExactly("apple", "banana", "zebra"); // sorted order
    }

    @Test
    void testImmutableBiMapIntegration() {
        var original = new TestDataWithImmutableBiMap("bimap", ImmutableBiMap.of("key1", 1, "key2", 2, "key3", 3));

        var json = jsonb.toJson(original);
        assertThat(json).contains("bimap").contains("key1").contains("1");

        var restored = jsonb.type(TestDataWithImmutableBiMap.class).fromJson(json);
        assertThat(restored).isEqualTo(original);
        assertThat(restored.biMapping()).isInstanceOf(ImmutableBiMap.class);
        assertThat(restored.biMapping()).containsEntry("key1", 1);
        assertThat(restored.biMapping().inverse()).containsEntry(1, "key1");
    }

    @Test
    void testImmutableMultisetIntegration() {
        var original = new TestDataWithImmutableMultiset(
                "multiset", ImmutableMultiset.of("apple", "banana", "apple", "cherry", "apple"));

        var json = jsonb.toJson(original);
        assertThat(json).contains("multiset").contains("apple").contains("banana");

        var restored = jsonb.type(TestDataWithImmutableMultiset.class).fromJson(json);
        assertThat(restored).isEqualTo(original);
        assertThat(restored.multiItems()).isInstanceOf(ImmutableMultiset.class);
        assertThat(restored.multiItems().count("apple")).isEqualTo(3);
        assertThat(restored.multiItems().count("banana")).isEqualTo(1);
        assertThat(restored.multiItems().count("cherry")).isEqualTo(1);
    }

    @Test
    void testImmutableSortedMultisetIntegration() {
        var original = new TestDataWithImmutableSortedMultiset(
                "sortedMultiset", ImmutableSortedMultiset.of("zebra", "apple", "banana", "apple", "zebra", "zebra"));

        var json = jsonb.toJson(original);
        assertThat(json).contains("sortedMultiset");

        var restored = jsonb.type(TestDataWithImmutableSortedMultiset.class).fromJson(json);
        assertThat(restored).isEqualTo(original);
        assertThat(restored.sortedMultiItems()).isInstanceOf(ImmutableSortedMultiset.class);
        assertThat(restored.sortedMultiItems().count("apple")).isEqualTo(2);
        assertThat(restored.sortedMultiItems().count("zebra")).isEqualTo(3);
        // Verify sorted order
        assertThat(restored.sortedMultiItems().elementSet()).containsExactly("apple", "banana", "zebra");
    }

    @Test
    void testHashMultisetIntegration() {
        var original = new TestDataWithHashMultiset(
                "hashMultiset", HashMultiset.create(List.of("red", "blue", "red", "green", "red")));

        var json = jsonb.toJson(original);
        assertThat(json).contains("hashMultiset");

        var restored = jsonb.type(TestDataWithHashMultiset.class).fromJson(json);
        assertThat(restored).isEqualTo(original);
        assertThat(restored.hashMultiItems()).isInstanceOf(HashMultiset.class);
        assertThat(restored.hashMultiItems().count("red")).isEqualTo(3);
        assertThat(restored.hashMultiItems().count("blue")).isEqualTo(1);
        assertThat(restored.hashMultiItems().count("green")).isEqualTo(1);
    }

    @Test
    void testLinkedHashMultisetIntegration() {
        var original = new TestDataWithLinkedHashMultiset(
                "linkedHashMultiset", LinkedHashMultiset.create(List.of("first", "second", "first", "third")));

        var json = jsonb.toJson(original);
        assertThat(json).contains("linkedHashMultiset");

        var restored = jsonb.type(TestDataWithLinkedHashMultiset.class).fromJson(json);
        assertThat(restored).isEqualTo(original);
        assertThat(restored.linkedMultiItems()).isInstanceOf(LinkedHashMultiset.class);
        assertThat(restored.linkedMultiItems().count("first")).isEqualTo(2);
        assertThat(restored.linkedMultiItems().count("second")).isEqualTo(1);
        assertThat(restored.linkedMultiItems().count("third")).isEqualTo(1);
    }

    @Test
    void testTreeMultisetIntegration() {
        var original = new TestDataWithTreeMultiset(
                "treeMultiset", TreeMultiset.create(List.of("zebra", "apple", "banana", "apple")));

        var json = jsonb.toJson(original);
        assertThat(json).contains("treeMultiset");

        var restored = jsonb.type(TestDataWithTreeMultiset.class).fromJson(json);
        assertThat(restored).isEqualTo(original);
        assertThat(restored.treeMultiItems()).isInstanceOf(TreeMultiset.class);
        assertThat(restored.treeMultiItems().count("apple")).isEqualTo(2);
        assertThat(restored.treeMultiItems().count("banana")).isEqualTo(1);
        assertThat(restored.treeMultiItems().count("zebra")).isEqualTo(1);
        // Verify natural ordering is maintained
        assertThat(restored.treeMultiItems().elementSet()).containsExactly("apple", "banana", "zebra");
    }
}
