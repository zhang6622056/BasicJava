package loadclass;

import com.sun.crypto.provider.DESKeyFactory;

public class ClassLoaderExp {

    public static void main(String[] args) {
        //-null =bootstrapClassloader
        System.out.println(String.class.getClassLoader());
        //- sun.misc.Launcher$ExtClassLoader
        System.out.println(DESKeyFactory.class.getClassLoader().getClass().getName());
        //-sun.misc.Launcher$AppClassLoader
        System.out.println(ClassLoaderExp.class.getClassLoader().getClass().getName());
        //-sun.misc.Launcher$AppClassLoader
        System.out.println(ClassLoader.getSystemClassLoader().getClass().getName());
    }
















}
