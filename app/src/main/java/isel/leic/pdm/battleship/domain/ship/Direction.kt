package isel.leic.pdm.battleship.domain.ship

/**
 * Enum Class with the direction of a Ship.
 */
enum class Direction(val value: Char) {
    HORIZONTAL('H'), VERTICAL('V');

    /**
     * The other direction.
     */
    val other: Direction
        get() = if (this == HORIZONTAL) VERTICAL else HORIZONTAL
}
