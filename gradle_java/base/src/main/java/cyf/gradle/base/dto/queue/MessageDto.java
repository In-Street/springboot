package cyf.gradle.base.dto.queue;

import lombok.Data;

/**
 * @author Cheng Yufei
 * @create 2017-12-31 下午7:50
 **/
@Data
public class MessageDto {

    private Integer primaryKey;

    private Type type;

   public enum Type {

        TOPIC,
        AUDIT_COMMENT;

    }
}
