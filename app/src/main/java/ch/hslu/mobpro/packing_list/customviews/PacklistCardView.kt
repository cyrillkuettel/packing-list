package ch.hslu.mobpro.packing_list.customviews

import android.content.Context
import android.content.res.Configuration
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View


/*** A custom view that represents a single Top-Level item.
 It's very much up to the intention of the designer here.*/

class PacklistCardView @JvmOverloads
constructor(
    ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private var title: String = "",
    private var location: String = "",
    private var date: String = "",
    private var cardColor: Int = Color.parseColor("#006400"), // green
) : View(ctx, attributeSet, defStyleAttr) {



    private var primaryTextColor = Color.WHITE
    private var mediumTextSize = convertToDP(15f)
    private var rectWidth = 0f
    private var rectHeight = convertToDP(100f)

    private var placeholderRect = Rect()
    private var currentViewHeight = 0
    private var currentViewWidth = 0


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCard(canvas)
        paintTitleText(canvas)

        when (context.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                primaryTextColor = Color.WHITE
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                primaryTextColor = Color.BLACK
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                primaryTextColor = Color.BLACK
            }
        }
       // for now, don't draw these. I don't want this anyway.
//        paintDate(canvas)
//        paintLocationText(canvas)
    }


    private fun drawCard(canvas: Canvas?) {

        val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = cardColor }
        rectWidth = currentViewWidth - 20f
        canvas?.drawRoundRect(20f, 20f, rectWidth, rectHeight, 20f, 20f, cardPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        currentViewHeight = h
        currentViewWidth = w
    }

    fun setTitle(text: String?) {
        text?.also { title = it }
    }


    fun setLocation(text: String?){
        text?.also { location = it }
    }

    fun setDate(text: String?){
        text?.also {
            date = it
        }
    }

    fun setColor(color: Int) {
        cardColor = color
    }

    private fun paintTitleText(canvas: Canvas?) {
        val titleText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = primaryTextColor
            textAlign = Paint.Align.LEFT
            textSize = mediumTextSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas?.let {
            titleText.getTextBounds(title, 0, title.length, placeholderRect)
            it.drawText(
                title,
                0,
                title.length,
                40f,
                placeholderRect.height().toFloat() + mediumTextSize,
                titleText
            )
        }
    }

    private fun paintDate(canvas: Canvas?) {

        val listDate = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = primaryTextColor
            textAlign = Paint.Align.RIGHT
            textSize = mediumTextSize
        }
        canvas?.let {
            listDate.getTextBounds(date, 0, date.length, placeholderRect)
            it.drawText(
                date,
                0,
                date.length,
                950f,
                85f,
                listDate
            )
        }
    }

    private fun paintLocationText(canvas: Canvas?) {
        val titleText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = primaryTextColor
            textAlign = Paint.Align.RIGHT
            textSize = mediumTextSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas?.let {
            titleText.getTextBounds(location, 0, location.length, placeholderRect)
            it.drawText(
                location,
                0,
                location.length,
                950f,
                240f,
                titleText
            )
        }
    }

    private fun convertToDP(value: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            value,
            resources.displayMetrics
        )
    }

    companion object {
        private const val TAG = "PacklistCardView"
    }

}