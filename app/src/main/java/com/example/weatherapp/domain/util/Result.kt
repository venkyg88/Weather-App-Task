package com.example.weatherapp.domain.util

/**
 * A generic sealed class to handle success and error states
 */
sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception, val message: String? = null) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

/**
 * Extension function to check if result is successful
 */
fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success

/**
 * Extension function to check if result is error
 */
fun <T> Result<T>.isError(): Boolean = this is Result.Error

/**
 * Extension function to get data or null
 */
fun <T> Result<T>.dataOrNull(): T? = when (this) {
    is Result.Success -> data
    else -> null
}
