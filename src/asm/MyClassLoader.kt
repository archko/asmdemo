package asm

/**
 * @author archko 2021/9/1:11:16
 */
class MyClassLoader : ClassLoader() {
    fun defineClassForName(name: String?, data: ByteArray): Class<*> {
        return this.defineClass(name, data, 0, data.size)
    }
}