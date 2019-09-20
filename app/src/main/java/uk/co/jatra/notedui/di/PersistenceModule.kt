package uk.co.jatra.notedui.di

import android.content.Context
import dagger.Module
import dagger.Provides
import uk.co.jatra.notedui.persistence.EventDao
import uk.co.jatra.notedui.persistence.NotedDatabase
import uk.co.jatra.notedui.persistence.OccurrenceDao
import javax.inject.Singleton

@Module
class PersistenceModule {
//    @Provides
//    @Singleton
//    fun provideUserDataSource(context: Context): UserDao {
//        val database = NotedDatabase.getInstance(context)
//        return database.userDao()
//    }

    @Provides
    @Singleton
    fun provideEventDataSource(context: Context): EventDao {
        val database = NotedDatabase.getInstance(context)
        return database.eventDao()
    }

    @Provides
    @Singleton
    fun provideOccurrenceDataSource(context: Context): OccurrenceDao {
        val database = NotedDatabase.getInstance(context)
        return database.occurrenceDao()
    }
}