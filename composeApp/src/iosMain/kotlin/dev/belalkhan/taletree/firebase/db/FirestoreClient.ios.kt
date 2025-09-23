package dev.belalkhan.taletree.firebase.db

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

// This actual class implements the expected FirestoreClient for the iOS platform.
actual class FirestoreClient actual constructor() {

    private val db = FirebaseFirestore.firestore()

    /**
     * Creates a new document in a specified collection.
     * This method wraps the Objective-C/Swift addDocumentWithData method in a coroutine.
     */
    actual suspend fun <T> create(
        collectionPath: String,
        data: T
    ): FirestoreDataState<Unit> {
        return suspendCancellableCoroutine { continuation ->
            db.collectionWithPath(collectionPath).addDocumentWithData(data as Map<Any?, *>) { error, _ ->
                if (error == null) {
                    continuation.resume(FirestoreDataState.Success(Unit))
                } else {
                    continuation.resume(FirestoreDataState.Error(Exception(error.localizedDescription)))
                }
            }
        }
    }

    /**
     * Reads a document from a specified collection by ID and listens for real-time updates.
     * This method returns a Flow to enable a reactive data stream, wrapping the
     * Firebase `addSnapshotListener`.
     */
    actual fun <T> read(
        collectionPath: String,
        documentId: String
    ): Flow<FirestoreDataState<T>> = callbackFlow {
        val listenerRegistration = db.collectionWithPath(collectionPath)
            .documentWithPath(documentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(FirestoreDataState.Error(Exception(error.localizedDescription)))
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists) {
                    // The iOS SDK's data() method returns a raw Map, which we cast to the generic type T.
                    // This aligns with the contract that the common module handles the deserialization.
                    trySend(FirestoreDataState.Success(snapshot.data() as T))
                } else {
                    trySend(FirestoreDataState.Error(NoSuchElementException("Document does not exist.")))
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    /**
     * Updates an existing document with new data using a suspendable coroutine.
     */
    actual suspend fun <T> update(
        collectionPath: String,
        documentId: String,
        data: Map<String, Any?>
    ): FirestoreDataState<Unit> {
        return suspendCancellableCoroutine { continuation ->
            db.collectionWithPath(collectionPath)
                .documentWithPath(documentId)
                .updateData(data) { error ->
                    if (error == null) {
                        continuation.resume(FirestoreDataState.Success(Unit))
                    } else {
                        continuation.resume(FirestoreDataState.Error(Exception(error.localizedDescription)))
                    }
                }
        }
    }

    /**
     * Deletes a document by ID using a suspendable coroutine.
     */
    actual suspend fun delete(
        collectionPath: String,
        documentId: String
    ): FirestoreDataState<Unit> {
        return suspendCancellableCoroutine { continuation ->
            db.collectionWithPath(collectionPath)
                .documentWithPath(documentId)
                .deleteDocumentWithCompletion { error ->
                    if (error == null) {
                        continuation.resume(FirestoreDataState.Success(Unit))
                    } else {
                        continuation.resume(FirestoreDataState.Error(Exception(error.localizedDescription)))
                    }
                }
        }
    }
}

