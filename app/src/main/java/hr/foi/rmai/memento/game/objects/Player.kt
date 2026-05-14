package hr.foi.rmai.memento.game.objects

class Player(locationX: Float, locationY: Float)
    : GameObject(
    1,
    2,
    5,
    "player",
    'p'
) {
    init {
        setWorldLocation(locationX, locationY, 0)
    }

    override fun update(fps: Int, gravity: Float) {

    }
}



