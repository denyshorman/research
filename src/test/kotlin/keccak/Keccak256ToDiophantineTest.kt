package keccak

import io.kotest.core.spec.style.FunSpec
import keccak.util.toDiophantineEquation
import org.web3j.crypto.Hash
import org.web3j.utils.Numeric

class Keccak256ToDiophantineTest : FunSpec({
    test("convert keccak256('') to single Diophantine equation") {
        val message = "".toByteArray()

        println("Generating Keccak256 equation system for empty string...")
        val generator = Keccak256EqSystemGenerator.INSTANCE
        val result = generator.hash(message)

        val andEqSystem = result.equationSystem

        println("Keccak256 AND equation system:")
        println("  Equations: ${andEqSystem.rows}")
        println("  Variables: ${andEqSystem.cols}")
        println("  Expected hash: ${Numeric.toHexString(result.hash)}")
        println("  Actual hash:   ${Numeric.toHexString(Hash.sha3(message))}")
        println()

        println("Converting to single sparse Diophantine equation...")
        val startTime = System.currentTimeMillis()
        val diophantineEq = andEqSystem.toDiophantineEquation()
        val endTime = System.currentTimeMillis()

        println("Conversion completed in ${endTime - startTime}ms")
        println()
        println("Single Diophantine equation:")
        println("  Total variables: ${diophantineEq.coefficients.size}")
        println("  Original variables: ${andEqSystem.cols}")
        println("  Auxiliary variables: ${diophantineEq.coefficients.size.toLong() - andEqSystem.cols}")
        println("  Right-hand side: ${diophantineEq.rhs}")
        println("  RHS bit length: ${diophantineEq.rhs.bitLength()}")
        println()

        val maxCoef = diophantineEq.coefficients.maxOf { it.abs() }
        val minCoef = diophantineEq.coefficients.minOf { it.abs() }
        val avgCoefBits = diophantineEq.coefficients.map { it.bitLength() }.average()
        println("Coefficient statistics:")
        println("  Max coefficient bits: ${maxCoef.bitLength()}")
        println("  Min coefficient bits: ${minCoef.bitLength()}")
        println("  Avg coefficient bits: ${"%.1f".format(avgCoefBits)}")
    }
})
