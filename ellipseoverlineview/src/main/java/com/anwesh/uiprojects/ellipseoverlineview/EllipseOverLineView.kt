package com.anwesh.uiprojects.ellipseoverlineview

/**
 * Created by anweshmishra on 07/04/19.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
import android.app.Activity
import android.content.Context

val nodes : Int = 5
val ellips : Int = 2
val scGap : Float = 0.05f
val scDiv : Double = 0.51
val strokeFactor : Int = 90
val sizeFactor : Float = 2.9f
val foreColor : Int = Color.parseColor("#f44336")
val backColor : Int = Color.parseColor("#BDBDBD")
val parts : Int = 2
val bFactor : Float = 2.8f

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()
fun Float.mirrorValue(a : Int, b : Int) : Float = (1 - scaleFactor()) * a.inverse() + scaleFactor() * b.inverse()
fun Float.updateValue(dir : Float, a : Int, b : Int) : Float = mirrorValue(a, b) * scGap * dir
fun Int.sf() : Float = 1f - 2 * this
fun Int.sjf() : Float = (this % 2).sf()

fun Canvas.drawEllipse(a : Float, b : Float, sc : Float, paint : Paint) {
    drawArc(RectF(-a, -b, a, b), 0f, 180f * sc, false, paint)
}

fun Paint.setStyle(minDimension : Float) {
    strokeWidth = minDimension / strokeFactor
    strokeCap = Paint.Cap.ROUND
    style = Paint.Style.STROKE
    color = foreColor
}

fun Canvas.drawEOLNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    val size : Float = gap / sizeFactor
    val sc1 : Float = scale.divideScale(0, 2)
    val sc2 : Float = scale.divideScale(1, 2)
    paint.setStyle(Math.min(w, h))
    save()
    translate(gap * (i + 1), h / 2 - (h / 2 + size) * sc2.divideScale(1, parts) * i.sjf())
    rotate(90f * sc2.divideScale(0, parts))
    drawLine(-size, 0f, size, 0f, paint)
    for (j in 0..(ellips - 1)) {
        save()
        scale(1f, j.sf())
        drawEllipse(size, size / bFactor, sc1.divideScale(j, ellips), paint)
        restore()
    }
    restore()
}

class EllipseOverLineView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scale.updateValue(dir, ellips, parts)
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}