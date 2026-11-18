package com.example.ricette.di
import android.content.Context
import androidx.room.Room
import com.example.ricette.data.local.AppDatabase
import com.example.ricette.data.local.DatabaseCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton fun provideDb(@ApplicationContext c: Context, cb: DatabaseCallback) = Room.databaseBuilder(c, AppDatabase::class.java, AppDatabase.DATABASE_NAME).fallbackToDestructiveMigration(true).addCallback(cb).build()
    @Provides fun provideFoodDao(db: AppDatabase) = db.foodItemDao()
    @Provides fun provideRecipeDao(db: AppDatabase) = db.recipeDao()
}
