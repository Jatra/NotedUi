package uk.co.jatra.notedui.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import uk.co.jatra.notedui.persistence.EventDao
import uk.co.jatra.notedui.persistence.NotedDatabase
import uk.co.jatra.notedui.persistence.OccurrenceDao
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class PersistenceModule {
//    @Provides
//    @Singleton
//    fun provideUserDataSource(context: Context): UserDao {
//        val database = NotedDatabase.getInstance(context)
//        return database.userDao()
//    }

    @Provides
    @Singleton
    fun provideEventDataSource(@ApplicationContext context: Context): EventDao {
        val database = NotedDatabase.getInstance(context)
        return database.eventDao()
    }

    @Provides
    @Singleton
    fun provideOccurrenceDataSource(@ApplicationContext context: Context): OccurrenceDao {
        val database = NotedDatabase.getInstance(context)
        return database.occurrenceDao()
    }
}