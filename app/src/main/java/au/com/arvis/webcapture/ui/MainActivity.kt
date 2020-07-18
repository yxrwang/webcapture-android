package au.com.arvis.webcapture.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.lifecycle.observe
import au.com.arvis.webcapture.databinding.ActivityMainBinding
import au.com.arvis.webcapture.model.CAPTURE_IMAGE_FILE
import au.com.arvis.webcapture.model.CAPTURE_URL
import au.com.arvis.webcapture.model.REQ_SHOW_HISTORY
import au.com.arvis.webcapture.model.viewmodel.MainViewModel
import au.com.arvis.webcapture.util.Injector
import com.bumptech.glide.Glide

import com.google.android.material.snackbar.Snackbar
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        Injector.provideMainViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setViewBinding()
        setWebView()
        setupButtonClicks()
        setupObservers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQ_SHOW_HISTORY) {
            if (resultCode == Activity.RESULT_OK) {
                data?.let { history ->
                    viewModel.showCaptureHistory(
                        history.getStringExtra(CAPTURE_URL), history.getStringExtra(
                            CAPTURE_IMAGE_FILE
                        )
                    )
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupButtonClicks() {
        binding.btnCapture.setOnClickListener {
            it.isEnabled = false
            viewModel.saveWebPageCapture(this, binding.webView)
        }

        binding.btnGo.setOnClickListener {
            viewModel.loadWebPage(binding.inputUrl.text.toString())
        }

        binding.btnHistory.setOnClickListener {
            Intent(this, HistoryActivity::class.java).apply {
                startActivityForResult(
                    this,
                    REQ_SHOW_HISTORY
                )
            }
        }
    }

    private fun loadURLInWebView(url: String) {
        binding.btnGo.isEnabled = false
        binding.btnCapture.isEnabled = false
        binding.webView.loadUrl(url)
        viewModel.startLoading()
    }

    private fun setupObservers() {
        viewModel.snackbar.observe(this) { text ->
            text?.let {
                Snackbar.make(binding.root, text, Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.spinner.observe(this) { show ->
            binding.webLoadingProgress.visibility = if (show) View.VISIBLE else View.GONE
            binding.btnGo.isEnabled = !show
            binding.btnCapture.isEnabled = !show
        }

        viewModel.currentUrl.observe(this) { url ->
            url?.let {
                binding.inputUrl.setText(it)
                loadURLInWebView(it)
            }
        }

        viewModel.imageFile.observe(this) { imageFile ->
            imageFile?.let {
                binding.webView.visibility = View.VISIBLE
                Glide.with(this).load(File(imageFile))
                    .into(binding.thumbnail.also { it.visibility = View.VISIBLE })
            }
        }
    }

    private fun setWebView() {
        binding.webView.webViewClient = CaptureWebViewClient()
        binding.webView.settings.javaScriptEnabled = true
    }


    inner class CaptureWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            viewModel.stopLoading()
            binding.thumbnail.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
        }
    }
}