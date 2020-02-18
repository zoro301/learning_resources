package com.zoro301.concurrent.safe;

import com.zoro301.concurrent.Entry.DOrder;
import com.zoro301.concurrent.Entry.POrder;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: renqiang
 * @Date: 2019-08-03 20:07
 * @Description: TODO
 */
public class CyclicBarrierDemo {

    private Vector<DOrder> dos = new Vector<>();

    private Vector<POrder> pos = new Vector<>();

    private Executor executor = Executors.newFixedThreadPool(1);

    private final CyclicBarrier barrier = new CyclicBarrier(2,()->{executor.execute(()->check());});

    private void check() {
        DOrder dData = null;
        POrder pData = null;
        if(dos.size() > 0 && pos.size() > 0) {
            dData = dos.remove(0);
            pData = pos.remove(0);
        }
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        int diff = check(dData, pData);
        save(diff);

    }


    private void checkAll() throws Exception {
        Thread t1 = new Thread(() -> {
            int i=0;
            while(i<5) {
                i++;
                try {
                    dos.add(getDOrders(i));
                }catch (Exception e){
                    System.err.println("test error");
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();

        Thread t2 = new Thread(() -> {
            int i = 0;
            while (i < 5) {
                i++;
                try {
                    pos.add(getPOrders(i));
                }catch (Exception e){
                    System.err.println("test2 error");
                }
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });

        t2.start();
    }


    private void save(int diff) {
        System.out.println("保存数据成功 "+diff);
    }

    public int check(DOrder dData, POrder pData){
        System.out.println("p "+pData.getMount()+"  d "+ dData.getMount());
        return pData.getMount() - dData.getMount();
    }

    public static void main(String[] args) throws Exception {
        CyclicBarrierDemo c = new CyclicBarrierDemo();
        c.checkAll();
    }

    public POrder getPOrders(int i) {
        POrder p = new POrder();
        p.setMount(p.getMount() + 20 * i);
        return p;
    }

    public DOrder getDOrders(int i) {
        DOrder d = new DOrder();
        d.setMount(d.getMount() + 40 * i);
        return d;
    }
}
