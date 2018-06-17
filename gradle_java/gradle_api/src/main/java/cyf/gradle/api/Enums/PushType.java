package cyf.gradle.api.Enums;

import lombok.Getter;

import java.util.Objects;

/**
 * @author Cheng Yufei
 * @create 2018-05-23 15:19
 **/
@Getter
public enum PushType {

    //1.喜欢我 2.新招呼 3.聊天 4.审核 5.匹配成功

    LIKE_ME(1, "喜欢我"),
    NEW_HELLO(2, "新招呼"),
    CHAT(3, "聊天"),
    AUDIT(4, "审核"),
    MATCH_SUCCESSFUL(5, "匹配成功"),

    ;
    private Integer code;
    private String value;

    PushType(Integer code, String value) {

        this.code = code;
        this.value = value;
    }

    public static PushType get(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        PushType[] enums = PushType.values();
        for (PushType anEnum : enums) {
            if (Objects.equals(anEnum.code, code)) {
                return anEnum;
            }
        }
        return null;
    }
}
