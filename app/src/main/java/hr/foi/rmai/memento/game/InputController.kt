package hr.foi.rmai.memento.game

import android.graphics.Rect
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintSet
import hr.foi.rmai.memento.game.levels.LevelManager

class InputController(
    screenWidth: Int,
    screenHeight: Int,
    val levelManager: LevelManager
) {
    private var x = 0
    private var y = 0
    var left: Rect
    var right: Rect
    var jump: Rect
    var shoot: Rect
    var pause: Rect

    init {
        val buttonWidth = screenWidth / 8
        val buttonHeight = screenHeight / 7
        val buttonPadding = screenWidth / 40

        left = Rect(
            buttonPadding,
            screenHeight - buttonHeight - buttonPadding,
            buttonWidth,
            screenHeight - buttonPadding
        )
        right = Rect(
            buttonWidth + buttonPadding,
            screenHeight - buttonHeight - buttonPadding,
            buttonWidth + buttonPadding + buttonWidth,
            screenHeight - buttonPadding
        )

        jump = Rect(
            screenWidth - buttonWidth - buttonPadding,
            screenHeight - 2*buttonHeight - 2*buttonPadding,
            screenWidth - buttonPadding,
            screenHeight - buttonHeight - 2*buttonPadding
        )

        shoot = Rect(
            screenWidth - buttonWidth - buttonPadding,
            screenHeight - buttonHeight - buttonPadding,
            screenWidth - buttonPadding,
            screenHeight - buttonPadding
        )

        pause = Rect(
            screenWidth - buttonPadding - buttonWidth,
            buttonPadding,
            screenWidth - buttonPadding,
            buttonPadding + buttonHeight
        )
    }

    fun getButtons(): ArrayList<Rect> {
        val currentButtonList = ArrayList<Rect>()

        currentButtonList.add(left)
        currentButtonList.add(right)
        currentButtonList.add(jump)
        currentButtonList.add(shoot)
        currentButtonList.add(pause)

        return currentButtonList
    }

    fun handleInput(motionEvent: MotionEvent) {
        for (i in 0..<motionEvent.pointerCount) {
            x = motionEvent.getX(i).toInt()
            y = motionEvent.getY(i).toInt()

            if (levelManager.playing) {
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> handleTouchDown()
                    MotionEvent.ACTION_UP -> handleTouchUp()
                    MotionEvent.ACTION_POINTER_DOWN -> handlePointerDown()
                    MotionEvent.ACTION_POINTER_UP -> handlePointerUp()
                }
            } else {
                when (motionEvent.action and MotionEvent.ACTION_MASK) {
                    MotionEvent.ACTION_DOWN -> handlePause()
                }
            }
        }
    }

    private fun handlePause() {
        if (pause.contains(x, y)) {
            levelManager.switchPlayingStatus()
        }
    }

    private fun handlePointerDown() {
        handleMovement()
        handlePause()
        handleShooting()
    }

    private fun handleTouchDown() {
        handleMovement()
        handlePause()
        handleShooting()
    }

    private fun handlePointerUp() {
        if (right.contains(x, y)) {
            levelManager.player.isPressingRight = false
        } else if (left.contains(x, y)) {
            levelManager.player.isPressingLeft = false
        }
    }

    private fun handleTouchUp() {
        if (right.contains(x, y)) {
            levelManager.player.isPressingRight = false
        } else if (left.contains(x, y)) {
            levelManager.player.isPressingLeft = false
        }
    }

    private fun handleMovement() {
        if (right.contains(x, y)) {
            levelManager.player.isPressingRight = true
            levelManager.player.isPressingLeft = false
        } else if (left.contains(x, y)) {
            levelManager.player.isPressingRight = false
            levelManager.player.isPressingLeft = true
        } else if (jump.contains(x, y)) {
            levelManager.player.startJump()
        }
    }

    private fun handleShooting() {

    }
}








