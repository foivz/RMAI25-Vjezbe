package hr.foi.rmai.memento.game.objects

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import hr.foi.rmai.memento.game.WorldLocation
import androidx.core.graphics.scale

abstract class GameObject(
    val width: Int,
    val height: Int,
    val animFrameCount: Int,
    val bitmapName: String,
    val type: Char
) {
    val worldLocation: WorldLocation = WorldLocation(0f, 0f, 0)

    fun setWorldLocation(x: Float, y: Float, z: Int) {
        worldLocation.x = x
        worldLocation.y = y
        worldLocation.z = z
    }

    fun prepareBitmap(context: Context, pixelsPerMeter: Int): Bitmap {
        val resID = context.resources.getIdentifier(
            bitmapName,
            "drawable",
            context.packageName
        )

        var bitmap = BitmapFactory.decodeResource(context.resources, resID)
        bitmap = bitmap.scale(
            (width * animFrameCount * pixelsPerMeter),
            (height * pixelsPerMeter),
            false
        )

        return bitmap
    }

    abstract fun update(fps: Int, gravity: Float)
}





