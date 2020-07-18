package au.com.arvis.webcapture.repository


import au.com.arvis.webcapture.data.WebPageCaptureDao
import au.com.arvis.webcapture.model.db.WebPageCapture

class WebPageCaptureRepository private constructor(
    private val webPageCaptureDao: WebPageCaptureDao
) {
    suspend fun getFullHistory() = webPageCaptureDao.getWebPageCaptures()

    suspend fun getWebPageCapturesWithUrl(url: String) =
        webPageCaptureDao.searchWebPageCapturesByUrl(url)

    suspend fun addWebPageCapture(dateTime: Long, imageFile: String, url: String) {
        webPageCaptureDao.addWebPageCapture(WebPageCapture(dateTime, url, imageFile))
    }

    suspend fun deleteWebPageCaptureHistory(history: WebPageCapture){
        webPageCaptureDao.deletePageCaptureById(history)
    }

    companion object {
        @Volatile
        private var instance: WebPageCaptureRepository? = null

        fun getInstance(webPageCaptureDao: WebPageCaptureDao) = instance ?: synchronized(this) {
            instance ?: WebPageCaptureRepository(webPageCaptureDao).also { instance = it }
        }
    }
}