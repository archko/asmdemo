package asm

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.INVOKEVIRTUAL
import org.objectweb.asm.tree.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * @author: archko 2021/9/11 :20:00
 */
fun main() {
    hello()
}

private fun hello() {
    val classNode = ClassNode()
    classNode.version = Opcodes.V1_8
    classNode.access = Opcodes.ACC_PUBLIC
    classNode.name = "asm/MyTreeClass"
    classNode.superName = "java/lang/Object"

    val mn = MethodNode(Opcodes.ACC_PUBLIC, "hello", "(I)V", null, null)
    classNode.methods.add(mn)

    mn.visitCode()
    mn.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
    mn.visitLdcInsn("hello")
    mn.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false)
    mn.visitEnd()

    val cw = ClassWriter(Opcodes.ASM5)
    classNode.accept(cw)
    val file = File("MyTreeClass.class")
    file.writeBytes(cw.toByteArray())
}