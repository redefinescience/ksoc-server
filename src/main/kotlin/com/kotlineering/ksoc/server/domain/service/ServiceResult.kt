package com.kotlineering.ksoc.server.domain.service

sealed interface ServiceResult<out T> {
    fun statusCode() = 200

    class Success<T>(val data: T) : ServiceResult<T>
    open class Failure(private val code: Int = 500) : ServiceResult<Nothing> {
        override fun statusCode() = code
    }
    object NotFound : Failure(404)

}
