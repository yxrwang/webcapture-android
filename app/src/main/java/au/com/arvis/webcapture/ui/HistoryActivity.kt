package au.com.arvis.webcapture.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import au.com.arvis.webcapture.databinding.ActivityHistoryBinding
import au.com.arvis.webcapture.model.CAPTURE_IMAGE_FILE
import au.com.arvis.webcapture.model.CAPTURE_URL
import au.com.arvis.webcapture.model.db.WebPageCapture
import au.com.arvis.webcapture.model.viewmodel.CaptureHistoryViewModel
import au.com.arvis.webcapture.ui.adapters.OnDeleteButtonClickListener
import au.com.arvis.webcapture.ui.adapters.OnHistoryItemClickListener
import au.com.arvis.webcapture.ui.adapters.WebPageCaptureHistoryAdapter
import au.com.arvis.webcapture.util.Injector

class HistoryActivity: AppCompatActivity(), OnHistoryItemClickListener, OnDeleteButtonClickListener {

    private lateinit var binding: ActivityHistoryBinding
    private val viewModel: CaptureHistoryViewModel by viewModels{
        Injector.provideHistoryViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewBinding()
        setupCaptureHistoryList()
        setupButtonClicks()
        loadCaptureHistory()
    }

    private fun setViewBinding(){
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupButtonClicks(){
        binding.btnClear.setOnClickListener{
            binding.inputSearch.setText("")
            viewModel.clearSearch()
        }

        binding.btnSearch.setOnClickListener{
            viewModel.searchHistory(binding.inputSearch.text.toString())
        }
    }

    private fun setupCaptureHistoryList(){
        val adapter = WebPageCaptureHistoryAdapter(this, this)
        binding.captureHistoryList.adapter = adapter

        viewModel.webPageCaptureHistory.observe(this){ history ->
            history?.let {
                if(it.isEmpty()){
                    binding.noCaptureHistory.visibility = View.VISIBLE
                    binding.captureHistoryList.visibility = View.INVISIBLE
                }else{
                    binding.captureHistoryList.visibility = View.VISIBLE
                    binding.noCaptureHistory.visibility = View.INVISIBLE
                    adapter.submitList(history)
                }
            }
        }
    }

    private fun loadCaptureHistory(){
        viewModel.getFullHistory()
    }

    override fun onDeleteCaptureHistory(history: WebPageCapture) {
       viewModel.deleteHistory(history)
    }

    override fun onHistoryItemClicked(history: WebPageCapture) {
        setResult(Activity.RESULT_OK, intent.apply {
            putExtra(CAPTURE_URL, history.url)
            putExtra(CAPTURE_IMAGE_FILE, history.image) })
        finish()
    }
}