/*
 * Copyright (C) 2021 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.hmi

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.gradle.api.Project
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils

class NoAdapterTransform extends Transform {

    private InjectCodeUtil injectCodeUtil
    private Project mProject

    NoAdapterTransform(Project mProject) {
        this.mProject = mProject
        injectCodeUtil = new InjectCodeUtil()
        injectCodeUtil.init()
    }

    @Override
    void transform(TransformInvocation transformInvocation)
            throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        println '--------------------transform 开始-------------------'

        boolean isIncremental = transformInvocation.isIncremental()

        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()
        if (!isIncremental) {
            //不需要增量编译，先清除全部
            outputProvider.deleteAll()
        }

        transformInvocation.getInputs().each { TransformInput input ->
            input.jarInputs.each { JarInput jarInput ->
                //处理Jar
                processJarInputWithIncremental(jarInput, outputProvider, isIncremental)
            }

            input.directoryInputs.each { DirectoryInput directoryInput ->
                //处理文件
                processDirectoryInputWithIncremental(directoryInput, outputProvider, isIncremental)
            }
        }

        injectCodeUtil.injectStaticCode()
    }

    void processJarInputWithIncremental(JarInput jarInput, TransformOutputProvider outputProvider, boolean isIncremental) {
        //重名名输出文件,因为可能同名,会覆盖
        def jarName = jarInput.name
        def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
        if (jarName.endsWith(".jar")) {
            jarName = jarName.substring(0, jarName.length() - 4)
        }
        println("当前处理的jar->" + jarName + ":" + jarInput.file.absolutePath)
        File dest = outputProvider.getContentLocation(
                jarName + md5Name,
                jarInput.getContentTypes(),
                jarInput.getScopes(),
                Format.JAR)
//        if (isIncremental) {
//            //处理增量编译
//            processJarInputWhenIncremental(jarInput, dest)
//        } else {
        //不处理增量编译
        processJarInput(jarInput, dest)
//        }
    }

    void processJarInput(JarInput jarInput, File dest) {
        transformJarInput(jarInput, dest)
    }

    void processJarInputWhenIncremental(JarInput jarInput, File dest) {
        switch (jarInput.status) {
            case Status.NOTCHANGED:
                break
            case Status.ADDED:
            case Status.CHANGED:
                //处理有变化的
                transformJarInputWhenIncremental(jarInput.getFile(), dest, jarInput.status)
                break
            case Status.REMOVED:
                //移除Removed
                if (dest.exists()) {
                    FileUtils.forceDelete(dest)
                }
                break
        }
    }

    void transformJarInputWhenIncremental(JarInput jarInput, File dest, Status status) {
        if (status == Status.CHANGED) {
            //Changed的状态需要先删除之前的
            if (dest.exists()) {
                FileUtils.forceDelete(dest)
            }
        }
        //真正transform的地方
        transformJarInput(jarInput, dest)
    }

    void processDirectoryInputWithIncremental(DirectoryInput directoryInput, TransformOutputProvider outputProvider, boolean isIncremental) {

        File dest = outputProvider.getContentLocation(
                directoryInput.getFile().getAbsolutePath(),
                directoryInput.getContentTypes(),
                directoryInput.getScopes(),
                Format.DIRECTORY)

//        if (isIncremental) {
//            //处理增量编译
//            processDirectoryInputWhenIncremental(directoryInput, dest)
//        } else {
        processDirectoryInput(directoryInput, dest)
//        }
    }

    void processDirectoryInputWhenIncremental(DirectoryInput directoryInput, File dest) {
        FileUtils.forceMkdir(dest)
        String srcDirPath = directoryInput.getFile().getAbsolutePath()
        String destDirPath = dest.getAbsolutePath()
        Map<File, Status> fileStatusMap = directoryInput.getChangedFiles()
        fileStatusMap.each { Map.Entry<File, Status> entry ->
            File inputFile = entry.getKey()
            Status status = entry.getValue()
            String destFilePath = inputFile.getAbsolutePath().replace(srcDirPath, destDirPath)
            File destFile = new File(destFilePath)
            switch (status) {
                case Status.NOTCHANGED:
                    break
                case Status.REMOVED:
                    if (destFile.exists()) {
                        FileUtils.forceDelete(destFile)
                    }
                    break
                case Status.ADDED:
                case Status.CHANGED:
                    FileUtils.touch(destFile)
                    transformSingleFile(inputFile, destFile, srcDirPath)
                    break
            }
        }
    }

    void processDirectoryInput(DirectoryInput directoryInput, File dest) {
        transformDirectoryInput(directoryInput, dest)
    }

    void transformJarInput(JarInput jarInput, File dest) {
        injectCodeUtil.findTargetByJar(jarInput, dest)
        FileUtils.copyFile(jarInput.getFile(), dest)
    }

    void transformDirectoryInput(DirectoryInput directoryInput, File dest) {
        injectCodeUtil.findTargetClass(directoryInput.file.absolutePath)
        FileUtils.copyDirectory(directoryInput.getFile(), dest)
    }

    void transformSingleFile(File inputFile, File destFile, String srcDirPath) {
        FileUtils.copyFile(inputFile, destFile)
    }

    static boolean checkClassFile(String name) {
        //只处理需要的class文件
        return (name.endsWith(".class") && !name.startsWith("R\$")
                && !"R.class".equals(name) && !"BuildConfig.class".equals(name)
                && !name.startsWith("android/support/v4/app"))
    }

    @Override
    String getName() {
        return "NoAdapterTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }
}
