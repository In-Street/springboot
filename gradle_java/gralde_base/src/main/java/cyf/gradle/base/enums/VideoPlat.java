package cyf.gradle.base.enums;

import lombok.Getter;

@Getter
public enum VideoPlat {
    OWN(1, "内部", "http://daishumovie.oss-cn-shanghai.aliyuncs.com/img/logo_black.png","daishu"),
	IQY(2, "爱奇艺", "http://daishumovie.oss-cn-shanghai.aliyuncs.com/img/aiqiyi%402x.png","iqiyi.com"),
	QQ(3 ,"腾讯视频", "http://daishumovie.oss-cn-shanghai.aliyuncs.com/img/tengxun%402x.png","qq.com"),
    YOU_KU(4, "优酷", "http://daishumovie.oss-cn-shanghai.aliyuncs.com/img/youku%402x.png","youku.com"),
    TU_DOU(5, "土豆", "http://daishumovie.oss-cn-shanghai.aliyuncs.com/img/tudou%402x.png","tudou.com"),
    LE(6, "乐视网", "http://daishumovie.oss-cn-shanghai.aliyuncs.com/img/leshi%402x.png","le.com"),
    SO_HU(7, "搜狐视频", "http://daishumovie.oss-cn-shanghai.aliyuncs.com/img/souhu%402x.png","sohu.com"),
    HEADLINE(8, "今日头条", "http://daishumovie.oss-cn-shanghai.aliyuncs.com/img/toutiao%402x.png","365yg.com|toutiao.com"),
    PGC(100, "用户上传", "http://daishumovie.oss-cn-shanghai.aliyuncs.com/img/%E4%B9%90%E8%A7%86.png","pgc");

    private Integer code;
    private String name;
    private String logo;
    private String website;
    VideoPlat(Integer code, String name, String logo, String website){
        this.code = code;
        this.name = name;
        this.logo = logo;
        this.website = website;
    }

    public static VideoPlat get(Integer code){
        VideoPlat[] values = VideoPlat.values();
        for (VideoPlat value : values) {
            if(value.getCode().equals(code)){
                return value;
            }
        }
        return null;
    }
    
    public static VideoPlat get(String website){
        VideoPlat[] values = VideoPlat.values();
        for (VideoPlat value : values) {
            if(value.getWebsite().equals(website)){
                return value;
            }
        }
        return null;
    }
}
