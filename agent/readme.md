### 个人理解
    将agent程序放入到main函数的项目中，提供Instrumentation对象调用JVMTI开放出来的功能。
    使得开发者可以对jvm进行一些灵活操作



### ManiFest文件
    Manifest-Version: 1.0
    Archiver-Version: Plexus Archiver
    Built-By: nero
    Created-By: Apache Maven 3.6.0
    Build-Jdk: 1.8.0_201
    Main-Class: premain.Main
    Premain-Class: premain.Premain


### 启动脚本：
    java -javaagent:./agent-1.0-SNAPSHOT.jar -jar agent-1.0-SNAPSHOT.jar

    -javaagent: 指明premain的代理程序，这个代理程序寻找的是agent，包含premain的jar
    -jar : 指明main方法入口，这个指明的main项目的jar
    
    
### Instrumentation对象

