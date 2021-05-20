package com.practice.socialclient.model.logger

interface Log {
    fun log(message: String?): Logger?
    fun withCause(cause: Exception?)
}
