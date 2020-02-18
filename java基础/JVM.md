**-server -Xmx2G -Xms2G  -XX:MaxTenuringThreshold=8 -XX:-UseBiasedLocking -XX:-UseCounterDecay -XX:AutoBoxCacheMax=20000 -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=75 -XX:+UseCMSInitiatingOccupancyOnly  -XX:+ExplicitGCInvokesConcurrent -XX:+ParallelRefProcEnabled -Dfile.encoding=UTF-8**

---

1. -XX:MaxTenuringThreshold=8, 设置对象经历多少次Minor GC才晋升到老生代 。
2. -XX:+UseBiasedLocking,设置启用偏向锁。  
3. -XX：-UseCounterDecay,来关闭JIT方法调用计数器热度衰减,衰减可能导致一些方法一直是温热,无法作为热点代码被即时编译器编译优化  

        方法调用计数器统计的并不是方法被调用的绝对次数，而是一个相对的执行频率，即一段时间之内方法被调用的次数。
        当超过一定的时间限度，如果方法的调用次数仍然不足以让它提交给即时编译器编译，那这个方法的调用计数器就会被减少一半，这个过程称为方法调用计数器热度的衰减（Counter Decay），而这段时间就称为此方法统计的半衰周期（Counter Half Life Time）

4. -XX:AutoBoxCacheMax=NNN, 可将Integer的自动缓存区间设置为[-128,NNN]。注意区间的下界固定在-128不可配置。  
5. -XX:+UseConcMarkSweepGC, 设置垃圾收集器为并发收集器
6. -XX:CMSInitiatingOccupancyFraction=70, CMS垃圾收集器，当老年代达到70%时，触发CMS垃圾回收
7. -XX:+UseCMSInitiatingOccupancyOnly, CMS回收使用固定的阈值，与-XX:+UseCMSInitiatingOccupancyOnly配合使用
8. -XX:+ExplicitGCInvokesConcurrent, 发生system.gc时, jvm会进行一次全暂停的full gc(stw),设置该参数,在cms gc下老生代的回收会变为一次正常的cms gc,不会暂停整个gc阶段。
    开启该参数-XX:+ExplicitGCInvokesConcurrent,cms gc情况下针对老生代的cms gc过程将会走background cms gc过程,这样会有一些阶段是并行执行的，不会让整个gc过程暂停,提高full gc效率。

9. -XX:+ParallelRefProcEnabled, 如果应用有很多的Reference or finalizable objects，那么可以使用-XX:+ParallelRefProcEnabled来减少持续时间。    
