package com.tonkeeper.uikit.drawable

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import com.tonkeeper.uikit.ArgbEvaluator
import com.tonkeeper.uikit.R
import com.tonkeeper.uikit.base.BaseDrawable
import com.tonkeeper.uikit.extensions.dp
import com.tonkeeper.uikit.extensions.getDimension

class InputDrawable(
    context: Context
): BaseDrawable() {

    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 160
    }

    private val cornerRadius = context.getDimension(R.dimen.cornerMedium)
    private val borderSize = 1.5f.dp
    private val backgroundColor = context.getColor(R.color.fieldBackground)
    private val borderColor = context.getColor(R.color.fieldActiveBorder)

    private val errorBackgroundColor = context.getColor(R.color.fieldErrorBackground)
    private val errorBorderColor = context.getColor(R.color.fieldErrorBorder)

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = backgroundColor
    }

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT
        strokeWidth = borderSize
        style = Paint.Style.STROKE
    }

    var error: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                updateState()
            }
        }

    var active: Boolean = false
        set(value) {
            if (value != field) {
                field = value
                updateState()
            }
        }

    private val boundF = RectF()

    override fun draw(canvas: Canvas) {
        drawBackground(canvas)
        drawBorder(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        canvas.drawRoundRect(
            boundF,
            cornerRadius,
            cornerRadius,
            backgroundPaint
        )
    }

    private fun drawBorder(canvas: Canvas) {
        canvas.drawRoundRect(
            boundF.left + (borderSize / 2f),
            boundF.top + (borderSize / 2f),
            boundF.right - (borderSize / 2f),
            boundF.bottom - (borderSize / 2f),
            cornerRadius,
            cornerRadius,
            borderPaint
        )
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        boundF.set(bounds)
    }

    private fun updateState() {
        val oldBackgroundColor = backgroundPaint.color
        val oldBorderColor = borderPaint.color

        val newBackgroundColor: Int
        val newBorderColor: Int
        if (error) {
            newBackgroundColor = errorBackgroundColor
            newBorderColor = errorBorderColor
        } else if (active) {
            newBackgroundColor = backgroundColor
            newBorderColor = borderColor
        } else {
            newBackgroundColor = backgroundColor
            newBorderColor = Color.TRANSPARENT
        }

        backgroundPaint.color = newBackgroundColor
        borderPaint.color = newBorderColor

        animator.cancel()
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            backgroundPaint.color = ArgbEvaluator.instance.evaluate(value, oldBackgroundColor, newBackgroundColor)
            borderPaint.color = ArgbEvaluator.instance.evaluate(value, oldBorderColor, newBorderColor)
            invalidateSelf()
        }
        animator.start()
    }

}