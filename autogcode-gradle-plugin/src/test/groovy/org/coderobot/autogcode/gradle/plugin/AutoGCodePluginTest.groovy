package org.coderobot.autogcode.gradle.plugin

import org.coderobot.autogcode.gradle.task.TemplateLoadTask
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.BeforeClass
import org.junit.Test

class AutoGCodePluginTest {

    static Project project
    static TemplateLoadTask loadTask

    @BeforeClass
    static void init() {
        project = ProjectBuilder.builder().build()
        project.apply plugin: 'gcode'

        project.gcode{
            singleFlag=false
            template="/home/wzt/work/hub/autogcode/autogcode-gradle-plugin/src/test/resources/templates"
            templateNameInitParam=["projectName":"saas-core","groupName":"org.appsugar.archetypes","module":"bus"]
        }

        loadTask = project.tasks.templateLoad{
            entityClass = AutoGCodePluginTest
        }
    }

    @Test
    void testExistTasks() {
        assert project.tasks.templateLoad instanceof TemplateLoadTask
    }

    @Test
    void testDoTask(){
        loadTask.execute()
    }

}
