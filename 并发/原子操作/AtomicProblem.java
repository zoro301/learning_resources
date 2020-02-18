package com.zoro301.concurrent.safe;

/**
 * @Author renqiang
 * @Description
 * 原子问题解决，原子问题产生的原因是线程切换。
 * <p>线程切换依赖CPU中断,禁止CPU中断是否可以解决线程切换问题？
 * <p>多核CPU时代不可行,一个线程在CPU-1上执行，一个线程在CPU-2上执行, 禁止线程中断只能保证每个线程在CPU可以连续执行,并不同保证同一个
 * 时刻只有一个线程执行。
 * <p>同一时刻只有一个线程执行，称之为互斥。
 * @Date 2019/7/4 13:15
 */
public class AtomicProblem {

    private Long value = 0L;
    private Object lock = new Object();
    private void addOne(){
        synchronized (new Object()){//new Object()这种只使用一次的锁，会被jVM优化掉
            value += 1L;
        }
    }

    private Long get(){
        synchronized (new Object()){
            return value;
        }
    }

    public static void main(String[] args) {
        final AtomicProblem atomicProblem = new AtomicProblem();
        for(int i = 0; i<5;i++){
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    atomicProblem.addOne();
                    System.out.println(Thread.currentThread().getName() +" "+ atomicProblem.get());
                }
            },"thread-"+i);
            thread.start();
        }

    }

}
