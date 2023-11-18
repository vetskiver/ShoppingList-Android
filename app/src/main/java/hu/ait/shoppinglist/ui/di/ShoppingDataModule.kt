package hu.ait.shoppinglist.ui.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.ait.shoppinglist.ui.data.ShoppingAppDatabase
import hu.ait.shoppinglist.ui.data.ShoppingDAO
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideShoppingDao(appDatabase: ShoppingAppDatabase): ShoppingDAO {
        return appDatabase.shoppingDao()
    }

    @Provides
    @Singleton
    fun provideShoppingAppDatabase(
        @ApplicationContext appContext: Context): ShoppingAppDatabase {
        return ShoppingAppDatabase.getDatabase(appContext)
    }
}