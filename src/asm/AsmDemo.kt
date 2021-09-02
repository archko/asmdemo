package asm

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.ACC_PRIVATE
import org.objectweb.asm.Opcodes.ACC_PUBLIC
import org.objectweb.asm.Opcodes.ASM4
import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.INVOKEVIRTUAL
import org.objectweb.asm.Opcodes.RETURN
import org.objectweb.asm.Opcodes.V1_7
import java.io.File

fun main() {
    //transformFromByte(createClz())
    transformFromClass()
}

fun createClz(): ByteArray {
    val cw = ClassWriter(0)
    //构造方法
    cw.visit(
        V1_7, ACC_PUBLIC,
        "MyClass",
        null,
        "java/lang/Object",
        null
    )
    //添加变量
    cw.visitField(
        ACC_PRIVATE,
        "test",
        "I",
        null,
        99
    ).visitEnd()

    //添加方法
    val hello = cw.visitMethod(
        ACC_PUBLIC,
        "hello",
        "()V",
        null,
        null
    )
    hello.visitCode()

    // 获取一个java.io.PrintStream对象
    hello.visitFieldInsn(
        GETSTATIC,
        "java/lang/System",
        "out",
        "Ljava/io/PrintStream;"
    )
    // 将int, float或 String 型常量值从常量池中推送至栈顶
    hello.visitLdcInsn("hello!")
    hello.visitMethodInsn(
        INVOKEVIRTUAL,
        "java/io/PrintStream",
        "println",
        "(Ljava/lang/String;)V",
        false
    )
    hello.visitInsn(RETURN)
    hello.visitMaxs(2, 1)
    hello.visitEnd()

    //类结束
    cw.visitEnd()
    val b = cw.toByteArray()
    val file = File("MyClass.class")
    file.writeBytes(b)
    return b
}

fun transformFromByte(clz: ByteArray) {
    val reader = ClassReader(clz)
    val writer = ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
    val classVisitor: ClassVisitor = MyMethodClassVisitor(writer)
    reader.accept(classVisitor, ASM4)

    val code: ByteArray = writer.toByteArray()

    val loader = MyClassLoader()
    val clazz: Class<*> = loader.defineClassForName("MyClass", code)

    for (method in clazz.methods) {
        println(method)
    }

    println("byte:${code.size}")
    //val file = File("OtherClass.class")
    //file.writeBytes(code)
}

fun transformFromClass() {
    val reader = ClassReader(Test::class.java.name)
    val writer = ClassWriter(reader, ClassWriter.COMPUTE_MAXS)
    val classVisitor: ClassVisitor = MyMethodClassVisitor(writer)
    reader.accept(classVisitor, ASM4)

    val code: ByteArray = writer.toByteArray()

    val loader = MyClassLoader()
    val clazz: Class<*> = loader.defineClassForName(Test::class.java.name, code)

    for (method in clazz.methods) {
        println(method)
    }

    println("byte:${code.size}")
    val file = File("Test.class")
    file.writeBytes(code)
}

