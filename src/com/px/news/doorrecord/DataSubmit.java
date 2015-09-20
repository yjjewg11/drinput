package com.px.news.doorrecord;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.log4j.Logger;

import com.company.common.PxStringUtils;
import com.company.common.SerializableUtil;
import com.company.news.ProjectProperties;
import com.company.news.SystemConstants;
import com.company.news.entity.DoorRecord;
import com.company.news.jsonform.DoorRecordJsonform;
import com.company.news.jsonform.DoorUserJsonform;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebResponse;
import com.px.news.doorrecord.utils.AbstractHttpTest;
import com.px.news.doorrecord.utils.HttpUtils;
import com.px.news.doorrecord.utils.JSONUtils;

/**
 * ����ύ
 * @author Administrator
 *
 */
public class DataSubmit extends AbstractHttpTest {
	private static final Logger log = Logger.getLogger(DataSubmit.class);
	/**
	 * 将获取的LIST提交到服务器，并返回是否成功
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	public boolean submitList(List<DoorRecord> list) throws Exception{
		WebConversation conversation = new WebConversation();
		
		DoorRecordJsonform form = new DoorRecordJsonform();
		form.setGroupuuid(ProjectProperties.getProperty("groupuuid", ""));
		form.setPrivate_key(ProjectProperties.getProperty("private_key", ""));

		form.setRecordlist(SerializableUtil.ObjectToString(list));
		
		

		String json = JSONUtils.getJsonString(form);
		HttpUtils.printjson(json);
		ByteArrayInputStream input = new ByteArrayInputStream(
				json.getBytes(SystemConstants.Charset));
		PostMethodWebRequest request = new PostMethodWebRequest(
				ProjectProperties.getProperty("px_host", "localhost") + "rest/doorrecord/insert.json", input,
				Constants.contentType);

		WebResponse response = tryGetResponse(conversation, request);

		HttpUtils.println(conversation, request, response);
		if(response.getText().indexOf("success") != -1)
			return true;
		else
			return false;
	}
	
	/**
	 * 将获取的LIST提交到服务器，并返回是否成功
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	public boolean autobind(DoorUserJsonform user) throws Exception{
		if(user==null){
			return false;
		}
		if(PxStringUtils.isNullOrEmpty(user.getUserName()))
		{
			user.setUserName("未知");
			
		}
		
		if(PxStringUtils.isNullOrEmpty(user.getUserid()))
		{
			log.info("Userid() isnull,不需要同步");
			return true;			
		}
		
		WebConversation conversation = new WebConversation();
		
		DoorUserJsonform form =user;
		form.setGroupuuid(ProjectProperties.getProperty("groupuuid", ""));
		form.setPrivate_key(ProjectProperties.getProperty("private_key", ""));
		
		String json = JSONUtils.getJsonString(form);
		HttpUtils.printjson(json);
		ByteArrayInputStream input = new ByteArrayInputStream(
				json.getBytes(SystemConstants.Charset));
		PostMethodWebRequest request = new PostMethodWebRequest(
				ProjectProperties.getProperty("px_host", "localhost") + "rest/doorrecord/autobind.json", input,
				Constants.contentType);
		WebResponse response = tryGetResponse(conversation, request);

		HttpUtils.println(conversation, request, response);
		if(response.getText().indexOf("success") != -1)
			return true;
		else
			return false;
	}
	
	/**
	 * 将获取的LIST提交到服务器，并返回是否成功
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	public boolean heartbeat(String msg) throws Exception{
		
		
		WebConversation conversation = new WebConversation();
		
		GetMethodWebRequest request = new GetMethodWebRequest(
				ProjectProperties.getProperty("px_host", "localhost") + "rest/doorrecord/heartbeat.json");
		request.setParameter("groupuuid", ProjectProperties.getProperty("groupuuid", ""));
		request.setParameter("app_id", ProjectProperties.getProperty("app_id", "1"));
		request.setParameter("frequency", Constants.frequency+"");
		request.setParameter("msg", msg);
		WebResponse response = tryGetResponse(conversation, request);

		HttpUtils.println(conversation, request, response);
		if(response.getText().indexOf("success") != -1)
			return true;
		else{
			log.info("连接服务器失败，请检查网络");
			return false;
		}
	}

}
