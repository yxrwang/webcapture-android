package au.com.arvis.webcapture.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class LoadingViewModel: ViewModel(){

    private val _snackbar = MutableLiveData<String?>()

    val snackbar: LiveData<String?>
        get() = _snackbar

    private val _spinner = MutableLiveData<Boolean>()

    val spinner: LiveData<Boolean>
        get() = _spinner

    fun startLoading(){
        _spinner.value = true
    }

    fun stopLoading(){
        _spinner.value = false
    }

    fun showMessage(message: String){
        _snackbar.value = message
    }

    protected fun launch(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                startLoading()
                block()
            } catch (error: Throwable) {
               showMessage(error.message?: "Error not available")
            } finally {
                stopLoading()
            }
        }
    }
}