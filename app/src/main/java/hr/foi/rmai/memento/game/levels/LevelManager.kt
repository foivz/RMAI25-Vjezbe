package hr.foi.rmai.memento.game.levels

import android.content.Context
import android.graphics.Bitmap
import hr.foi.rmai.memento.game.objects.GameObject
import hr.foi.rmai.memento.game.objects.Grass
import hr.foi.rmai.memento.game.objects.Player

class LevelManager(
    level: String,
    context: Context,
    pixelsPerMeter: Int,
    playerX: Float,
    playerY: Float
) {
    val gameObjects: ArrayList<GameObject> = ArrayList()
    val bitmaps: Array<Bitmap?> = arrayOfNulls(20)

    private var currentLevel: LevelData? = null
    var playing = false
    private var currentIndex = 0
    var player: Player

    init {
        currentLevel = when (level) {
            "TestLevel" -> TestLevel()
            // još njih
            else -> TestLevel()
        }

        player = Player(0f, 0f)
        playing = true

        // load
    }

    fun getBitmapIndex(blockType: Char) : Int {
        var index = 0

        return when (blockType) {
            '1' -> 1
            'p' -> 2
            else -> 0
        }
    }

    fun getBitmap(blockType: Char) : Bitmap {
        val index = getBitmapIndex(blockType)
        return bitmaps[index]!!
    }

    private fun loadMapData(
        context: Context,
        pixelsPerMeter: Int,
        playerX: Float,
        playerY: Float
    ) {
        val levelHeight = currentLevel!!.tiles.size
        val levelWidth = currentLevel!!.tiles[0].length

        var c: Char
        for (i in 0..< levelWidth) {
            for (j in 0..< levelHeight) {
                c = currentLevel!!.tiles[j][i]

                if (c != '.') {
                    when (c) {
                        '1' -> gameObjects.add(Grass(
                            i,
                            j,
                            c
                        ))
                        'p' -> {
                            player = Player(
                                playerX,
                                playerY
                            )
                            gameObjects.add(player)
                        }
                    }

                    if (bitmaps[getBitmapIndex(c)] == null) {
                        bitmaps[getBitmapIndex(c)] =
                            gameObjects[currentIndex].prepareBitmap(
                                context,
                                pixelsPerMeter
                            )
                    }
                    currentIndex++
                }
            }
        }
    }
}





