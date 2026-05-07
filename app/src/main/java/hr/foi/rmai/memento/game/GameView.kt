package hr.foi.rmai.memento.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import hr.foi.rmai.memento.database.TasksDatabase
import hr.foi.rmai.memento.game.objects.EnemyGameEntity
import hr.foi.rmai.memento.game.objects.PlayerGameEntity
import hr.foi.rmai.memento.game.objects.SpaceDustEntity

class GameView(context: Context, width: Int, height: Int) : SurfaceView(context) {
    private val paint = Paint()
    private val player: PlayerGameEntity
    private val spaceDustNum = 30
    private val enemyList = ArrayList<EnemyGameEntity>()
    private val spaceDustList = ArrayList<SpaceDustEntity>()
    init {
        player = PlayerGameEntity(context, width, height)
        val taskCourses = TasksDatabase
            .getInstance()
            .getTaskCoursesDao()
            .getAllCourses()

        taskCourses.forEach { taskCourse ->
            val newEnemy = EnemyGameEntity(context, width, height, taskCourse.name)
            enemyList.add(newEnemy)
        }

        for (i in 1..spaceDustNum) {
            spaceDustList.add(SpaceDustEntity(context, width, height))
        }
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (holder.surface.isValid) {
            canvas.drawColor(Color.argb(255, 0, 0, 0))

            canvas.drawBitmap(
                player.bitmap,
                player.x.toFloat(),
                player.y.toFloat(),
                paint
            )

            paint.color = Color.argb(255, 255, 255, 255)
            paint.textSize = 100f
            paint.strokeWidth = 10f

            enemyList.forEach { enemy ->
                canvas.drawText(
                    enemy.title,
                    enemy.x.toFloat(),
                    enemy.y.toFloat(),
                    paint
                )
            }

            spaceDustList.forEach { spaceDust ->
                canvas.drawPoint(spaceDust.x.toFloat(), spaceDust.y.toFloat(), paint)
            }
        }
    }

    fun update() {
        player.update()
        enemyList.forEach { enemy ->
            enemy.playerSpeed = player.speed
            enemy.update()
        }

        spaceDustList.forEach { spaceDust ->
            spaceDust.update()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_UP -> {
                player.boosting = false
            }

            MotionEvent.ACTION_DOWN -> {
                player.boosting = true
            }
        }

        return true
    }
}