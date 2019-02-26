## CopyFile2Assets

这是一个自定义的gradle plugin，用于app打包期间，拷贝其他文件目录到assets，最终会被合并打入到apk中。

由于是demo的原因，并未将插件发布到maven仓库，而是通过构建`buildSrc`方式，直接添加到编译体系之中。同时，使用方式也很简单，只需要在application module的`build.gradle`中添加如下代码：
```gradle
apply plugin: 'com.xpleemoon.plugin.copyfiles2assets'

files2Assets {
    // 需要排除到文件和文件目录
    excludeFiles '**/.git/**', '**/.gitignore', '**test**'//, '**/data2.txt'
    from {
        myAssets {
            // 需要拷贝的文件路径
            path = "${project.rootDir.absolutePath}/myAssets"
        }
        myData{
            // // 需要拷贝的文件路径
            path = "${project.rootDir.absolutePath}/myData"
        }
    }
}
```
