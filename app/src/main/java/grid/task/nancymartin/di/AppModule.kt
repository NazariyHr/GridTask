package grid.task.nancymartin.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import grid.task.nancymartin.data.TasksRepositoryImpl
import grid.task.nancymartin.data.local_db.TasksDatabase
import grid.task.nancymartin.domain.repository.TasksRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideTasksDatabase(
        @ApplicationContext context: Context
    ): TasksDatabase {
        return Room.databaseBuilder(
            context,
            TasksDatabase::class.java,
            "tasks.db"
        ).build()
    }

    @Provides
    fun provideTasksRepository(
        tasksDatabase: TasksDatabase
    ): TasksRepository {
        return TasksRepositoryImpl(
            tasksDatabase
        )
    }
}