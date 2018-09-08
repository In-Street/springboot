package cyf.gradle.newredis.module;

import lombok.*;

import java.io.Serializable;

/**
 * @author Cheng Yufei
 * @create 2017-10-30 17:04
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User implements Serializable {


    private static final long serialVersionUID = 7503371984380217250L;

    private Integer id;

    private String username;

    private String pwd;


}
