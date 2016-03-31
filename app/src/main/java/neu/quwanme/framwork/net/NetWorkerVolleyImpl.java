package neu.quwanme.framwork.net;

import android.graphics.Bitmap;
import android.util.Config;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import java.util.Map;

import neu.quwanme.WanApplication;
import neu.quwanme.tools.CONFIG;
import neu.quwanme.tools.LogUtil;

/**
 * Created by Lonie233 on 2016/3/11.
 */
public class NetWorkerVolleyImpl {

    private static RequestQueue mrequestQueue;

    public NetWorkerVolleyImpl() {

            mrequestQueue = Volley.newRequestQueue(WanApplication.getAppContext());
    }

    public void get(final String url,final NetWorker.ICallback callback,final Object... params) {
        //  2016/3/11 加入log打印
        LogUtil.d("hzm","volley get "+url+" "+params.toString());
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                LogUtil.d(CONFIG.MYTAG,"volley get ok "+ res);
                //数据返回给调用类
                callback.onResponse(NetWorker.HTTP_OK,res);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.e(CONFIG.MYTAG,"volley get error "+volleyError.getMessage()+" "+volleyError.toString());
                if (volleyError.networkResponse != null) {
                    int index = url.lastIndexOf("http");
                    if (index > 7) {
                        String redirecturl = url.substring(index);
                        get(redirecturl,callback,params);
                        return;
                    }
                    callback.onResponse(volleyError.networkResponse.statusCode,volleyError.toString());
                }
                callback.onResponse(volleyError.networkResponse.statusCode,volleyError.toString());
            }
        }){
            // TODO: 2016/3/11 这两个函数还不知道是干嘛用的
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                return super.parseNetworkResponse(response);
            }
        };

       LogUtil.d("hzm", stringRequest.getBodyContentType());
        mrequestQueue.add(stringRequest);
    }

    public void post(final String url, final NetWorker.ICallback callback, Object... params) {
        LogUtil.d("hzm","volley post "+url+" "+params.toString());

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                LogUtil.d("hzm","volley post ok "+NetWorker.HTTP_OK);

                callback.onResponse(NetWorker.HTTP_OK,res);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.d("hzm","volley post error "+volleyError.toString());
            }
        });
        mrequestQueue.add(stringRequest);
    }
}