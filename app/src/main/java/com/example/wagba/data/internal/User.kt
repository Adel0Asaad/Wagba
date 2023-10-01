package com.example.wagba.data.internal

import androidx.room.Entity
import androidx.room.PrimaryKey

data class User(
    val id: String? = null,
    var name: String? = null,
    var pass: String? = null,
    var email: String? = null,
    var phone: String? = null
){
    constructor(user: UserEntity) : this(user.id, user.name, user.pass, user.email, user.phone)
}

//Room database class, for some reason it doesn't accept nullable ids, but for the weirder reason firebase doesn't accept NONNULLABLE parameters so idk
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    var name: String,
    var pass: String,
    var email: String,
    var phone: String
){
    constructor(user: User) : this(user.id!!, user.name!!, user.pass!!, user.email!!, user.phone!!)
}