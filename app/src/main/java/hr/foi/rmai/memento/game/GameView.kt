package hr.foi.rmai.memento.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.View
import hr.foi.rmai.memento.game.levels.LevelManager
import hr.foi.rmai.memento.game.objects.GameObject
import java.util.logging.Level

class GameView(context: Context, width: Int, height: Int): SurfaceView(context) {
    private val paint = Paint()
    private val viewport: Viewport
    private lateinit var levelManager: LevelManager
    init {
        viewport = Viewport(width, height)
        loadLevel("TestLevel", 16f, 0.25f)

        levelManager.playing = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> levelManager.switchPlayingStatus()
        }

        return true
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (holder.surface.isValid) {
            canvas.drawColor(Color.argb(255, 0, 0, 200))

            var toScreen2d: Rect
            for (layer in -1..2) {
                for (gameObject in levelManager.gameObjects) {
                    if (gameObject.visible && gameObject.worldLocation.z == layer) {
                        toScreen2d = viewport.worldToScreen(
                            gameObject.worldLocation.x,
                            gameObject.worldLocation.y,
                            gameObject.width,
                            gameObject.height
                        )
                        canvas.drawBitmap(
                            levelManager.getBitmap(gameObject.type),
                            toScreen2d.left.toFloat(),
                            toScreen2d.top.toFloat(),
                            paint
                        )
                    }
                }
            }
        }
    }

    fun update(fps: Int) {
        for (gameObject: GameObject in levelManager.gameObjects) {
            if (gameObject.active) {
                if (!viewport.clipObjects(
                    gameObject.worldLocation.x,
                    gameObject.worldLocation.y,
                    gameObject.width.toFloat(),
                    gameObject.height.toFloat()
                )) {
                    gameObject.visible = true
                }

                if (levelManager.playing) {
                    gameObject.update(fps, levelManager.gravity)
                }
            } else {
                gameObject.visible = false
            }
        }

        if (levelManager.playing) {
            viewport.setWorldCenter(
                levelManager.player.worldLocation.x,
                levelManager.player.worldLocation.y
            )
        }
    }

    fun loadLevel(
        level: String,
        playerX: Float,
        playerY: Float
    ) {
        levelManager = LevelManager(
            level,
            context,
            viewport.pixelsPerMeterX,
            playerX,
            playerY
        )

        viewport.setWorldCenter(
            levelManager.player.worldLocation.x,
            levelManager.player.worldLocation.y
        )
    }
}





