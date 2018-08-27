package ircbot;
import org.jibble.pircbot.*;
import com.google.gson.*;
import com.google.gson.reflect.*;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.*;

public class MyBot extends PircBot{
	String WEATHER_API_KEY = "4c070f7f28ce8a9448f1db1988594471";
	Map<String, Object> weatherMap; // contains the location name
	Map<String, Object> weatherMainMap; // main contains the current temp, high and lows
	Map<String, Object> issMap; 
	Map<String, Object> issPositionMap; // contains longitude and latitude of the ISS
	
	// constructor
	public MyBot() {
		this.setName("APIBot");
	}
	
	// json to map function
	public static Map<String, Object> jsonToMap(String str){
		Map<String, Object> map = new Gson().fromJson(str, new TypeToken<HashMap<String, Object>>(){}.getType());
		return map;
	}
	
	// gets message from the user and passes it to a parse function.
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		parseMessage(channel, sender, message);
	}
	
	// utilises the weather api to retrive json weather data and convert it to a map and outputs the weather info
	public void onWeather(String ch, String sen, String usrInput) {
		try {
			String weatherURLZip = "http://api.openweathermap.org/data/2.5/weather?zip=" + usrInput + "&appid=" + WEATHER_API_KEY + "&units=imperial";
			StringBuilder res = new StringBuilder();
			URL wurl = new URL(weatherURLZip);
			URLConnection conn = wurl.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while((line = rd.readLine()) != null) {
				res.append(line);
			}
			rd.close();
			Map<String, Object> weatherMap = jsonToMap(res.toString()); 
			Map<String, Object> weatherMainMap = jsonToMap(weatherMap.get("main").toString()); // main contains the current temp, high and lows
			
			sendMessage(ch, sen + ": The current temp in " + weatherMap.get("name") + " is " + weatherMainMap.get("temp") + "F."); // print to user
		}
		catch(IOException e) {
			System.out.print("error at " + e.getMessage());
		}
	}
	
	// utilises the iss api to retrive position data in json and convert it to a map and outputs the info
		public void onISS(String ch, String sen) {
			try {
				String issURL = "http://api.open-notify.org/iss-now.json";
				StringBuilder res = new StringBuilder();
				URL wurl = new URL(issURL);
				URLConnection conn = wurl.openConnection();
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				while((line = rd.readLine()) != null) {
					res.append(line);
				}
				rd.close();
				Map<String, Object> issMap = jsonToMap(res.toString()); 
				Map<String, Object> issPositionMap = jsonToMap(issMap.get("iss_position").toString()); // has longitude and latitude of the international space station.
				
				sendMessage(ch, sen + ": The International Space Station is currently at a longitude of " + issPositionMap.get("longitude") + " and latitude of " + issPositionMap.get("latitude") + "."); // print to user
			}
			catch(IOException e) {
				System.out.print("error at " + e.getMessage());
			}
		}
	
	// parses message and checks whether to use the weather api or other api
	public void parseMessage(String cha, String send, String msg) {
		//String wPattern = "(\\weather(.*))";
		
		String[] arr = msg.split(" "); // creates an array by splitting the message by the spaces.
		if(msg.contains("weather")) {
			for(String s : arr) { // checking for zip code
				if(s.length() == 5) {
					onWeather(cha, send, s);
				}
				if (s.length() == 10) {
					if(s.indexOf('-') == 5) { // zip with hyphen
						onWeather(cha, send, s);
					}
				}
			}
		}
		else if(msg.contains("iss") || msg.contains("ISS")) {
			onISS(cha, send);
		}
	}
	
}
