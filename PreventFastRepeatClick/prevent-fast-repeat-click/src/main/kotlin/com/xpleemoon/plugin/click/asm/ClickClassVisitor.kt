package com.xpleemoon.plugin.click.asm

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * @author xpleemoon
 */
class ClickClassVisitor(cv: ClassVisitor) : ClassVisitor(Opcodes.ASM6, cv) {
    private var nameOfOnClickListenerImpl: String? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        val isOnClickListenerImpl = interfaces?.any {
            it.equals("android/view/View\$OnClickListener", ignoreCase = false)
        } ?: false
        if (isOnClickListenerImpl) {
            nameOfOnClickListenerImpl = name
        }
        super.visit(version, access, name, signature, superName, interfaces)
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        desc: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val mv = super.visitMethod(access, name, desc, signature, exceptions)
        return if (!nameOfOnClickListenerImpl.isNullOrEmpty()
            && access == Opcodes.ACC_PUBLIC
            && name.equals("onClick")
            && desc.equals("(Landroid/view/View;)V")
        ) {
            object : MethodVisitor(Opcodes.ASM6, mv) {
                override fun visitCode() {
                    super.visitCode()

                    mv.visitVarInsn(Opcodes.ALOAD, 1)
                    mv.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "com/xpleemoon/plugin/weave/FastRepeatClickUtilsKt",
                        "isPreventFastRepeatClick",
                        "(Landroid/view/View;)Z",
                        false
                    )
                    val weave_label = Label()
                    mv.visitJumpInsn(Opcodes.IFEQ, weave_label)
                    mv.visitInsn(Opcodes.RETURN)
                    mv.visitLabel(weave_label)

                    println("Complete bytecode weaving：向$nameOfOnClickListenerImpl.${name}方法织入快速点击拦截")
                }
            }
        } else {
            mv
        }
    }
}