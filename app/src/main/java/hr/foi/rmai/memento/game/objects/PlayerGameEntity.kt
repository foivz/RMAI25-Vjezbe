package hr.foi.rmai.memento.game.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import hr.foi.rmai.memento.R
import kotlin.math.max
import kotlin.math.min
import androidx.core.graphics.scale

class PlayerGameEntity(context: Context, width: Int, height: Int) :
    GameEntity(context, width, height) {
    var boosting = false
    private val GRAVITY = -12
    private val MAX_SPEED = 20
    private val MIN_SPEED = 1
    var bitmap: Bitmap

    init {
        bitmap = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.ship
        ).scale(sizeX, sizeY, false)

        maxY = height - bitmap.height * 2
    }

    override fun update() {
        if (boosting) {
            speed += 3
        } else {
            speed -= 5
        }

        speed = min(speed, MAX_SPEED)
        speed = max(speed, MIN_SPEED)

        y -= (speed + GRAVITY)

        if (y < minY) {
            y = minY
        }

        if (y > maxY) {
            y = maxY
        }
    }
}





