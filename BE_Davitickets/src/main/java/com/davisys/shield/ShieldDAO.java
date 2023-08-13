package com.davisys.shield;

import java.util.Map.Entry;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

public class ShieldDAO {
	RestTemplate rest = new RestTemplate();

	String url = "https://davitickets-2e627-default-rtdb.firebaseio.com/login/shield.json";
	//https://console.firebase.google.com/u/1/project/davitickets-2e627/database/davitickets-2e627-default-rtdb/data/~2Flogin~2Fshield

	public String getKeyByEmail(String email) {
		ShieldMap shields = this.findAll();
		
		if(shields == null)
			return null;

		// Xuất các phần tử từ ShieldMap
		for (Entry<String, Shield> entry : shields.entrySet()) {
			System.out.println("Key: " + entry.getKey() + ", Shield: " + entry.getValue());
			if (entry.getValue().getEmail().equalsIgnoreCase(email)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public Shield findByEmail(String email) {
		ShieldMap shields = this.findAll();
		if(shields == null)
			return null;

		// Xuất các phần tử từ ShieldMap
		for (Entry<String, Shield> entry : shields.entrySet()) {
			System.out.println("Key: " + entry.getKey() + ", Shield: " + entry.getValue());
			if (entry.getValue().getEmail().equalsIgnoreCase(email)) {
				return entry.getValue();
			}
		}
		return null;
	}

	private String getUrl(String key) {
		return url.replace(".json", "/" + key + ".json");
	}

	public ShieldMap findAll() {
		return rest.getForObject(url, ShieldMap.class);
	}

	public Shield findByKey() {
		return rest.getForObject(url, Shield.class);
	}

	public String create(Shield data) {
		HttpEntity<Shield> entity = new HttpEntity<>(data);
		JsonNode res = rest.postForObject(url, entity, JsonNode.class);
		return res.get("name").asText();
	}

	public Shield update(String key, Shield data) {
		HttpEntity<Shield> entity = new HttpEntity<>(data);
		rest.put(getUrl(key), entity);
		return data;
	}

	public void delete(String key) {
		rest.delete(getUrl(key));
	}
}
