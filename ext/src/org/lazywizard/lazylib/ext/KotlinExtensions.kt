package org.lazywizard.lazylib.ext

/**
 * Null-safely checks for equality, covering the case when either (or both) of the objects are null.
 */
fun Any?.safeEquals(other: Any?): Boolean {
    // Check if both objects are null
    if (this == null && other == null) {
        return true
    }
    // Check if one of the objects is null
    if (this == null || other == null) {
        return false
    }
    // Use the regular equals() method to compare non-null objects
    return this == other
}