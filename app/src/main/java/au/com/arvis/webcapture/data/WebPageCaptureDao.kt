package au.com.arvis.webcapture.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import au.com.arvis.webcapture.model.db.WebPageCapture

@Dao
interface WebPageCaptureDao {

    @Query("SELECT * FROM WebPageCaptures ORDER BY dateTime DESC")
    suspend fun getWebPageCaptures(): List<WebPageCapture>

    @Query("SELECT * FROM WebPageCaptures WHERE url LIKE :urlKeyWord ORDER BY dateTime DESC")
    suspend fun searchWebPageCapturesByUrl(urlKeyWord: String): List<WebPageCapture>

    @Insert
    suspend fun addWebPageCapture(webPageCapture: WebPageCapture)

    @Delete
    suspend fun deletePageCaptureById(capture: WebPageCapture)
}