package kr.baka.groupriding.repository.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kr.baka.groupriding.repository.room.dao.RouteDao
import kr.baka.groupriding.repository.room.dao.RouteSubDao
import kr.baka.groupriding.repository.room.entity.RouteEntity
import kr.baka.groupriding.repository.room.entity.RouteSubEntity

@Database(
    entities = [RouteEntity::class,RouteSubEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase(){

    abstract fun routeDao(): RouteDao
    abstract fun routeSubDao(): RouteSubDao


    companion object{
        private const val DB_NAME = "GROUP_RIDING_DB"

        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase{
            return instance ?: synchronized(this) { instance ?: buildDatabase(context) }
        }

        private fun buildDatabase(context: Context): AppDatabase{
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
//                .fallbackToDestructiveMigration()
                .addCallback(object: RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }).build()
        }
    }
}