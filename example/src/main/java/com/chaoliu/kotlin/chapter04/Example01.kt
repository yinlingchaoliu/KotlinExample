package com.chaoliu.kotlin.chapter04

import java.util.concurrent.locks.Lock

//lambda

fun double(x: Int): Int {
    return 2 * x
}

//单表达式函数
fun double1(x: Int): Int = x * 2

fun double2(x: Int) = x * 2

//默认参数
fun powerOf(number: Int = 0, expoent: Int) {

}

fun printHello(name: String): Unit {

}

fun printHello1(name: String) {

}

//可变参数
fun <T> asList(vararg args: T): List<T> {
    val result = ArrayList<T>()
    for (t in args) {
        result.add(t)
    }
    return result
}

//中缀表达式
//成员函数 或 扩展函数 参数必须1个

infix fun Int.plus(x: Int): Int {
    return this + x
}

class Computer {
    private var value: Int = 0

    infix fun plus(x: Int): Int {
        return value + x
    }

    infix fun mult(x: Int): Int {
        return value * x
    }

    fun result() {
        this plus 1
        plus(2)
        this mult 4
        mult(4)
    }

}

//tailrec 递归函数修饰

//高阶函数  函数也可以作为参数传递
// (acc:R, next:T) ->R 函数 返回值为R

fun <T, R> Collection<T>.fold(initial: R, combine: (acc: R, next: T) -> R): R {
    var acc: R = initial
    for (element: T in this) {
        acc = combine(acc, element)
    }
    return acc
}


//函数类型 不能使用泛型
typealias combine = (Int, Int) -> Int

typealias susp = suspend (Int) -> Int

//函数类型实例化
class IntTransformer : (Int) -> Int {
    override fun invoke(x: Int): Int = TODO()
}

val intFun: (Int) -> Int = IntTransformer()

// 函数也可以作为参数传递
fun testFun() {
    //lambda 表达式
    val stringPlus: (String, String) -> String = { a, b -> a + b }

    //扩展函数
    val intPlus :Int.(Int)->Int = Int::plus
    // 接受函数字面值
    val intPlus1 :Int.(Int)->Int = { x->this + x }

    println(stringPlus.invoke("hello","world"))
    println(stringPlus("hello","world"))

    println(intPlus.invoke(1,2))
    println(intPlus(1,2))
    println(2.intPlus(3))
}

inline fun <reified T> membersOf()  = T::class.members


// 获得T.class
inline fun <reified T> classOf()  = T::class.java

//获得T
inline fun <reified T> instanceOf()  = T::class.java.newInstance()


fun main() {

    1.plus(1)
    //中缀表达式
    1 plus 2

    var computer = Computer()
    computer.plus(1)

    var clazz = classOf<String>()
    var member = membersOf<String>()
    var str = instanceOf<String>()

}