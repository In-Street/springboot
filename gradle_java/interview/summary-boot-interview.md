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