package ru.skillbranch.devintensive.extensions

fun String.truncate(maxLength : Int = 16): String {
    if (this.length < maxLength) {
        return this
    }
    var truncated = this.substring(0, maxLength)
    while (truncated.endsWith(" ")) {
        truncated = truncated.substring(0, truncated.length - 1)
    }
    if (truncated.length < maxLength) {
        truncated + "..."
    }
    return truncated
}