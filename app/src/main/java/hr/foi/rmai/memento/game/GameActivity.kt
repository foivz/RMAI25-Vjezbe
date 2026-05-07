package hr.foi.rmai.memento.game

import android.os.Bundle
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import androidx.window.layout.WindowMetrics
import androidx.window.layout.WindowMetricsCalculator

class GameActivity : AppCompatActivity() {
    private var gameThread: GameThread? = null
    private lateinit var gameView: GameView
    private lateinit var surfaceHolder: SurfaceHolder

    private val surfaceCallback: SurfaceHolder.Callback =
        object : SurfaceHolder.Callback {
            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {

            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                gameThread = GameThread(holder, gameView)
                gameThread?.start()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                gameThread?.gameRunning = false
                try {
                    gameThread?.join()
                } catch (e: InterruptedException) {

                }
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val windowMetrics: WindowMetrics =
            WindowMetricsCalculator
                .getOrCreate()
                .computeCurrentWindowMetrics(this)

        val height = windowMetrics.bounds.height()
        val width = windowMetrics.bounds.width()

        gameView = GameView(this, width, height)
        surfaceHolder = gameView.holder
        surfaceHolder.addCallback(surfaceCallback)

        setContentView(gameView)
    }

    override fun onResume() {
        super.onResume()
        gameThread?.gameRunning = true
    }

    override fun onPause() {
        super.onPause()
        gameThread?.gameRunning = false
    }
}

