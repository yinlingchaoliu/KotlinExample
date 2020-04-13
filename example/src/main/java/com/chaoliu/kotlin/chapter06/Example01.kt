package com.chaoliu.kotlin.chapter06


import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.math.log
import kotlin.system.measureTimeMillis

//https://github.com/hltj/kotlinx.coroutines-cn

fun testHello() {
    GlobalScope.launch { // 在后台启动一个新的协程并继续
        delay(1000L) // 非阻塞的等待 1 秒钟(默认时间单位是毫秒)
        println("World!") // 在延迟后打印输出
    }

    println("Hello,") // 协程已在等待时主线程还在继续
    Thread.sleep(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
}

fun testBlock() {
    //阻塞主线程2秒
    runBlocking { // 但是这个表达式阻塞了主线程
        delay(2000L) // ......我们延迟 2 秒来保证 JVM 的存活
    }
}

fun testHello1() {

    runBlocking<Unit> {
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 非阻塞的等待 1 秒钟(默认时间单位是毫秒)
            println("World!") // 在延迟后打印输出
        }

        println("Hello,") // 协程已在等待时主线程还在继续
        delay(2000L) // 阻塞主线程 2 秒钟来保证 JVM 存活
    }
}

//等待一个协程
suspend fun testJoin() {

    var job = GlobalScope.launch {
        delay(1000L)
        println("world!")
    }

    println("hello ")
    job.join() //等待一个作业完成
}

//结构化并发  无须显式join
fun testRunBlock() =
    runBlocking {
        launch {
            delay(1000L)
            println("World!")
        }
        println("hello ")
    }

//作用域构建器
fun testCoroutineScope() {
    runBlocking {

        launch {
            delay(200L)
            println("Task from runBlocking -2")
        }

        //作用域构建器
        coroutineScope {
            launch {
                delay(50L)
                println("Task from nested launch -3")
            }
            delay(100L)
            println("Task from coroutine scope -1")
        }

        println("Coroutine scope is over-4") // 这一行在内嵌coroutineScope  launch 执行完毕后才输出
    }
}

// output=>
//Task from nested launch -3
//Task from coroutine scope -1
//Coroutine scope is over-4
//Task from runBlocking -2

//提取函数重构
fun testSupend() {

    runBlocking {
        launch {
            sayWorld()
        }

        coroutine()

        println("Hello, ")
    }

}

//挂起函数
suspend fun sayWorld() {
    delay(1000L)
    println("world")
}

suspend fun coroutine() {
    //作用域构建器
    coroutineScope {
        launch {
            delay(50L)
            println("Task from nested launch -3")
        }
        delay(100L)
        println("Task from coroutine scope -1")
    }
}

suspend fun testGloabl() {
    GlobalScope.launch {
        repeat(1000) { i ->
            println("$i")
            delay(500L)
        }
    }
    delay(1300L)
}

//GlobalScope 守护线程

//cancel
suspend fun testCancel() {
    val job = GlobalScope.launch {
        repeat(100) { i ->
            delay(500L)
            println("sleep $i")
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancel()
    job.join()
    println("main: Now I can quit.")
}

suspend fun testCancelAndJoin() {
    val job = GlobalScope.launch {
        repeat(100) { i ->
            delay(500L)
            println("sleep $i")
        }
    }
    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: Now I can quit.")
}

//取消是协作的
// 计算任务是不能取消的
fun testWorkingNoCancel() {
    runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0
            while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
                // 每秒打印消息两次
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L) // 等待一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消一个作业并且等待它结束
        println("main: Now I can quit.")
    }
}


