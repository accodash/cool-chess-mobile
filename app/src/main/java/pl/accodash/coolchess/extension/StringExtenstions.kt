package pl.accodash.coolchess.extension

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("SimpleDateFormat")
fun String.formatDate(): String {
    return try {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = input.parse(this)
        SimpleDateFormat("dd MMMM yyyy", Locale.UK).format(date ?: return this)
    } catch (e: Exception) {
        this
    }
}
