package hr.foi.rmai.memento.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.SurfaceView

class GameView(context: Context, width: Int, height: Int): SurfaceView(context) {
    private val paint = Paint()
    init {

    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (holder.surface.isValid) {
            canvas.drawColor(Color.argb(255, 0, 0, 200))
        }
    }

    fun update(fps: Int) {

    }
}