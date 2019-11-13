###Hystrix 业务线程池隔离及熔断降级
1. 引入：hystrix-javanica

2. 将 HystrixCommandAspect 注入spring

3. CommandUserForAnnotation 使用，降级方法与主方法的返回值与参数需保持一致
    ```
        a. 同步执行：
                 @HystrixCommand(fallbackMethod = "fallback",
                            commandProperties = {
                                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
                            },
                            threadPoolKey = "syn_handle",
                            threadPoolProperties = {
                                    @HystrixProperty(name = "coreSize", value = "5"),
                                    @HystrixProperty(name = "maxQueueSize", value = "7"),
                                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "5"),
                                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "50")
                            })
   
        b.异步执行，方法需返回 AsyncResult：
                @HystrixCommand(threadPoolKey = "asyn_handle",
                            threadPoolProperties = {
                                    @HystrixProperty(name = "coreSize", value = "5"),
                                    @HystrixProperty(name = "maxQueueSize", value = "7"),
                                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "5"),
                                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "50")
                            })

   ```
   
###EventBus 
1. 同步、异步EventBus Bean注册，异步时指定线程池。eg：EventBusPool

    ```
        同步： new EventBus()；
    
        异步： new AsyncEventBus(threadPoolExecutor)； 
   ```
   
2. 带有@Subscribe 方法所属类 注入到EventBus eg: EventBusRegister
     ```
        eventBus.register(bean);
   ```   
   
3. post 发送消息，EventBus 订阅处理 @Subscribe

    ```
        发送： eventBus.post(xxx);
   
        订阅： eg: EventListener

            a. 当采用同步EventBus时，可开启并行处理： @AllowConcurrentEvents ，否则 线程阻塞，需等待上个线程完成后才能继续
               
            b. 哪个 Subscribe 处理取决于方法的参数类型，若参数有继承关系且父类和子类都有相应的 Subscribe 处理方法，则 post(子类) 后，子类的 Subscribe 方法和 父类的 Subscribe 方法都会处理这条消息。
   
            c. 出现异常时默认打印异常信息，异常语句之后的代码不会执行，但异常语句之前的代码设置还有效【eg：set属性值在return后仍存在】然后return，返回结果不会报500；可在创建EventBus时候对异常进行处理
                
    EventBus eventBus = new EventBus((exception, context) -> {
               System.out.println(context.getSubscriber());
               log.error(exception.getMessage());
           });

   ```   