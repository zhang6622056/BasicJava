package example.simplerpc.remote;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;




/**
 *
 * 仿照dubbo invocation
 * @author Nero
 * @date 2020-04-02
 * *@param: null
 * @return 
 */
public class Invocation implements Serializable {
    private String interfaceName;
    private String className;
    private String methodName;
    private Class[] parametersType;
    private Object[] args;

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class[] getParametersType() {
        return parametersType;
    }

    public void setParametersType(Class[] parametersType) {
        this.parametersType = parametersType;
    }


    public static void main(String[] args) {
        Invocation invocation = new Invocation();
        invocation.setArgs(null);
        invocation.setMethodName("getUsername");
        invocation.setClassName("example.simplerpc.service.UserServiceImpl");
        invocation.setInterfaceName("example.simplerpc.service.UserService");
        System.out.println(JSON.toJSONString(invocation));
    }



}
