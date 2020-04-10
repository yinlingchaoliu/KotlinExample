package com.chaoliu.kotlin.chapter02

//Byte 8
//Short 16
//Int 32
//Long 64
//Float 32
//Double 64

//数据类型显式转换
val byte:Byte = 8
val short:Short = 16
val int:Int = 32
val long:Long = 64L
val float:Float = 32f
val double:Double = 64.00

//字面常量
val oneMillion = 1_000_000
val creditCardNumber = 1234_5678_9012_3456L
val hexBytes = 0xFF_EC_DE_5E
val bytes = 0b11010010_01101001_10010100_10010010

//无符号 u后缀

//— toByte(): Byte
//— toShort(): Short
//— toInt(): Int
//— toLong(): Long
//— toFloat(): Float
//— toDouble(): Double — toChar(): Char

//字符
fun decimalDigitValue(c:Char):Int{
    if (c !in '0'..'9') throw IllegalArgumentException("out of range")
    return c.toInt() - '0'.toInt()
}

//数组
//Array<T>
fun testArray(){

    val asc = Array(5){i -> (i*i).toString()}
    asc.forEach { println(it) }

    val x: IntArray = intArrayOf(1,2,3)
    x[0] = x[1]+x[2]

    val arr = IntArray(5)

    val arr1 = IntArray(5){42}

    //it 指index
    val arr2 = IntArray(5){it}

}

//采用 label 控制跳出点
fun testLoop(){

   loop@ for (i in 1..100){

        for (j in 1..100){
            if (j == 30) break@loop
        }

    }

    listOf(1,2,3,4,5).forEach {
        if (it == 2) return@forEach
        println(it)
    }

}