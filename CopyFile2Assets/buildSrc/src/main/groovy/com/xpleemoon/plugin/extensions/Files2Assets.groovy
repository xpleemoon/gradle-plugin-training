package com.xpleemoon.plugin.extensions

class Files2Assets {
    /**
     * 拷贝的文件源
     */
    String filesSrc
    /**
     * 需要排除的文件信息
     */
    String[] excludeFiles = ['**/.git/**', '**/.gitignore']
}