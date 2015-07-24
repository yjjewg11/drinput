package com.px.news.doorrecord;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.company.common.SerializableUtil;
import com.company.news.ProjectProperties;
import com.company.news.SystemConstants;
import com.company.news.entity.DoorRecord;
import com.company.news.jsonform.DoorRecordJsonform;
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

}
