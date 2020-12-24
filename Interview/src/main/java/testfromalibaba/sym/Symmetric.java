package testfromalibaba.sym;


public class Symmetric {

    private static final String unSym = "ababaw";
    private static final String Sym = "aba";

    public static void main(String[] args) {
        System.out.println(isSymmetric(unSym));
        System.out.println(isSymmetric(Sym));
    }


    /***
     * @Description 校验字符是否为对称字符串，采用左右偏移量以此递进的方式，如有一个字符不匹配立即返回false,以此类推
     * 扩展思想：本程序仅适用于少量字符的案例，如果字符量大，则可以采用二分法，将字符串fork/join到多个线程中以此来提高性能。
     * @Author zhanglei
     * @Date 2020/12/8 12:33 上午
     * @param param : 要识别的字符
     * @return : boolean true：对称字符 false：非对称字符
     **/
    public static final boolean isSymmetric(String param){
        if (null == param || "".equals(param)){
            return false;
        }

        int totalLen = param.length();
        int rightOffset = 1;
        for (int i = 0 ; i < param.length() ; i++){
            int currentCharIndex = totalLen - rightOffset;
            //- 只有一个字符
            if (i == currentCharIndex && i == 0){
                return true;
            }

            //- 左右offset各递进一步
            if (param.charAt(i) != param.charAt(currentCharIndex)){
                return false;
            }else{
                rightOffset++;
                continue;
            }
        }
        return true;
    }
}
