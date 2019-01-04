### interview回顾点
         
* 代理：
    1. JDK 动态代理：
        1. 存在代理接口
        2. 实现 InvocationHandler
        3. 
        ```
        Object proxy = Proxy.newProxyInstance(impl.getClass().getClassLoader(), interfaces, new JdkProxy(impl));
        
        Hello hello = (Hello) proxy;
        ```
        
    2. CGLib 代理：
        1. 基于继承实现
        2. 实现 MethodInterceptor，重写方法中 methodProxy.invokeSuper(o, objects); 将实际的执行转到父类（被代理的类）
        3. 
        ```
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(new Apple().getClass());
        enhancer.setCallback(new CGLibProxy());
        
        //获取代理对象，所有非final方法调用转发到 intercept()
        Apple o = (Apple)enhancer.create()
        ```
---

* AtomicStampedReference : 

        ```
        添加版本因素 来控制并发情况下执行可控制次数
        
        if (stampReference.compareAndSet(reference, reference + 10, stamp, stamp + 1)) {
                                log.debug("充值成功");
                                ato.getAndAdd(10);
                            }
                            
           if (reference > 3) {
                        threadPool.submit(() -> {
                            log.info("Thread:{} ，reference:{}", Thread.currentThread().getName(), reference);
                            if (stampReference.compareAndSet(reference, reference - 3, stamp, stamp + 1)) {
                                ato.getAndAdd(-3);
                                log.debug("消费3元,余额：{}，版本：{}", ato.get(), stampReference.getStamp());
                            }
                            return ato.get();
                        });
                    }                            
        ```     
        
* CompletableFuture
    1.  完成并行处理，或者采用CountDownLatch 、CyclicBarrier
    2. 未指定自定义线程池时，使用 ForkJoinPool.commonPool-worker-1 线程池；
    ```
    CompletableFuture<Void> nameResult = CompletableFuture.runAsync(() -> {
                log.info("name 处理：{}", Thread.currentThread().getName());
                userBuilder.name("Taylor Swift");
            }, executor);
            
     //汇聚任务结果
       CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(result.toArray(new CompletableFuture[result.size()]));
        //get()阻塞等待汇聚的所有任务完成
         allDoneFuture.get();
     
     /////////////////////////////////////////////////////////////////////////////////////    
         
     CompletableFuture<User.UserBuilder> resultId = CompletableFuture.supplyAsync(
                    () -> {
                        log.info("id 处理：{}, User:{}", Thread.currentThread().getName(),userBuilder.build());
                       return userBuilder.id(200);
                    }, executor);     
                    
                    
            //thenAccept:请求线程处理;  thenAcceptAsync: 异步处理，不指定线程池时默认使用 ForkJoinPool.commonPool
             resultId.thenAcceptAsync(b -> {
                 b.city("北京");
                 log.info("resultId -- thenAcceptAsync 处理：{}，User:{}", Thread.currentThread().getName(),userBuilder.build());
             },executor);    
         
    ```    
    
* CountDownLatch:
    1. 请求线程阻塞，线程池异步完成后，结束请求。
    2. 不能再利用
    ```
    for (int i = 0; i < 7; i++) {
                int finalI = i;
                Future<Integer> future = threadPool.submit(() -> {
                    log.info("Thread ：{} 完成任务：{}", Thread.currentThread().getName(), finalI);
                    return finalI;
                });
                if (future.get() >= 0) {
                    countDownLatch.countDown();
                }
            }
            //请求线程阻塞，异步线程完成后，结束请求
            countDownLatch.await();
            log.info("全部任务完成");
    ```
    
* CyclicBarrier:
     1. 请求线程不阻塞，结束请求；    
     2. 线程池异步线程业务处理后，到达await 阻塞，等待其他线程到达屏障
     3. 所有线程都到达屏障后执行 CyclicBarrier 里的任务。
     4. cyclicBarrier.reset(); 可循环使用
     #####注意：
        线程池核心数量5 ，队列：100
        7个任务，核心线程执行5个，await()后核心线程阻塞，
        2个任务进队列，队列未满，不会创建新的线程执行，等待核心线程执行，但核心线程一直阻塞，无限期等待
     ```
      CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
                 log.info("分批任务已全部完成");
             });
     
             for (int i = 0; i < 7; i++) {
                 int finalI = i;
     //            log.info("阻塞等待线程数：{}",cyclicBarrier.getNumberWaiting());
                 threadPool.execute(() -> {
                     log.info("Thread ：{} ，完成任务：{}", Thread.currentThread().getName(),finalI);
                     try {
                         cyclicBarrier.await();
                     } catch (InterruptedException |BrokenBarrierException e) {
                         e.printStackTrace();
                     }
                 });
             }
     ```   
        