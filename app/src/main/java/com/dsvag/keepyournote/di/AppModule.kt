package com.dsvag.keepyournote.di

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.room.Room
import com.dsvag.keepyournote.data.database.note.NoteDao
import com.dsvag.keepyournote.data.database.note.NoteDatabase
import com.dsvag.keepyournote.data.repository.NoteRepository
import com.dsvag.keepyournote.data.repository.SettingsRepository
import com.dsvag.keepyournote.utils.KeyBoardUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext appContext: Context) =
        appContext.createDataStore(name = "settings")

    @Singleton
    @Provides
    fun provideKeyBoardUtils(@ApplicationContext appContext: Context) =
        KeyBoardUtils(getSystemService(appContext, InputMethodManager::class.java)!!)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): NoteDatabase {
        return Room.databaseBuilder(appContext, NoteDatabase::class.java, "note-database")
            .build()
    }

    @Singleton
    @Provides
    fun provideNoteDao(database: NoteDatabase) = database.noteDao()

    @Singleton
    @Provides
    fun provideNoteRepository(noteDao: NoteDao) = NoteRepository(noteDao)

    @Singleton
    @Provides
    fun provideThemeRepository(dataStore: DataStore<Preferences>) = SettingsRepository(dataStore)
}
