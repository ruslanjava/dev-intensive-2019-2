package ru.skillbranch.devintensive.utils

object Utils {

    val russianToLatin = mapOf(
        "а" to "a",  "б" to "b",   "в" to "v",  "г" to "g",  "д" to "d",
        "е" to "e",  "ё" to "yo",  "ж" to "zh", "з" to "z",  "и" to "i",
        "й" to "y",  "к" to "k",   "л" to "l",  "м" to "m",  "н" to "n",
        "о" to "o",  "п" to "p",   "р" to "r",  "с" to "s",  "т" to "t",
        "у" to "u",  "ф" to "f",   "х" to "h",  "ц" to "с",  "ч" to "ch",
        "ш" to "sh", "щ" to "sch", "ъ" to "",   "ы" to "y",  "ь" to "",
        "э" to "e",  "ю" to "yu",  "я" to "ya",
        "А" to "A",  "Б" to "B",   "В" to "V",  "Г" to "G",  "Д" to "D",
        "Е" to "E",  "Ё" to "Yo",  "Ж" to "Zh", "З" to "Z",  "И" to "I",
        "Й" to "Y",  "К" to "K",   "Л" to "L",  "М" to "M",  "Н" to "N",
        "О" to "O",  "П" to "P",   "Р" to "R",  "С" to "S",  "Т" to "T",
        "У" to "U",  "Ф" to "F",   "Х" to "H",  "Ц" to "С",  "Ч" to "Ch",
        "Ш" to "Sh", "Щ" to "Sch", "Ъ" to "",   "Ы" to "Y",  "Ь" to "",
        "Э" to "E",  "Ю" to "Yu",  "Я" to "Ya"
    )

    fun parseFullName(fullName:String?):Pair<String?, String?> {
        var parts: List<String>? = fullName?.trim()?.split(" ")

        var firstName = parts?.getOrNull(0)
        if (firstName != null && firstName.isEmpty()) {
            firstName = null
        }
        var lastName = parts?.getOrNull(1)
        if (lastName != null && lastName.isEmpty()) {
            lastName = null
        }
        return firstName to lastName
    }

    fun transliteration(payload: String, divider : String = " "): String {
        val parts: List<String>? = payload.split(" ")
        
        val builder = StringBuilder()
        parts?.forEach {
            val chars = it.toCharArray()
            chars.forEach { 
                val russian = it.toString()
                var latin = russianToLatin.get(russian)
                if (latin != null) {
                    builder.append(latin)
                } else {
                   builder.append(russian)
                }
            }
            builder.append(divider)
        }
        
        while (builder.endsWith(divider)) {
            builder.setLength(builder.length - 1)
        }

        return builder.toString()
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        val first = firstName?.substring(0, 1)?.toUpperCase() ?: ""
        val last = lastName?.substring(0, 1)?.toUpperCase() ?: ""
        return "${first}${last}"
    }

}