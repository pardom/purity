package deps

import deps.dsl.Dependency
import deps.dsl.Dependency.Group

object Dokka : Group("org.jetbrains.dokka", "0.9.18") {
    object Gradle : Group(this) {
        val Plugin = Dependency(this, "dokka-gradle-plugin")
    }
}

object Kotlin : Group("org.jetbrains.kotlin", "1.3.30") {
    private const val BASE = "kotlin"

    object Compiler : Group(this) {
        private const val BASE = "${Kotlin.BASE}-compiler"
        val Embeddable = Dependency(this, "$BASE-embeddable")
    }

    object Gradle : Group(this) {

        object Plugin : Group(this) {
            private const val BASE = "kotlin-gradle-plugin"
            override val default = Dependency(this, BASE)
            val Api = Dependency(this, "$BASE-api")
        }
    }

    object StdLib : Group(this) {
        private const val BASE = "kotlin-stdlib"
        override val default = Dependency(this, BASE)
    }

    object Test : Group(this) {
        private const val BASE = "kotlin-test"
        val AnnotationsCommon = Dependency(this, "$BASE-annotations-common")
        val Common = Dependency(this, "$BASE-common")
        val JS = Dependency(this, "$BASE-js")
        val JVM = Dependency(this, BASE)
        val JUnit5 = Dependency(this, "$BASE-junit5")
        override val default = Dependency(this, BASE)
    }
}

