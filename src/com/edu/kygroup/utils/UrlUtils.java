package com.edu.kygroup.utils;

public class UrlUtils {
	// http://www.yifulou.cn:8080/exam/detail?user.email=619123696@qq.com
	// public final static String BASE_URL = "http://192.168.0.129:8080";
	// private final static String BASE_URL = "http://116.255.238.45:20178";
	// public final static String BASE_URL = "http://192.168.1.115:8080";
	public final static String BASE_URL = "http://www.yifulou.cn:8180";
	public final static String REGISTER_URL = BASE_URL
			+ "/exam/register.action?";
	public final static String GET_COLLEAGE_URL = BASE_URL
			+ "/exam/colleges.action?";
	public final static String GET_MAJOR_URL = BASE_URL
			+ "/exam/majors.action?";
	public final static String ADD_AIM_URL = BASE_URL + "/exam/addaim.action?";
	public final static String UPLOAD_IMG_URL = BASE_URL
			+ "/exam/upload.action";// 添加头像
	public final static String PERSONAL_MSG_URL = BASE_URL
			+ "/exam/addpersoninfo.action?";
	public final static String GET_BROWSER_URL = BASE_URL
			+ "/exam/getsome.action?";// 获取浏览数据
	public final static String GET_POST_GRADUATES_URL = BASE_URL
			+ "/exam/sameaim.action?";// 获取研友

	public final static String ADD_FRIENDs_URL = BASE_URL
			+ "/exam/addfriends.action?";// 添加好友
	public final static String REMOVE_FRIENDS_URL = BASE_URL
			+ "/exam/delfriends.action?"; // 移除好友
	public final static String GET_FRIENDS_USER = BASE_URL
			+ "/exam/getfriends.action?"; // 获取好友
	public final static String LOGIN_URL = BASE_URL + "/exam/login.action?";// 登录
	public final static String MODIFY_PASSWORD_URL = BASE_URL
			+ "/exam/changepass.action?";// 修改稿密码
	public final static String LOSE_PASSWORD_URL = BASE_URL
			+ "/exam/getbackpass.action?";// 找回密码
	// public final static String GET_MESSAGE_LIST = BASE_URL
	// + "/exam/getreclist?"; // 获取私信列表
	public final static String GET_CHAT_LIST = BASE_URL
			+ "/exam/getmessages.action?"; // 获取某好友的聊天记录
	public final static String POST_MESSAGE = BASE_URL
			+ "/exam/sendmsg.action?"; // 发送私信
	public final static String CHANGE_AIM_URL = BASE_URL
			+ "/exam/changeaim.action?";// 更改目标

	public final static String UPDATE_MSG_URL = BASE_URL
			+ "/exam/updateinfo.action?";// 更个人信息

	public final static String GET_FELLOW_URL = BASE_URL
			+ "/exam/fellowtown.action?";

	public final static String GET_MATES_URL = BASE_URL
			+ "/exam/fromsame.action?";// 获取校友

	public final static String UPDATE_LOCATION = BASE_URL
			+ "/exam/updatelocaction.action?"; // 上送本地地址到服务器

	public final static String POST_FEEDBACK = BASE_URL
			+ "/exam/addcomments.action?"; // 建议反馈

	public final static String ADDTO_BLACKLIST_URL = BASE_URL
			+ "/exam/addtoblacklist.action?";// 添加黑名单

	public final static String DETAILS_URL = BASE_URL
			+ "/exam/getmajordetail.action?";// 专业详情

	public final static String BLACKLIST_URL = BASE_URL
			+ "/exam/getblacklist.action?";// 获取黑名单

	public final static String AUTH_PIC_URL = BASE_URL + "/exam/confirm.action";// 认证图片

	public final static String GET_CONFIRM = BASE_URL
			+ "/exam/confirmfinish.action";// 获取认证结果
	public final static String DIZCUZ = BASE_URL + "/exam/dizcuz.action?";// 帖子列表
	public final static String CANCEL_BLACK = BASE_URL
			+ "/exam/deletefromblacklist.action";// 解除黑名单关系

	public final static String GET_UPGRADE = BASE_URL + "/exam/upgrade.action?";

	public final static String ADD_POSTER = BASE_URL + "/exam/addtopic.action?";// 发帖

	public final static String GET_POSTER = BASE_URL
			+ "/exam/gettopics.action?";// 查看帖子

	public final static String GET_POSTER_RESPONSE = BASE_URL
			+ "/exam/getresponses.action?";// 查看帖子回复

	public final static String RES_POSTER = BASE_URL
			+ "/exam/addresponse.action?";// 回复帖子

	public final static String GET_MY_POSTER = BASE_URL
			+ "/exam/getmytopics.action?";// 查看自己帖子

	public final static String GET_ANNOUNCES = BASE_URL
			+ "/exam/getannounces.action?";// 查看公告

	public final static String GET_DETAIL = BASE_URL + "/exam/detail.action?";// 查看个人信息

	public final static String ADD_CONCERN = BASE_URL
			+ "/exam/addconcern.action?";// 添加关注

	public final static String GET_CONCERN = BASE_URL
			+ "/exam/getconcerns.action?";// 获取关注

	public final static String DEL_CONCERN = BASE_URL
			+ "/exam/delconcern.action?";// 删除关注
	public final static String GET_VERIFY_CODE = BASE_URL
			+ "/exam/check.action?";// 注册时获取验证码
	public final static String CHECK_CODE = BASE_URL
			+ "/exam/checkValid.action?";// 验证验证码
	public final static String GET_SCHOOL_LIST_BY_MAJOR = BASE_URL
			+ "/exam/classify.action?";

	public final static String GETDIRECTIONDETAIL = BASE_URL
			+ "/exam/getdirectiondetail.action?";// 获取方向详情
	public final static String TIAOJIYUANXIAO = BASE_URL
			+ "/exam/tiaojiinfos.action?";//
	// 调剂院校
	public final static String TIAOJI_DIZCUS = BASE_URL
			+ "/exam/tiaojiDiscuz.action?";//
	// 发布任务
	public final static String ADD_TASK = BASE_URL + "/exam/addtask.action?";
	public final static String GET_TASK_LIST = BASE_URL
			+ "/exam/gettasklist.action?";
	public final static String ADD_TASK_COMMENT = BASE_URL
			+ "/exam/addtaskcomment.action?";
	public final static String GET_TASK_COMMENT_LIST = BASE_URL
			+ "/exam/gettaskcommentlist.action?";
	public final static String GET_TIP_COMMENT_LIST = BASE_URL
			+ "/exam/gettipcommentlist.action?";
	public final static String GET_USER_INFO = BASE_URL
			+ "/exam/getUserInfo.action?";
	public final static String GET_MY_TASK = BASE_URL
			+ "/exam/getMyTask.action?";
	public final static String DEL_TASK = BASE_URL + "/exam/delTask.action?";
	public final static String EDIT_TASK = BASE_URL + "/exam/editTask.action?";
	public final static String ADD_CHENGJI = BASE_URL
			+ "/exam/addChengJi.action?";
	public final static String GET_CHENGJI = BASE_URL
			+ "/exam/getChengji.action?";
}
