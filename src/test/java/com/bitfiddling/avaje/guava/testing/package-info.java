/// This package must not be the same as the package where jsonb annotated
/// classes live in the main source set.
///
/// Annotation processing when applied to the test source set will result in
/// another GeneratedJsonComponent being generated for this source set, and
/// the clash will cause the tests to fail
package com.bitfiddling.avaje.guava.testing;
