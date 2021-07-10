package ca.nkrishnaswamy.picshare.data.db.roomDbs

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ca.nkrishnaswamy.picshare.data.db.DAOs.UserAccountDAO
import ca.nkrishnaswamy.picshare.data.models.UserModel
import ca.nkrishnaswamy.picshare.data.models.UserPost

@Database(entities= [UserModel::class, UserPost::class], version = 6, exportSchema = false)
abstract class CurrentLoggedInUserCache : RoomDatabase() {
    abstract fun userAccountDAO() : UserAccountDAO

    companion object{
        @Volatile
        private var INSTANCE: CurrentLoggedInUserCache ?= null

        fun getInstance(context: Context) : CurrentLoggedInUserCache {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, CurrentLoggedInUserCache::class.java, "currentLoggedInUserDbv6").addMigrations(
                        MIGRATION_5_6).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}