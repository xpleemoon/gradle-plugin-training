package com.xpleemoon.plugin.click.asm

import org.objectweb.asm.*

/**
 * @author xpleemoon
 */
class PreventFastRepeatClickClassVisitor(cv: ClassVisitor, private val defaultIntervalTimeMs: Long) :
    ClassVisitor(Opcodes.ASM6, cv) {
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
                private var isExcludeWeave = false
                private var intervalTimeMsOfAnnotation: Long? = null

                override fun visitAnnotation(desc: String?, visible: Boolean): AnnotationVisitor {
                    val av = super.visitAnnotation(desc, visible)
                    return if (desc.equals("Lcom/xpleemoon/plugin/click/annotation/PreventFastRepeatClick;")) {
                        object : AnnotationVisitor(Opcodes.ASM6, av) {
                            override fun visit(name: String?, value: Any?) {
                                super.visit(name, value)
                                if (name.equals("intervalTimeMs")) {
                                    intervalTimeMsOfAnnotation = value as? Long
                                }
                                when (name) {
                                    "isExclude" -> isExcludeWeave = value as Boolean
                                    "intervalTimeMs" -> intervalTimeMsOfAnnotation = value as Long
                                }
                            }
                        }
                    } else {
                        av
                    }
                }

                override fun visitCode() {
                    super.visitCode()

                    val intervalTimeMs = intervalTimeMsOfAnnotation ?: defaultIntervalTimeMs
                    if (!isExcludeWeave && intervalTimeMs > 0) {
                        mv.visitVarInsn(Opcodes.ALOAD, 1)
                        mv.visitLdcInsn(intervalTimeMs)
                        mv.visitMethodInsn(
                            Opcodes.INVOKESTATIC,
                            "com/xpleemoon/plugin/click/utils/FastRepeatClickUtilsKt",
                            "isPreventFastRepeatClick",
                            "(Landroid/view/View;J)Z",
                            false
                        )
                        @Suppress("LocalVariableName")
                        val prevent_fast_repeat_click_label = Label()
                        mv.visitJumpInsn(Opcodes.IFEQ, prevent_fast_repeat_click_label)
                        mv.visitInsn(Opcodes.RETURN)
                        mv.visitLabel(prevent_fast_repeat_click_label)

                        println("prevent-fast-repeat-click：向$nameOfOnClickListenerImpl.${name}方法织入快速点击拦截字节码，intervalTimeMs = $intervalTimeMs")
                    } else {
                        println("prevent-fast-repeat-click：$nameOfOnClickListenerImpl.${name}方法不做字节码织入处理，isExclude = $isExcludeWeave, intervalTimeMs = $intervalTimeMs")
                    }
                }
            }
        } else {
            mv
        }
    }
}