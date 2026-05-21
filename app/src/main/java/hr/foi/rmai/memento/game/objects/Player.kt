package hr.foi.rmai.memento.game.objects

import android.util.Log

class Player(locationX: Float, locationY: Float)
    : GameObject(
    1,
    2,
    5,
    "player",
    'p'
) {
    val MAX_X_VELOCITY = 10f
    var isPressingRight = false
    var isPressingLeft = false
    var isFalling = false
    var isJumping = false
    var jumpTime: Long = 0
    val maxJumpTime: Long = 700

    init {
        setWorldLocation(locationX, locationY, 0)
        facing = LEFT
        isFalling = false



        moves = true
    }

    override fun update(fps: Int, gravity: Float) {
        checkCurrentMovementDirection()
        checkPlayerDirection()
        handleJumping(gravity)

        move(fps)

        Log.i("aaa", worldLocation.x.toString())
        Log.i("aaa", worldLocation.y.toString())
        Log.i("aaa", "------")
    }

    fun startJump() {
        if (!isFalling && !isJumping) {
            isJumping = true
            jumpTime = System.currentTimeMillis()
        }
    }

    private fun checkCurrentMovementDirection() {
        if (isPressingRight) {
            xVelocity = MAX_X_VELOCITY
        } else if (isPressingLeft) {
            xVelocity = -MAX_X_VELOCITY
        } else {
            xVelocity = 0f
        }
    }

    private fun checkPlayerDirection() {
        if (xVelocity > 0) {
            facing = RIGHT
        } else if (xVelocity < 0) {
            facing = LEFT
        }
    }

    private fun handleJumping(gravity: Float) {
        if (isJumping) {
            val timeJumping = System.currentTimeMillis() - jumpTime
            if (timeJumping < maxJumpTime) {
                if (timeJumping < maxJumpTime / 2) {
                    yVelocity = -gravity
                } else if (timeJumping > maxJumpTime / 2) {
                    yVelocity = gravity
                }
            } else {
                isJumping = false
            }
        } else {
            yVelocity = gravity
            isFalling = true
        }
    }
}






