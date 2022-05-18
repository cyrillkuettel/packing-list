package ch.hslu.mobpro.packing_list.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View


class PacklistCardView @JvmOverloads
constructor(
    private val ctx: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private var title: String = "Wanderung Martinsloch",
    private var location: String = "Location: Martinsloch",
    private var date: String = "Date: 25.05.2022",
    private var duration: String = "Duration: 1d",
    private val cardColor: Int = Color.parseColor("#006400"),
) : View(ctx, attributeSet, defStyleAttr) {


    private var primaryTextColor = Color.WHITE
    private var mediumTextSize = convertToDP(15f)
    private var bigTextSize = convertToDP(40f)
    private var rectWidth = 0f
    private var rectHeight = convertToDP(100f)

    private var placeholderRect = Rect()
    private var currentViewHeight = 0
    private var currentViewWidth = 0


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCard(canvas)
        paintTitleText(canvas)
        // setDurationText(canvas, duration, 0)
        paintLocationText(canvas)
    }


    private fun drawCard(canvas: Canvas?) {

        val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = cardColor }
        rectWidth = currentViewWidth - 20f
        canvas?.drawRoundRect(20f, 20f, rectWidth, rectHeight, 20f, 20f,
            cardPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        currentViewHeight = h
        currentViewWidth = w
    }


    fun setTitle(text: String?) {
        if (text != null) {
            title = text
        }
    }

    fun setLocation(text: String?){
        if (text != null){
            location = text
        }
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

    private fun paintDurationText(canvas: Canvas?, text: String, fromTop: Int) {

        val durationTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = primaryTextColor
            textAlign = Paint.Align.RIGHT
            textSize = mediumTextSize
        }
        canvas?.let {
            durationTextPaint.getTextBounds(text, 0, text.length, placeholderRect)
            it.drawText(
                text,
                0,
                text.length,
                (40f + placeholderRect.width()),
                placeholderRect.height() + mediumTextSize + fromTop.toFloat(),
                durationTextPaint
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