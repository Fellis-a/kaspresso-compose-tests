package ru.tinkoff.fintech.meowle.testing.mock.response

object AddCatResponseFactory {
    fun success(): String {
        return """{"success":true,"id":123}"""
    }
}