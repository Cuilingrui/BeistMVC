package com.shike.beistmvc.webmvc.common.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shike.webmvc.common.enums.ContentType;
import com.shike.webmvc.converter.PrimitiveConverter;
import com.shike.webmvc.converter.PrimitiveTypeUtil;
import com.shike.webmvc.http.BeistHttpRequest;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;
import org.apache.tools.ant.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HttpRequestUtil {

    /**
     * 获取请求参数的Map
     *
     * @param request http请求
     * @return 参数map
     */

    public static void fillParamsMap(HttpRequest request, BeistHttpRequest beistRequest) {


        HttpMethod method = request.method();
        if (HttpMethod.GET.equals(method)) {
            String uri = request.uri();
            QueryStringDecoder queryDecoder = new QueryStringDecoder(uri, CharsetUtil.UTF_8);
            beistRequest.setRequestParamsMap(queryDecoder.parameters());

        } else if (HttpMethod.POST.equals(method)) {
            FullHttpRequest fullRequest = (FullHttpRequest) request;
            try {
                fillPostParamsMap(fullRequest, beistRequest);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取post请求的参数map
     * 目前支持最常用的 application/json 、application/x-www-form-urlencoded 几种 POST Content-type，可自行扩展！！！
     */
    @SuppressWarnings("unchecked")
    private static void fillPostParamsMap(FullHttpRequest fullRequest, BeistHttpRequest request) throws IOException {
        Map<String, List<String>> paramMap = new HashMap<>();
        HttpHeaders headers = fullRequest.headers();
        String contentType = getContentType(headers);
        PrimitiveConverter converter = PrimitiveConverter.getInstance();


        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), fullRequest);
        List<InterfaceHttpData> datas = decoder.getBodyHttpDatas();
        for (InterfaceHttpData data : datas) {
            if (InterfaceHttpData.HttpDataType.FileUpload.equals(data.getHttpDataType())){
                FileUpload fileUpload = (FileUpload) data;
                String fileName = fileUpload.getFilename();
                fileUpload.renameTo(new File(DiskFileUpload.baseDirectory + fileName));
            }
        }


        if (ContentType.APPLICATION_JSON.toString().equals(contentType)) {
            String jsonStr = fullRequest.content().toString(CharsetUtil.UTF_8);
            JSONObject obj = JSON.parseObject(jsonStr);
            for (Map.Entry<String, Object> item : obj.entrySet()) {
                String key = item.getKey();
                Object value = item.getValue();
                Class<?> valueType = value.getClass();

                List<String> valueList;
                if (paramMap.containsKey(key)) {
                    valueList = paramMap.get(key);
                } else {
                    valueList = new ArrayList<>();
                }

                if (PrimitiveTypeUtil.isPriType(valueType)) {
                    valueList.add(value.toString());
                    paramMap.put(key, valueList);

                } else if (valueType.isArray()) {
                    int length = Array.getLength(value);
                    for (int i = 0; i < length; i++) {
                        String arrayItem = String.valueOf(Array.get(value, i));
                        valueList.add(arrayItem);
                    }
                    paramMap.put(key, valueList);

                } else if (List.class.isAssignableFrom(valueType)) {
                    if (valueType.equals(JSONArray.class)) {
                        JSONArray jArray = JSONArray.parseArray(value.toString());
                        for (int i = 0; i < jArray.size(); i++) {
                            valueList.add(jArray.getString(i));
                        }
                    } else {
                        valueList = (ArrayList<String>) value;
                    }
                    paramMap.put(key, valueList);

                } else if (Map.class.isAssignableFrom(valueType)) {
                    Map<String, String> tempMap = (Map<String, String>) value;
                    for (Map.Entry<String, String> entry : tempMap.entrySet()) {
                        List<String> tempList = new ArrayList<>();
                        tempList.add(entry.getValue());
                        paramMap.put(entry.getKey(), tempList);
                    }
                }
            }

        } else if (ContentType.APPLICATION_FORM_URLENCODED.toString().equals(contentType)) {
            String jsonStr = fullRequest.content().toString(CharsetUtil.UTF_8);
            QueryStringDecoder queryDecoder = new QueryStringDecoder(jsonStr, false);
            paramMap = queryDecoder.parameters();
        }

        request.setRequestParamsMap(paramMap);
    }

    /**
     * 获取contentType
     *
     * @param headers http请求头
     * @return 内容类型
     */
    private static String getContentType(HttpHeaders headers) {
        String contentType = headers.get(HttpHeaderNames.CONTENT_TYPE);
        String[] list = contentType.split(";");
        return list[0];
    }

    public static String joinOptimizePath(String... path) {
        StringBuilder _url = new StringBuilder();
        _url.append("/");
        if (null != path) {
            for (String url : path) {
                _url.append(url);
            }
        }
        int idx = _url.indexOf("//");

        while (idx != -1) {
            _url.deleteCharAt(idx);
            idx = _url.indexOf("//");
        }

        return _url.toString();
    }

    public static void fillCookies(FullHttpRequest request, BeistHttpRequest context) {
        Map<String, io.netty.handler.codec.http.cookie.Cookie> cookies = new HashMap<>();
        String value = request.headers().get(HttpHeaderNames.COOKIE);
        if (null != value && value.length() > 0) {
            for (Cookie cookie : ServerCookieDecoder.STRICT.decode(value)) {
                cookies.put(cookie.name(), cookie);
            }
        }
        context.setCookies(cookies);
    }

}
