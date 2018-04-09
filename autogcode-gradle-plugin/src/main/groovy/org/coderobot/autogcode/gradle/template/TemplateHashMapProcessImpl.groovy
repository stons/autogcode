package org.coderobot.autogcode.gradle.template

import com.samskivert.mustache.Mustache.Compiler

import java.util.concurrent.ConcurrentHashMap;

class TemplateHashMapProcessImpl implements  TemplateHashMapProcess{
    @Override
    Map<String, String> getTemplateModel(Class clz) {
        Map<String,?> result = new ConcurrentHashMap<>()

        //get class name
        String className = clz.getSimpleName()
        result.put("className",className)
        //get class name first char lowercase
        result.put("lowercaseClassName",className.substring(0,1).toLowerCase()+className.substring(1))

        return  result
    }


}
