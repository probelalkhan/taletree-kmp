package dev.belalkhan.taletree

import android.app.Application
import com.google.firebase.FirebaseApp

class TaleTreeApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}