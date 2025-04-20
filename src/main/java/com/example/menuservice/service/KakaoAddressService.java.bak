package com.example.menuservice.service;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KakaoAddressService {
    @Value("${kakao.rest.api-key}")
   private String kakaoApiKey ;//카카오 REST API 키

   public double[] convertToCoordinates(String kakaoAddress) throws IOException {
       String url = "https://dapi.kakao.com/v2/local/search/address.json?query=" + URLEncoder.encode(kakaoAddress, "UTF-8");
       HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
       connection.setRequestMethod("GET");
       connection.setRequestProperty("Authorization", kakaoApiKey);

       BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
       String response = reader.lines().collect(Collectors.joining("\n"));
       reader.close();

       JSONObject jsonObject = new JSONObject(response);
       JSONArray documents = jsonObject.getJSONArray("documents");

       if (documents.length() == 0) {
           throw new IllegalArgumentException("주소로부터 좌표를 찾을 수 없습니다.");
       }

       JSONObject location = documents.getJSONObject(0);
       double longitude = location.getDouble("x");
       double latitude = location.getDouble("y");

       return new double[]{longitude, latitude};
   }

}
