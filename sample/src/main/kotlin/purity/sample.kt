package purity

@Immutable
data class Foo(val int: Int)

var x = 0

@Pure
fun increment() {
    var y = 0
    x++
    y++
}

@Pure
fun foo(a: Foo) = bar()

@Pure
fun bar() {
    baz()
}

fun baz() {
    foo(Foo(1))
}
