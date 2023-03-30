package com.kotlineering.ksoc.server.domain.service

sealed interface ServiceResult<out T> {
    data class Success<T>(val auth: T) : ServiceResult<T>
    object Failure : ServiceResult<Nothing>
}