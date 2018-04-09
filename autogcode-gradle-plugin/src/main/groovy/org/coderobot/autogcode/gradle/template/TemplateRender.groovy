package org.coderobot.autogcode.gradle.template

import com.samskivert.mustache.Mustache
import com.samskivert.mustache.Template
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.nio.charset.Charset

class TemplateRender {

    public static final Logger logger = LoggerFactory.getLogger(TemplateRender)

    Mustache.Compiler mustache
    String templatePath

    TemplateRender(templatePath){
        logger.debug("TemplateRender counstruct templatePath is {}",templatePath)
        println("TemplateRender counstruct templatePath is {}"+ templatePath)
        this.templatePath = templatePath
        this.mustache = mustacheCompiler()
    }

    public String process(String name, Map<String, ?> model) {
        try {
            Template template = loadTemplate(name);
            return template.execute(model);
        }
        catch (Exception e) {
            throw new IllegalStateException("Cannot render template", e);
        }
    }

    private Template loadTemplate(String name) {
        try {
            Reader template = mustache.loader.getTemplate(name)
            return mustache.compile(template)
        }
        catch (Exception e) {
            throw new IllegalStateException("Cannot load template " + name, e);
        }
    }


    private    Mustache.Compiler mustacheCompiler() {
        return Mustache.compiler().withLoader(mustacheTemplateLoader())
    }

    private   Mustache.TemplateLoader mustacheTemplateLoader() {
        Charset charset = Charset.forName("UTF-8")
        return {name -> new InputStreamReader(new FileInputStream(this.templatePath + "/"+name), charset)}
    }

    public void writeFile(String outFilePath,String content){
        File outFile = new File(outFilePath)
        if(!outFile.exists()){
            if(!outFile.getParentFile().exists()){
                outFile.getParentFile().mkdirs()
            }
            outFile.createNewFile()
        }
        FileWriter writer = new FileWriter(outFile)
        writer.write(content)
        writer.flush()
        writer.close()
    }
}
