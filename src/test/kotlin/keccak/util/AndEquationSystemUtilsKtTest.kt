package keccak.util

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import keccak.diophantine.solveBinaryDiophantineEquation

class AndEquationSystemUtilsKtTest : FunSpec({
    context("rotate") {
        test("1") {
            val solution = BitSet("110")

            val system = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + x1 + x2 + 1)*(x0 + 1) = 0"
            )

            system.rotate(solution, left = false, right = false)

            val expectedSystem = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x1 + x2 + 1)*(x0 + 1) = 0"
            )

            system.shouldBe(expectedSystem)
        }

        test("2") {
            val solution = BitSet("110")

            val system = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + x1 + x2 + 1)*(x0 + 1) = 0"
            )

            system.rotate(solution, left = true, right = false)

            val expectedSystem = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + x1 + x2 + 1)*(x0 + 1) = 0"
            )

            system.shouldBe(expectedSystem)
        }

        test("3") {
            val solution = BitSet("110")

            val system = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + x1 + x2 + 1)*(x0 + 1) = 0"
            )

            system.rotate(solution, left = false, right = true)

            val expectedSystem = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + 1)*(x0 + x1 + x2 + 1) = 0"
            )

            system.shouldBe(expectedSystem)
        }

        test("4") {
            val solution = BitSet("100")

            val system = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + x1 + x2 + 1)*(x0 + 1) = 0"
            )

            system.rotate(solution, left = false, right = false)

            val expectedSystem = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + x1 + x2 + 1)*(x0 + 1) = 0"
            )

            system.shouldBe(expectedSystem)
        }

        test("5") {
            val solution = BitSet("100")

            val system = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + x1 + x2 + 1)*(x0 + 1) = 0"
            )

            system.rotate(solution, left = false, right = true)

            val expectedSystem = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + x1 + x2 + 1)*(x1 + x2 + 1) = 0"
            )

            system.shouldBe(expectedSystem)
        }

        test("6") {
            val solution = BitSet("100")

            val system = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + x1 + x2 + 1)*(x0 + 1) = 0"
            )

            system.rotate(solution, left = true, right = false)

            val expectedSystem = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x1 + x2 + 1)*(x0 + 1) = 0"
            )

            system.shouldBe(expectedSystem)
        }

        test("7") {
            val solution = BitSet("100")

            val system = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + x1 + x2)*(x0 + 1) = 0"
            )

            system.rotate(solution, left = false, right = true)

            val expectedSystem = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + 1)*(x0 + x1 + x2) = 0"
            )

            system.shouldBe(expectedSystem)
        }

        test("8") {
            val solution = BitSet("100")

            val system = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + x1 + x2)*(x0 + 1) = 0"
            )

            system.rotate(solution, left = false, right = false)

            val expectedSystem = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x1 + x2)*(x0 + 1) = 0"
            )

            system.shouldBe(expectedSystem)
        }
    }

    context("toDiophantineEquation") {
        test("simple single equation - 0*0 = 0") {
            val system = AndEquationSystem(
                rows = 1, cols = 2, humanReadable = true,
                "(x0)*(x1) = 0"
            )

            val diophantineEquation = system.toDiophantineEquation()
            val solutions = solveBinaryDiophantineEquation(diophantineEquation)

            println("Solutions: ${solutions.toBitString()}")
            solutions.size.shouldBe(3) // (0,0), (0,1), (1,0) - all satisfy a*b=0
        }

        test("simple single equation - 1*1 = 1") {
            val system = AndEquationSystem(
                rows = 1, cols = 2, humanReadable = true,
                "(x0 + 1)*(x1 + 1) = 1"
            )

            val diophantineEquation = system.toDiophantineEquation()
            val solutions = solveBinaryDiophantineEquation(diophantineEquation)

            println("Solutions: ${solutions.toBitString()}")
            solutions.size.shouldBe(1) // Only (0,0) satisfies 1*1=1
        }

        test("single equation with 3 variables") {
            val system = AndEquationSystem(
                rows = 1, cols = 3, humanReadable = true,
                "(x0 + x1)*(x1 + x2) = x0 + x2"
            )

            val diophantineEquation = system.toDiophantineEquation()
            val solutions = solveBinaryDiophantineEquation(diophantineEquation)

            println("Solutions: ${solutions.toBitString()}")

            solutions.forEach { solution ->
                val x0 = solution[0]
                val x1 = solution[1]
                val x2 = solution[2]
                val left = (x0 xor x1) and (x1 xor x2)
                val right = x0 xor x2
                left.shouldBe(right)
            }
        }

        test("two equations system") {
            val system = AndEquationSystem(
                rows = 2, cols = 3, humanReadable = true,
                "(x0)*(x1) = x2",
                "(x0 + x1)*(x2 + 1) = x0 + x1"
            )

            val diophantineEquation = system.toDiophantineEquation()
            val solutions = solveBinaryDiophantineEquation(diophantineEquation)

            println("Solutions: ${solutions.toBitString()}")

            solutions.forEach { solution ->
                val x0 = solution[0]
                val x1 = solution[1]
                val x2 = solution[2]
                
                // First equation: x0*x1 = x2
                (x0 and x1).shouldBe(x2)
                
                // Second equation: (x0+x1)*(x2+1) = x0+x1
                ((x0 xor x1) and (x2 xor true)).shouldBe(x0 xor x1)
            }
        }

        test("complex system") {
            val system = AndEquationSystem(
                rows = 6, cols = 5, humanReadable = true,
                "(x0 + x1 + 1)*(x0 + x1 + x3 + x4 + 1) = x0 + x1 + x4",
                "(x0)*(x1 + x2 + 1) = x1 + x4",
                "(x3 + x4 + 1)*(x0 + x2) = x0 + x3 + x4",
                "(x0)*(x0 + x1 + x2 + x3 + x4 + 1) = x1",
                "(x1 + x3 + x4 + 1)*(x3) = x1 + x3 + 1",
                "(x1)*(x2 + 1) = x1 + x3 + x4",
            )

            val diophantineEquation = system.toDiophantineEquation().negate()
            println(diophantineEquation)
        }

        xtest("complex system - verify unique solution") {
            val system = AndEquationSystem(
                rows = 6, cols = 5, humanReadable = true,
                "(x0 + x1 + 1)*(x0 + x1 + x3 + x4 + 1) = x0 + x1 + x4",
                "(x0)*(x1 + x2 + 1) = x1 + x4",
                "(x3 + x4 + 1)*(x0 + x2) = x0 + x3 + x4",
                "(x0)*(x0 + x1 + x2 + x3 + x4 + 1) = x1",
                "(x1 + x3 + x4 + 1)*(x3) = x1 + x3 + 1",
                "(x1)*(x2 + 1) = x1 + x3 + x4",
            )

            val diophantineEquation = system.toDiophantineEquation()
            val solutions = solveBinaryDiophantineEquation(diophantineEquation)

            println(solutions.toBitString())
            solutions.size.shouldBe(1)
        }

        test("system with multiple solutions") {
            val system = AndEquationSystem(
                rows = 1, cols = 4, humanReadable = true,
                "(x0 + x1)*(x2 + x3) = 0"
            )

            val diophantineEquation = system.toDiophantineEquation()
            val solutions = solveBinaryDiophantineEquation(diophantineEquation)
            val expectedSolutionCounts = system.countSolutions()

            solutions.size.shouldBe(expectedSolutionCounts)
        }

        test("verify auxiliary variables work correctly") {
            val system = AndEquationSystem(
                rows = 3, cols = 4, humanReadable = true,
                "(x0 + x1 + x2)*(x1 + x2 + x3) = x0 + x3",
                "(x0 + x1)*(x2 + x3 + 1) = x1 + x2",
                "(x0)*(x1 + x2) = x3"
            )

            val diophantineEquation = system.toDiophantineEquation()
            println("Variables: ${diophantineEquation.coefficients.size}")
            println("Original vars: 4, Auxiliary vars: ${diophantineEquation.coefficients.size - 4}")
            
            val solutions = solveBinaryDiophantineEquation(diophantineEquation)
            println("Solutions: ${solutions.toBitString()}")
            
            // Verify each solution satisfies the original AND equations
            solutions.forEach { solution ->
                val x0 = solution[0]
                val x1 = solution[1]
                val x2 = solution[2]
                val x3 = solution[3]
                
                // Equation 1: (x0+x1+x2)*(x1+x2+x3) = x0+x3
                val eq1Left = (x0 xor x1 xor x2) and (x1 xor x2 xor x3)
                val eq1Right = x0 xor x3
                eq1Left.shouldBe(eq1Right)
                
                // Equation 2: (x0+x1)*(x2+x3+1) = x1+x2
                val eq2Left = (x0 xor x1) and (x2 xor x3 xor true)
                val eq2Right = x1 xor x2
                eq2Left.shouldBe(eq2Right)
                
                // Equation 3: x0*(x1+x2) = x3
                val eq3Left = x0 and (x1 xor x2)
                val eq3Right = x3
                eq3Left.shouldBe(eq3Right)
            }
        }

        test("constant terms handling") {
            val system = AndEquationSystem(
                rows = 2, cols = 2, humanReadable = true,
                "(x0 + 1)*(x1) = 1",
                "(x0)*(x1 + 1) = 0"
            )

            val diophantineEquation = system.toDiophantineEquation()
            val solutions = solveBinaryDiophantineEquation(diophantineEquation)

            println("Solutions: ${solutions.toBitString()}")
            solutions.forEach { solution ->
                val x0 = solution[0]
                val x1 = solution[1]
                
                // Equation 1: (x0+1)*x1 = 1
                ((x0 xor true) and x1).shouldBe(true)
                
                // Equation 2: x0*(x1+1) = 0
                (x0 and (x1 xor true)).shouldBe(false)
            }
        }
    }
})
