package dev.belalkhan.taletree.firebase.db

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

// This actual class implements the expected FirestoreClient for the iOS platform.
actual class FirestoreClient actual constructor() {

    private val wrapper = Firesto

    /**
     * Creates a new document in a specified collection.
     * This method wraps the Objective-C/Swift addDocumentWithData method in a coroutine.
     */
    actual suspend fun <T> create(
        collectionPath: String,
        data: T
    ): FirestoreDataState<Unit> {
        TODO()
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
        TODO()
    }

    /**
     * Updates an existing document with new data using a suspendable coroutine.
     */
    actual suspend fun <T> update(
        collectionPath: String,
        documentId: String,
        data: Map<String, Any?>
    ): FirestoreDataState<Unit> {
        TODO()
    }

    /**
     * Deletes a document by ID using a suspendable coroutine.
     */
    actual suspend fun delete(
        collectionPath: String,
        documentId: String
    ): FirestoreDataState<Unit> {
        TODO()
    }
}

