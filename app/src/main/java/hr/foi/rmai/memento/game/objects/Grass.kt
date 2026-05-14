package hr.foi.rmai.memento.game.objects

class Grass(locationX: Int, locationY: Int, type: Char)
    : GameObject(
    1,
    1,
    1,
    "turf",
    type
) {
    init {
        setWorldLocation(locationX.toFloat(), locationY.toFloat(), 0)
    }

    override fun update(fps: Int, gravity: Float) {

    }
}



