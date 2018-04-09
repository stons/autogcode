package org.coderobot.autogcode.gradle.task

import org.coderobot.autogcode.gradle.template.TemplateHashMapProcess
import org.coderobot.autogcode.gradle.template.TemplateHashMapProcessImpl
import org.coderobot.autogcode.gradle.template.TemplateRender
import org.gradle.api.tasks.Input

class TemplateLoadTask extends  AbstractGCodePluginTask {

    @Input
    TemplateHashMapProcess dataProcess = new TemplateHashMapProcessImpl()

    @Input
    Class entityClass

    TemplateLoadTask(){
        super()
        description = "load template"
    }


    @Override
    protected void doTask() {
        TemplateRender render = new TemplateRender(template)
        def model = dataProcess.getTemplateModel(entityClass)
        def rootDir = project.getRootDir().toString()+"/"
        def templatenames = getTemplateNames(template)
        for(int i = 0;i< templatenames.size();i++){
            String it = templatenames[i]

            String process = render.process(it, model)
            String className = model.get("className")
            String outPath = outPathProcess(it, className)
            outPath = rootDir + outPath
            render.writeFile(outPath ,process)
        }
    }

    private String[] getTemplateNames(String templatePath){
        File file = new File(templatePath)
        return file.list()
    }


    private  String outPathProcess(String templateName,String className){
        String result = null
        String trueName = templateName.substring(templateName.lastIndexOf("_")+1)
        trueName = trueName.replace("ClassName",className)

        String pareToReplace = templateName.substring(0,templateName.lastIndexOf("_"))
        String[] params = pareToReplace.split("_")
        result = pareToReplace
        Set setOfStrings = templateNameInitParam.keySet()
        setOfStrings.each {
            if (params.contains(it)) {
                String replaceStr = templateNameInitParam.get(it)
                result = result.replace(it, replaceStr)
            }
        }
        result = result.replace("_","/")
        result = result.replace(".","/")
        result += result +"/"+ trueName
        return result

    }




}
