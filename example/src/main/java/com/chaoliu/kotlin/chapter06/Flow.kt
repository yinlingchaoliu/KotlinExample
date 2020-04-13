package com.chaoliu.kotlin.chapter06

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

//流计算 Flow

//正常函数
fun foo(): List<Int> = listOf(1, 2, 3)

fun fooseq(): Sequence<Int> = sequence { // 序列构建器
    for (i in 1..3) {
        Thread.sleep(100) // 假装我们正在计算
        yield(i) // 产生下一个值
    }
}

suspend fun fooDelay(): List<Int> {
    delay(1000) // 假装我们在这里做了一些异步的事情
    return listOf(1, 2, 3)
}

fun fooFlow(): Flow<Int> = flow { //流构建器
    println("Flow started")
    for (i in 1..3) {
        delay(100) // 假装我们在这里做了一些有用的事情
        emit(i) // 发送下一个值
    }
}

fun testFlow() {
    runBlocking {
        launch {
            for (k in 1..3) {
                println("I'm not blocked $k")
                delay(100)
            }
        }

        // 收集这个流
        fooFlow().collect { value -> println(value) }
    }
}


//流取消
fun testFlowCancel() {
    runBlocking {
        withTimeoutOrNull(250) { // 在 250 毫秒后超时
            fooFlow().collect { value -> println(value) }
        }
        println("Done")
    }
}

//
fun testAsFlow() {
    runBlocking {
        (1..3).asFlow().collect { value -> println(value) }
    }
}

//应用挂起函数
suspend fun performRequest(request: Int): String {
    delay(1000) // 模仿⻓时间运行的异步工作
    return "response $request"
}

fun testSupFlow() {
    runBlocking {
        (1..3).asFlow() // 一个请求流
            .map { request -> performRequest(request) }
            .collect { response -> println(response) }
    }
}

fun testTransform() {
    runBlocking {
        (1..3).asFlow() // 一个请求流
            .transform { request ->
                emit("Making request $request")
                emit(performRequest(request))
            }
            .collect { response -> println(response) }
    }
}

fun numbers(): Flow<Int> = flow {
    try {
        emit(1)
        emit(2)
        println("This line will not execute")
        emit(3)
    } finally {
        println("Finally in numbers")
    }
}

//take截取
fun testTake() {
    runBlocking {
        numbers().take(2).collect { value -> println(value) }
    }
}

//计算公式 reduce
fun testReduce() {
    runBlocking {
        val sum = (1..5).asFlow()
            .map { it * it } // 数字 1 至 5 的平方
            .reduce { a, b -> a + b } // 求和(末端操作符)
        println(sum)
    }
}

fun fooWasteTime(): Flow<Int> = flow {
    // 在流构建器中更改消耗 CPU 代码的上下文的错误方式
    withContext(Dispatchers.Default) {
        for (i in 1..3) {
            Thread.sleep(100) // 假装我们以消耗 CPU 的方式进行计算
            emit(i) // 发射下一个值
        }
    }
}

//error
fun testWasteTime() {
    runBlocking {
        fooWasteTime().collect { value -> println(value) }
    }
}

//更改流发射的上下文 flowOn
fun testFlowWasteTime() {
    runBlocking {
        fooWasteTime().flowOn(Dispatchers.Default).collect { value -> println(value) }
    }
}

//buffer 缓存  流并行执行
//执行第一个，直接执行下一步操作  非常高效 不存在等待
fun testBuffer() {
    runBlocking {

        val time = measureTimeMillis {
            fooFlow()
                .buffer()// 缓冲发射项，无需等待
                .collect { value ->
                    delay(300) // 假装我们花费 300 毫秒来处理它
                    println(value)
                }
        }


    }

}

//组合多个流
//combine
fun testZip() {

    runBlocking {
        val nums = (1..3).asFlow() // 数字 1..3
        val strs = flowOf("one", "two", "three") // 字符串
        nums.zip(strs) { a, b -> "$a -> $b" } // 组合单个字符串
            .collect { println(it) } // 收集并打印
    }

}

fun testCatch() {
    runBlocking {
        fooFlow()
            .onEach { value ->
                check(value <= 1) { "Collected $value" }
                println(value)
            }
            .catch { e -> println("Catch $e") }
            .onCompletion { println("Done") } //流完成
            .collect()
    }
}

fun main() {
//    foo().forEach { value -> println(value) }
//    fooseq().forEach { value -> println(value) }
//    runBlocking {
//        fooDelay().forEach { value -> println(value) }
//    }

//    testFlow()
//    testFlowCancel()
//    testSupFlow()
//    testTransform()
//    testTake()
//    testWasteTime()
//    testFlowWasteTime()
    testCatch()
}