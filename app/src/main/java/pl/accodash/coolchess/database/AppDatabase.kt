package pl.accodash.coolchess.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.accodash.coolchess.database.dao.UserPreferenceDao
import pl.accodash.coolchess.database.entity.UserPreference

@Database(entities = [UserPreference::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userPreferenceDao(): UserPreferenceDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cool_chess_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
