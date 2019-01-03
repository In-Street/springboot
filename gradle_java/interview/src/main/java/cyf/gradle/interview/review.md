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