package hr.foi.rmai.memento.game

class RectHitbox {
    var top = 0f
    var left = 0f
    var bottom = 0f
    var right = 0f

    fun intersects(rectHitbox: RectHitbox): Boolean {
        var hit = false

        // Provjera po X osi
        if (right > rectHitbox.left && left < rectHitbox.right) {
            // Provjera po Y osi
            if (top < rectHitbox.bottom && bottom > rectHitbox.top) {
                hit = true
            }
        }

        return hit
    }
}






