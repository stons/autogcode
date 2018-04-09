package org.coderobot.autogcode.gradle.template

/**
 * 通过已有的类获取模板数据
 */
interface TemplateHashMapProcess {

    Map<String,String> getTemplateModel(Class clz)

}