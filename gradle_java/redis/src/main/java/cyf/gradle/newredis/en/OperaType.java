package cyf.gradle.newredis.en;

import lombok.Getter;

@Getter
public enum OperaType {


    online(1, "上线"),
    offline(2, "下线");


    private Integer code;

    private String name;

    OperaType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static OperaType get(Integer code) {
        if (null != code) {
            OperaType[] values = OperaType.values();
            for (OperaType value : values) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
        }
        return null;
    }
}
