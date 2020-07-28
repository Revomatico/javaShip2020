package com.revomatico.play.javaship2020.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.java_websocket.util.Base64.OutputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.raisercostin.jedio.Locations;
import org.raisercostin.nodes.Nodes;
import org.springframework.http.HttpEntity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.request.HttpRequest;
import com.revomatico.play.javaship2020.MediaItem;
import com.revomatico.play.javaship2020.MediaItemImporter;

import io.vavr.collection.Seq;

public class YouTubeImporterAntonia implements MediaItemImporter {
	private static String myApiKey = "AIzaSyBQ0yj3OzsENXmMJ5sXlwp-r-U7ZzAUAZM";
    //static String playListId1 = "PLRXt5CxEntwiGu-3FXEo5iMCeaHFkJKOw";
    //static String videoId = "LXXQLa-5n5w";
    private static String channelId = "UCogyDjqEVcRWajNRm6PWx7Q";
    
	  @Override
	  public List<MediaItem> importMediaItems(String path) {
		
		List<String> idsOfPlaylists = getIdOfPlaylist(channelId,myApiKey);
		List<MediaItem> allTracks=new ArrayList<>();
	
		for( String playListId:idsOfPlaylists ) {
			String content = Locations.url(
				"https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+playListId+"&key="+myApiKey).readContentSync();
			//System.out.println(content);
			 YoutubePlaylist result = Nodes.json.withIgnoreUnknwon().toObject(content, YoutubePlaylist.class);
			   
			 allTracks.addAll(
				  result.items.map(youtubeItem -> youtubeItem.snippet)
			      .map(x -> x.validate())
			      .toJavaList());
		}		
		return allTracks;
	  }
	  

	public static List<String> getIdOfPlaylist(String channelId,String myApiKey) {
		String content = Locations.url("https://www.googleapis.com/youtube/v3/playlists?part=snippet,contentDetails&channelId="+channelId+"&key="+myApiKey).readContentSync();    
		
		List<String> idsOfPlaylists=new ArrayList<String>();
		
		//YoutubePlaylistAntonia result = Nodes.json.withIgnoreUnknwon().toObject(content, YoutubePlaylistAntonia.class);
		JSONObject obj = new JSONObject(content);
		JSONArray arr = obj.getJSONArray("items");
	    for (int i = 0; i < arr.length(); i++) {
	        String id = arr.getJSONObject(i).getString("id");
	        idsOfPlaylists.add(id);
	        //System.out.println(id);
	      }  
	    return idsOfPlaylists;
	}
}

