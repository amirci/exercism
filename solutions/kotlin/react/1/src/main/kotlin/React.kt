class Reactor<T> {
    interface Subscription {
        fun cancel()
    }

    interface Cell<T> {
        val value: T
    }

    private val computes = mutableListOf<ComputeCell>()

    inner class InputCell(initial: T) : Cell<T> {
        override var value: T = initial
            set(newValue) {
                field = newValue
                stabilize()
            }
    }

    inner class ComputeCell(
        private vararg val inputs: Cell<T>,
        private val compute: (List<T>) -> T,
    ) : Cell<T> {
        override var value: T = compute(inputs.map { it.value })
            private set

        private val callbacks = mutableListOf<(T) -> Unit>()

        init {
            computes += this
        }

        fun addCallback(callback: (T) -> Unit): Subscription {
            callbacks += callback
            return object : Subscription {
                override fun cancel() {
                    callbacks.remove(callback)
                }
            }
        }

        fun update(): Boolean {
            val newValue = compute(inputs.map { it.value })
            if (newValue == value) return false
            value = newValue
            return true
        }

        fun notifyCallbacks() {
            callbacks.toList().forEach { it(value) }
        }
    }

    private fun stabilize() {
        val changed = computes.filter { it.update() }
        changed.forEach { it.notifyCallbacks() }
    }
}
