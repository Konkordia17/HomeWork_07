package otus.homework.customview

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PayloadUiModel(
    val id: Int,
    val name: String,
    val amount: Int,
    val category: String,
    val time: Int
) : Parcelable

