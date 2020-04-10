package com.chaoliu.kotlin.chapter01

import java.io.File
import java.lang.ArithmeticException
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

//习惯用法

//创建DTOs
data class Customer(val name: String, val email: String)
//提供 equal  hashCode toString copy

//函数默认参数
fun foo(a: Int = 0, b: String = "") {

}

//过滤
fun testFilter() {

    var list = 0..20 step 3

    //两种写法
    list.filter { x -> x > 0 }

    list.filter { it > 0 }

    if (10 in list) {
        println("in the range")
    }

    if (20 !in list) {
        println("out the range")
    }

}

fun testMap() {

    var map = mutableMapOf("a" to 1, "b" to 2, "c" to 3)
    for ((k, v) in map) {
        println("$k -> $v")
    }

    //访问map
    println(map["a"])
    map["a"] = 3

}

//扩展函数
fun String.spaceToX() = "Mask"

fun testSpace() {
    "Elon".spaceToX()
}

//单例
object Test01 {

    val name = "Name"

}

//null 处理

fun testNull() {

    //if not null
    val files = File("Test").listFiles()
    print(files?.size)

    //if not null and else  三目运算符
    println(files.size ?: "empty")

    // if null
    files ?: throw Exception("empty")

    // 取出第一个元素
    val fileName = files.firstOrNull()?.name ?: "unKnown"

    //if not null
    files?.let {
        files.forEach {
            println(it.name)
        }
    }

    //非空默认值
    val name = files.firstOrNull()?.name ?: "defaultValue"

}

//when 表达式
fun transform(color: String): Int {
    return when (color) {
        "Red" -> 0
        "Green" -> 1
        "Blue" -> 2
        else -> throw IllegalArgumentException("Invaild color param value")
    }
}

// try表达式
fun testTry() {

    val result = try {
        1 / 0
    } catch (e: ArithmeticException) {
        println("div by zero")
    }

    result
}

// if 表达式

fun testIf(param: Int) {
    val result = if (param == 1) {
        "one"
    } else if (param == 2) {
        "two"
    } else {
        "other"
    }
}

//单表达式
fun theAnswer() = 42

//等价于
fun theAnswer1(): Int {
    return 42
}

//建造者with
class Turtle {
    var name: String? = null
    fun penDown() {}
    fun penUp() {}
    fun turn(degree: Double) {}
    fun forward(pixeks: Double) {}
}

// with 建造者
// apply 配置对象属性
fun testWith() {
    //apply 配置对象属性
    var myTurtle = Turtle().apply { name = "张三丰" }
    //建造者
    with(myTurtle) {
        penDown()
        for (i in 1..4) {
            forward(1.0)
            turn(2.0)
        }
        penUp()
    }
}

// get res
//val stream = Files.newInputStream(Paths.get("/some/file.txt"))

//泛型 可以直接获得泛型的T类
inline fun <reified T : Number> sum(a: T, b: T): Number? {
    return when (T::class) {
        Float::class -> a as Float + b as Float
        Double::class -> a as Double + b as Double
        Int::class -> a as Int + b as Int
        else -> null
    }
}

fun testBoolean(){
    var b: Boolean ? = null

    if (b == true){
        // do anything
    }else{
        // false or null
    }

}

fun testSwap(){
    var a = 1
    var b =2
    a=b.alsotest {
        b=a
    }
    println("$a and $b")
}

fun main(){
    testSwap()
}

//扩展函数 通用式写法
@OptIn(ExperimentalContracts::class)
@SinceKotlin("1.1")
public inline fun <T> T.alsotest(block: (T) -> Unit): T {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    block(this)
    return this
}

public inline fun TEST(): Nothing = throw NotImplementedError()
