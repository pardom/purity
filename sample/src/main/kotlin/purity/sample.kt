package purity

@Pure
fun factorial(n: Long): Long {
    return (2L..n).fold(1L, Long::times)
}

@Pure
fun inlineFactorial(n: Long) = (2L..n).fold(1L, Long::times)

val lambdaFactorial = @Pure { n: Long -> (2L..n).fold(1L, Long::times) }
