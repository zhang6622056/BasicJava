public class Main {
    public static void main(String[] args) {
        for (;;){
            System.out.println("main method...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
