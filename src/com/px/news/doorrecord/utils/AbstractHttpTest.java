package com.px.news.doorrecord.utils;

import junit.framework.TestCase;

import com.meterware.httpunit.HttpNotFoundException;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

public class AbstractHttpTest extends TestCase{
  
  public WebResponse tryGetResponse(WebConversation conversation,WebRequest request) throws Exception {
    WebResponse response=null;
    try {
        response = conversation.getResponse( request );
    } catch (HttpNotFoundException nfe) {
        System.err.println("The URL Not found:Method='"+request.getMethod()+"',URL='"+request.getURL()+"' is not active any more");
        throw nfe;
    }
    return response;
}

}
