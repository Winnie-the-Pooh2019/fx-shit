package com.example.proj.data.models

data class Settings(
        var name: String? = null,
        var surname: String? = null,
        var group: String? = null,
        var sourcePath: String? = null
) {
    companion object {
        const val FILENAME = "settings.json"
    }

    override fun toString(): String {
        return "$name $surname $group"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Settings

        if (name != other.name) return false
        if (surname != other.surname) return false
        if (group != other.group) return false
//        if (sourcePath != other.sourcePath) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (surname?.hashCode() ?: 0)
        result = 31 * result + (group?.hashCode() ?: 0)
//        result = 31 * result + (sourcePath?.hashCode() ?: 0)
        return result
    }
}
