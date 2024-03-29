package ru.skillbranch.devintensive.models

import ru.skillbranch.devintensive.utils.Utils
import java.lang.IllegalStateException
import java.util.*

data class User (
    val id: String,
    var firstName: String?,
    var lastName:String?,
    var avatar: String?,
    var rating: Int = 0,
    var respect: Int = 0,
    val lastVisit: Date? = null,
    val isOnline: Boolean = false
) {

    constructor(id:String, firstName:String?, lastName:String?) : this(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatar = null
    )

    constructor(id:String) : this(id, "John", "Doe")

    init {
        println("It`s Alive!!!\n" +
                "${if(lastName==="Doe") "His name id $firstName $lastName" else "And his name is $firstName $lastName!!!" }\n")
    }

    companion object Factory {
        private var lastId : Int = -1
        fun makeUser(fullName: String?) : User {
            lastId++
            val (firstName, lastName) = Utils.parseFullName(fullName)
            return User(id= "$lastId", firstName = firstName, lastName = lastName)
        }
    }

    class Builder {

        val user: User = User("")
        var id : String? = null
        var lastVisit : Date? = null
        var isOnline : Boolean? = null

        fun id(id: String) : Builder = apply { this.id = id }
        fun firstName(firstName: String) = apply { user.firstName = firstName }
        fun lastName(lastName: String) = apply { user.lastName = lastName }
        fun avatar(avatar: String) = apply { user.avatar = avatar }
        fun rating(rating : Int) = apply { user.rating = rating }
        fun respect(respect : Int) = apply { user.respect = respect }
        fun lastVisit(lastVisit: Date? ) = apply { this.lastVisit = lastVisit }
        fun isOnline(isOnline : Boolean) = apply { this.isOnline = isOnline }

        fun build() : User {
            if (id == null) {
                throw IllegalStateException("call id(String) first")
            }
            return user.copy(id ?: "", lastVisit = lastVisit, isOnline = isOnline ?: false)
        }

    }

}