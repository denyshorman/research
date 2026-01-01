package keccak

class PermutationsIterator<T : Comparable<T>>(val values: Array<T>) {
    private fun findLargestIndexK(): Int {
        for (k in values.size - 2 downTo 0) {
            if (values[k] < values[k + 1]) return k
        }
        return -1
    }

    private fun findLargestIndexL(k: Int): Int {
        for (l in values.size - 1 downTo 0) {
            if (values[k] < values[l]) return l
        }
        return -1
    }

    private fun reverse(start: Int) {
        var left = start
        var right = values.size - 1
        while (left < right) {
            swap(left, right)
            left++
            right--
        }
    }

    private fun swap(i: Int, j: Int) {
        val temp = values[i]
        values[i] = values[j]
        values[j] = temp
    }

    fun hasNext(): Boolean {
        return findLargestIndexK() != -1
    }

    fun next() {
        val k = findLargestIndexK()
        if (k == -1) return

        val l = findLargestIndexL(k)
        swap(k, l)
        reverse(k + 1)
    }

    fun reset() {
        values.sort()
    }

    inline fun iterateAll(callback: () -> Unit) {
        callback()

        while (hasNext()) {
            next()
            callback()
        }

        reset()
    }

    inline fun iterate(callback: () -> Boolean) {
        callback()

        while (hasNext() && callback()) {
            next()
        }

        reset()
    }

    override fun toString(): String {
        return values.joinToString(" ")
    }
}
