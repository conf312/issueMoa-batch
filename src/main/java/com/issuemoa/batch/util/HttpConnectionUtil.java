package com.issuemoa.batch.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Component
public class HttpConnectionUtil {

    @Value("${globals.naver.client.id}")
    private String naverClientId;

    @Value("${globals.naver.client.secret}")
    private String naverClientSecret;

    public HashMap<String, Object> getDataFromUrlJson (String type, String url, String parameter, String encoding, boolean isPost, String contentType) throws Exception {
        HashMap<String, Object> resultMap = new HashMap<>();

        URL apiUrl = new URL(url);

        HttpURLConnection conn = null;
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            conn = (HttpURLConnection) apiUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            /*
            POST 또는 PUT 요청과 같이 요청 본문 을 보내려면 ( output ) true로 설정해야 합니다. GET을 사용하면 일반적으로 시체를 보내지 않으므로 필요하지 않습니다.
            */
            conn.setDoOutput(true);

            if (isPost) {
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", contentType);
                conn.setRequestProperty("Accept", "*/*");
            } else {
                conn.setRequestMethod("GET");
            }

            if ("naver".equals(type)) {
                conn.setRequestProperty("X-Naver-Client-Id", naverClientId);
                conn.setRequestProperty("X-Naver-Client-Secret", naverClientSecret);
            }

            conn.connect();

            if (isPost) {
                bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                bw.write(parameter);
                bw.flush();
                bw = null;
            }

            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));

            String line = null;
            StringBuffer result = new StringBuffer();

            while ((line = br.readLine()) != null) result.append(line);

            System.out.println("==> PULL API: " + url);
            System.out.println(result);

            ObjectMapper mapper = new ObjectMapper();

            resultMap = mapper.readValue(result.toString(), HashMap.class);

        } catch (Exception exception) {
            throw new Exception(url + "api interface fail : " + exception.toString());
        } finally {
            if (conn != null) conn.disconnect();
            if (br != null) br.close();
            if (bw != null) bw.close();
        }

        return resultMap;
    }


}
