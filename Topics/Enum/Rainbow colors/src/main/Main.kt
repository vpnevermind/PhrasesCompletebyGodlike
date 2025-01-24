fun main() {
    val color = readln()
    val answer = when (color) {
        "red" -> true
        "orange" -> true
        "yellow" -> true
        "green" -> true
        "blue" -> true
        "indigo" -> true
        "violet" -> true
        else -> false
    }
    println(answer)
}