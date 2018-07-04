package cyf.gradle.base.enums;

import lombok.Getter;

@Getter
public enum PraiseEnum {

    TOPIC(1,"话题"),
    CHANNEL(2,"频道");

    private Integer code;
    private String name;

    PraiseEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

   public static PraiseEnum get(Integer code) {
        PraiseEnum[] values = PraiseEnum.values();
        for (PraiseEnum value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
