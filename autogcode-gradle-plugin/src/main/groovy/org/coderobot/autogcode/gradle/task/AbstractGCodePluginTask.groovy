package org.coderobot.autogcode.gradle.task

import org.coderobot.autogcode.gradle.plugin.AutoGCodePlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class AbstractGCodePluginTask extends  DefaultTask{
    // 单个文件生成标识
    @Input
    boolean singleFlag
    // 单个模板文件或 模板目录
    @Input
    String template

    //模板文件名参数
    @Input
    Map<String,String>  templateNameInitParam

    AbstractGCodePluginTask(){
        super()
        group = AutoGCodePlugin.PLUGIN_GROUP
    }


    @TaskAction
    void run() {
        doTask()
    }

    protected abstract void doTask()
}
