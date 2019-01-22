package com.xpleemoon.plugin.click.transform

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.xpleemoon.plugin.click.asm.utils.weavePreventFastRepeatClick2ClassByteArray
import com.xpleemoon.plugin.click.asm.utils.weavePreventFastRepeatClick2ClassFile
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileOutputStream
import java.util.zip.CRC32
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream


/**
 * @author xpleemoon
 */
class PreventFastRepeatClickTransform : Transform() {
    override fun getName(): String = javaClass.simpleName

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> = TransformManager.CONTENT_CLASS

    override fun isIncremental(): Boolean = true

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> = TransformManager.SCOPE_FULL_PROJECT

    override fun transform(transformInvocation: TransformInvocation?) {
        val outputProvider = transformInvocation?.outputProvider ?: return

        val isIncremental = transformInvocation.isIncremental
        if (!isIncremental) {
            outputProvider.deleteAll()
        }

        transformInvocation.inputs.forEach { transformInput ->
            transformInput.jarInputs.forEach { jarInput ->
                val srcJar = jarInput.file
                val destJar = outputProvider.getContentLocation(
                    "${srcJar.name}_${DigestUtils.md5(srcJar.absolutePath)}",
                    jarInput.contentTypes,
                    jarInput.scopes,
                    Format.JAR
                )

                if (isIncremental) {
                    when (jarInput.status) {
                        Status.NOTCHANGED -> {
                        }
                        Status.ADDED, Status.CHANGED -> transformJar(srcJar, destJar)
                        Status.REMOVED -> FileUtils.forceDeleteOnExit(destJar)
                        else -> {
                        }
                    }
                } else {
                    transformJar(srcJar, destJar)
                }
            }

            transformInput.directoryInputs.forEach { directoryInput ->
                val srcDir = directoryInput.file
                val destDir = outputProvider.getContentLocation(
                    directoryInput.name,
                    directoryInput.contentTypes,
                    directoryInput.scopes,
                    Format.DIRECTORY
                )
                FileUtils.forceMkdir(destDir)

                if (isIncremental) {
                    val srcDirPath = srcDir.absolutePath
                    val destDirPath = destDir.absolutePath
                    directoryInput.changedFiles.entries.forEach { (changedFile, status) ->
                        val destFilePath = changedFile.absolutePath.replace(srcDirPath, destDirPath)
                        val destFile = File(destFilePath)
                        when (status) {
                            Status.NOTCHANGED -> {
                            }
                            Status.ADDED, Status.CHANGED -> {
                                FileUtils.touch(destFile)
                                transformFile(changedFile, destFile)
                            }
                            Status.REMOVED -> FileUtils.forceDeleteOnExit(destFile)
                            else -> {
                            }
                        }
                    }
                } else {
                    transformDir(directoryInput.file, destDir)
                }
            }
        }
    }

    private fun transformJar(srcJar: File, destJar: File) {
//        println("srcJar：${srcJar.absolutePath}，destJar：${destJar.absolutePath}")

        // 防止文件目录不存在
        FileUtils.touch(destJar)

        ZipOutputStream(FileOutputStream(destJar)).use { zos ->
            val crc32 = CRC32()
            val srcZipFile = ZipFile(srcJar)
            srcZipFile.entries().toList().forEach { srcZipEntry ->
                val newBytes = weavePreventFastRepeatClick2ClassByteArray(
                    srcZipEntry.name to srcZipFile.getInputStream(srcZipEntry)
                )
                crc32.reset()
                crc32.update(newBytes)
                val destZipEntry = ZipEntry(srcZipEntry.name).apply {
                    method = ZipEntry.STORED
                    size = newBytes.size.toLong()
                    compressedSize = newBytes.size.toLong()
                    crc = crc32.value
                }
                zos.putNextEntry(destZipEntry)
                zos.write(newBytes)
                zos.closeEntry()
            }
            zos.flush()
        }
    }

    private fun transformDir(srcDir: File, destDir: File) {
        val srcDirPath = srcDir.absolutePath
        val destDirPath = destDir.absolutePath
        srcDir.takeIf {
            it.isDirectory
        }?.walk()?.forEach { srcFile ->
            //            println("文件名：${srcFile.name}，文件路径：${srcFile.absolutePath}")
            val destFilePath = srcFile.absolutePath.replace(srcDirPath, destDirPath)
//            println("srcFile：${srcFile.absolutePath}，destFilePath：$destFilePath")
            val destFile = File(destFilePath)
            weavePreventFastRepeatClick2ClassFile(srcFile, destFile)
        }
    }

    private fun transformFile(changedFile: File, destFile: File) {
        weavePreventFastRepeatClick2ClassFile(changedFile, destFile)
    }
}