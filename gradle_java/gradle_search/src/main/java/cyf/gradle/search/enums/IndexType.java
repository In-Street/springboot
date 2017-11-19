package cyf.gradle.search.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum IndexType {


    album("smallbronze","album"),
    user("smallbronze","user"),
    topic("smallbronze","topic"),
    ;

    private String index;

    private String type;

    IndexType(String index, String type) {
        this.index = index;
        this.type = type;
    }

    public static IndexType get(String type) {

        if (null != type) {
            for (IndexType status : IndexType.values()) {
                if (Objects.equals(type, status.getType())) {
                    return status;
                }
            }
        }
        return null;
    }
}
