package ar.com.todoapp.core

import java.lang.Exception

/**
 * Created by Fernando Moreno on 8/4/2021.
 */
sealed class Resource<out T> {
    data class  Success<out T>(val data: T): Resource<T>()
    data class  Failure(val exception: Exception): Resource<Nothing>()
}