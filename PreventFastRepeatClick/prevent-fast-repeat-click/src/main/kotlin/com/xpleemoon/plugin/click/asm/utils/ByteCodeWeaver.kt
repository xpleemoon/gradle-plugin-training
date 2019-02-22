package com.xpleemoon.plugin.click.asm.utils

import com.xpleemoon.plugin.click.asm.PreventFastRepeatClickClassVisitor
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import java.io.File
import java.io.InputStream

/**
 * 根据[fileName]判断是否可进行字节码织入
 */
private fun isWeavable(fileName: String) = fileName.substringAfterLast('/').run {
    endsWith(".class")
            && this != "BuildConfig.class"
            && this != "R.class"
            && !Regex("^R\\\$(\\S*).class\$").matches(this)
}

internal fun weavePreventFastRepeatClick2ClassFile(inputFile: File, outputFile: File, clickIntervalTimeMs: Long) {
    if (inputFile.isDirectory) return

    // 防止文件目录不存在
    outputFile.parentFile.takeIf { !it.exists() }?.mkdirs()

    if (isWeavable(inputFile.name)) {
        val cr = ClassReader(inputFile.readBytes())
        val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        val cv = PreventFastRepeatClickClassVisitor(cw, clickIntervalTimeMs)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        outputFile.writeBytes(cw.toByteArray())
    } else {
        FileUtils.copyFile(inputFile, outputFile)
    }
}

internal fun weavePreventFastRepeatClick2ClassByteArray(name2InputStream: Pair<String, InputStream>, clickIntervalTimeMs: Long): ByteArray {
    val (name, inputStream) = name2InputStream
    return if (isWeavable(name)) {
        val cr = ClassReader(inputStream)
        val cw = ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        val cv = PreventFastRepeatClickClassVisitor(cw, clickIntervalTimeMs)
        cr.accept(cv, ClassReader.EXPAND_FRAMES)
        cw.toByteArray()
    } else {
        IOUtils.toByteArray(inputStream)
    }
}