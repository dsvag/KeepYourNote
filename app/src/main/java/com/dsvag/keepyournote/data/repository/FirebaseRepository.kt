package com.dsvag.keepyournote.data.repository

import android.util.Log
import com.dsvag.keepyournote.data.database.note.NoteDao
import com.dsvag.keepyournote.models.Note
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val noteDao: NoteDao,
) {
    private var auth: FirebaseAuth? = null

    val user: FirebaseUser? get() = auth?.currentUser

    private var database: DatabaseReference? = null

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val note = snapshot.getValue(Note::class.java)
            if (note != null) {
                Log.d(TAG, "insert ${note.id}")
                runBlocking {
                    noteDao.insertNote(note)
                }
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val note = snapshot.getValue(Note::class.java)
            if (note != null) {
                Log.d(TAG, "changed ${note.id}")
                runBlocking {
                    noteDao.insertNote(note)
                }
            }

        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val note = snapshot.getValue(Note::class.java)
            if (note != null) {
                Log.d(TAG, "removed ${note.id}")
                runBlocking {
                    noteDao.deleteNote(note)
                }
            }
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }

    fun auth(auth: FirebaseAuth) {
        this.auth = auth
    }

    fun initDatabase() {
        database = Firebase.database.reference
        database
            ?.child("users/${user?.uid}/notes/")
            ?.addChildEventListener(childEventListener)
    }

    fun signOut() {
        auth?.signOut()
        database?.removeEventListener(childEventListener)
    }

    fun login(email: String, password: String): Task<AuthResult> {
        return auth!!.signInWithEmailAndPassword(email, password)
    }

    fun registration(email: String, password: String): Task<AuthResult> {
        return auth!!.createUserWithEmailAndPassword(email, password)
    }

    fun pushNote(note: Note) {
        database?.child("users/${user?.uid}/notes/${note.id}")?.setValue(note)
    }

    fun deleteNote(note: Note) {
        database?.child("users/${user?.uid}/notes/${note.id}")?.removeValue()
    }

    companion object {
        private const val TAG = "FirebaseRepository"
    }
}