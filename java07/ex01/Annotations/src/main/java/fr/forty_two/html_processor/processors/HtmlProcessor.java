package fr.forty_two.html_processor.processors;

import java.io.IOException;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import fr.forty_two.html_processor.annotations.HtmlForm;
import fr.forty_two.html_processor.annotations.HtmlInput;

@SupportedSourceVersion(SourceVersion.RELEASE_25)
@SupportedAnnotationTypes({"fr.forty_two.html_processor.annotations.HtmlForm"})
public class HtmlProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(HtmlForm.class);
        for (Element element : elements) {
            TypeElement classElement = (TypeElement) element;
            HtmlForm htmlForm = classElement.getAnnotation(HtmlForm.class);
            String html = generateHtml(classElement, htmlForm);
            writeToFile(htmlForm.fileName(), html);
        }
        return true;
    }

    private String generateHtml(TypeElement classElement, HtmlForm htmlForm) {
        StringBuilder sb = new StringBuilder();

        sb.append("<form action = \"").append(htmlForm.action()).append("\" ")
            .append("method = \"").append(htmlForm.method()).append("\">\n");

        for (Element enclosed : classElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.FIELD) {

                HtmlInput htmlInput = enclosed.getAnnotation(HtmlInput.class);
                if (htmlInput != null) {
                    sb.append("<input type = \"").append(htmlInput.type()).append("\" ")
                        .append("name = \"").append(htmlInput.name()).append("\" ")
                        .append("placeholder = \"").append(htmlInput.placeholder()).append("\">\n");
                }
            }
        }

        sb.append("<input type = \"submit\" value = \"Send\">\n")
            .append("</form>\n");
        return sb.toString();
    }

    private void writeToFile(String fileName, String content) {
        try {
            FileObject file = processingEnv.getFiler().createResource(
                StandardLocation.CLASS_OUTPUT,
                "",
                fileName
            );

            try (Writer writer = file.openWriter()){
                writer.write(content);
            }

        } catch(IOException e) {
            processingEnv.getMessager()
                .printError("Failed to write file '" + fileName + "': " + e.getMessage());
        }
    }
}
