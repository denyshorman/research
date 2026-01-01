package keccak.arithmetic.any

import io.kotest.core.spec.style.FunSpec
import keccak.CombWithoutRepetitionIterator
import keccak.math.arithmetic.*

class EquationSystemTest : FunSpec({
    val x0 = BooleanVariable("x0", BooleanVariable.Type.ANY)
    val x1 = BooleanVariable("x1", BooleanVariable.Type.ANY)
    val x2 = BooleanVariable("x2", BooleanVariable.Type.ANY)
    val x3 = BooleanVariable("x3", BooleanVariable.Type.ANY)
    val x4 = BooleanVariable("x4", BooleanVariable.Type.ANY)
    val x5 = BooleanVariable("x5", BooleanVariable.Type.ANY)
    val x6 = BooleanVariable("x6", BooleanVariable.Type.ANY)
    val x7 = BooleanVariable("x7", BooleanVariable.Type.ANY)
    val x8 = BooleanVariable("x8", BooleanVariable.Type.ANY)
    val x9 = BooleanVariable("x9", BooleanVariable.Type.ANY)
    val x10 = BooleanVariable("x10", BooleanVariable.Type.ANY)
    val x11 = BooleanVariable("x11", BooleanVariable.Type.ANY)
    val x12 = BooleanVariable("x12", BooleanVariable.Type.ANY)
    val x13 = BooleanVariable("x13", BooleanVariable.Type.ANY)
    val x14 = BooleanVariable("x14", BooleanVariable.Type.ANY)
    val x15 = BooleanVariable("x15", BooleanVariable.Type.ANY)
    val x16 = BooleanVariable("x16", BooleanVariable.Type.ANY)

    test("1") {
        val initialSystem = listOf<ArithmeticNode>(
            x0 - 2*x1 + x4 - 2*x5 - x6 + 2*x7 + x14 - 2*x15 - 2,
            x1 - x2 - 2*x3 - x4 + 2*x5 + x10 - 2*x11 - 2,
            -x2 + x3 + 2*x4 + x8 - 2*x9 - x12 + 2*x13,
            x0 - x1 - 2*x2 - x4 + 4*x6,
            x0 - 2*x1 + x3 - 2*x4 - x6 + 4*x8,
            x1 - x2 - 2*x3 - x8 + 4*x10,
            x0 - 2*x1 + x2 - 2*x3 - x10 + 4*x12,
            x1 - 2*x2 + x3 - 2*x4 - x12 + 4*x14,
            x0 - x1 - 2*x2 + x3 - 2*x4 - x14 + 4*x16,
        )

        val vars = (0..16).map { BooleanVariable("x$it", BooleanVariable.Type.ANY) }

        val system = initialSystem.asSequence()
            .flatMap { eq -> sequenceOf(eq.expand()) + vars.asSequence().map { v -> (v * eq).expand() } }
            .toList()
            .toTypedArray()

        val expVars = run {
            val iter = CombWithoutRepetitionIterator(vars.size, 2)
            val expVars = mutableListOf<ArithmeticNode>()
            expVars.addAll(vars)
            iter.iterateAll {
                expVars.add(vars[iter.combination[0]] * vars[iter.combination[1]])
            }
            expVars.toTypedArray()
        }

        solveLinearSystem(system, expVars)

        for (eq in system) {
            println("$eq = 0")
        }
    }
})
