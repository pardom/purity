package purity

@Pure
fun factorial(n: Long): Long {
    return (2L..n).fold(1L, Long::times)
}
