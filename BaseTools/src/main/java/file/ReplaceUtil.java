package file;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;



public class ReplaceUtil {





    public static void main(String[] args) {
      //  replace("/Users/zhanglei10/static/BaseApp/pom.xml","jfdq","rebill");
        //listDir("/Users/zhanglei10/static","jfdq","rebill");

        replaceDir("/Users/zhanglei10/static","rebill","jfdq");

    }







    /****
     * @Description  将指定目录下的文件夹和文件全部替换关键字.
     * @Author zhanglei
     * @Date 2020/12/24 10:25 上午
     * @param path :  重命名的目录
     * @param oriStr : 被替换的字符
     * @param repStr :  替换后的字符
     * @return : void
     **/
    public static void replaceDir(String path,String oriStr,String repStr){
        File file = new File(path);
        LinkedList<File> list = new LinkedList<>();

        if (file.exists()) {
            File[] subFiles = file.listFiles();
            if (null == subFiles || subFiles.length == 0) {
                return;
            }

            list.addAll(Arrays.asList(subFiles));
            while (!list.isEmpty()) {
                File curFile = list.removeFirst();
                File newCurFile = renameFile(curFile,oriStr,repStr);
                File[] files = newCurFile.listFiles();
                if (null == files || files.length == 0) {
                    continue;
                }
                for (File f : files) {
                    if (f.isDirectory()) {
                        System.out.println("文件夹:" + f.getAbsolutePath());
                        File newSaveFile = renameFile(f,oriStr,repStr);
                        if (null != newSaveFile){
                            list.add(newSaveFile);
                        }
                    } else {
                        File newSaveFile = renameFile(f,oriStr,repStr);
                        if (null != newSaveFile){
                            replaceFileContent(newSaveFile.getAbsolutePath(),oriStr,repStr);
                            System.out.println("文件:" + f.getAbsolutePath());
                        }
                    }
                }
            }
        } else {
            System.out.println("文件不存在!");
        }
    }



    /***
     * @Description 重命名文件
     * @Author zhanglei
     * @Date 2020/12/24 10:25 上午
     * @param f :  重命名的文件
     * @param oriStr : 被替换的字符
     * @param repStr : 替换后的字符
     * @return : java.io.File
     **/
    private static File renameFile(File f,String oriStr,String repStr){
        if (null == f || !f.exists()){
            return null;
        }
        String abPath = f.getAbsolutePath();
        if (abPath.contains(oriStr)){
            String targetDir = abPath.replaceAll(oriStr,repStr);
            File saveFile = new File(targetDir);
            f.renameTo(saveFile);
            return saveFile;
        }
        return f;
    }



    /****
     * @Description 替换文件中的指定字符
     * @Author zhanglei
     * @Date 2020/12/24 10:24 上午
     * @param path : 文件目录
     * @param oriStr : 被替换的字符
     * @param repStr : 替换后的字符
     * @return : void
     **/
    public static void replaceFileContent(String path,String oriStr,String repStr){
        try {
            FileReader fis = new FileReader(path); // 创建文件输入流
//			BufferedReader br = new BufferedReader(fis);
            char[] data = new char[1024]; // 创建缓冲字符数组
            int rn = 0;
            StringBuilder sb = new StringBuilder(); // 创建字符串构建器
            // fis.read(data)：将字符读入数组。在某个输入可用、发生 I/O
            // 错误或者已到达流的末尾前，此方法一直阻塞。读取的字符数，如果已到达流的末尾，则返回 -1
            while ((rn = fis.read(data)) > 0) { // 读取文件内容到字符串构建器
                String str = String.valueOf(data, 0, rn);// 把数组转换成字符串
//				System.out.println(str);
                sb.append(str);
            }
            fis.close();// 关闭输入流
            // 从构建器中生成字符串，并替换搜索文本
            String str = sb.toString().replace(oriStr, repStr);
            FileWriter fout = new FileWriter(path);// 创建文件输出流
            fout.write(str.toCharArray());// 把替换完成的字符串写入文件内
            fout.close();// 关闭输出流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
