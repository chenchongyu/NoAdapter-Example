# 代码库模板说明
通过iCode创建代码库后建议进行如下操作：
1. settings.gradle文件中修改：
    1. 实际的`rootProject.name`
2. build.gradle文件中修改：
    1. 实际的`group`、`version`、`artifactId`
    2. 实际的工程依赖`dependencies`部分
3. 包名修改：
    1. Java包名不能有"`-`"等特殊字符，建议结合实际情况修改
    2. `src/main/java`和`src/test/java`中都需要修改
4. 编译命令修改：
    1. 修改`ci.yml`中`command`内容
    2. 如需使用特殊的env，请在`command`中加入export命令

**以下为README模板，请参照填写！！！**
# noadapter
极简的RecyclerView Adapter 库，可以让开发人员专注于ViewHolder的开发，从而忽略繁冗的adapter的编写和viewholder与model的映射。

## 快速开始
如何构建、安装、运行

## 测试
如何执行自动化测试

## 如何贡献
贡献patch流程、质量要求

## 讨论
百度Hi讨论群：XXXX
