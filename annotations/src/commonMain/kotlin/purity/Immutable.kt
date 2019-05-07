package purity

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPE
)
@Retention(AnnotationRetention.SOURCE)
annotation class Immutable
