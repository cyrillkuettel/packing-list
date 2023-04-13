package ch.hslu.mobpro.packing_list.utils

import androidx.core.graphics.toColorInt

class CommonUtils {
    companion object {
        fun getRandomColor(): Int {

            val colors = listOf(
                "#FF0000",
                "#00FF00",
                "#0000FF",
                "#FFFF00",
                "#FF00FF",
                "#00FFFF",
                "#800000",
                "#008000",
                "#000080",
                "#808080"
            )
            return colors.random().toColorInt()
        }
    }
}
