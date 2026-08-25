fun <T> List<T>.customAppend(list: List<T>): List<T> = list.accumulateInto(copyToMutableList()) { items, item ->
    items.add(item)
}

fun List<Any>.customConcat(): List<Any> = accumulateToList { item -> appendFlattened(item, this) }

fun <T> List<T>.customFilter(predicate: (T) -> Boolean): List<T> = accumulateToList { item ->
    if (predicate(item)) {
        add(item)
    }
}

val List<Any>.customSize: Int
    get() = customFoldLeft(0) { size, _ -> size + 1 }

fun <T, U> List<T>.customMap(transform: (T) -> U): List<U> = accumulateToList { item -> add(transform(item)) }

fun <T, U> List<T>.customFoldLeft(initial: U, f: (U, T) -> U): U {
    var result = initial
    for (item in this) {
        result = f(result, item)
    }
    return result
}

fun <T, U> List<T>.customFoldRight(initial: U, f: (T, U) -> U): U {
    var result = initial
    for (index in lastIndex downTo 0) {
        result = f(this[index], result)
    }
    return result
}

fun <T> List<T>.customReverse(): List<T> = accumulateToList { item -> add(0, item) }

private fun <T, U> List<T>.accumulateToList(addItem: MutableList<U>.(T) -> Unit): List<U> =
    accumulateInto(mutableListOf(), addItem)

private fun <T, U> List<T>.accumulateInto(initial: U, addItem: (U, T) -> Unit): U =
    customFoldLeft(initial) { result, item ->
        addItem(result, item)
        result
    }

private fun <T> List<T>.copyToMutableList(): MutableList<T> {
    val result = mutableListOf<T>()
    for (item in this) {
        result.add(item)
    }
    return result
}

private fun appendFlattened(item: Any, result: MutableList<Any>) {
    if (item is List<*>) {
        for (nestedItem in item) {
            if (nestedItem != null) {
                appendFlattened(nestedItem, result)
            }
        }
    } else {
        result.add(item)
    }
}
