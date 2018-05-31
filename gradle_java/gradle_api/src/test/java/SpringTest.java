import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Cheng Yufei
 * @create 2018-05-31 下午9:29
 **/

public class SpringTest {

    /**
     * beanfactory
     */
    @Test
    public void spring() {
        ClassPathResource resource = new ClassPathResource("bean.xml");
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(resource);
        beanFactory.getBean("");



    }
}
