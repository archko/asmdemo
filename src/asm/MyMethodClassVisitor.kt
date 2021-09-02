package asm

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * @author archko 2021/9/2:15:34
 */
class MyMethodClassVisitor(cv: ClassVisitor) : ClassVisitor(Opcodes.ASM4, cv), Opcodes {

    override fun visitMethod(
        access: Int,
        name: String,
        desc: String?,
        signature: String?,
        exceptions: Array<String?>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, desc, signature, exceptions)
        if (name == "hello") {
            return MyMethodVisitor(mv)
        } else {
            return mv
        }
    }

    inner class MyMethodVisitor(val methodVisitor: MethodVisitor) : MethodVisitor(Opcodes.ASM4, methodVisitor) {

        override fun visitMethodInsn(
            opcode: Int,
            owner: String?,
            name: String?,
            descriptor: String?,
            isInterface: Boolean
        ) {
            super.visitMethodInsn(opcode, owner, name, descriptor, false)
            methodVisitor.visitFieldInsn(
                Opcodes.GETSTATIC,
                "java/lang/System",
                "out",
                "Ljava/io/PrintStream;"
            )
            // 将int, float或 String 型常量值从常量池中推送至栈顶
            methodVisitor.visitLdcInsn("hello!-------=====-------")
            // 添加执行的方法，执行的是参数为字符串，无返回值的println函数
            methodVisitor.visitMethodInsn(
                Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V",
                false
            )
            methodVisitor.visitMaxs(2, 2)
        }
    }
}