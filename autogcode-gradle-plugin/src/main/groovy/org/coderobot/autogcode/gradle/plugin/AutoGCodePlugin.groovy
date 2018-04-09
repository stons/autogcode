package org.coderobot.autogcode.gradle.plugin

import org.coderobot.autogcode.gradle.task.AbstractGCodePluginTask
import org.coderobot.autogcode.gradle.task.TemplateLoadTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class AutoGCodePlugin implements Plugin<Project> {
    public static final String PLUGIN_GROUP = "AutoGCode"
    @Override
    void apply(Project project) {

        createExtensions(project)
        applyConventionMappingToGCodeTaask(project)
        registerTasks(project)
        project.task('hello') {

            doLast {
                println 'Hello from the GreetingPlugin'
                def extensions = project.extensions.findByName(AutoGCodeExtensions.NAME)
                println extensions.singleFlag
                println extensions.template
            }
        }
    }

    private void applyConventionMappingToGCodeTaask(Project project){
        def gcodeExtension = project.extensions.findByName('gcode')
        project.tasks.withType(AbstractGCodePluginTask){
            conventionMapping.singleFlag = {gcodeExtension.singleFlag}
            conventionMapping.template = {gcodeExtension.template}
            conventionMapping.templateNameInitParam = {gcodeExtension.templateNameInitParam}
        }
    }

    private AutoGCodeExtensions createExtensions(Project project){
        return project.extensions.create(AutoGCodeExtensions.NAME,AutoGCodeExtensions)
    }

    private void registerTasks(Project project) {
        project.task('templateLoad', type: TemplateLoadTask)
    }
}
