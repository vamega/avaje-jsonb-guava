# Avaje JsonB Guava Adapters

JSON serialization adapters for Google Guava collection types using [Avaje JsonB](https://avaje.io/jsonb/).

## Overview

This library provides custom JsonB adapters for Google Guava immutable collection types, enabling seamless JSON serialization and deserialization when using Avaje JsonB.

## Supported Types

- `ImmutableList<T>`
- `ImmutableSet<T>`
- `ImmutableSortedSet<T>`
- `ImmutableMap<K,V>`
- `ImmutableBiMap<K,V>`
- `ImmutableMultiset<T>`
- `ImmutableSortedMultiset<T>`
- `HashMultiset<T>`
- `LinkedHashMultiset<T>`
- `TreeMultiset<T>`

## Installation

### Maven

```xml
<dependency>
    <groupId>com.bitfiddling</groupId>
    <artifactId>avaje-jsonb-guava</artifactId>
    <version>0.0.1</version>
</dependency>
```

### Gradle

```kotlin
implementation("com.bitfiddling:avaje-jsonb-guava:0.0.1")
```

## Usage

The adapters are automatically registered with Avaje JsonB through the `@AdapterFactory` annotation. Simply include this library in your classpath and the Guava collection types will be supported automatically.

```java
public record Example(
    ImmutableList<String> items,
    ImmutableSet<Integer> numbers,
    ImmutableMap<String, String> metadata
) {}

// Serialization and deserialization work automatically
Jsonb jsonb = Jsonb.builder().build();
String json = jsonb.toJson(example);
Example restored = jsonb.fromJson(json, Example.class);
```

## License

Licensed under the Apache License, Version 2.0. See [LICENSE](LICENSE) for details.
