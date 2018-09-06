# batch

####  名词解释：

* ItemReader：读操作抽象接口, 填充对象给ItemProcessor使用
* ItemProcessor：处理逻辑抽象接口，返回对象给ItemWriter使用
* ItemWriter：写操作抽象接口；
* Step：组成一个Job的各个步骤；
* Job：可被多次执行的任务，每次执行返回一个JobInstance,Batch操作的基础执行单元。
* Job Launcher：启动Job
* Tasklet(小任务): Step的一个事务过程，包括重复执行，同步、异步策略
* chunk：给定数量的Item集合，达到该值时进行一次提交处理
* 一个job可以包含0到多个step;一个step可以包含0到多个tasklet;一个tasklet可以包含0到多个chunk

---

1. 组装Step异常跳过：

    通过调用FaultTolerantStepBuilder的skip来指定跳过的异常，因为在ItemProcessor中抛出任意异常都会将Job状态变为Failed，
    例如我们代码中指定了MoneyNotEnoughException，那么如果抛出该异常，任务会跳过该数据继续执行。

2. 组装Job条件决策：

      我们可以实现自己的决策逻辑——实现JobExecutionDecider接口，通过判断程序是正常结束的且跳过了非零条数据（说明有余额不足），
      返回自定义的FlowExecutionStatus，在配置任务是通过on(String)来进行条件决策是执行下一个Step还是终结Job。
    
    ```
    .next((jobExecution, stepExecution) -> {
    
    if (stepExecution.getExitStatus().equals(ExitStatus.COMPLETED) &&
    stepExecution.getSkipCount() > 0) {
         return new FlowExecutionStatus("NOTICE USER");
    } else {
            return new FlowExecutionStatus(stepExecution.getExitStatus().toString());
         }
    })
    .on("COMPLETED").end()
    .on("NOTICE USER").to(noticeStep)

    ```

3. 监听器：
        
      可以给Job、Step指定相应的监听器，来记录日志信息或其他用途，本例通过给Job添加JobExecutionListener，来记录整个任务的执行时间。
      
---

一个健壮的Job通常需要具备如下的几个特性：
1.  容错性:

    在Job执行期间非致命的异常，Job执行框架应能够进行有效的容错处理，而不是让整个Job执行失败；通常只有致命的、导致业务不正确的异常才可以终止Job的执行。

2. 可追踪性:

    Job执行期间任何发生错误的地方都需要进行有效的记录，方便后期对错误点进行有效的处理。例如在Job执行期间任何被忽略处理的记录行需要被有效的记录下来，应用程序维护人员可以针对被忽略的记录后续做有效的处理。

3. 可重启性:

    Job执行期间如果因为异常导致失败，应该能够在失败的点重新启动Job；而不是从头开始重新执行Job。
        【
            skip：跳过异常，让Processor能够顺利的处理其余的记录行。
            retry： 重试
            restart： 在最后执行失败的地方重启job，而不是从头开始执行，提高Job执行效率。
        】
        
---

框架扩展模式：

* Multithreaded Step 多线程执行一个Step;
* Parallel Step 通过多线程并行执行多个Step;
* Remote Chunking 在远端节点上执行分布式Chunk操作;
* Partitioning Step 对数据进行分区，并分开执行;