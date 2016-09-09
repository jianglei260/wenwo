package com.myworld;

import com.google.auto.service.AutoService;
import com.myworld.annotation.CopyObject;
import com.myworld.wenwo.data.entity.AskMe;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.myworld.annotation.CopyObject"})
public class CopyProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(CopyObject.class);
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder("ObjectCopyUtil").addModifiers(Modifier.PUBLIC, Modifier.FINAL);
        for (Element element : set) {
            if (element.getKind() != ElementKind.CLASS) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "only support class");
            }
            String className = element.getSimpleName().toString();


            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("copy" + className).addModifiers(Modifier.PUBLIC, Modifier.STATIC).returns(TypeName.get(element.asType()))
                    .addParameter(TypeName.get(element.asType()), lowerFirst(className));
            methodBuilder.addStatement(className + " copy=new " + className + "()");
            Field[] fields = new Field[0];
            try {
                fields = Class.forName(TypeName.get(element.asType()).toString()).getDeclaredFields();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (field.getType() == boolean.class)
                    if (field.getName().startsWith("is")) {
                        fieldName = field.getName().replaceFirst("is", "");
                    }
                char firstCh = fieldName.charAt(0);
                String methodSuffix = fieldName;
                if (Character.isLowerCase(firstCh)) {
                    methodSuffix = fieldName.replaceFirst(Character.toString(firstCh), Character.toString(Character.toUpperCase(firstCh)));
                }
                methodBuilder.addStatement("copy.set" + methodSuffix + "(" + lowerFirst(className) + "." + (field.getType() == boolean.class ? "is" : "get") + methodSuffix + "())");
            }
            methodBuilder.addStatement("return copy");
            typeSpecBuilder.addMethod(methodBuilder.build());
        }

        JavaFile javaFile = JavaFile.builder("com.myworld.wenwo.utils", typeSpecBuilder.build()).build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String lowerFirst(String str) {
        return str.toLowerCase();
    }
}
