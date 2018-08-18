#线程池

线程池是对线程的统一分配管理，既可重复利用已有的线程，减少频繁创建、销毁带来的损耗也可提高响应速度。
阿里编码规约中指出：杜绝显示创建线程：避免创建、销毁的时间及资源的开销，可能会创建出大量同类线程，消耗内存或者过度切换，    
应使用线程池。

* 基本的类与接口：
    * Executor：接口，提供execute(Runnable command) 方法。
    * ExecutorService: 接口，继承Executor，提供了操作线程池的shutdown()、shutdownNow()等方法；submit()方法可接受
       Runnable或者Callable参数，有Future返回结果。
    * Executors:  提供创建线程池的类，但阿里编码规约中不建议采用此类创建线程池

* 依赖

    `
    compile group: 'com.google.guava', name: 'guava', version: '24.0-jre'
   `
   
   