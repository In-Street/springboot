package cyf.gradle.trans.enums;

import lombok.Getter;

@Getter
public enum IndexType {


    kerr_2("miranda","kerr_2"),

    ;

    private String index;

    private String type;

    IndexType(String index, String type) {
        this.index = index;
        this.type = type;
    }

    public IndexType get(String type) {

        if (null != type) {
            IndexType[] values = IndexType.values();
            for (IndexType value : values) {
                if (type.equals(value.getType())) {
                    return value;
                }
            }
        }
        return null;
    }
}



