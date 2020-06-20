package com.example.podcasfy.api;

import android.util.Log;

import com.example.podcasfy.model.Episode;
import com.example.podcasfy.model.Podcast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Provider {

    // Different options to get podcasts
    public static final String SUBSCRIBED = "subscribed";
    public static final String SPAIN = "spain";
    public static final String UK = "uk";
    public static final String DONWLOADS ="donwloads";

    // Number of podcasts/episodes per search

    private static final int MAX_SEARCH_SIZE = 12;

    // URL for each kind of option
    private static final String SPAIN_URL = "http://www.radio-espana.es/podcasts";
    private static final String SPAIN_URL_SUB = "http://www.radio-espana.es";

    private static final String UK_URL = "https://www.radio-uk.co.uk/podcasts";
    private static final String UK_URL_SUB = "https://www.radio-uk.co.uk";

    private String url;
    private String url_sub;

    public Provider(){
    }

    private int setSearchSize(int sizeRecoveredFromWeb){
        return Math.min(sizeRecoveredFromWeb, MAX_SEARCH_SIZE);
    }

    public List<Podcast> getRecommended(String podcastProvider) {

        if(podcastProvider.equals(SPAIN)){
            url = SPAIN_URL;
            url_sub = SPAIN_URL_SUB;
        } else if(podcastProvider.equals(UK)){
            url = UK_URL;
            url_sub = UK_URL_SUB;
        }
        List<Podcast> podcastList = new ArrayList<>();
        Document doc = null;

        try{
            doc = Jsoup.connect(url).get();

            Elements podcastElements = doc.select("a.mdc-list-item");

            int search_size = setSearchSize(podcastElements.size());


            for (int i = 0; i < search_size; i++) {
                Podcast podcast = new Podcast();

                podcast.setName(podcastElements.get(i).select("img").attr("alt"));
                podcast.setMediaURL(podcastElements.get(i).select("img").attr("src"));
                podcast.setUrl(url_sub + podcastElements.get(i).attr("href"));
                podcast.setProvider(podcastProvider);
                podcast.generateId();

                // We need to get the description from the podcast webpage

                Document doc2 = Jsoup.connect(podcast.getUrl()).get();
                String description = doc2.select("div.content-column-left").select("div.secondary-span-color").first().text();
                podcast.setDescription(description);
                podcastList.add(podcast);
            }

            for(int i = 0; i < podcastList.size(); i++){
                Document docPodcastPage = Jsoup.connect(podcastList.get(i).getUrl()).get();
            }

            Log.d("PODCAST__","recomen size: " + podcastList.size());
            return podcastList;

        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Episode> getEpisodes(String podcastURL){
        List<Episode> episodeList = new ArrayList<>();
        Document doc = null;

        try{
            doc = Jsoup.connect(podcastURL).get();

            Elements elements = doc.select("div.podcast-item");

         //   String description = doc.select("div.content-column-left").select("div.secondary-span-color").first().text();

            String imageURL = doc.select("div.content-column-left").select("img").attr("src");


            for(int i = 0; i < elements.size(); i++){

                Episode episode = new Episode();

                episode.setName(elements.get(i).text());
                episode.setImageURL(imageURL);
                episode.setMediaURL(elements.get(i).select("svg[data-url$=.mp3]").attr("data-url"));

                episodeList.add(episode);
            }
            return episodeList;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public List<Podcast> searchPodcast(String query){

        final String search_url_prefix = "https://www.radio-uk.co.uk/search?q=";
        List<Podcast> podcastList = new ArrayList<>();

        String search_url = search_url_prefix + query;

        Log.d("TESTING__", "SEARCH URL: " + search_url);

        Document doc = null;

        try{
            doc = Jsoup.connect(search_url).get();

            Elements elements = doc.select("div.mdc-list.mdc-list--avatar-list").select("a.mdc-list-item");

            int search_size = setSearchSize(elements.size());

            for(int i = 0; i < search_size; i++){
                Podcast podcast = new Podcast();
                podcast.setName(elements.get(i).select("img").attr("alt"));
                podcast.setMediaURL(elements.get(i).select("img").attr("src"));
                podcast.setUrl(url_sub + elements.get(i).attr("href"));

                podcast.setProvider(Provider.SUBSCRIBED);

                podcast.generateId();

                podcastList.add(podcast);
            }

            Log.d("PODCAST__","size: "+ elements.size());
            return podcastList;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }

    }
}
