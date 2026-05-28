package hr.foi.rmai.memento.game.objects

import android.util.Log
import hr.foi.rmai.memento.game.RectHitbox

class Player(locationX: Float, locationY: Float)
    : GameObject(
    1,
    2,
    5,
    "player",
    'p'
) {
    private var rectHitboxFeet = RectHitbox()
    private var rectHitboxHead = RectHitbox()
    private var rectHitboxLeft = RectHitbox()
    private var rectHitboxRight = RectHitbox()

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

        updateLeftHitbox(worldLocation.x, worldLocation.y)
        updateRightHitbox(worldLocation.x, worldLocation.y)
        updateHeadHitbox(worldLocation.x, worldLocation.y)
        updateFeetHitbox(worldLocation.x, worldLocation.y)
    }

    private fun updateLeftHitbox(lx: Float, ly: Float) {
        rectHitboxLeft.top = ly + height * 0.2f
        rectHitboxLeft.left = lx + width * 0.2f
        rectHitboxLeft.bottom = ly + height * 0.8f
        rectHitboxLeft.right = lx + width * 0.3f
    }

    private fun updateRightHitbox(lx: Float, ly: Float) {
        rectHitboxRight.top = ly + height * 0.2f
        rectHitboxRight.left = lx + width * 0.8f
        rectHitboxRight.bottom = ly + height * 0.8f
        rectHitboxRight.right = lx + width * 0.7f
    }

    private fun updateFeetHitbox(lx: Float, ly: Float) {
        rectHitboxFeet.top = ly + height * 0.95f
        rectHitboxFeet.left = lx + width * 0.2f
        rectHitboxFeet.bottom = ly + height * 0.98f
        rectHitboxFeet.right = lx + width * 0.8f
    }

    private fun updateHeadHitbox(lx: Float, ly: Float) {
        rectHitboxHead.top = ly
        rectHitboxHead.left = lx + width * 0.2f
        rectHitboxHead.bottom = ly + height * 0.6f
        rectHitboxHead.right = lx + width * 0.8f
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

    fun checkCollisions(rectHitbox: RectHitbox) : Int {
        var collided = 0

        // Lijeva strana
        if (rectHitboxLeft.intersects(rectHitbox)) {
            worldLocation.x = rectHitbox.right - width * 0.2f
            collided = 1
        }

        // Desna strana
        if (rectHitboxRight.intersects(rectHitbox)) {
            worldLocation.x = rectHitbox.left - width * 0.8f
            collided = 1
        }

        // Stopala
        if (rectHitboxFeet.intersects(rectHitbox)) {
            worldLocation.y = rectHitbox.top - height
            collided = 2
        }

        // Glava
        if (rectHitboxHead.intersects(rectHitbox)) {
            worldLocation.y = rectHitbox.bottom
            collided = 3
        }

        return collided
    }
}






