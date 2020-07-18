package au.com.arvis.webcapture.util

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.Canvas

import android.webkit.WebView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*

fun captureWebViewContentAsBitmap(webView: WebView): Bitmap {
    val imageHeight = webView.contentHeight
    val imageWidth = webView.width
    val bitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    webView.draw(canvas)
    return bitmap
}

fun saveBitmapToFile(context: Context, bitmap: Bitmap): String{
    val wrapper = ContextWrapper(context.applicationContext)
    var file = wrapper.getDir("Images",Context.MODE_PRIVATE)
    file = File(file,"${UUID.randomUUID()}.jpg")

    val stream: OutputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
    stream.flush()
    stream.close()
    return file.absolutePath
}