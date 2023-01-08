package com.example.proj.data.models

data class Result(
        val question: String,
        val answers: List<String>,
        var current: String?,
        val correct: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Result

        if (current == other.current && question == other.question && correct == other.correct) return true

        return false
    }

    override fun hashCode(): Int {
        return current.hashCode() + answers.hashCode() + question.hashCode()
    }

    override fun toString(): String {
        return "Result(current='$current')"
    }

    companion object {
        const val RESULT_FILE = "result"
    }
}