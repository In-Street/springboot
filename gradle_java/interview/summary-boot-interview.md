###RequestInputStream多次读取配置

1. 获取请求url上的参数可以多次获取:
    
      ```
        
        Map<String, String[]> parameterMap = request.getParameterMap();
                parameterMap.forEach((k, v) -> {
                    System.out.println(k);
                    Stream.of(v).forEach(v2 -> System.out.println(v2));
                });
   ```
   
2. 获取body参数: InputStream 只能使用一次

      ```
   
         InputStream inputStream = request.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String s;
                while((s = bufferedReader.readLine()) != null) {
                    System.out.println(s);
                }
   ```   
   
3. 自定义使 InputStream 能多次读取使用 [eg: MyRequestInputStream \ FilterConfig]

    ```
       
         1.继承 HttpServletRequestWrapper , 重写 getInputStream
   
         2. MyRequestInputStream 定义全局变量 byte[] bytes, 构造方法中传入HttpServletRequest将 request.getInputStream 通过 ByteArrayOutputStream 转化为 bytes
   
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                
                        byte[] by = new byte[4096];
                
                        int length;
                        while ((length = inputStream.read(by)) != -1) {
                            outputStream.write(by, 0, length);
                        }
                        bytes = outputStream.toByteArray();
   
        
         3. 重写的getInputStream 的 read 方法中 读入 bytes 返回 Stream
   
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
   
               byteArrayInputStream.read()
   
            
   ```  
   
4. 过滤器中将 request 传入MyRequestInputStream 构造方法. 

###controller 统一返回类型处理 [eg: ReturnConfig]

```

    1. @RestControllerAdvice

    2.  实现 ReponseBodyAdvice , beforeBodyWrite 方法中对 将body 组装到自己的返回类中.

```
  
  
### 重试机制

1. spring-retry
```
    1. jar: spring-retry

    2. 方法注解:
        @Retryable(value = {RuntimeException.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))

        value: 只针对特定异常进行重试
        maxAttempts：最大重试次数
        backoff: 指定重试间隔（delay），重试延迟倍数(multiplier)

    3. @Recover : 方法注解,用于达到最大重试次数后处理逻辑

```  

2. guava-retry

```

    1.jar: guava-retrying

    2. 
        Retryer<Object> retryer = RetryerBuilder.newBuilder()
                       //根据异常重试
                       //.retryIfExceptionOfType(RuntimeException.class)
                       //根据返回结果时候符合要求进行重试
                       .retryIfResult(j -> {
                                   String reMsg = ((JSONObject) j).getString("reMsg");
                                   log.info("reMsg:{}", reMsg);
                                   return !reMsg.equals("success");
                               }
                       )
                       //间隔3秒
                       .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                       //重试4次停止
                       .withStopStrategy(StopStrategies.stopAfterAttempt(4))
                       .build();

                  JSONObject call = (JSONObject) retryer.call(() -> retryTest2());
                   return call;
```