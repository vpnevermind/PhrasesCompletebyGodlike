fun main() {
    val color = readln()
        println(Rainbow.valueOf(color).number)
}

enum class Rainbow(val number: Int) {
    red(1),
    orange(2),
    yellow(3),
    green(4),
    blue(5),
    indigo(6),
    violet(7)
}