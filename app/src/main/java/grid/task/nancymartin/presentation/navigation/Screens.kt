package grid.task.nancymartin.presentation.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

sealed class Screen {
    @Parcelize
    @Serializable
    data object Tasks : Screen(), Parcelable

    @Parcelize
    @Serializable
    data object CreateTask : Screen(), Parcelable
}