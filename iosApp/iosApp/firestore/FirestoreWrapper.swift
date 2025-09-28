//
//  FirestoreWrapper.swift
//  iosApp
//
//  Created by Belal Khan on 28/09/25.
//

import Foundation
import FirebaseFirestore

@objc public class FirestoreWrapper: NSObject {
    private let db: Firestore

    @objc public override init() {
        self.db = Firestore.firestore()
    }

    // MARK: - Create
    @objc public func create(collection: String, data: [String: Any], completion: @escaping (NSError?) -> Void) {
        db.collection(collection).addDocument(data: data) { error in
            completion(error as NSError?)
        }
    }

    // MARK: - Read
    private var listeners: [String: ListenerRegistration] = [:]

    @objc public func listen(collection: String, documentId: String, callbackId: String, onUpdate: @escaping ([String: Any]?, NSError?) -> Void) {
        let docRef = db.collection(collection).document(documentId)
        let listener = docRef.addSnapshotListener { snapshot, error in
            if let error = error {
                onUpdate(nil, error as NSError)
            } else {
                onUpdate(snapshot?.data(), nil)
            }
        }
        listeners[callbackId] = listener
    }

    @objc public func removeListener(callbackId: String) {
        listeners[callbackId]?.remove()
        listeners.removeValue(forKey: callbackId)
    }

    // MARK: - Update
    @objc public func update(collection: String, documentId: String, data: [String: Any], completion: @escaping (NSError?) -> Void) {
        db.collection(collection).document(documentId).updateData(data) { error in
            completion(error as NSError?)
        }
    }

    // MARK: - Delete
    @objc public func delete(collection: String, documentId: String, completion: @escaping (NSError?) -> Void) {
        db.collection(collection).document(documentId).delete { error in
            completion(error as NSError?)
        }
    }
}
