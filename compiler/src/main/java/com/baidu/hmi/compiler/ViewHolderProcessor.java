/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.hmi.compiler;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;

import com.baidu.hmi.annotation.ViewHolder;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;


@AutoService(Processor.class)
public class ViewHolderProcessor extends AbstractProcessor {
    private static final String PKG = "com.baidu.adu.noadapter.compiler";
    private Elements elementUtils;

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ViewHolder.class.getCanonicalName());
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        //        ClassName hashMap = ClassName.get("java.util", "HashMap");
        System.out.println("============Enter ViewHolderProcessor===========");
        // 属性
        FieldSpec size =
                FieldSpec.builder(TypeName.INT, "size",
                        Modifier.PRIVATE, Modifier.STATIC)
                        .build();

        FieldSpec modelList =
                FieldSpec.builder(ParameterizedTypeName.get(List.class, Class.class), "modelList",
                        Modifier.PRIVATE, Modifier.STATIC)
                        .initializer("new $T()", ArrayList.class)
                        .build();

        FieldSpec vhTypeMaps =
                FieldSpec.builder(ParameterizedTypeName.get(Map.class, Integer.class, Class.class),
                        "vhTypeMaps",
                        Modifier.PRIVATE, Modifier.STATIC)
                        .initializer("new $T()", HashMap.class)
                        .build();

        FieldSpec modelTypeMap =
                FieldSpec.builder(ParameterizedTypeName.get(Map.class, Class.class,
                        String.class),
                        "modelTypeMap",
                        Modifier.PRIVATE, Modifier.STATIC)
                        .initializer("new $T()", HashMap.class)
                        .build();
        // 泛型嵌套
        //        ParameterizedTypeName subType = ParameterizedTypeName.get(List.class, Integer
        //        .class);
        //        FieldSpec multiValueMap =
        //                FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(Map.class),
        //                        ClassName.get(Class.class), subType),
        //                        "multiValueMap",
        //                        Modifier.PRIVATE, Modifier.STATIC)
        //                        .initializer("new $T()", HashMap.class)
        //                        .build();

        FieldSpec multiValueMap =
                FieldSpec.builder(ParameterizedTypeName.get(Map.class, Class.class, int[].class),
                        "multiValueMap",
                        Modifier.PRIVATE, Modifier.STATIC)
                        .initializer("new $T()", HashMap.class)
                        .build();

        Set<? extends Element> elements =
                roundEnvironment.getElementsAnnotatedWith(ViewHolder.class);
        // getGeneric方法

        MethodSpec getGeneric = MethodSpec.methodBuilder("getGeneric")
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .addParameter(Class.class, "vhClass")
                .returns(Class.class)
                .addCode(getGenericCode())
                .build();

        // getItemViewType
        MethodSpec getItemViewType = MethodSpec.methodBuilder("getItemViewType")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Object.class, "data")
                .returns(int.class)
                .addCode(getItemViewTypeCode())
                .build();

        // indexOf
        MethodSpec indexOf = MethodSpec.methodBuilder("indexOf")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(Class.class, "cls")
                .addParameter(int.class, "anInt")
                .returns(int.class)
                .addCode(indexOfCode())
                .build();

        // getVHClass
        MethodSpec getVHClass = MethodSpec.methodBuilder("getVHClass")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(int.class, "type")
                .returns(Class.class)
                .addStatement(CodeBlock.of("return vhTypeMaps.get(type)"))
                .build();
        // 类
        TypeSpec clazz = TypeSpec.classBuilder("ViewHolderRegistry")
                .addModifiers(Modifier.PUBLIC)
                .addField(size)
                //                .addField(vhList)
                .addField(modelList)
                .addField(vhTypeMaps)
                .addField(modelTypeMap)
                .addField(multiValueMap)
                .addStaticBlock(initCode(elements))
                .addMethod(getGeneric)
                .addMethod(indexOf)
                .addMethod(getItemViewType)
                .addMethod(getVHClass)
                .build();
        try {
            System.out.println("============APT开始创建文件===========");
            JavaFile build = JavaFile.builder(PKG, clazz)
                    .indent("    ")
                    .addFileComment("\nAutomatically generated file. DO NOT MODIFY\n")
                    .skipJavaLangImports(false)
                    .build();
            System.out.println(build.toString());
            build.writeTo(processingEnv.getFiler());
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private CodeBlock indexOfCode() {
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.addStatement("int[] ints = multiValueMap.get(cls)");
        builder.beginControlFlow("for (int i = 0; i < ints.length; i++)");
        builder.beginControlFlow("if (ints[i] == anInt)");
        builder.addStatement("return i");
        builder.endControlFlow();
        builder.endControlFlow();
        builder.addStatement("return -1");
        return builder.build();
    }

    /**
     * public static Integer getItemViewType(Object data) {
     * Class<?> cls = data.getClass();
     * int[] values = multiValueMap.get(cls);
     * if (values != null && modelTypeMap.get(cls) != null) {
     * try {
     * Field field = cls.getDeclaredField(modelTypeMap.get(cls));
     * field.setAccessible(true);
     * int anInt = field.getInt(data);
     * int i = indexOf(cls, anInt);
     * if (i != -1) {
     * return i * size;
     * } else {
     * System.err.println("find type value by " + cls + " is error.");
     * }
     * <p>
     * } catch (NoSuchFieldException e) {
     * e.printStackTrace();
     * } catch (IllegalAccessException e) {
     * e.printStackTrace();
     * }
     * }
     * return modelList.indexOf(cls);
     * }
     *
     * @return
     */
    private CodeBlock getItemViewTypeCode() {
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.addStatement("Class<?> cls = data.getClass()");
        builder.addStatement("$T values = multiValueMap.get(cls)", int[].class);

        builder.beginControlFlow("if (values != null && modelTypeMap.get(cls) != null)");
        builder.add(getTryCatchCode());
        builder.endControlFlow();
        builder.addStatement("return modelList.indexOf(cls)");
        return builder.build();
    }

    private CodeBlock getTryCatchCode() {
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.beginControlFlow("try");
        builder.addStatement("$T field = cls.getDeclaredField(modelTypeMap.get(cls))", Field.class);
        builder.addStatement("field.setAccessible(true)");
        builder.addStatement("int anInt = field.getInt(data)");
        builder.addStatement("int i = indexOf(cls, anInt) + 1");
        builder.beginControlFlow("if (i != -1)");
        builder.addStatement("return i * size");
        builder.nextControlFlow("else");
        builder.addStatement("System.err.println(\"find type value by \" + cls + \" is error"
                + ".\")");
        builder.endControlFlow();
        builder.nextControlFlow("catch ($T e)", NoSuchFieldException.class);
        builder.addStatement("e.printStackTrace()");
        builder.nextControlFlow("catch ($T e)", IllegalAccessException.class);
        builder.addStatement("e.printStackTrace()");
        builder.endControlFlow();
        return builder.build();
    }

    private CodeBlock getGenericCode() {
        CodeBlock.Builder builder = CodeBlock.builder();
        builder.addStatement("$T type = vhClass.getGenericSuperclass()", Type.class);
        builder.beginControlFlow("if(type instanceof $T)", ParameterizedType.class)
                .addStatement("$T parameterizedType = ($T) type", ParameterizedType.class,
                        ParameterizedType.class)
                .addStatement("Type[] types = parameterizedType.getActualTypeArguments()")
                .beginControlFlow("if (types.length > 0) ")
                .addStatement("return (Class<?>) types[0]")
                .endControlFlow()
                .endControlFlow()
                .addStatement("return null");
        return builder.build();
    }

    private CodeBlock initCode(Set<? extends Element> elements) {

        CodeBlock.Builder builder = CodeBlock.builder();
        builder.addStatement("size = $L", elements.size());
        List<Element> multiValueList = new ArrayList<>();
        int i = 0;
        for (Element element : elements) {
            if (element.getAnnotation(ViewHolder.class).type() > 0) {
                multiValueList.add(element);
            } else {
                builder.addStatement("vhTypeMaps.put($L,$L.class)", i, element.asType());
                builder.addStatement("modelList.add(getGeneric($L.class))", element.asType());
                i++;
            }
        }

        i = 0;
        // 一对多
        if (multiValueList.size() > 0) {
            Map<String, List<Integer>> map = new HashMap<>();
            for (Element element : multiValueList) {
                builder.addStatement("vhTypeMaps.put($L * size,$L.class)", i + 1, element.asType());
                ViewHolder annotation = element.getAnnotation(ViewHolder.class);

                String cls = "";
                try {
                    // Get The Annotation's class filed.
                    // Yes, this is a total hack of a solution,
                    // and I'm not sure why the API developers decided to go this direction with
                    // the annotation processor feature.
                    // However, There is a number of people implement this (including myself),
                    annotation.cls();
                } catch (MirroredTypeException mte) {
                    cls = mte.getTypeMirror().toString();
                }

                builder.addStatement("modelTypeMap.put($L.class,$S)", cls, annotation.filed());

                if (map.get(cls) == null) {
                    List<Integer> list = new ArrayList<>();
                    map.put(cls, list);
                }

                map.get(cls).add(annotation.type());
                System.out.println(
                        "multiValueList -->" + element.toString() + ":" + map.get(element));

                i++;
            }

            Set<Map.Entry<String, List<Integer>>> entries = map.entrySet();
            for (Map.Entry<String, List<Integer>> entry : entries) {
                builder.addStatement("multiValueMap.put($L.class,new int[]$L)", entry.getKey(),
                        listToStr(entry.getValue()));
            }
        }

        //        builder.beginControlFlow("for (int i = 0; i < vhList.size(); i++)")
        //                .addStatement("vhTypeMaps.put(i, vhList.get(i))")
        //                .addStatement("vhModelMaps.put(modelList.get(i), i)")
        //                .endControlFlow();

        return builder.build();
    }

    private String listToStr(List<Integer> values) {
        StringBuilder sb = new StringBuilder("{");
        for (Integer value : values) {
            sb.append(",").append(value);
        }
        sb.append("}");
        return sb.toString().replaceFirst(",", "");
    }

    private String getPackageName(TypeElement type) {
        return elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }
}