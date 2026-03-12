package keccak.diophantine

import keccak.CombinationIteratorSimple
import java.math.BigInteger
import java.util.*

fun solveBinaryDiophantineEquation(
    equation: BinaryDiophantineEquation,
): List<BooleanArray> {
    val coefficients = equation.coefficients
    val rhs = equation.rhs

    val canUseLongs = coefficients.all { it.bitLength() < 64 } && rhs.bitLength() < 64

    if (canUseLongs) {
        val longCoefficients = LongArray(coefficients.size) { i -> coefficients[i].toLong() }
        val longRhs = rhs.toLong()
        return solveBinaryDiophantineEquation(longCoefficients, longRhs)
    }

    val iter = CombinationIteratorSimple(coefficients.size)
    val solutions = LinkedList<BooleanArray>()

    iter.iterateAll {
        var sum = BigInteger.ZERO

        var i = 0
        while (i < equation.coefficients.size) {
            if (iter.combination[i]) {
                sum += equation.coefficients[i]
            }
            i++
        }

        if (sum == equation.rhs) {
            solutions.add(iter.combination.clone())
        }
    }

    return solutions
}

private fun solveBinaryDiophantineEquation(
    coefficients: LongArray,
    rhs: Long,
): List<BooleanArray> {
    val iter = CombinationIteratorSimple(coefficients.size)
    val solutions = LinkedList<BooleanArray>()

    iter.iterateAll {
        var sum = 0L

        var i = 0
        while (i < coefficients.size) {
            if (iter.combination[i]) {
                sum += coefficients[i]
            }
            i++
        }

        if (sum == rhs) {
            solutions.add(iter.combination.clone())
        }
    }

    return solutions
}
