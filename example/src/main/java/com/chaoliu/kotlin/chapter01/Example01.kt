package com.chaoliu.kotlin.chapter01

import java.awt.Rectangle

//01
fun sum(a: Int, b: Int): Int {
    return a + b
}

//02
fun sum1(a: Int, b: Int) = a + b

//Unit 返回无意义值
fun printSum(a: Int, b: Int): Unit {
    println("sum of $a and $b is ${a + b}")
}

//Unit 可省略
fun printSum1(a: Int, b: Int) {
    println("sum of $a and $b is ${a + b}")
}

// 变量
//只读
val a: Int = 1
val b = 2
//val赋值不能省略


//变量
var x = 5

val PI = 3.14

//+1
/**
 * 多行注释
 * */
fun incrementX() {
    x += 1
}

//字符串模板
var str1 = 1

var s1 = "str1 is $str1"

//模板表达式
var s2 = "${s1.replace("is", "was")}, bus now is $str1"

//条件表达式
fun maxof(a: Int, b: Int): Int {
    if (a > b) {
        return a
    } else {
        return b
    }
}

//if表达式
fun maxif(a: Int, b: Int) = if (a > b) a else b

//空值监测
fun parseInt(string: String): Int? {
    return null
}

//类型监测
fun getStringLength(obj: Any): Int? {

    if (obj is String) {
        return obj.length
    }

    return null
}

//for 循环写法
fun testFor() {
    val items = listOf("apple", "banana", "fruit")

    //1.
    for (item in items) {
        println(item)
    }

    //2  indices index复数
    for (index in items.indices) {
        println("item at $index is ${items[index]}")
    }

    //3.
    var index: Int = 0
    while (index < items.size) {
        println("item at $index is ${items[index]}")
        index++
    }

}

// when表达式
fun testWhen(obj: Any): String =
    when (obj) {
        "1" -> "one"
        is Long -> "Long"
        else -> "unknow"
    }

//使用区间
fun testRange() {

    val x = 3
    val y = 9

    // in 检测
    if (x in 1..y) {
        println(" in the range")
    }

    val list = listOf("a", "b", "c")
    if (2 in 0..list.lastIndex) {
        println(" in the range")
    }


    for (x in 1..5) {
        print(x)
    }

    //步数
    for (x in 1..10 step 2) {
        print(x)
    }

    //倒序
    for (x in 9 downTo 0 step 3) {
        print(x)
    }

}


//集合
fun testSet() {
    val items = listOf("apple", "banana", "fruit")

    for (item in items) {
        println(item)
    }

    when {
        "apple" in items -> println("iphone")
        "fruit" in items -> println("hello")
    }

    //lambda表达式处理

    items.filter { it.startsWith("a") } //过滤非a开头
        .sortedBy { it } //排序
        .map { it.toUpperCase() }//大写
        .forEach { println(it) }//循环打印
}

//创建类
val rect = Rectangle(5, 2)