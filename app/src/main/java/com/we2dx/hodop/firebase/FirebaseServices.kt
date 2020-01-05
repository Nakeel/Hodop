package com.we2dx.hodop.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.we2dx.hodop.utils.ApplicationConstants
import com.we2dx.hodop.utils.ApplicationUtility
import io.reactivex.Completable

class FirebaseServices {

    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestoreDB: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }


    fun login(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful) {
                    emitter.onComplete()

                }
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

//    fun saveUserData(userID: String,username: String, password: String,email: String) = Completable.create
//    {
//        firestoreDB.collection(ApplicationConstants.USERS)
//            .document(userID)
//            .set()
//            .add
//    }

    fun register(email: String, password: String) = Completable.create { emitter ->
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!emitter.isDisposed) {
                if (it.isSuccessful)
                    emitter.onComplete()
                else
                    emitter.onError(it.exception!!)
            }
        }
    }

    fun logout() = firebaseAuth.signOut()

    fun currentUser() = firebaseAuth.currentUser

}
    
