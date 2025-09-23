package dev.belalkhan.taletree.firebase.db

import kotlinx.coroutines.flow.Flow

expect class FirestoreClient() {
    /**
     * Creates a new document in a specified collection.
     * @param collectionPath The path to the collection.
     * @param data The data to be stored.
     * @return A FirestoreDataState indicating the result of the operation.
     */
    suspend fun <T> create(
        collectionPath: String,
        data: T
    ): FirestoreDataState<Unit>

    /**
     * Reads a document from a specified collection by ID.
     * @param collectionPath The path to the collection.
     * @param documentId The ID of the document to retrieve.
     * @return A Flow emitting the document data or an error state.
     */
    fun <T> read(
        collectionPath: String,
        documentId: String
    ): Flow<FirestoreDataState<T>>

    /**
     * Updates an existing document with new data.
     * @param collectionPath The path to the collection.
     * @param documentId The ID of the document to update.
     * @param data The data to update.
     * @return A FirestoreDataState indicating the result.
     */
    suspend fun <T> update(
        collectionPath: String,
        documentId: String,
        data: Map<String, Any?>
    ): FirestoreDataState<Unit>

    /**
     * Deletes a document from a specified collection by ID.
     * @param collectionPath The path to the collection.
     * @param documentId The ID of the document to delete.
     * @return A FirestoreDataState indicating the result.
     */
    suspend fun delete(
        collectionPath: String,
        documentId: String
    ): FirestoreDataState<Unit>
}