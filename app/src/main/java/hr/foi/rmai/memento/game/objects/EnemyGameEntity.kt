package hr.foi.rmai.memento.game.objects

import android.content.Context
import java.util.Random

class EnemyGameEntity(
    context: Context,
    width: Int,
    height: Int,
    val title: String
): GameEntity(context, width, height) {
    val generator = Random()
    init {
        speed = generator.nextInt(6) + 10
        x = width
        y = generator.nextInt(maxY)
    }

    override fun update() {
        x -= speed
        x -= playerSpeed

        if (x < minX) {
            speed = generator.nextInt(10)
            x = maxX
            y = generator.nextInt(maxY)
        }
    }

}