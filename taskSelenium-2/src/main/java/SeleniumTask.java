public class SeleniumTask {
    public static void main(String[] args) {


        Thread t1 = new Thread(new FirstThreadClass());
        t1.start();  // запуск нового потока

        Thread t2 = new Thread(new SecondThreadClass());
        t2.start();  // запуск нового потока

    }

}