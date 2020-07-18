package au.com.arvis.webcapture.model.viewmodel

import android.content.Context
import android.webkit.URLUtil
import android.webkit.WebView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import au.com.arvis.webcapture.repository.WebPageCaptureRepository
import au.com.arvis.webcapture.util.captureWebViewContentAsBitmap
import au.com.arvis.webcapture.util.saveBitmapToFile

class MainViewModel internal constructor(
    private val captureRepository: WebPageCaptureRepository
) : LoadingViewModel() {

    private val _currentUrl = MutableLiveData<String?>()
    private val _imageFile = MutableLiveData<String?>()

    val currentUrl: LiveData<String?>
        get() = _currentUrl

    val imageFile: LiveData<String?>
        get() = _imageFile

    fun saveWebPageCapture(context: Context, webView: WebView) {
        launch {
            val webPageScreenshot =
                captureWebViewContentAsBitmap(webView) //Capture web page screenshot
            val savedImageFile =
                saveBitmapToFile(context, webPageScreenshot) // Save image to local images folder
            captureRepository.addWebPageCapture(
                System.currentTimeMillis(),
                savedImageFile,
                webView.url
            )
            showMessage("Web page was captured and stored")
        }
    }


    fun showCaptureHistory(url: String, imageFile: String) {
        loadWebPage(url)
        _imageFile.value = imageFile
    }

    fun loadWebPage(url: String) {
        if (URLUtil.isValidUrl(url))
            _currentUrl.value = url
        else {
            showMessage("Please enter a valid URL")
        }
    }
}