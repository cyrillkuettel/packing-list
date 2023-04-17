package ch.hslu.mobpro.packing_list.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.graphics.toColorInt

object Colors {
    val defaultNoteColor: Int = Color.parseColor("#07b335")
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
