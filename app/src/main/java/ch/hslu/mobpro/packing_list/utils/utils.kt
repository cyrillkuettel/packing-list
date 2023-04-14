package ch.hslu.mobpro.packing_list.utils

import androidx.core.graphics.toColorInt

object colors {
   val noteDefaultColor  = "#fff68fh"
}

class CommonUtils {
    companion object {
        fun getRandomColor(): Int {

            val colors = listOf(
                "#FF0000",
                "#00FF00",
                "#0000FF",
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
