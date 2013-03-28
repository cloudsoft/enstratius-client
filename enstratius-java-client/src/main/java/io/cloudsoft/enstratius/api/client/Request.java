package io.cloudsoft.enstratius.api.client;

import io.cloudsoft.enstratius.api.HttpMethod;
import io.cloudsoft.enstratius.api.mime.TypedOutput;
import org.apache.http.Header;
import org.apache.http.NameValuePair;

import java.util.List;

public interface Request {

   String getURI();

   HttpMethod getRestMethodName();

   String getPathToResult();

   List<NameValuePair> getQueryParameters();

   TypedOutput getBody();

   Header[] getHeaders();
}