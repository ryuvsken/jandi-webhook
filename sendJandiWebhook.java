package com.travelking.api.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import com.amazonaws.util.json.JSONArray;
import com.travelking.api.Friend.controller.FriendController;

/**
 * 푸쉬용 유틸 클래스
 * @author ray
 *
 */
public class sendJandiWebhook {
	
	// log init
	private static final Logger logger = LoggerFactory.getLogger(pushCommonUtil.class);

	/**
	 * Jandi를 이용한 리뷰작성 정보 전송
	 * @param title
	 * @param user_name
	 * @param body
	 * @param frist_img
	 * @param address
	 */
	@SuppressWarnings("unchecked")
	@Async
	public void sendTravelJandi(String title, String user_name , String body , String frist_img , String address , String area_idx , String review_idx){
		try{
			String url = "https://wh.jandi.com/connect-api/webhook/본인 고유아이디";
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept", "application/vnd.tosslab.jandi-v2+json");
			con.setRequestProperty("Content-Type", "application/json");
			
			JSONObject objectResult = new JSONObject();
			JSONArray objectConnectInfo = new JSONArray();
			JSONObject objectConnectInfoItem = new JSONObject();

			if(ApiKeyPref.isLiveServer == true){
				// LIVE
				objectResult.put("body", "큰 타이틀(LIVE)");
				objectResult.put("connectColor", "#FAC11B");  // 라인 색상을 지정한다
			}else{
				// DEV
				objectResult.put("body", "큰 타이틀(DEV)");
				objectResult.put("connectColor", "#000000");   // 라인 색상을 지정한다
			}
			
			// 리뷰 기본정보
			
			objectConnectInfoItem.put("title", "아이템 타이틀");
			objectConnectInfoItem.put("description", "설명");
			objectConnectInfo.put(objectConnectInfoItem);
			
			// 리뷰 내용
			objectConnectInfoItem.put("title", "두번째 아이템 타이틀");
			objectConnectInfoItem.put("description","설명");
			objectConnectInfoItem.put("imageUrl", "이미지가 있을경우 이미지 URL");
			objectConnectInfo.put(objectConnectInfoItem);

			objectResult.put("connectInfo", objectConnectInfo);
			
			String urlParameters = objectResult.toJSONString();
			
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(objectResult.toJSONString().getBytes());
			wr.flush();
			wr.close();
			
			int responseCode = con.getResponseCode();
			logger.info("\nSending 'POST' request to URL : " + url);
			logger.info("Post parameters : " + urlParameters);
			logger.info("Response Code : " + responseCode);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			//print result
			logger.info(response.toString());
			
			if(responseCode == 200){
			}else{
			}
		}catch(Exception e){
		}
	}
	
}