//计算代码 可以取消
fun testWorking() {
    runBlocking {
        val startTime = System.currentTimeMillis()
        val job = launch(Dispatchers.Default) {
            var nextPrintTime = startTime
            var i = 0

            // isActive 是一个可以被使用在 CoroutineScope 中的扩展属性  可以用来取消
            while (isActive) { // 一个执行计算的循环，只是为了占用 CPU
                // 每秒打印消息两次
                if (System.currentTimeMillis() >= nextPrintTime) {
                    println("job: I'm sleeping ${i++} ...")
                    nextPrintTime += 500L
                }
            }
        }
        delay(1300L) // 等待一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消一个作业并且等待它结束
        println("main: Now I can quit.")
    }
}

fun testTry() {
    runBlocking {
        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                println("job: I'm running finally")
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并且等待它结束
        println("main: Now I can quit.")
    }
}

//withContext(NonCancellable) 不能被取消 用于做清理
fun testTryNoCancel() {
    runBlocking {

        val job = launch {
            try {
                repeat(1000) { i ->
                    println("job: I'm sleeping $i ...")
                    delay(500L)
                }
            } finally {
                //不能被取消 作为清理函数
                withContext(NonCancellable) {
                    println("job: I'm running finally")
                    delay(1000L)
                    println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                }
            }
        }
        delay(1300L) // 延迟一段时间
        println("main: I'm tired of waiting!")
        job.cancelAndJoin() // 取消该作业并等待它结束
        println("main: Now I can quit.")

    }

}

//处理超时问题
fun testTimeOut() {
    runBlocking {
        withTimeout(1000L) {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
    }
}

fun testTimeOutOrNull() {
    runBlocking {
        var result = withTimeoutOrNull(1300L) {
            repeat(100) { i ->
                println("I'm sleep $i")
                delay(100L)
            }
            "Done"
        }
        println("Result is $result")
    }
}

suspend fun retOne(): Int {
    delay(1000L)
    return 1
}

suspend fun retTwo(): Int {
    delay(1000L)
    return 2
}

fun testPlus() {
    runBlocking {
        //计算时间
        var time = measureTimeMillis {
            var value = retOne() + retTwo()
        }
        println("Completed in $time ms")
    }
}

//async并发
fun testAsyncPlus() {
    runBlocking {
        //计算时间
        var time = measureTimeMillis {
            var one = async { retOne() }
            var two = async { retTwo() }
            var value = one.await() + two.await()
        }
        println("Completed in $time ms ")
    }
}

//惰性sync并发
//只有start 或 await启动
fun testLasyAsyncPlus() {
    runBlocking {
        //计算时间
        var time = measureTimeMillis {
            var one = async(start = CoroutineStart.LAZY) { retOne() }
            var two = async(start = CoroutineStart.LAZY) { retTwo() }
            one.start()
            two.start() //没有start不是lazy,会顺序执行 不能并发
            var value = one.await() + two.await()
        }
        println("Completed in $time ms ")
    }
}

//async 风格函数
fun asyncFunOne() = GlobalScope.async { retOne() }
fun asyncFunTwo() = GlobalScope.async { retTwo() }

fun testAsyncFun() {
    runBlocking {
        var time = measureTimeMillis {
            var one = asyncFunOne()
            var two = asyncFunTwo()
            println("this answer is " + one.await() + two.await())
        }
        println("$time")
    }
}

//结构化并发
suspend fun concurrentSum(): Int = coroutineScope {
    var one = async { retOne() }
    var two = async { retTwo() }
    one.await() + two.await()
}

fun testSum() {
    runBlocking {
        var time = measureTimeMillis {
            println("this answer is " + concurrentSum())
        }
        println("$time")
    }
}

//调度器与线程
fun testPrintDispatcher() {

    runBlocking {
        launch { // 运行在父协程的上下文中，即 runBlocking 主协程
            println("main runBlocking : I'm working in thread[ ${Thread.currentThread().name}]")
        }

        launch(Dispatchers.Unconfined) { // 不受限的——将工作在主线程中
            println("Unconfined : I'm working in thread [${Thread.currentThread().name}]")
        }

        launch(Dispatchers.Default) { // 将会获取默认调度器
            println("Default : I'm working in thread [${Thread.currentThread().name}]")
        }

        launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
            println("newSingleThreadContext: I'm working in thread [${Thread.currentThread().name}]")
        }

    }
}

