package keccak.diophantine

import java.math.BigInteger

class BinaryDiophantineEquation(
    val coefficients: Array<BigInteger>,
    val rhs: BigInteger,
) {
    fun evaluate(solution: BooleanArray): BigInteger {
        var sum = BigInteger.ZERO
        var i = 0
        while (i < coefficients.size) {
            if (solution[i]) {
                sum += coefficients[i]
            }
            i++
        }

        return sum
    }

    fun isDegenerate(): Boolean = coefficients.all { it == BigInteger.ZERO }

    fun copy(): BinaryDiophantineEquation {
        return BinaryDiophantineEquation(
            coefficients = coefficients.clone(),
            rhs = rhs
        )
    }

    fun scaleBy(factor: BigInteger): BinaryDiophantineEquation {
        return BinaryDiophantineEquation(
            coefficients = Array(coefficients.size) { coefficients[it] * factor },
            rhs = rhs * factor
        )
    }

    fun negate(): BinaryDiophantineEquation {
        return BinaryDiophantineEquation(
            coefficients = Array(coefficients.size) { -coefficients[it] },
            rhs = -rhs
        )
    }

    fun normalizeToPositive(): BinaryDiophantineEquation {
        val newCoeffs = coefficients.clone()
        var newRhs = rhs

        var i = 0
        while (i < newCoeffs.size) {
            if (newCoeffs[i] < BigInteger.ZERO) {
                newCoeffs[i] = -newCoeffs[i]
                newRhs += newCoeffs[i]
            }
            i++
        }

        return BinaryDiophantineEquation(newCoeffs, newRhs)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        var i = 0
        while (i < coefficients.size) {
            if (coefficients[i] < BigInteger.ZERO) {
                sb.append('(')
                sb.append(coefficients[i])
                sb.append(')')
            } else {
                sb.append(coefficients[i])
            }
            sb.append("*x")
            sb.append(i)
            if (i != coefficients.size - 1) sb.append(" + ")
            i++
        }
        sb.append(" = ")
        sb.append(rhs)
        return sb.toString()
    }
}
