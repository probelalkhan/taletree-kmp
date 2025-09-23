package dev.belalkhan.taletree.firebase.db

sealed class FirestoreDataState<out T> {
    object Loading : FirestoreDataState<Nothing>()
    data class Success<T>(val data: T) : FirestoreDataState<T>()
    data class Error(val exception: Throwable) : FirestoreDataState<Nothing>()
}