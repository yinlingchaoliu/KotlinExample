package com.chaoliu.kotlin.chapter03

import kotlin.properties.Delegates
import kotlin.reflect.KProperty

//类与继承
class Empty

class Person constructor(firstName: String)

//省略 constructor
class Person1(val name: String) {
    private var age: Int? = null

    constructor(name: String, age: Int) : this(name) {
        this.age = age
    }

    init {
        println("$name")
    }
}

//集成
open class Base(p: Int)
class Derived(p: Int) : Base(p)

//重写
open class Shape {
    open val count: Int = 1

    open fun draw() {}
    open fun initView() {}
    fun fill() {}
}

class Circle : Shape() {

    //覆盖属性
    override val count: Int = 4

    //后续继承不容许重写
    final override fun initView() {

    }

    //覆盖方法
    override fun draw() {

    }
}

//val 本质 get方法  var 本质 get和set方法
//var 可以覆盖 val 反之不能

//初始化顺序
// base  attr - init - constructor
//child  init - attr - constructor
open class BaseTip constructor() {

    private var name: String? = null

    constructor(name: String, lastName: String) : this() {
        this.name = name
        println("Initializing Base constructor $name")
    }

    open val size: Int? =
        name?.length.also {
            println("Initializing Base attr :$it")
        }

    init {
        println("Initializing Base init")
    }

}

class DerivedTip() : BaseTip() {

    private var lastName: String? = null

    constructor(name: String, lastName: String) : this() {
        println("Initializing Derived constructor")
        this.lastName = lastName
    }

    init {
        println("Initializing Derived init")
    }

    override val size: Int? =
        (super.size?.plus(lastName?.length ?: 0)).also {
            println("Initializing Derived attr: $it")
        }
}

//伴生对象


//lateinit 延时初始化

interface MyInterface {
    val prop: Int // 抽象的
    val propertyWithImplementation: String get() = "foo"
    fun foo() {
        print(prop)
    }
}

class Child : MyInterface {
    override val prop: Int = 29
}

//接口冲突覆盖
interface A {
    fun foo() {}
    fun bar() {}
}

interface B {
    fun foo() {}
    fun bar() {}
}

class C : A {
    override fun foo() {
    }

    override fun bar() {
    }
}

class D : A, B {

    override fun foo() {
        super<A>.foo()
        super<B>.foo()
    }

    override fun bar() {
        super<B>.foo()
    }

}

//扩展函数
fun <T> MutableList<T>.swap(index1: Int, index2: Int) {

    if (index1 < lastIndex && index2 < lastIndex) {
        this[index1] = this[index2].also {
            this[index2] = this[index1]
        }
    }

}

// 可空接收者 即时 传入参数为null
fun Any?.toSafeString(): String {
    if (this == null) return ""
    return toString()
}

//扩展属性
class Test {

    var property: String
        get() = "foo"
        set(value) {
            property = value
        }

    //属性扩展
    val <T> List<T>.lastIndex: Int get() = size - 1

    //伴生方法扩展
    companion object {

    }

}

//扩展
fun Test.Companion.println(string: String) {
    println(string)
}

data class User(val name: String, val age: Int)

//sealed 密封类 所以子类在一个文件  枚举类型扩展 用于when表达式
sealed class Expr
data class Const(val number: Double) : Expr()
data class Sum(val e1: Expr, val e2: Expr) : Expr()
object NotANumber : Expr()

fun eval(expr: Expr): Double {
    return when (expr) {
        is Const -> expr.number
        is Sum -> eval(expr.e1) + eval(expr.e2)
        NotANumber -> Double.NaN
    }
}

//泛型
class Box<T>(t: T) {
    var value = t
}

//形变
// out  生产者
// in   消费者

interface Source<out T> {
    fun nextT(): T
}

interface Comparable<in T> {
    fun compareTo(other: T): Int
}

//类型投影  copy时对类型有控制
class Array<T>(val size: Int) {
    fun get(index: Int): T {
        TODO()
    }

    fun set(index: Int, value: T) {}
}

// Array<out Any> => Array<? extends Object>  自身及子类
// Array<in String> => Array<? super String>  自身及父类

//星投影  * 相当于java原始类型

//泛型函数
fun <T> addItem(item: T) {

}

fun <T> T.basicToString(): String {
    TODO()
}

//泛型约束

//上界  相当于 extends
fun <T : Comparable<T>> sort(list: List<T>) {

}

//泛型会类型擦除


//嵌套类
class Outer {
    private val bar: Int = 1

    class Nested {
        fun foo() = 2
    }

    fun test() {
        val demo = Outer.Nested().foo() // == 2
    }
}

//内部类 inner 可以访问外部类
class Outer1 {
    private val bar: Int = 1

    inner class Inner {
        fun foo() = bar
    }

    fun test() {
        val demo = Outer1().Inner().foo() // == 1
    }
}

//枚举类
enum class Direction {
    NORTH, SOUTH, WEST, EAST
}


enum class ProtocolState {
    WAITING {
        override fun signal() = TALKING
    },
    TALKING {
        override fun signal() = WAITING
    };

    abstract fun signal(): ProtocolState
}

//对象表达式

//类型别名  函数别名让框架更通用

typealias EasyMap<V> = MutableMap<Int, V>

//函数方法  返回值
typealias Handler = (String, Int) -> Unit

typealias Predicate<T> = (T) -> Boolean

fun test(name: String, age: Int) {
    println("$name  $age")
}

fun foo(p: Predicate<Int>) = p(42)

fun testAlias() {

    var map: EasyMap<String> = mutableMapOf()
    map.put(1, "name")

    var handler: Handler = { name: String, age: Int -> test(name, age) }

    run {
        handler("jack", 123)
    }

    val f: (Int) -> Boolean = { it > 0 }
    foo(f)
}

//inline 内联类 优化性能 代码展开

//属性委托
class Delegate {
    private var str:String = ""

    // thisRef 当前引用的class  property.name 变量名称
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return str
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        str = value
    }
}

//委托即代理类
interface BaseL {
    fun print()
}

class BaseLImpl(val x: Int) : BaseL {
    //属性委托
    var p: String by Delegate()

    var name: String by Delegates.observable("defalutValue") { prop, old, new ->
        println("$old -> $new")
    }


    override fun print() {
        println(x)
    }
}

class DerivedL(b: BaseL) : BaseL by b


val lazyValue: String by lazy {
    println("computed!")
    "Hello"
}

class UserMap(val map: Map<String, Any?>) {
    val name: String by map
    val age: Int by map
}

// by map
fun testMap() {
    val user = UserMap(mapOf("name" to "jack", "age" to 25))
    println(user.name)
    println(user.age)
}

//属性委托 即对属性获得值和设置值过程的封装
//最终体现为直接对变量操作
interface ReadOnlyProperty<in R, out T> {
    operator fun getValue(thisRef: R, property: KProperty<*>): T
}

interface ReadWriteProperty<in R, T> {
    operator fun getValue(thisRef: R, property: KProperty<*>): T
    operator fun setValue(thisRef: R, property: KProperty<*>, value: T)
}

fun main() {

    val list = mutableListOf(1, 2, 3)
    list.swap(0, 2)

    val jane = User("jane", 35)
    val jack = jane.copy(name = "jack")
    //Component 函数
    val (name, age) = jane
    println("$name, $age")

    //代理类
    var impl = BaseLImpl(10)
    var derived = DerivedL(impl)
    derived.print()

    println(impl.p)
    impl.p = "123"
    println(impl.p)
    impl.p = "kkk"
    println(impl.p)

    println(lazyValue)

}






