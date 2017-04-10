package com.jacoli.roadsitesupervision.EasyRequest;

import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Response;

/**
 * Created by lichuange on 17/4/3.
 */

public interface Processor {
    Response buildRequestAndWaitingResponse() throws IOException;
    ResponseBase jsonModelParsedFromResponseString(String responseJsonString, Gson gson);
    void onSuccessHandleBeforeNotify(ResponseBase responseModel);
}
