package purity

var x = 0

@Pure
fun namedFunction() {
    var y = 0
    x++
    y++
    bar()
}

val functionLiteral = @Pure { a: Int ->
    var y = 0
    x++
    y++
    bar()
}

fun bar() = baz()
fun baz() = Unit
