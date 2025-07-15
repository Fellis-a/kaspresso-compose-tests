package ru.tinkoff.fintech.meowle.testing.mock.response

object LikesResponseFactory {
    fun success(): String {
        return """{"dislike":false,"like":true}"""
    }
}