package au.com.arvis.webcapture.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import au.com.arvis.webcapture.databinding.ItemCaptureHistoryBinding
import au.com.arvis.webcapture.model.CAPTURE_HISTORY_DATETIME_FORMAT
import au.com.arvis.webcapture.model.db.WebPageCapture
import com.bumptech.glide.Glide
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class WebPageCaptureHistoryAdapter(private val itemClickListener: OnHistoryItemClickListener, private val deleteButtonClickListener: OnDeleteButtonClickListener): ListAdapter<WebPageCapture, RecyclerView.ViewHolder>(WebPageCaptureDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WebPageCaptureHistoryViewHolder(ItemCaptureHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       val capture = getItem(position)
        (holder as WebPageCaptureHistoryViewHolder).bind(capture, itemClickListener, deleteButtonClickListener)
    }

    class WebPageCaptureHistoryViewHolder(private val binding: ItemCaptureHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: WebPageCapture, itemClickListener: OnHistoryItemClickListener, deleteButtonClickListener: OnDeleteButtonClickListener){
            binding.apply {
                capture = item
                executePendingBindings()
                dateTime.text = SimpleDateFormat(CAPTURE_HISTORY_DATETIME_FORMAT).format(Date(item.dateTime))
                btnDelete.setOnClickListener{
                    deleteButtonClickListener.onDeleteCaptureHistory(item)
                }
                itemView.setOnClickListener{
                    itemClickListener.onHistoryItemClicked(item)
                }
                Glide.with(imageCaptured.context).load(File(item.image)).into(imageCaptured)
            }
        }
    }
}

private class WebPageCaptureDiffCallback: DiffUtil.ItemCallback<WebPageCapture>() {
    override fun areItemsTheSame(oldItem: WebPageCapture, newItem: WebPageCapture): Boolean {
        return oldItem.dateTime == newItem.dateTime
    }

    override fun areContentsTheSame(oldItem: WebPageCapture, newItem: WebPageCapture): Boolean {
        return oldItem == newItem
    }

}

interface OnDeleteButtonClickListener{
    fun onDeleteCaptureHistory(history: WebPageCapture)
}

interface OnHistoryItemClickListener{
    fun onHistoryItemClicked(history: WebPageCapture)
}