package com.yitu.cpcFounding.api.constant;

/**
 * Configs
 *
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
public class Configs {
    /**
     * 最大上传图片尺寸（kb）
     */
    public static final int MAX_UPLOAD_SIZE = 400;

    /**
     * 登录用户redis缓存key前缀
     */
    public static final String REDIS_LOGIN_USER_PREFIX = "cpcFounding:u";

    /**
     * 登录用户redis缓存用户、token关联
     */
    public static final String REDIS_LOGIN_USER_TOKEN_PREFIX = "cpcFounding:user_token:userId_";

    /**
     * 用户token有效期秒
     */
    public static final long USER_TOKEN_TIMEOUT_SEC = 86400; //1天
    /**
     * 摇一摇奖品列表redis缓存key前缀
     */
    public static final String REDIS_YAO_PRIZE_LIST = "cpcFounding:yao_prize_list";
    /**
     * 用户中奖礼品id redis缓存key前缀
     */
    public static final String REDIS_USER_WINNING_PRIZE_ID_ = "cpcFounding:user_winning_prize_id_";
    /**
     * 奖品未中奖概率redis缓存key前缀
     */
    public static final String REDIS_NO_WINNING_PROBABILITY = "cpcFounding:no_winning_probability";
    /**
     * 配置文件保存redis
     */
    public static final String CONFIG_TYPE = "cpcFounding:config:type_";
    public static final String CONFIG_TYPE_KEY = "cpcFounding:config:type_key_";
    public static final String CONFIG_ID = "cpcFounding:config:id_";

    public static final String SHOW_YEAR_THUMBNAIL = "90x90";

    /**
     * 用户今天中奖礼品id redis缓存key前缀
     */
    public static final String REDIS_USER_TODAY_WINNING_PRIZE_ID_ = "cpcFounding:user_today_winning_prize_id_";


    /**
     * 摇一摇奖品库存redis缓存key前缀
     */
    public static final String REDIS_PRIZE_STOCK_COUNT = "cpcFounding:prize_stock_count_";

    public static final String LS_SHOW_YEAR_ADD = "cpcFounding:ls_show_year_add:";

    /**
     * 短信验证码模板
     */
    public static final String SMS_MSG_TEMPLATE = "您的验证码为：%s，如非本人操作，请忽略此短信。";

    /**
     * 手机短信验证码当天最大发送次数
     */
    public static final int MAX_SMS_PHONE_COUNT = 5;

    /**
     * 手机短信验证码存活时间（秒）
     */
    public static final int MAX_SMS_CODE_TIME = 5 * 60;

    /**
     * 手机短信验证码redis缓存key前缀
     */
    public static final String REDIS_SMS_PHONE = "cpcFounding:SMS_phone_verifi_";

    /**
     * 点赞总数
     */
    public static final String REDIS_PRAISE_TOTAL = "cpcFounding:praise_total";
    /**
     * 参与用户
     */
    public static final String REDIS_USER_TOTAL = "cpcFounding:user_total";

    /**
     * 参与用户redis失效时间
     */
    public static final long REDIS_USER_TOTAL_TIMEOUT = 120;


    /**
     * 帖子点赞数
     * item:showYearId
     * data:praiseNum
     */
    public static final String REDIS_PRAISE_NUM = "cpcFounding:post_praise_num";

    /**
     * 帖子当天点赞详情
     * key:showYearId
     * object:userId,userId
     */
    public static final String REDIS_PRAISE_TODAY = "cpcFounding:praise_today:showYearId_";

    /**
     * 发帖用户表
     * item:showYearId
     * data:addUserId
     */
    public static final String REDIS_POST_USER = "cpcFounding:post_user";

    /**
     * 用户动态信息redis缓存key前缀
     */
    public static final String REDIS_USER_DYNAMIC_INFO = "cpcFounding:user_dynamic_info_";

    /**
     * 广告信息redis缓存
     */
    public static final String REDIS_LS_AD_TYPE = "cpcFounding:redis_ls_ad_type_";

    /**
     * 获奖用户redis缓存hkey前缀
     */
    public static final String REDIS_PRIZE_WINNER = "cpcFounding:prize_winner_";

    /**
     * 晒年味获奖用户redis缓存key前缀(pageIndex，pageSize)
     */
    public static final String REDIS_SHOW_YEAR_WINNER_KEY = "cpcFounding:PAGE_%s_SIZE_%s";

    /**
     * 获取小程序码access_token
     */
    public static final long REDIS_KEY_REG_WXCODE_CACHE = 3600;

    /**
     * 访问接口限制redis缓存key前缀
     */
    public static final String REDIS_ACCESS_API_LIMIT = "cpcFounding:access_";

    /**
     * 晒活动页面数据缓存
     */
    public static final String REDIS_ACTIVE_PAGE = "cpcFounding:active:page:index_%s_size_%s_order_%s_id_%s";

    /**
     * 晒活动页面数据缓存过期时间
     */
    public static final long REDIS_ACTIVE_PAGE_TIME_OUT = 60;

    /**
     * 晒活动页面数据缓存页数
     */
    public static final int REDIS_ACTIVE_PAGE_NUM = 10;

    /**
     * 答题数量
     */
    public static final int ANSWER_COUNT = 10;

    /**
     * 完成答题次数
     */
    public static final int FINISH_ANSWER_COUNT = 10;
    /**
     * 活动排名
     */
    public static final String REDIS_ACTIVE_RANK = "cpcFounding:active_rank";

    /**
     * 活动排名缓存过期时间
     */
    public static final int REDIS_ACTIVE_RANK_TIME_OUT = 300;

    /**
     * 团队总榜
     */
    public static final String REDIS_ACTIVE_TEAM_RANKS = "cpcFounding:active_team_ranks";

    /**
     * 片区总榜
     */
    public static final String REDIS_ACTIVE_AREA_RANK = "cpcFounding:active_area_rank";

    /**
     * 街道总榜
     */
    public static final String REDIS_ACTIVE_STREET_RANK = "cpcFounding:active_street_rank";

    /**
     * 团队周榜
     */
    public static final String REDIS_ACTIVE_TEAM_WEEK_RANK = "cpcFounding:active_team_week_rank";

    /**
     * 片区周榜
     */
    public static final String REDIS_ACTIVE_AREA_WEEK_RANK = "cpcFounding:active_area_week_rank";

    /**
     * 街道周榜
     */
    public static final String REDIS_ACTIVE_STREET_WEEK_RANK = "cpcFounding:active_street_week_rank";

    /**
     * 团队排名缓存过期时间
     */
    public static final int REDIS_ACTIVE_TEAM_RANK_TIME_OUT = 300;

    /**
     * 个人总榜
     */
    public static final String REDIS_ACTIVE_USER_RANK = "cpcFounding:active_user_rank";

    /**
     * 个人周榜
     */
    public static final String REDIS_ACTIVE_USER_WEEK_RANK = "cpcFounding:active_user_week_rank";

    /**
     * 答题总榜
     */
    public static final String REDIS_ACTIVE_ANSWER_RANK = "cpcFounding:active_answer_rank";

    /**
     * 用户摇奖次数redis缓存key前缀
     */
    public static final String REDIS_USER_SHAKE_COUNT = "cpcFounding:user_shake_count_";

    /**
     * 晒幸福个人作品排名前缀
     */
    public static final String REDIS_SELF_POST_RANK = "cpcFounding:self_post_rank_";

    /**
     * 答题总数redis前缀
     */
    public static final String REDIS_ANSWER_COUNT = "cpcFounding:answer_count_";

    /**
     * 答题用户头像redis前缀
     */
    public static final String REDIS_ANSWER_USER_HEADER = "cpcFounding:answer_user_header_";


    /**
     * 用户奖章使用次数
     */
    public static final String REDIS_USER_MEDAL_COUNT = "cpcFounding:user_medal";

    /**
     * 所有奖章总数
     */
    public static final String REDIS_USER_MEDAL_ALL = "cpcFounding:user_medal_all";

    /**
     * 用户摇一摇中奖次数redis缓存key前缀
     */
    public static final String REDIS_USER_PRIZE_COUNT = "cpcFounding:user_prize_count_";

    /**
     * 用户答题头像redis缓存key前缀
     */
    public static final String REDIS_USER_ANSWER_HEADER = "cpcFounding:user_answer_header";

    /**
     * 用户答题数量
     */
    public static final String REDIS_USER_ANSWER_COUNT = "cpcFounding:user_answer_count";

    /**
     * 用户阅读文章次数缓存
     *
     * @return
     * @author wangping
     * @date 2021/6/8 9:35
     */
    public static final String REDIS_READ_ARTIClE_SCORE_COUNT = "cpcFounding:read_article_score_";

    /**
     * 用户当日阅读文章缓存
     *
     * @return
     * @author wangping
     * @date 2021/6/8 9:35
     */
    public static final String REDIS_READ_ARTIClE_COUNT = "cpcFounding:read_article_";

    /**
     * access_token放进缓存的key
     */
    public static final String CACHE_KEY = "cpcFounding:wx_access_token";

    /**
     * 加入团队判断
     */
    public static final String TEAM = "cpcFounding:team:user_id_";

    /**
     * 用户随机答题答案
     */
    public static final String USER_ANSWER = "cpcFounding:answer_number:";

    /**
     * 用户批次随机答题答案
     */
    public static final long USER_ANSWER_TIMEOUT = 1800;

    /**
     * 用户批次正确数量
     */
    public static final String USER_ANSWER_RIGHT_COUNT = "cpcFounding:answer_number_right_count:";

    /**
     * 用户回答实体
     */
    public static final String USER_ANSWER_ENTITY = "cpcFounding:user_answer_entity:";

    /**
     * 缓存页数
     */
    public static final int pageIndexRedis = 10;

    /**
     * 排行分页过期时间
     */
    public static final long RANKING_PAGE_TIMEOUT = 300;

    /**
     * 用户总榜分页缓存数据
     */
    public static final String RANKING_PAGE_USER_TOTAL = "cpcFounding:ranking_page:user_total";

    /**
     * 用户周榜分页缓存数据
     */
    public static final String RANKING_PAGE_USER_WEEK = "cpcFounding:ranking_page:user_week";

    /**
     * 团队总榜分页缓存数据
     */
    public static final String RANKING_PAGE_TEAM_TOTAL = "cpcFounding:ranking_page:team_total";

    /**
     * 团队周榜分页缓存数据
     */
    public static final String RANKING_PAGE_TEAM_WEEK = "cpcFounding:ranking_page:team_week";

    /**
     * 街道总榜分页缓存数据
     */
    public static final String RANKING_PAGE_STREET_TOTAL = "cpcFounding:ranking_page:street_total";

    /**
     * 街道周榜分页缓存数据
     */
    public static final String RANKING_PAGE_STREET_WEEK = "cpcFounding:ranking_page:street_week";

    /**
     * 片区总榜分页缓存数据
     */
    public static final String RANKING_PAGE_AREA_TOTAL = "cpcFounding:ranking_page:area_total";

    /**
     * 片区周榜分页缓存数据
     */
    public static final String RANKING_PAGE_AREA_WEEK = "cpcFounding:ranking_page:area_week";

    /**
     * 答题总榜分页缓存数据
     */
    public static final String RANKING_PAGE_ANSWER_TOTAL = "cpcFounding:ranking_page:answer_total";

    /**
     * 个人总榜名次
     */
    public static final String REDIS_ACTIVE_USER_SORT = "cpcFounding:active_user_sort";

    /**
     * 个人周榜名次
     */
    public static final String REDIS_ACTIVE_USER_WEEK_SORT = "cpcFounding:active_user_week_sort";

    /**
     * 答题总榜名次
     */
    public static final String REDIS_ACTIVE_ANSWER_SORT = "cpcFounding:active_answer_sort";

    /**
     * 团队周榜名次
     */
    public static final String REDIS_ACTIVE_TEAM_WEEK_SORT = "cpcFounding:active_team_week_sort";

    /**
     * 街道周榜名次
     */
    public static final String REDIS_ACTIVE_STREET_WEEK_SORT = "cpcFounding:active_street_week_sort";

    /**
     * 片区周榜名次
     */
    public static final String REDIS_ACTIVE_AREA_WEEK_SORT = "cpcFounding:active_area_week_sort";

    /**
     * 街道总榜名次
     */
    public static final String REDIS_ACTIVE_STREET_SORT = "cpcFounding:active_street_sort";

    /**
     * 片区总榜名次
     */
    public static final String REDIS_ACTIVE_AREA_SORT= "cpcFounding:active_area_sort";

    /**
     * 团队总榜名次
     */
    public static final String REDIS_ACTIVE_TEAM_SORT = "cpcFounding:active_team_sort";
}
