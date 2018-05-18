import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Cheng Yufei
 * @create 2018-05-03 16:51
 **/
@Getter
@Setter
@ToString
public class LikeDto {

    /**
     * club_like表中id
     */
    private Integer id;

    private Integer uid;

    private String avatar;

    private String nickname;

    /**
     * 是否vip： 0 否 1 是
     */
    private Integer vipLevel;

    /**
     * 是否认证：0：否 1 是
     */
    private Integer authPhoto;

    /**
     * 是否查看过此人的个人主页（喜欢我的人列表中红点显示） 0：未 1 已查看
     */
    private Integer whetherCheck;
    /**
     * 喜欢我的人列表状态： 1 已pass 2 有招呼
     */
    private Integer status;

    private Integer age;

    private String province;

    /**
     * 匹配度
     */
    private String matchingRate;


}
