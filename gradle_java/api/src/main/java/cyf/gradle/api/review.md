 ## 回顾点：
 * BiFunction
 
 * getAndIncrement
 * BeanUtils.copyProperties (org.springframework.beans.BeanUtils 包下，而不是Apache包下的)
 * 深克隆、浅克隆
 * localDate.plus(1, ChronoUnit.WEEKS)
 * LocalTime
 * DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 * LocalDateTime
 * Date date = Date.from(from.atZone(ZoneId.systemDefault()).toInstant());
 * LocalDate localDate1 = LocalDate.from(date2.toInstant().atZone(ZoneId.systemDefault()));
 * ChronoUnit.DAYS.between(from, time);
 * Optional.ofNullable  orElse  ifPresent
 * stream flatMap
 * Multiset
 * HashMultimap
 * HashBiMap
 * HashBasedTable
 * Joiner Splitter
 * ImmutableMap.of("B", 1, "C", null);
 * Iterables.filter transform any concat frequency elementsEqual
 * Sets.difference union  intersection
 * EnumUtils
 * Collections.max
 * Ordering.natural().greatestOf(integers, 2);
 * Range.closed
 * Lists.partition
 *
 * FutureTask futureTask = new FutureTask(new MyCallable());
 * FutureTask 为 Future接口的实现，创建对象后可以作为submit参数，也可取到执行的结果 threadPoolExecutor.submit(futureTask);
 *
   ```
    @HystrixCommand(fallbackMethod = "fallback",
                 commandProperties = {
                         @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                 },
                 threadPoolProperties = {
                         @HystrixProperty(name = "coreSize", value = "5"),
                         @HystrixProperty(name = "maxQueueSize", value = "7"),
                         @HystrixProperty(name = "keepAliveTimeMinutes", value = "5"),
                         @HystrixProperty(name = "queueSizeRejectionThreshold", value = "50")
                 })
   ```
 
 
 
 
 * 同步利用模块用自己的线程池执行方法，出错不会影响全局线程池； 若要异步，方法返回结果需为 AsyncResult
 
 
 * 事件机制：EventBus 、 AsyncEventBus post / @Subscribe 业务处理
 *
 * Guava-cache ：
 *  @Bean(name = "asyncRefreshLoadingCache")
 *  CacheBuilder.newBuilder(). refreshAfterWrite(1, TimeUnit.MINUTES).build 重写load 、reload方法{MoreExecutors.listeningDecorator(getThreadPoolExecutor())}
 *
 *  异步回调：
     ```
        ListenableFuture<String> listenableFuture = MoreExecutors.listeningDecorator(threadPoolExecutor).submit(new Callable)
        Futures.addCallback(listenableFuture, new FutureCallback<String>(){override onSuccess(){}},threadPoolExecutor)
     ```
    
          
    
 
 