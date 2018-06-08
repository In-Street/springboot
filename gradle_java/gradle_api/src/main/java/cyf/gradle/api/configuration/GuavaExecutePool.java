package cyf.gradle.api.configuration;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.Executors;

/**
 * @author Cheng Yufei
 * @create 2018-06-08 10:58
 **/
public class GuavaExecutePool   {

   public void get() {
       MoreExecutors.listeningDecorator()
   }
}
