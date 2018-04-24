package ch.comstock.hivecontroller.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.pmw.tinylog.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public abstract class MHPSjson {
	
	public static List<Float> getConsumption(String surl) {
		JsonArray watt = getArray(surl,"watt");
		
		List<Float> resp = null;
		Type listType = new TypeToken<List<Float>>() {}.getType();
		resp = new Gson().fromJson(watt, listType);
		Logger.debug(resp);
		return resp;
	}
	
	public static List<Boolean> getState(String surl) {
		JsonArray switchArr = getArray(surl,"switch");
		
		List<Boolean> resp = null;
		Type listType = new TypeToken<List<Integer>>() {}.getType();
		List<Integer> intArr = new Gson().fromJson(switchArr, listType);
		resp = intArr.stream().map(x -> (x==1)).collect(Collectors.toList());
		Logger.debug(resp);
		return resp;
	}
	
	private static JsonArray getArray(String surl, String target){
		URL url = null;
		try {
			url = new URL(surl+"/?cmd=511");
		} catch (MalformedURLException e1) {
			Logger.error("{} is no valid URL",surl);
			return null;
		}
		try(BufferedReader powerstation = new BufferedReader(new InputStreamReader(url.openStream(), "utf-8"))){
		StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = powerstation.readLine()) != null) 
            response.append(inputLine);
        powerstation.close();

        Logger.debug(response.toString());	
        
		JsonObject json = new Gson().fromJson(response.toString(), JsonObject.class);
		JsonArray jsonarr = json.get("data").getAsJsonObject().get(target).getAsJsonArray();

		
		return jsonarr;
        
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
}
