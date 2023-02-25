package com.shakibaenur.learnandroid

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.shakibaenur.learnandroid.databinding.LayoutOverlayBinding
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Created by Shakiba E Nur on 21,February,2023
 */
class OverlayView : FrameLayout {
    private val binding = LayoutOverlayBinding.inflate(LayoutInflater.from(context), this)

    private var strokeColor = ContextCompat.getColor(context, R.color.purple_500)
    private var highLightColor = getHighLightColor()
    private var backgroundColor =
        ColorUtils.setAlphaComponent(Color.BLACK, BACKGROUND_ALPHA.roundToInt())

    private val alphaPaint = Paint().apply { alpha = BACKGROUND_ALPHA.roundToInt() }

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val loadingBackgroundPaint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply { color = backgroundColor }

    private val transparentPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    private var frameHeight = 12f.toPx()
    private var frameWidth = 12f.toPx()
    private var outerRadius = OUT_RADIUS.toPx()
    private var innerRadius = (OUT_RADIUS - STROKE_WIDTH).toPx()

    private val outerFrame = RectF()
    private val innerFrame = RectF()
    private var maskBitmap: Bitmap? = null
    private var maskCanvas: Canvas? = null
    private var horizontalFrameRatio = frameWidth/frameHeight

    private var isHighlighted = false
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    var isLoading = false

    init {
        setWillNotDraw(false)
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attr: AttributeSet) : super(context, attr) {
        initParams(attr)
    }

    constructor(context: Context, attr: AttributeSet, defStyle: Int) : super(
        context,
        attr,
        defStyle
    ) {
        initParams(attr)
    }

    private fun initParams(attr: AttributeSet) {
        val a = context.theme.obtainStyledAttributes(attr, R.styleable.OverlayView, 0, 0)

        try {
            strokeColor = a.getInt(R.styleable.OverlayView_strokeColor, strokeColor)
            backgroundColor = a.getInt(R.styleable.OverlayView_backgroundColor, backgroundColor)
            highLightColor = a.getInt(R.styleable.OverlayView_highLightColor, highLightColor)
            isHighlighted = a.getBoolean(R.styleable.OverlayView_highLight, isHighlighted)
            outerRadius = a.getDimension(R.styleable.OverlayView_outerRadius, outerRadius)
            innerRadius = a.getDimension(R.styleable.OverlayView_innerRadius, innerRadius)
            horizontalFrameRatio= a.getFloat(R.styleable.OverlayView_ratio,horizontalFrameRatio)

        } finally {
            a.recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (maskBitmap == null && width > 0 && height > 0) {
            maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                .apply { maskCanvas = Canvas(this) }
            calculateFrameAndTitlePos()
        }
    }

    @Suppress("UnsafeCallOnNullableType")
    override fun onDraw(canvas: Canvas) {
        strokePaint.color =
            if (isHighlighted) highLightColor else strokeColor
        maskCanvas!!.drawColor(backgroundColor)
        maskCanvas!!.drawRoundRect(outerFrame, outerRadius, outerRadius, strokePaint)
        maskCanvas!!.drawRoundRect(innerFrame, innerRadius, innerRadius, transparentPaint)
        if (isLoading) maskCanvas!!.drawRoundRect(
            innerFrame,
            innerRadius,
            innerRadius,
            loadingBackgroundPaint
        )
        canvas.drawBitmap(maskBitmap!!, 0f, 0f, alphaPaint)
        super.onDraw(canvas)
    }

    private fun calculateFrameAndTitlePos() {
        val centralX = width / 2
        val centralY = height / 2
        val minLength = min(centralX, centralY)
        val marginRatio = if (horizontalFrameRatio > 1f) {
            FRAME_MARGIN_RATIO * ((1f / horizontalFrameRatio) * 1.5f)
        } else {
            FRAME_MARGIN_RATIO
        }
        val strokeLength = minLength - (minLength * marginRatio)
        val strokeWidth = STROKE_WIDTH.toPx()
        outerFrame.set(
            centralX - strokeLength / frameWidth,
            centralY - strokeLength/frameHeight,
            centralX + strokeLength / frameWidth,
            centralY + strokeLength/frameHeight
        )
        innerFrame.set(
            outerFrame.left + strokeWidth,
            outerFrame.top + strokeWidth,
            outerFrame.right - strokeWidth,
            outerFrame.bottom - strokeWidth
        )
        val topInsetsToOuterFrame = (-paddingTop + centralY - strokeLength).roundToInt()
    }

    private fun getHighLightColor(): Int {
        return TypedValue().let {
            if (context.theme.resolveAttribute(android.R.attr.colorAccent, it, true)) {
                it.data
            } else {
                ContextCompat.getColor(context, R.color.purple_500)
            }
        }
    }

    private fun View.updateTopMargin(topPx: Int) {
        val params =
            layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = topPx
        layoutParams = params
    }

    private fun Float.toPx() =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics)

    companion object {
        private const val BACKGROUND_ALPHA = 0.80 * 255
        private const val STROKE_WIDTH = 0f
        private const val OUT_RADIUS = 1f
        private const val FRAME_MARGIN_RATIO = 1f / 4
    }
}