/**
 * 工程名: KyGroup
 * 文件名: MessageView.java
 * 包名: com.edu.kygroup.ui
 * 日期: 2013-11-30上午9:29:35
 * Copyright (c) 2013, 108room All Rights Reserved.
 *
 */

package com.edu.kygroup.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.edu.kygroup.R;
import com.edu.kygroup.adapter.ChatAllHistoryAdapter;
import com.edu.kygroup.adapter.MessageAdapter;
import com.edu.kygroup.domin.MessageBean;

/**
 * 类名: MessageView <br/>
 * 功能: TODO 私信页面. <br/>
 * 日期: 2013-11-30 上午9:29:35 <br/>
 * 
 * @author lx
 * @version
 */
public class MessageView implements OnClickListener {

	private static final String TAG = "MessageView";
	public static int MESSAGE_TOTAL = 0;

	private Context mContext;

	private View mView;

	private ListView listView;

	private ArrayList<MessageBean> messageList;

	private MessageAdapter messageAdapter;
	private LinearLayout mLoadMore;

	private Dialog mDialog;
	private View mDelView;
	private TextView mDelTitle;
	private TextView mDelTextView;
	private ChatAllHistoryAdapter adapter;
	private List<EMGroup> groups;
	private HomeActivity mActivity;

	public MessageView(Context context, HomeActivity activcity) {
		mView = LayoutInflater.from(context).inflate(R.layout.message_view,
				null);
		mContext = context;
		mActivity = activcity;
		initView();
	}

	public View getView() {
		((BaseActivity) mContext).setTitleBarVisibility(View.VISIBLE);
		// initData();
		return mView;
	}

	public void initView() {
		mLoadMore = (LinearLayout) mView
				.findViewById(R.id.search_loadmore_layout);
		listView = (ListView) mView.findViewById(R.id.list);
		adapter = new ChatAllHistoryAdapter(mContext, 1,
				loadConversationsWithRecentChat());
		groups = EMGroupManager.getInstance().getAllGroups();
		// 注册上下文菜单
		mActivity.registerForContextMenu(listView);
		// 设置adapter
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EMConversation conversation = adapter.getItem(position);
				String username = conversation.getUserName();

				// 进入聊天页面
				Intent intent = new Intent(mContext, ChatActivity1.class);
				EMContact emContact = null;
				groups = EMGroupManager.getInstance().getAllGroups();
				for (EMGroup group : groups) {
					if (group.getGroupId().equals(username)) {
						emContact = group;
						break;
					}
				}
				if (emContact != null && emContact instanceof EMGroup) {
					// it is group chat
					intent.putExtra("chatType", ChatActivity1.CHATTYPE_GROUP);
					intent.putExtra("groupId",
							((EMGroup) emContact).getGroupId());
				} else {
					// it is single chat
					intent.putExtra("toChatUsername", username);
				}
				TextView name = (TextView) view.findViewById(R.id.name);
				intent.putExtra("user_name", name.getText().toString());

				mContext.startActivity(intent);
			}

		});
	}

	/**
	 * 刷新页面
	 */
	public void refresh() {
		adapter = new ChatAllHistoryAdapter(mContext,
				R.layout.row_chat_history, loadConversationsWithRecentChat());
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public void delMessage(MenuItem item) {
		EMConversation tobeDeleteCons = adapter
				.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
		// 删除此会话
		EMChatManager.getInstance().deleteConversation(
				tobeDeleteCons.getUserName(), tobeDeleteCons.isGroup());
		adapter.remove(tobeDeleteCons);
		adapter.notifyDataSetChanged();
	}

	/**
	 * 获取所有会话
	 * 
	 * @param context
	 * @return
	 */
	private List<EMConversation> loadConversationsWithRecentChat() {
		// 获取所有会话，包括陌生人
		Hashtable<String, EMConversation> conversations = EMChatManager
				.getInstance().getAllConversations();
		List<EMConversation> conversationList = new ArrayList<EMConversation>();
		// 过滤掉messages seize为0的conversation
		for (EMConversation conversation : conversations.values()) {
			if (conversation.getAllMessages().size() != 0)
				conversationList.add(conversation);
		}
		// 排序
		sortConversationByLastChatTime(conversationList);
		return conversationList;
	}

	/**
	 * 根据最后一条消息的时间排序
	 * 
	 * @param usernames
	 */
	private void sortConversationByLastChatTime(
			List<EMConversation> conversationList) {
		Collections.sort(conversationList, new Comparator<EMConversation>() {
			@Override
			public int compare(final EMConversation con1,
					final EMConversation con2) {

				EMMessage con2LastMessage = con2.getLastMessage();
				EMMessage con1LastMessage = con1.getLastMessage();
				if (con2LastMessage.getMsgTime() == con1LastMessage
						.getMsgTime()) {
					return 0;
				} else if (con2LastMessage.getMsgTime() > con1LastMessage
						.getMsgTime()) {
					return 1;
				} else {
					return -1;
				}
			}

		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.delete_view:
			break;
		default:
			break;
		}
	}

}
