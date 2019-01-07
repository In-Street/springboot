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
    1. **请求线程阻塞**，线程池异步完成后，结束请求。[计数器减为0后，执行await()后的代码]
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
  
* ForkJoin
    
    1. 将任务分割成若干个小任务执行
    2. extends RecursiveTask (有返回值)、 RecursiveAction (无返回值)
    ```
        @Override
        protected Integer compute() {
            //任务分割数
            int hold = (end - start) / threshold;
            int sum = 0;
            //最小直接执行
            if (hold <= 1) {
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
            } else {
                int mid = (end - start) / hold;
                int e = end;
                for (int i = 0; i < hold; i++) {
                    log.info("Thread:{},第 {} 个 Task", Thread.currentThread().getName(), i);
                    end = start + mid > e ? e : start + mid;
                    Task task = new Task(start, end);
                    
                    // join() 、 get() 阻塞获取执行结果
                    sum += (Integer) task.fork().join();
                    log.info("start:{} - end:{}", start, end);
                    start = end + 1;
                }
            }
            return sum;
        }
       
       
          Task task = new Task(start, end);
          ForkJoinPool pool = new ForkJoinPool();
          Object o = pool.submit(task).get();
    ```        
 * ReentrantLock、Condition:
    1. condition可指定某个具体的condition获取锁，而不是参与锁竞争时获取的不确定性
    2. 读读互斥
    ```
        ReentrantLock lock = new ReentrantLock();
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        
        if (lock.tryLock()) {
            conditionA.await(3000, TimeUnit.MILLISECONDS);
            lock.unlock();
        }
        
         if (lock.tryLock()) {
                    conditionA.single();
                    lock.unlock();
                }
    ```

* ReentrantReadWritLock:
    
   1. 读读不互斥，还有写情况互斥
   
   ```
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
     try {
                readWriteLock.readLock().lock();
    //            读锁不允许获取Condition、写锁可以
    //            Condition condition = readWriteLock.readLock().newCondition();
                System.out.println(Thread.currentThread().getName() + "获取读锁" + System.currentTimeMillis());
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //不释放锁其他线程无法获取到对象锁
                readWriteLock.readLock().unlock();
            }
            
            
           try {
                    readWriteLock.writeLock().lock();
                    System.out.println(Thread.currentThread().getName() + "获取写锁" + System.currentTimeMillis());
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    readWriteLock.writeLock().unlock();
                }
                
                
           new Thread(()->{
                     demo.writHandle();
                 },"Thread_3").start();
         
                 new Thread(()->{
                     demo.writHandle();
                 },"Thread_4").start();                   
   ```    
  
* 单例：
    1. 枚举型
    2. 静态内部类: 
        1. 只有调用getInstance() 时才会创建单例
        2. 第一次调用静态字段时，触发类加载器（同一个类只加载一次），静态内部类同理；
        3. 利用类加载器创建对象时，jvm会加锁，同步多个线程对同一个类的初始化，进而保证单例对象的唯一性（由类加载器负责加锁，保证线程安全性）
    
    ```
        public class SingletonDemo {
        
           private static class SingletonHolder{
                private static final SingletonDemo singletonDemo = new SingletonDemo();
            }
        
            private SingletonDemo() {
            }
        
            public static final SingletonDemo getInstance() {
                return SingletonHolder.singletonDemo;
            }
        }
    ```    
    
* synchronized:
    
     1. 重入性：获取对象锁后再次请求仍会得到锁。【一个对象一把锁】
     2. 继承性：子类synchronized方法中调用父类的synchronized方法，仍会执行。
     3. 5个线程同时操作synchronized方法：
        1. 若new 出操作对象后，5个线程共用，则共享变量无需设置成static，结果仍正确
        2. 若每个线程都是自己new 的操作对象，各自都持有对象锁，此时共享变量必须设置为static，才能结果正确