//-Dkotlinx.coroutines.debug 调试协程

//协程上下文切换
fun testSwitchContext() {
    runBlocking {
        newSingleThreadContext("Ctx1").use { ctx1 ->
            newSingleThreadContext("Ctx2").use { ctx2 ->
                runBlocking(ctx1) {
                    //上下文中作业
                    println("started in ctx1 job is ${coroutineContext[Job]}")
                    withContext(ctx2) {
                        println("working in ctx2 job is ${coroutineContext[Job]}")
                    }
                    println("back to ctx1 job is ${coroutineContext[Job]}")
                }
            }
        }
    }
}


// GlobalScope启动 不受到影响
fun testChildJob() {

    runBlocking {

        val request = launch {
            // 孵化了两个子作业, 其中一个通过 GlobalScope 启动
            GlobalScope.launch {
                println("job1: I run in GlobalScope and execute independently!")
                delay(1000)
                println("job1: I am not affected by cancellation of the request")
            }

            // 另一个则承袭了父协程的上下文
            launch {
                delay(100)
                println("job2: I am a child of the request coroutine")
                delay(1000)
                println("job2: I will not execute this line if my parent request is cancelled")
            }
        }

        delay(500)
        request.cancel() // 取消请求(request)的执行
        delay(1000) // 延迟一秒钟来看看发生了什么
        println("main: Who has survived request cancellation?")
    }

}

//父协程等待所有子协程结束
fun testParentJob() {
    runBlocking {
        // 启动一个协程来处理某种传入请求(request)
        val request = launch {
            repeat(3) { i -> // 启动少量的子作业
                launch {
                    delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒的时间
                    println("Coroutine $i is done")
                }
            }
            println("request: I'm done and I don't explicitly join my children that are still active")
        }
        request.join() // 等待请求的完成，包括其所有子协程
        println("Now processing of the request is complete")
    }
}

//给协程增加名字 便于打印查看
fun testName() {
    runBlocking {
        val v1 = async(CoroutineName("v1coroutine")) {
            delay(500)
            println("Computing v1")
            252
        }


        launch(Dispatchers.Default + CoroutineName("test")) {
            println("I'm working in thread ${Thread.currentThread().name}")
        }

    }
}

//协程作用域  即生命周期管理

class LifeCycle : CoroutineScope by CoroutineScope(Dispatchers.Default) {

    fun doSomething() {
        // 在示例中启动了 10 个协程，且每个都工作了不同的时⻓
        repeat(10) { i ->
            launch {
                delay((i + 1) * 200L) // 延迟 200 毫秒、400 毫秒、600 毫秒等等不同的时间 println("Coroutine $i is done")
            }
        }
    }

    fun destroy() {
        cancel()
    }
}

//作用域 即生命周期
fun testLife() {
    runBlocking {
        val activity = LifeCycle()
        activity.doSomething() // 运行测试函数
        println("Launched coroutines")
        delay(500L) // 延迟半秒钟
        println("Destroying activity!")
        activity.destroy() // 取消所有的协程
        delay(1000) // 为了在视觉上确认它们没有工作
    }
}

// 线程私有数据
// threadLocal.asContextElement(value = "launch")
// threadLocal.set("main")
fun testThreadLocal() {
    val threadLocal = ThreadLocal<String?>() // declare thread-local variable

    runBlocking {
        threadLocal.set("main")
        println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
            println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
            yield()
            println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        }
        job.join()
        println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")

    }
}

//异步流



fun main() {
//    testPlus()
//    testAsyncPlus()
//    testLasyAsyncPlus()
//    testAsyncFun()
//    testSum()
//    testPrintDispatcher()
//    testSwitchContext()
//    testChildJob()
//    testParentJob()
//    testLife()
    testThreadLocal()
}
