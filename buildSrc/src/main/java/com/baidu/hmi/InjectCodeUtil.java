/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.hmi;

import com.android.build.api.transform.JarInput;
import com.android.utils.FileUtils;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * 借助 Javassit 操作 Class 文件
 */
class InjectCodeUtil {
    private ClassPool sClassPool = ClassPool.getDefault();
    private Set<String> registrySet = new HashSet<>();
    private CtClass viewHolderRegistry;
    private String viewHolderRegistryPath;
    private JarInput viewHolderJarInput;
    private File viewHolderDest;

    void init() {
        sClassPool = ClassPool.getDefault();
    }

    void findTargetClass(String classPath) {

        System.out.println(classPath.indexOf("ViewHolderRegistry") +
                "：" + classPath.indexOf("$"));
        try {
            sClassPool.appendClassPath(classPath);
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        File dir = new File(classPath);
        findAllFile(dir, dir.getAbsolutePath());
    }

    private boolean addToSet(String originPath, String diskPath) {
        try {
            String classPath = originPath;
            System.out.println("classPath: " + classPath);
            if (classPath.indexOf("ViewHolderRegistry") > 0) {
                // 获取Class
                if (!"".equals(diskPath)) {
                    classPath = classPath.replace(diskPath, "").substring(1);
                }
                //                System.out.println("classPath: " + classPath);

                String className = classPath
                        .replace(File.separator, ".")
                        .replace(".class", "");
                System.out.println("className: " + className);

                // 加入当前路径
                sClassPool.appendClassPath(classPath);

                CtClass ctClass = sClassPool.getCtClass(className);
                // 解冻
                if (ctClass.isFrozen()) {
                    ctClass.defrost();
                }

                if (classPath.indexOf("ViewHolderRegistry_") > 0) {
                    registrySet.add(className);
                    return false;
                } else if (viewHolderRegistry == null) {
                    viewHolderRegistry = ctClass;
                    viewHolderRegistryPath = originPath;
                    return true;
                }

                ctClass.detach();

            }

        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void findAllFile(File dir, String absolutePath) {
        if (dir.isDirectory()) {
            //            System.out.println(dir.getName() + " isDirectory");
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    //                    System.out.println(file.getName() + " isFile");
                    addToSet(file.getAbsolutePath(), absolutePath);
                } else {
                    findAllFile(file, absolutePath);
                }
            }
        } else {
            //            System.out.println("isFile");
            addToSet(dir.getAbsolutePath(), "");
        }

    }

    void findTargetByJar(JarInput jarInput, File dest) {
        // 加入当前路径
        try {
            sClassPool.appendClassPath(jarInput.getFile().getAbsolutePath());

            if (jarInput.getFile().getAbsolutePath().endsWith(".jar")) {

                JarFile jarFile = new JarFile(jarInput.getFile());
                Enumeration enumeration = jarFile.entries();
                //用于保存
                while (enumeration.hasMoreElements()) {
                    JarEntry jarEntry = (JarEntry) enumeration.nextElement();
                    String entryName = jarEntry.getName();

                    //插桩class
                    //class文件处理
                    System.out.println("============deal with jar class file " + entryName);

                    if (addToSet(entryName, "")) {
                        viewHolderJarInput = jarInput;
                        viewHolderDest = dest;
                    }

                }
                //结束
                jarFile.close();
            }

        } catch (NotFoundException | IOException e) {
            e.printStackTrace();
        }

    }

    void injectStaticCode() {
        if (viewHolderRegistry == null) {
            System.err.println("viewHolderRegistry is not found. please check");
            return;
        }

        try {
            File tmpFile =
                    new File(viewHolderJarInput.getFile().getParent() + File.separator
                            + "classes_temp.jar");
            //避免上次的缓存被重复插入
            if (tmpFile.exists()) {
                tmpFile.delete();
            }

            JarOutputStream jarOutputStream =
                    new JarOutputStream(new FileOutputStream(tmpFile));

            JarFile jarFile = new JarFile(viewHolderJarInput.getFile());
            Enumeration enumeration = jarFile.entries();

            //用于保存
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enumeration.nextElement();
                String entryName = jarEntry.getName();
                ZipEntry zipEntry = new ZipEntry(entryName);
                jarOutputStream.putNextEntry(zipEntry);

                if (entryName.indexOf("ViewHolderRegistry") > 0 && entryName.indexOf(
                        "ViewHolderRegistry_") <= 0) {
                    //插桩class
                    System.out.println("============inject with jar class file " + entryName);

                    jarOutputStream.write(doInject());

                } else {
                    InputStream inputStream = jarFile.getInputStream(jarEntry);
                    jarOutputStream.write(IOUtils.toByteArray(inputStream));
                }
                jarOutputStream.closeEntry();
            }
            //结束
            jarOutputStream.close();
            jarFile.close();

            //处理完输入文件之后，要把输出给下一个任务
            FileUtils.copyFile(tmpFile, viewHolderDest);
            tmpFile.delete();

            //            viewHolderRegistry.writeFile();
        } catch (CannotCompileException | IOException e) {
            e.printStackTrace();
        }

    }

    private byte[] doInject() throws CannotCompileException, IOException {
        CtConstructor ctConstructor = viewHolderRegistry.makeClassInitializer();
        System.out.println("registrySet ->" + registrySet);
        if (registrySet.size() > 0) {
            for (String s : registrySet) {
                ctConstructor.insertAfter("add(\"" + s + "\");");
            }
        }
        return viewHolderRegistry.toBytecode();
    }

}
