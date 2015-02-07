package com.edu.kygroup.net;

import java.util.ArrayList;

import android.os.AsyncTask;

import com.edu.kygroup.domin.ChatBean;
import com.edu.kygroup.domin.ColleageList;
import com.edu.kygroup.domin.DirectContent;
import com.edu.kygroup.domin.DizcuzList;
import com.edu.kygroup.domin.MessageBean;
import com.edu.kygroup.domin.Register;
import com.edu.kygroup.domin.TiaoJiYuanXiao;
import com.edu.kygroup.iface.IBindData;
import com.edu.kygroup.utils.TagUtils;

public class NetworkTask extends AsyncTask<Object, Integer, Object> {
	private int mTag;
	private IBindData mBindData;
	private GetFinish mFinish;

	public void setmFinish(GetFinish mFinish) {
		this.mFinish = mFinish;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		mBindData = (IBindData) params[0];
		mTag = (Integer) params[1];
		String url = (String) params[2];

		switch (mTag) {
		case TagUtils.REGISTER_TAG:
			Register reg = (Register) params[3];
			LoginParse.getInstance().parseRegister(url, reg);
			break;
		case TagUtils.GET_COLLEAGE_TAG:
			return UniversityParse.getInstance().parseColleage(url);
		case TagUtils.GET_MAJOR_TAG:
			return UniversityParse.getInstance().parseMajor(url);
		case TagUtils.REPORT_PERSONAL_MSG:
			return LoginParse.getInstance().submitMsg(url);
		case TagUtils.GET_BROWSER_USER_TAG: {
			return UserMsgParse.getInstance().getFriendsUser(url, 0);
		}
		case TagUtils.GET_POST_GRADUATE_TAG: {
			return UserMsgParse.getInstance().getFriendsUser(url, 1);
		}
		case TagUtils.GET_FELLOW_TAG: {
			return UserMsgParse.getInstance().getFriendsUser(url, 2);
		}
		case TagUtils.GET_FRIENDS_USER_TAG: {
			return UserMsgParse.getInstance().getFriendsUser(url, 3);
		}
		case TagUtils.GET_MATES_TAG: {
			return UserMsgParse.getInstance().getFriendsUser(url, 4);
		}
		case TagUtils.GET_DETAIL_POST: {
			return UserMsgParse.getInstance().getFriendsUser(url, 5);
		}
		case TagUtils.BLACKLIST_TAG: {
			return UserMsgParse.getInstance().getFriendsUser(url, 6);
		}
		case TagUtils.GET_SAME_MAJOR_TAG: {
			return UserMsgParse.getInstance().getFriendsUser(url, 7);
		}
		case TagUtils.GET_SAME_COLLEAGE_TAG: {
			return UserMsgParse.getInstance().getFriendsUser(url, 8);
		}
		case TagUtils.GET_GRADUATE_TAG: {
			return UserMsgParse.getInstance().getFriendsUser(url, 9);
		}
		case TagUtils.GET_DETAIL_MAJOR: {
			return UserMsgParse.getInstance().getFriendsUser(url, 10);
		}
		case TagUtils.GET_DETAIL_COLLEAGE: {
			return UserMsgParse.getInstance().getFriendsUser(url, 11);
		}
		case TagUtils.GET_DETAIL_POST_GRADUATE: {
			return UserMsgParse.getInstance().getFriendsUser(url, 12);
		}
		case TagUtils.GET_DETAIL: {
			return UserMsgParse.getInstance().getUser(url);
		}
		case TagUtils.GET_MESSAGE_LIST: {
			ArrayList<MessageBean> messageList = (ArrayList<MessageBean>) params[3];
			return MessageParse.getInstance().getMessage(url, messageList, 1);
		}
		case TagUtils.GET_NEW_CHAT_LIST:
		case TagUtils.GET_CHAT_LIST: {
			ArrayList<ChatBean> chatList = (ArrayList<ChatBean>) params[3];
			return ChatBeanParse.getInstance().getChatList(url, chatList);
		}
		case TagUtils.LOGIN_TAG: {
			return LoginParse.getInstance().login(url);
		}
		case TagUtils.REPORT_ADD_FRIENDS: {
			return LoginParse.getInstance().submitMsg(url);
		}
		case TagUtils.MODIFY_PASSWORD: {
			return LoginParse.getInstance().submitMsg(url);
		}
		case TagUtils.LOSE_PASSWORD: {
			return LoginParse.getInstance().losePassword(url);
		}
		case TagUtils.REPORT_REMOVE_FRIENDS: {
			return LoginParse.getInstance().submitMsg(url);
		}
		case TagUtils.POST_MESSAGE: {
			ArrayList<ChatBean> chatList = (ArrayList<ChatBean>) params[3];
			return ChatBeanParse.getInstance().getChatList(url, chatList);
		}
		case TagUtils.CHANGE_AIM_TAG: {
			return EditParse.getInstance().alterBaokaoAim(url);
		}
		case TagUtils.UPDATE_MSG_TAG: {
			return EditParse.getInstance().alterPersonalMsg(url);
		}
		case TagUtils.UPDATE_LOCATION: {
			return LoginParse.getInstance().submitMsg(url);
		}
		case TagUtils.POST_FEEDBACK: {
			return LoginParse.getInstance().submitMsg(url);
		}
		case TagUtils.ADDTO_BLACKLIST_TAG: {
			return LoginParse.getInstance().submitMsg(url);
		}
		case TagUtils.DETAILS_TAG: {
			return MessageParse.getInstance().getDetailsMsg(url);
		}
		case TagUtils.GET_CONFIRM: {
			return MessageParse.getInstance().getConfirmMsg(url);
		}
		case TagUtils.CANCEL_BLACK: {
			return LoginParse.getInstance().submitMsg(url);
		}
		case TagUtils.UPGRADE_TAG: {
			return MessageParse.getInstance().getUpgrade(url);
		}
		case TagUtils.TOPIC_RET_TAG: {
			return MessageParse.getInstance().getTopicRet(url);
		}
		case TagUtils.GET_POSTER_TAG: {
			return MessageParse.getInstance().getPosters(url);
		}
		case TagUtils.GET_RESPOSTER_TAG: {
			return MessageParse.getInstance().getResPoster(url);
		}
		case TagUtils.RESPOSTER_TAG: {
			return MessageParse.getInstance().getResRet(url);
		}
		case TagUtils.GET_MY_POSTER_TAG: {
			return MessageParse.getInstance().getPosters(url);
		}
		case TagUtils.GET_ANNOUNCE_TAG: {
			return MessageParse.getInstance().getAnnounce(url);
		}
		case TagUtils.GET_CONCERN: {
			return MessageParse.getInstance().getConcern(url);
		}
		case TagUtils.ADD_CONCERN: {

			return MessageParse.getInstance().addConcern(url);
		}
		case TagUtils.DEL_CONCERN: {
			return MessageParse.getInstance().delConcern(url);
		}
		case TagUtils.GET_VERIFY_CODE:
			Register re = new Register();
			return re.getVerifyCode(url);
		case TagUtils.CHECK_CODE:
			Register rec = new Register();
			return rec.checkCode(url);
		case TagUtils.GET_SCHOOL_LIST_BY_MAJOR:
			ColleageList list = new ColleageList();
			return list.refush(url);
		case TagUtils.DIZCUZ:
			DizcuzList lists = new DizcuzList();
			lists.setUrl(url);
			return lists.refush();
		case TagUtils.GETDIRECTIONDETAIL:
			DirectContent direct = new DirectContent();
			direct.refush(url);
			return direct;
		case TagUtils.TIAOJIYUANXIAO:
			TiaoJiYuanXiao tiJiYuanXiao = new TiaoJiYuanXiao();
			return tiJiYuanXiao.refush(url);
		default:
			break;
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		if (null != mBindData) {
			mBindData.bindData(mTag, result);
		}
		if (mFinish != null) {
			mFinish.finish(result);
		}
		super.onPostExecute(result);
	}

	public interface GetFinish {
		void finish(Object result);
	}
}
