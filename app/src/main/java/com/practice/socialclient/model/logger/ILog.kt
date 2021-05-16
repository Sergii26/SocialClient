package com.practice.socialclient.model.logger

interface ILog {
    fun log(message: String?): Logger?
    fun withCause(cause: Exception?)
}