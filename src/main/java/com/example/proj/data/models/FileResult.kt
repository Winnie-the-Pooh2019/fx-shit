package com.example.proj.data.models

data class FileResult(
        val results: List<Result> = listOf(),
        val time: String,
        val name: String,
        val surname: String,
        val group: String
)
