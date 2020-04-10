package com.chaoliu.kotlin.chapter05

//集合
fun testMap() {
    val numbers = mutableListOf("one", "two", "three", "four")
    numbers.add("five")
}

//Collection
fun printAll(strings: Collection<String>) {
    for (s in strings) {
        println("$s")
    }
}

fun testFor() {

    val numbers = listOf("one", "two", "three", "four")

    //迭代器
    val iterator = numbers.iterator()
    while (iterator.hasNext()) {
        println(iterator.next())
    }

    //in
    for (item in numbers) {
        println(item)
    }

    // foreach
    numbers.forEach {
        println(it)
    }

    // list迭代 双向迭代
    var listIterator = numbers.listIterator()
    while (listIterator.hasPrevious()) {
        println(listIterator.previous())
    }
}

//可变迭代
fun testMutMap() {
    val numbers = mutableListOf("one", "two", "three", "four")
    val mutableIterator = numbers.listIterator()
    mutableIterator.next()
    mutableIterator.remove()
    mutableIterator.add("add")
    mutableIterator.set("this")
}

//序列
//map 映射
// zip 合并数据
//关联

//flatMap
//多个数组合并
fun testFlatMap() {
    val containers = listOf(
        listOf("one", "two", "three"),
        listOf("four", "five", "six"),
        listOf("seven", "eight")
    )
    val list = containers.flatMap { it.toList() }

}

//filter 过滤

//划分 不匹配单放一个位置
fun testPart() {
    val numbers = listOf("one", "two", "three", "four")
    val (match, rest) = numbers.partition { it.length > 3 }
    println(match)
    println(rest)
}

// plus(+) minus(-)
//加减数组

//groupby

//取集合一部分
fun testTake() {
    //取集合一部分
    val numbers = listOf("one", "two", "three", "four", "five", "six")
    println(numbers.slice(1..3)) //取一部分
    println(numbers.slice(0..4 step 2))
    println(numbers.take(3)) //从头取
    println(numbers.drop(2)) //从尾取
    println(numbers.chunked(3)) //分块数据

    //取单个元素
    println(numbers.elementAt(3))
    println(numbers.elementAtOrNull(3))
    println(numbers.first { it.length > 3 })
    println(numbers.last { it.startsWith("f") })
    println(numbers.firstOrNull { it.length > 6 })
    println(numbers.find { it.length > 3 })
    println(numbers.findLast { it.length > 3 })
    println(numbers.random())

    //监测
    println(numbers.contains("four"))
    println("zero" in numbers)
    println(numbers.containsAll(listOf("four", "two")))
    println(numbers.isEmpty())
    println(numbers.isNotEmpty())
}

//排序
fun testSort() {
    val numbers = listOf("one", "two", "three", "four", "five", "six")
    numbers.sortedWith(compareBy { it.length })
    numbers.reversed()
    numbers.shuffled()
}

fun testSet() {
    val numbers = listOf(6, 42, 10, 4)
    println("Count: ${numbers.count()}")
    println("Max: ${numbers.max()}")
    println("Min: ${numbers.min()}")
    println("Average: ${numbers.average()}")
    println("Sum: ${numbers.sum()}")
}

//List Set Map
fun main() {

    val stringList = listOf("one", "two", "one")
    printAll(stringList)

    val articles = setOf("a", "A", "an", "An", "the", "The")
    val numbersMap = mapOf("key1" to 1, "key2" to 2, "key3" to 3, "key4" to 1)

    val numbers = mutableMapOf("one" to 1, "two" to 2)
    numbers.put("three", 3)
    numbers["one"] = 11

    val empty = emptyList<String>()

    //初始化
    val doubled = List(3, { it * 2 })


    val datas = listOf("one", "two", "three", "four")
    val dataSeq = datas.asSequence()

    val numbersSequence = sequenceOf("four", "three", "two", "one")

    numbersSequence.take(2).toList()
}