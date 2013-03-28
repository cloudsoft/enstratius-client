package io.cloudsoft.enstratius.api.client;

import java.io.IOException;

public interface Client {

   Response execute(Request request) throws IOException;

}
