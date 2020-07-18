package au.com.arvis.webcapture.model.viewmodel

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.arvis.webcapture.model.db.WebPageCapture
import au.com.arvis.webcapture.repository.WebPageCaptureRepository

class CaptureHistoryViewModel internal constructor(
    private val captureRepository: WebPageCaptureRepository
): LoadingViewModel() {

    private val _webPageCaptureHistory = MutableLiveData<List<WebPageCapture>?>()
    private var searchKeyWord: String = ""

    val webPageCaptureHistory: LiveData<List<WebPageCapture>?>
    get() = _webPageCaptureHistory

    fun searchHistory(keyword: String){
        searchKeyWord = keyword
        launch {
            val searchResult = captureRepository.getWebPageCapturesWithUrl("%${keyword}%")
            _webPageCaptureHistory.value = searchResult
        }
    }

    fun getFullHistory(){
        launch {
            val history = captureRepository.getFullHistory()
            _webPageCaptureHistory.value = history
        }
    }

    fun deleteHistory(history: WebPageCapture){
        launch {
            captureRepository.deleteWebPageCaptureHistory(history)
            getFullHistoryOrSearch()
        }
    }

    private fun getFullHistoryOrSearch(){
        if(TextUtils.isEmpty(searchKeyWord)){
            getFullHistory()
        }else{
            searchHistory(searchKeyWord)
        }
    }

    fun clearSearch(){
        searchKeyWord = ""
        getFullHistory()
    }
}