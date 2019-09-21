package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import ru.skillbranch.devintensive.R

class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs : AttributeSet? = null,
    defStyleAttr: Int = 0
) : CircleImageView(context, attrs, defStyleAttr) {

    private var initials : String = "??"
    private var textPaint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var initialsX : Float = 0.0f
    private var letterY : Float = 0.0f
    private var textRect: Rect = Rect()

    private var colors: IntArray

    init {
        textPaint.setColor(resources.getColor(android.R.color.white, context.theme))
        textPaint.setTextAlign(Paint.Align.CENTER)
        textPaint.textSize = convertDpToPixel(16.0f, context)

        colors = resources.getIntArray(R.array.rainbow)
        setInitials(initials)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
        letterY = (height / 2) - ((textPaint.descent() + textPaint.ascent()) / 2)
    }

    override fun onDraw(canvas : Canvas) {
        super.onDraw(canvas)

        if (hasAvatar) {
            return
        }

        textPaint.getTextBounds(initials, 0, initials.length, textRect)
        initialsX = width * 0.5f
        canvas.drawText(initials, initialsX, letterY, textPaint)
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi / 160f)
    }

    fun setInitials(initials: String) {
        this.initials = initials
        val index = initials.hashCode() % colors.size
        setBackgroundColor(colors[index])
    }

}