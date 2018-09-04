package cyf.gradle.dao.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
@Getter
@Setter
public class ClubUserDailyStat implements Serializable {
    private Integer id;

    private Integer userId;

    /**
     * 0：正常用户 1：马甲号
     */
    private Integer userType;

    private String date;

    /**
     * 本人被推荐展示次数
     */
    private Integer recommendNum;

    /**
     * 被喜欢测试
     */
    private Integer beLikedNum;

    /**
     * 喜欢次数
     */
    private Integer likeNum;

    /**
     * 不喜欢次数
     */
    private Integer dislikeNum;

    /**
     * 经纬度 39.12345,116.236
     */
    private String location;

    /**
     * 活跃时间
     */
    private Date activeTime;

    private Date createTime;

    private Date modifyTime;

    private static final long serialVersionUID = 1L;

}