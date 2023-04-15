package ch.hslu.mobpro.packing_list.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.graphics.toColorInt

object colors {
   val noteDefaultColor  = "#fff68fh"
}

class CommonUtils {


    companion object {


        /** fancy kotlin extension function ( ͡° ͜ʖ ͡°) */
        fun View.showKeyboard() {
            this.requestFocus()
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }

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
