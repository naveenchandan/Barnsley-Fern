package com.player.barnsleyfern

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import java.util.concurrent.ThreadLocalRandom

class BarnsleyFernView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    private var bitmap: Bitmap? = null

    private var previousX = 0.0f
    private var previousY = 0.0f

    private val valueAnimator = ValueAnimator.ofFloat(0.0f, 10000.0f).apply {
        duration = 20000L
        addUpdateListener {
            for (i in 0 until 200) {
                val random = randomFloat()
                val nextX: Float
                val nextY: Float
                when {
                    random < 0.02f -> {
                        nextX = 0.0f
                        nextY = 0.16f * previousY
                    }
                    random < 0.86f -> {
                        nextX = 0.85f * previousX + 0.04f * previousY
                        nextY = -0.04f * previousX + 0.85f * previousY + 1.6f
                    }
                    random < 0.93f -> {
                        nextX = 0.2f * previousX - 0.26f * previousY
                        nextY = 0.23f * previousX + 0.22f * previousY + 1.6f
                    }
                    else -> {
                        nextX = -0.15f * previousX + 0.28f * previousY
                        nextY = 0.26f * previousX + 0.24f * previousY + 0.44f
                    }
                }
                val bx = map(
                    nextX, START_X,
                    END_X, 0.0f, width - 1.0f
                )
                val by = map(
                    nextY,
                    END_Y,
                    START_Y, 0.0f, height - 1.0f
                )
                bitmap?.setPixel(bx.toInt(), by.toInt(), Color.GREEN)
                previousX = nextX
                previousY = nextY
            }
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (valueAnimator.isRunning) {
            valueAnimator.cancel()
        }
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        valueAnimator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (valueAnimator.isRunning) {
            valueAnimator.cancel()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(
            bitmap ?: return,
            0.0f, 0.0f, null
        )
    }

    private fun randomFloat(): Float {
        return ThreadLocalRandom.current().nextFloat()
    }

    private fun map(
        value: Float,
        startValue1: Float,
        endValue1: Float,
        startValue2: Float,
        endValue2: Float
    ): Float =
        (value - startValue1) / (endValue1 - startValue1) * (endValue2 - startValue2) + startValue2

    private companion object {
        const val START_X = -2.1820F
        const val END_X = 2.6558F
        const val START_Y = 0.0F
        const val END_Y = 9.9983F
    }
}