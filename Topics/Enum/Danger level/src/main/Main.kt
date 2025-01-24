enum class DangerLevel {
    LOW,
    MEDIUM,
    HIGH;

    fun getLevel(): Int {
        return this.ordinal + 1
    }
}
fun main() {
    val high = DangerLevel.HIGH
    val medium = DangerLevel.MEDIUM
    println(medium.getLevel())
}