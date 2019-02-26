## PreventFastRepeatClick[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?)](https://www.apache.org/licenses/LICENSE-2.0.html)

SDK|prevent-fast-repeat-click|prevent-fast-repeat-click-api
:---|:---:|:---:
latest version|[ ![Download](https://api.bintray.com/packages/xpleemoon/maven/prevent-fast-repeat-click/images/download.svg) ](https://bintray.com/xpleemoon/maven/prevent-fast-repeat-click/_latestVersion)|[ ![Download](https://api.bintray.com/packages/xpleemoon/maven/prevent-fast-repeat-click-api/images/download.svg) ](https://bintray.com/xpleemoon/maven/prevent-fast-repeat-click-api/_latestVersion)

这是一个gradle plugin，用于在app编译的transform阶段，通过[ASM](https://asm.ow2.io)对project中所有的`OnClickListener.onClick()`进行字节码织入，织入的字节码是关于防止`View`的快速重复点击。

> 插件对于防止快速重复点击的默认间隔时间设定是300ms，若要更改默认时间或针对某些特定View的点击还有特殊需求（比如设置特定时间或者不拦截），可参考[进阶用法](#进阶用法)

### 使用方法

1、在工程目录的`build.gradle`中设置插件依赖：
```
buildscript {
    dependencies {
        classpath 'com.xpleemoon.plugin:prevent-fast-repeat-click:latest_version'
    }
}
```

2、在application module的`build.gradle`引用插件
```
apply plugin: 'xpleemoon-prevent-fast-repeat-click'

dependencies {
    implementation 'com.xpleemoon.plugin:prevent-fast-repeat-click-api:latest_version'
}
```

完成上述两个步骤之后，`PreventFastRepeatClick`插件即可在编译的时候，对app中的`OnClickListener.onClick()`进行字节码（防止快速重复点击）织入。

### 进阶用法

通常不同开发者对于防止快速重复点击的默认间隔时间有不同的要求，那么可以在application module的`build.gradle`设置：
```
preventFastRepeatClick {
    defaultIntervalTimeMs 500//根据实际需求更改默认时间
}
```

在实际项目中，可能针对某些特定`View`的点击还有特殊需求，比如设置特定时间或者不拦截：
```
class MyFirstOnClickListeners : View.OnClickListener {
    /**
	 * 快速点击拦截，点击的间隔时间为250
	 */
    @PreventFastRepeatClick(intervalTimeMs = 250L)
    override fun onClick(v: View?) {
        Log.d("info_out", "123456")
    }
}

class MySecondOnClickListeners : View.OnClickListener {
	/**
	 * 不做快速点击拦截，因为isExclude为true
	 */
    @PreventFastRepeatClick(isExclude = true)
    override fun onClick(v: View?) {
        Log.d("info_out", "123456")
    }
}

class MyThirdOnClickListeners : View.OnClickListener {
    /**
	 * 不做快速点击拦截，因为intervalTimeMs为0
	 */
    @PreventFastRepeatClick(intervalTimeMs = 0L)
    override fun onClick(v: View?) {
        Log.d("info_out", "123456")
    }
}

class MyFourthOnClickListeners : View.OnClickListener {
    /**
	 * 不做快速点击拦截，虽然intervalTimeMs为250，但是isExclude为true
	 */
    @PreventFastRepeatClick(isExclude = true, intervalTimeMs = 250L)
    override fun onClick(v: View?) {
        Log.d("info_out", "123456")
    }
}
```

> 注解`PreventFastRepeatClick`有两个参数：
> - `isExclude`——对所注解的[android.view.View.OnClickListener.onClick]方法是否排除快速拦截的标记，简单的说若取值为true：不做快速点击拦截处理；反之，做快速点击拦截处理
> - `intervalTimeMs`——快速点击拦截的间隔时间，优先级高于gradle配置，注意若取值小于等于0，实际也相当于不拦截
