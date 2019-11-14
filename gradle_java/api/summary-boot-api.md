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
   
            c. 出现异常时默认打印异常信息，异常语句之后的代码不会执行，但异常语句之前的代码设置还有效【eg：set属性值在return后仍有效】然后return，返回结果不会报500；可在创建EventBus时候对异常进行处理
                
    EventBus eventBus = new EventBus((exception, context) -> {
               System.out.println(context.getSubscriber());
               log.error(exception.getMessage());
           });

   ```   
### Cache eg：GuavaCache

1.  refreshAfterWrite 代替 expireAfterWrite ：

    ```
    
       CacheBuilder.newBuilder()
    
       expireAfterWrite： 缓存过期后，多个线程请求，会阻塞，只有一个线程进入load方法去加载设置缓存，设置好后 其余阻塞线程取缓存返回。
    
       refreshAfterWrite： 缓存过期后，只有一个线程进入load方法重新设置缓存，其余线程不阻塞，返回旧缓存值； 但有缓存同时过期的较多时，仍会有多个用于load缓存的线程阻塞；
    
       refreshAfterWrite + relaod： 使用异步线程加载缓存，所有请求线程此时均返回旧缓存值；
    ```   
    
2. 
    ```
        .recordStats() 添加统计信息
   
                CacheStats stats = asyncRefreshLoadingCache.stats();
                        long hitCount = stats.hitCount();
                        double hitRate = stats.hitRate();
                        double loadTime = stats.averageLoadPenalty();
   
   
        .removalListener() ：监听器，eg：缓存失效原因等
   
                RemovalListener removalListener = new RemovalListener() {
                            @Override
                            public void onRemoval(RemovalNotification notification) {
                                String cause = notification.getCause().name();
                                log.info("缓存移除监听，原因：{}", cause);
                            }
                        };
   ```    
    
    
3. 回调：

    ```
        ListenableFuture<String> listenableFuture = MoreExecutors.listeningDecorator(threadPoolExecutor).submit(new Callable);
   
        Futures.addCallback(listenableFuture, new FutureCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println(Thread.currentThread().getName());
                        System.out.println(result.toUpperCase());
                    }
        
                    @Override
                    public void onFailure(Throwable t) {
                        return;
                    }
                }, threadPoolExecutor);
    
   ```    
   
###工具

1. HuTool : https://www.hutool.cn/docs/#/

2. 时间：
    ```
     1.MonthDay: 检查与年份无关的的周期性日期 【生日、节日等】
    
        LocalDate localDate = LocalDate.of(2019, 10, 1);
        MonthDay.of(localDate.getMonth,localDate.getDayOfMonth)
    
        of.equals(MonthDay.now())
        
   
     2.YearMonth: 年有多少天、月有多少天【有效判断2月天数等情况】
        
             YearMonth.now().lenthOfYear() \ lengthOfMonth() \ atEndOfMonth()
   
     3.获取时区：
            Set<String> zoneIds = ZoneOffset.getAvailableZoneIds();         
   
            ZoneId zone = ZoneId.of("America/New_York");   
   
            LocalDateTime.now(zone);
   ```
   
3. jodd： 
       
     ```
        1. 字符串模板
   
            String content = "hello ${foo} , ${name}"
   
            map.put("foo","A");
            map.put("name","B");
   
            StringTemplateParser parser = new StringTemplateParser();
   
            parser.parse(content, s -> maps.get(s))
   
        2. http 请求：
            
            HttpRequest request = HttpRequest.post(url).query/form(参数)
            HttpResponse response = request.send();
            response.bodyText()/body();
            
   ```   
   
3. 集合操作：
           
        ```
            Multiset
            HashMultimap
            HashBiMap
            HashBasedTable
            Joiner Splitter
            ImmutableMap.of("B", 1, "C", null);
            Iterables.filter transform any concat frequency elementsEqual
            Sets.difference union  intersection
            Collections.max
            Ordering.natural().greatestOf(integers, 2);
            Range.closed
            Lists.partition

       ```   