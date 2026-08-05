package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.OpportunityDao
import com.example.data.dao.UserProfileDao
import com.example.data.model.OpportunityEntity
import com.example.data.model.UserProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [OpportunityEntity::class, UserProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ScholarLinkDatabase : RoomDatabase() {

    abstract fun opportunityDao(): OpportunityDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: ScholarLinkDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ScholarLinkDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScholarLinkDatabase::class.java,
                    "scholarlink_database"
                )
                    .addCallback(ScholarLinkDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class ScholarLinkDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialDatabase(database.opportunityDao())
                    }
                }
            }

            suspend fun populateInitialDatabase(dao: OpportunityDao) {
                dao.insertOpportunities(InitialSeedData.getInitialOpportunities())
            }
        }
    }
}
