package hu.ait.shoppinglist.ui.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItem::class], version = 3, exportSchema = false)
abstract class ShoppingAppDatabase : RoomDatabase() {

    abstract fun shoppingDao(): ShoppingDAO

    companion object {
        @Volatile
        private var Instance: ShoppingAppDatabase? = null

        fun getDatabase(context: Context): ShoppingAppDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ShoppingAppDatabase::class.java,
                    "shopping_database.db")
                    // Setting this option in your app's database builder means that Room
                    // permanently deletes all data from the tables in your database when it
                    // attempts to perform a migration with no defined migration path.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}