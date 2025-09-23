package dev.belalkhan.taletree.firebase.db

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

// This actual class implements the expected FirestoreClient for the Android platform.
actual class FirestoreClient actual constructor() {

    private val db = Firebase.firestore

    /**
     * Creates a new document in a specified collection.
     * This method uses Firebase's suspend functions to provide a clean coroutine-based API.
     */
    actual suspend fun <T> create(
        collectionPath: String,
        data: T
    ): FirestoreDataState<Unit> {
        return try {
            db.collection(collectionPath).add(data as Any).await()
            FirestoreDataState.Success(Unit)
        } catch (e: Exception) {
            FirestoreDataState.Error(e)
        }
    }

    /**
     * Reads a document from a specified collection by ID and listens for real-time updates.
     * This method uses a Flow to enable a reactive data stream, wrapping the
     * Firebase `addSnapshotListener`.
     */
    actual fun <T> read(
        collectionPath: String,
        documentId: String
    ): Flow<FirestoreDataState<T>> = callbackFlow {
        val docRef = db.collection(collectionPath).document(documentId)

        val listenerRegistration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(FirestoreDataState.Error(error))
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                // The toObject method from the Android SDK is not part of the common interface,
                // so we handle the data as a Map and cast it.
                // Note: Your common code will need to handle the mapping from Map<String, Any?> to T.
                trySend(FirestoreDataState.Success(snapshot.data as T))
            } else {
                trySend(FirestoreDataState.Error(NoSuchElementException("Document does not exist.")))
            }
        }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    /**
     * Updates an existing document with new data using Firebase suspend functions.
     */
    actual suspend fun <T> update(
        collectionPath: String,
        documentId: String,
        data: Map<String, Any?>
    ): FirestoreDataState<Unit> {
        return try {
            db.collection(collectionPath).document(documentId).update(data).await()
            FirestoreDataState.Success(Unit)
        } catch (e: Exception) {
            FirestoreDataState.Error(e)
        }
    }

    /**
     * Deletes a document by ID using Firebase suspend functions.
     */
    actual suspend fun delete(
        collectionPath: String,
        documentId: String
    ): FirestoreDataState<Unit> {
        return try {
            db.collection(collectionPath).document(documentId).delete().await()
            FirestoreDataState.Success(Unit)
        } catch (e: Exception) {
            FirestoreDataState.Error(e)
        }
    }
}
