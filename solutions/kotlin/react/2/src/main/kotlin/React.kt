class Reactor<T> {
    interface Subscription {
        fun cancel()
    }

    interface Cell<T> {
        val value: T
    }

    private val consumers = mutableMapOf<Cell<T>, MutableSet<ComputeCell>>()

    inner class InputCell(initial: T) : Cell<T> {
        override var value: T = initial
            set(newValue) {
                if (field == newValue) return
                field = newValue
                stabilizeFrom(this@InputCell)
            }

    }

    inner class ComputeCell(
        vararg val inputs: Cell<T>,
        private val compute: (List<T>) -> T,
    ) : Cell<T> {
        override var value: T = compute(inputs.map { it.value })
            private set

        private val callbacks = mutableMapOf<Int, (T) -> Unit>()
        private var nextCallbackId = 0

        init {
            inputs.forEach { consumers.getOrPut(it) { mutableSetOf() } += this }
        }

        fun addCallback(callback: (T) -> Unit): Subscription {
            val id = nextCallbackId++
            callbacks[id] = callback
            return object : Subscription {
                override fun cancel() {
                    callbacks.remove(id)
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
            callbacks.values.toList().forEach { it(value) }
        }
    }

    private fun stabilizeFrom(source: Cell<T>) {
        val affected = mutableSetOf<ComputeCell>()
        val visited = mutableSetOf<ComputeCell>()

        fun visit(cell: ComputeCell) {
            if (!visited.add(cell)) return
            consumersOf(cell).forEach(::visit)
            affected += cell
        }

        consumersOf(source).forEach(::visit)
        val depths = mutableMapOf<ComputeCell, Int>()
        val ordered = affected.sortedBy { depth(it, depths) }
        val changed = ordered.filter { it.update() }
        changed.forEach { it.notifyCallbacks() }
    }

    private fun depth(cell: ComputeCell, depths: MutableMap<ComputeCell, Int>): Int =
        depths.getOrPut(cell) {
            cell.inputs
                .filterIsInstance<ComputeCell>()
                .maxOfOrNull { depth(it, depths) }
                ?.plus(1)
                ?: 0
        }

    private fun consumersOf(cell: Cell<T>): Set<ComputeCell> = consumers[cell] ?: emptySet()
}
