package au.com.arvis.webcapture.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import au.com.arvis.webcapture.model.db.WebPageCapture

@Database(entities = [WebPageCapture::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun webPageCaptureDao(): WebPageCaptureDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(
                        context
                    ).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }
}
private const val DATABASE_NAME = "web-page-capture-db"
