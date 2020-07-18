package au.com.arvis.webcapture.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import au.com.arvis.webcapture.data.AppDatabase
import au.com.arvis.webcapture.model.viewmodel.CaptureHistoryViewModel
import au.com.arvis.webcapture.model.viewmodel.MainViewModel
import au.com.arvis.webcapture.repository.WebPageCaptureRepository

//Custom lightweight dependency injector
interface ViewModelFactoryProvider{
    fun provideMainViewModelFactory(context: Context): MainViewModelFactory
    fun provideHistoryViewModelFactory(context: Context): HistoryViewModelFactory
}

val Injector: ViewModelFactoryProvider
get() = currentInjector

private object DefaultViewModelProvider: ViewModelFactoryProvider {
    private fun getWebPageCaptureRepository(context: Context): WebPageCaptureRepository {
        return WebPageCaptureRepository.getInstance(webPageCaptureDao(context))
    }
    private fun webPageCaptureDao(context: Context) = AppDatabase.getInstance(context.applicationContext).webPageCaptureDao()

    override fun provideMainViewModelFactory(context: Context): MainViewModelFactory {
        val repository = getWebPageCaptureRepository(context)
        return MainViewModelFactory(repository)
    }

    override fun provideHistoryViewModelFactory(context: Context): HistoryViewModelFactory {
        val repository = getWebPageCaptureRepository(context)
        return HistoryViewModelFactory(repository)
    }

}
@Volatile private var currentInjector: ViewModelFactoryProvider =
    DefaultViewModelProvider

class MainViewModelFactory(private val repository: WebPageCaptureRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = MainViewModel(repository) as T
}

class HistoryViewModelFactory(private val repository: WebPageCaptureRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = CaptureHistoryViewModel(repository) as T
}