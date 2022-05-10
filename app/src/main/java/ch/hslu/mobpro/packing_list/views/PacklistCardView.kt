package ch.hslu.mobpro.packing_list.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup.MarginLayoutParams


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
        setTitleText(canvas, title)
       // setDurationText(canvas, duration, 0)
        // setLocationText(canvas, location, 0)
    }



    private fun drawCard(canvas: Canvas?) {

        val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = cardColor }
        rectWidth = currentViewWidth - 20f
        canvas?.drawRoundRect(20f, 20f, rectWidth, rectHeight, 20f, 20f,
            cardPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.v(TAG, "onSizeChanged")
        currentViewHeight = h
        currentViewWidth = w
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
         updateMargin()
    }

    private fun updateMargin() {
        if (layoutParams is MarginLayoutParams) {
            val layoutParams = layoutParams as MarginLayoutParams
            val additionalMargin = 10
            val leftMargin: Int = layoutParams.leftMargin + additionalMargin
            val topMargin: Int = layoutParams.topMargin + additionalMargin
            val rightMargin: Int = layoutParams.rightMargin + additionalMargin
            val bottomMargin: Int = layoutParams.bottomMargin + additionalMargin
            Log.d(TAG, leftMargin.toString())
            Log.d(TAG, topMargin.toString())
            Log.d(TAG, bottomMargin.toString())
            Log.d(TAG, rightMargin.toString())

            layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin)
            requestLayout()
        }
    }


    private fun setTitleText(canvas: Canvas?, text: String) {
        val titleText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = primaryTextColor
            textAlign = Paint.Align.LEFT
            textSize = mediumTextSize
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas?.let {
            titleText.getTextBounds(text, 0, text.length, placeholderRect)
            it.drawText(
                text,
                0,
                text.length,
                40f,
                placeholderRect.height().toFloat() + mediumTextSize,
                titleText
            )
        }
    }

    private fun setDurationText(canvas: Canvas?, text: String, fromTop: Int) {

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

    private fun setLocationText(canvas: Canvas?, text: String, fromTop: Int) {

        val locationTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = primaryTextColor
            textAlign = Paint.Align.RIGHT
            textSize = bigTextSize
        }
        canvas?.let {
            locationTextPaint.getTextBounds(text, 0, text.length, placeholderRect)
            it.drawText(
                text,
                0,
                text.length,
                (40f + placeholderRect.width()),
                placeholderRect.height() + 120f + bigTextSize + 20f + fromTop.toFloat(),
                locationTextPaint
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
        private const val TAG = "SpecialCardView"
    }

}