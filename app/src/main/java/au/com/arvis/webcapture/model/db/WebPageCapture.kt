package au.com.arvis.webcapture.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "WebPageCaptures")
data class WebPageCapture(
    @PrimaryKey
    val dateTime: Long,
    val url: String,
    val image: String
)
