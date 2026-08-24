import kotlin.random.Random

class Robot {

    var name = uniqueName()
        private set

    fun reset() {
        name = uniqueName()
    }

    companion object {
        private val usedNames = mutableSetOf<String>()

        @Synchronized
        private fun uniqueName(): String {
            val name = generateSequence { robotName() }
                .first { it !in usedNames }

            usedNames.add(name)
            return name
        }

        private fun robotName() = "${newPrefix()}${newId()}"

        private fun newPrefix() = generateSequence {
            Random.nextInt('A'.code, 'Z'.code + 1).toChar()
        }
            .take(2)
            .joinToString("")

        private fun newId() = Random.nextInt(1000).toString().padStart(3, '0')
    }
}
