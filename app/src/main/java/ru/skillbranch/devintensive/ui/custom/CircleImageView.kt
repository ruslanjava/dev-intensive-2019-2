package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.graphics.drawable.Drawable
import android.os.Build
import android.graphics.drawable.BitmapDrawable
import android.graphics.*
import android.graphics.RectF
import android.graphics.Bitmap
import android.graphics.Shader
import androidx.annotation.*
import android.graphics.Outline
import android.view.ViewOutlineProvider
import android.annotation.TargetApi
import android.view.View
import ru.skillbranch.devintensive.R

open class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    @ColorRes
    val defaultBorderId = R.color.color_primary

    @DimenRes
    val defaultWidthId = R.dimen.avatar_border

    private var borderColor: Int
    private var borderWidth: Float

    private var mBitmapShader: Shader? = null
    private val mShaderMatrix: Matrix = Matrix()

    private val mBitmapDrawBounds: RectF = RectF()
    private var mStrokeBounds: RectF = RectF()

    private var mBitmap: Bitmap? = null

    private var mBitmapPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mStrokePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mInitialized: Boolean = false

    private var backgroundPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    @ColorInt
    private var backgroundColor : Int? = null

    protected var hasAvatar : Boolean = false

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getColor(
                R.styleable.CircleImageView_ci_borderColor,
                resources.getColor(defaultBorderId, context.theme)
            )
            borderWidth = a.getDimension(
                R.styleable.CircleImageView_ci_borderWidth,
                resources.getDimension(defaultWidthId)
            )
            a.recycle()
        } else {
            borderColor = resources.getColor(defaultBorderId, context.theme)
            borderWidth = resources.getDimension(defaultWidthId)
        }

        mStrokePaint.setColor(borderColor)
        mStrokePaint.setStyle(Paint.Style.STROKE)
        mStrokePaint.setStrokeWidth(borderWidth)

        backgroundPaint.setColor(resources.getColor(R.color.green, context.theme))
        backgroundPaint.setStyle(Paint.Style.FILL)

        mInitialized = true

        setupBitmap()
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        setupBitmap()
        hasAvatar = true
    }

    override fun setImageDrawable(@Nullable drawable: Drawable?) {
        super.setImageDrawable(drawable)
        setupBitmap()
        hasAvatar = drawable != null
    }

    override fun setImageBitmap(@Nullable bm: Bitmap?) {
        super.setImageBitmap(bm)
        setupBitmap()
        hasAvatar = bm != null
    }

    override fun setBackgroundColor(@ColorInt color: Int) {
        this.backgroundColor = color
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val halfStrokeWidth = mStrokePaint.getStrokeWidth() / 2f
        updateCircleDrawBounds(mBitmapDrawBounds)
        mStrokeBounds.set(mBitmapDrawBounds)
        mStrokeBounds.inset(halfStrokeWidth, halfStrokeWidth)

        updateBitmapSize()

        outlineProvider = CircleImageViewOutlineProvider(mStrokeBounds)
    }

    override fun onDraw(canvas: Canvas) {
        if (hasAvatar) {
            canvas.drawOval(mBitmapDrawBounds, mBitmapPaint)
            return
        }

        if (backgroundColor != null) {
            mBitmapPaint.setColor(backgroundColor!!)
            canvas.drawOval(mStrokeBounds, backgroundPaint)
        }

        if (mStrokePaint.getStrokeWidth() > 0f) {
            canvas.drawOval(mStrokeBounds, mStrokePaint)
        }
    }

    @ColorInt
    fun getStrokeColor(): Int {
        return mStrokePaint.getColor()
    }

    fun setStrokeColor(@ColorInt color: Int) {
        mStrokePaint.setColor(color)
        invalidate()
    }

    @Dimension
    fun getStrokeWidth(): Float {
        return mStrokePaint.getStrokeWidth()
    }

    fun setStrokeWidth(@Dimension width: Float) {
        mStrokePaint.setStrokeWidth(width)
        invalidate()
    }

    protected fun updateCircleDrawBounds(bounds: RectF) {
        val contentWidth = (width - paddingLeft - paddingRight).toFloat()
        val contentHeight = (height - paddingTop - paddingBottom).toFloat()

        var left = paddingLeft.toFloat()
        var top = paddingTop.toFloat()
        if (contentWidth > contentHeight) {
            left += (contentWidth - contentHeight) / 2f
        } else {
            top += (contentHeight - contentWidth) / 2f
        }

        val diameter = Math.min(contentWidth, contentHeight)
        bounds.set(left, top, left + diameter, top + diameter)
    }

    private fun setupBitmap() {
        if (!mInitialized) {
            return
        }

        mBitmap = getBitmapFromDrawable(drawable)
        if (mBitmap == null) {
            return
        }

        mBitmapShader = BitmapShader(mBitmap!!, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapPaint.setShader(mBitmapShader)

        updateBitmapSize()
    }

    private fun updateBitmapSize() {
        if (mBitmap == null) {
            return
        }

        val dx: Float
        val dy: Float
        val scale: Float

        // scale up/down with respect to this view size and maintain aspect ratio
        // translate bitmap position with dx/dy to the center of the image
        if (mBitmap!!.getWidth() < mBitmap!!.getHeight()) {
            scale = (mBitmapDrawBounds.width() / mBitmap!!.getWidth())
            dx = mBitmapDrawBounds.left
            dy =
                mBitmapDrawBounds.top - mBitmap!!.getHeight() * scale / 2f + mBitmapDrawBounds.width() / 2f
        } else {
            scale = (mBitmapDrawBounds.height() / mBitmap!!.getHeight())
            dx =
                mBitmapDrawBounds.left - mBitmap!!.getWidth() * scale / 2f + mBitmapDrawBounds.width() / 2f
            dy = mBitmapDrawBounds.top
        }
        mShaderMatrix.setScale(scale, scale)
        mShaderMatrix.postTranslate(dx, dy)
        mBitmapShader!!.setLocalMatrix(mShaderMatrix)
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)

        return bitmap
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    inner class CircleImageViewOutlineProvider internal constructor(rect: RectF) :
        ViewOutlineProvider() {

        private val mRect: Rect

        init {
            mRect = Rect(
                rect.left.toInt(),
                rect.top.toInt(),
                rect.right.toInt(),
                rect.bottom.toInt()
            )
        }

        override fun getOutline(view: View, outline: Outline) {
            outline.setOval(mRect)
        }

    }

}