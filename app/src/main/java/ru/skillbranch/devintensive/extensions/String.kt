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


fun String.isValidRepository(): Boolean {
    var nickName = ""
    arrayOf("https://github.com/", "www.github.com/", "https://www.github.com/", "github.com/").forEach {
        if (this.startsWith(it)) {
            nickName = this.substring(it.length)
            return@forEach
        }
    }
    if (nickName.isEmpty() || nickName.contains("/")) {
        return false
    }

    if (!nickName.matches(Regex("(?![\\\\W])(?!\\\\w+[-]{2})[a-zA-Z0-9-]+(?<![-])(/)?\$"))) {
        return false
    }

    arrayOf(
        "enterprise", "features", "topics", "collections", "trending", "events", "marketplace",
        "pricing", "nonprofit", "customer-stories", "security", "login", "join"
    ).forEach {
        if (nickName.equals(it)) {
            return false
        }
    }

    return true
}