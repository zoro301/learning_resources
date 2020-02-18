package com.zoro301.concurrent.happensbefore;

/**
 * @Author zoro301
 * @Description happens-before原则
 * start()原则: 主线程A启动子线程B后，子线程B可看到主线程在启动子线程B前的操作。即，线程A调用线程B的start()方法，则该start()
 *              操作happens-before于线程B中的任意操作。
 * join()原则: 主线程A调用子线程B.join()后,主线程能够看到子线程B的操作，"看到"是指对共享变量的操作。即，线程A调用线程B.join()
 *              并成功返回,则线程B中的任意操作happens-before于该join()操作的返回。
 * @Date 2019/7/3 15:27
 */
public class ThreadStartAndJoin {

    private static Integer var = 11;

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //可看到主线程在调用子线程B.start()(线程启动)之前对var修改，var=33
                System.out.println(Thread.currentThread().getName()+" 5= "+ var);
                var = 44;
            }
        });

        System.out.println(Thread.currentThread().getName()+" 1= "+ var);
        var = 33;
        thread.start();
        System.out.println(Thread.currentThread().getName()+" 2= "+ var);
        thread.join();

        //主线程可看到子线程对共享变量的修改
        System.out.println(Thread.currentThread().getName()+" 3= "+ var);

    }
}
