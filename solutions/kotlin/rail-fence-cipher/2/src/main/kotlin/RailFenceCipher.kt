class RailFenceCipher {

    private val numberOfRails: Int
    private val cycle: List<Int>

    constructor(numberOfRails: Int) {
        require(numberOfRails > 0)
        this.numberOfRails = numberOfRails
        this.cycle = (0 until numberOfRails) + (numberOfRails - 2 downTo 1)
    }

    private fun railOf(position: Int): Int = cycle[position % cycle.size]

    private fun positionsInRailOrder(length: Int): List<Int> =
        (0 until length).sortedBy(::railOf)

    fun getEncryptedData(input: String): String =
        positionsInRailOrder(input.length).joinToString("") { input[it].toString() }

    fun getDecryptedData(input: String): String =
        positionsInRailOrder(input.length)
            .zip(input.toList())
            .sortedBy { it.first }
            .joinToString("") { it.second.toString() }

}
