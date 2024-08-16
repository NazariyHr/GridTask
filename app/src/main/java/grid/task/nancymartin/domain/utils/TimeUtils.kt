package grid.task.nancymartin.domain.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Long.toFormattedDate(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(Date(this))
}

fun Long.toFormattedDateAndTime(): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
    return formatter.format(Date(this))
}

fun getToday(): Calendar {
    return Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

fun Long.getSecondsOfThisDay(): Int {
    return Calendar
        .getInstance()
        .apply { timeInMillis = this@getSecondsOfThisDay }
        .let {
            (it.get(Calendar.HOUR_OF_DAY) * 60 * 60) +
                    (it.get(Calendar.MINUTE) * 60) +
                    it.get(Calendar.SECOND)
        }
}