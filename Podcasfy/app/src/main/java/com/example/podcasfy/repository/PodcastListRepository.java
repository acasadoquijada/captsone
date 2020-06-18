package com.example.podcasfy.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.podcasfy.api.Provider;
import com.example.podcasfy.model.Episode;
import com.example.podcasfy.model.Podcast;
import com.example.podcasfy.utils.EpisodeCallBack;
import com.example.podcasfy.utils.PodcastCallBack;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

@Singleton
public class PodcastListRepository {

    private  MutableLiveData<List<Podcast>> podcasts;
    private  MutableLiveData<List<Podcast>> subscriptionList;

    final private PodcastCallBack podcastCallBack;
    final private EpisodeCallBack episodeCallBack;

    private Provider provider;


    public PodcastListRepository(PodcastCallBack podcastCallBack, EpisodeCallBack episodeCallBack){
        this.podcastCallBack = podcastCallBack;
        this.episodeCallBack = episodeCallBack;

        provider = new Provider();
    }

    public MutableLiveData<List<Podcast>> getSubscriptionList() {

        if(subscriptionList != null){
            return subscriptionList;
        }

        subscriptionList = new MutableLiveData<>();
        List<Podcast> podcastList = new ArrayList<>();

        Podcast p1 = new Podcast("Planos y Centellas","description","url",
                "https://static-2.ivoox.com/canales/3/8/0/0/2671546770083_MD.jpg","Ivoox");

        Podcast p2 = new Podcast("Amigos del Mapa","description2","url",
                "https://static-1.ivoox.com/canales/8/5/2/4/9071587544258_MD.jpg","digitalPodcast");

        podcastList.add(p1);
        podcastList.add(p2);
        podcastList.add(p2);

        subscriptionList.setValue(podcastList);

        return subscriptionList;
    }

    public MutableLiveData<List<Podcast>> getPodcasts(){

        if (podcasts != null) {
            return podcasts;
        }

        podcasts = new MutableLiveData<>();

        List<Podcast> podcastList = new ArrayList<>();

        Podcast p1 = new Podcast("Planos y Centellas","description","url",
                "https://static-2.ivoox.com/canales/3/8/0/0/2671546770083_MD.jpg","Ivoox");

        Podcast p2 = new Podcast("Amigos del Mapa","description2","url",
                "https://static-1.ivoox.com/canales/8/5/2/4/9071587544258_MD.jpg","provider2");
        Podcast p3 = new Podcast("Amigos del Mapa","description2","url",
                "https://static-1.ivoox.com/canales/8/5/2/4/9071587544258_MD.jpg","provider2");
        Podcast p4 = new Podcast("Amigos del Mapa","description2","url",
                "https://static-1.ivoox.com/canales/8/5/2/4/9071587544258_MD.jpg","provider2");
        Podcast p5 = new Podcast("Amigos del Mapa","description2","url",
                "https://static-1.ivoox.com/canales/8/5/2/4/9071587544258_MD.jpg","provider2");
        Podcast p6 = new Podcast("Amigos del Mapa","description2","url",
                "https://static-1.ivoox.com/canales/8/5/2/4/9071587544258_MD.jpg","provider2");

        podcastList.add(p1);
        podcastList.add(p2);
        podcastList.add(p3);
        podcastList.add(p4);
        podcastList.add(p5);
        podcastList.add(p6);

        podcasts.setValue(podcastList);

        return podcasts;
    }

    public void getSpainRecommended(){

        // asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
       // new FetchPodcastListTask().execute(Provider.SPAIN);
        new FetchPodcastListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Provider.SPAIN);
    }

    public void getUKRecommended(){
        new FetchPodcastListTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Provider.UK);
    }

    public void getSpainEpisodes(String podcastURL){
        // asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        new FetchEpisodeListTask().execute(Provider.SPAIN,podcastURL);
    }

    public void getUKEpisodes(String podcastURL){
        // asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        new FetchEpisodeListTask().execute(Provider.UK,podcastURL);
    }

    class FetchPodcastListTask extends AsyncTask<String, Void, List<Podcast> > {

        private String argument;
        @Override
        protected List<Podcast> doInBackground(String... strings) {

            argument = strings[0];
            Log.d("PODCAST__", "argument: " + argument);

            if(argument.equals(Provider.SPAIN)){
                return provider.getRecommended(Provider.SPAIN);
            } else if(argument.equals(Provider.UK)){
                return provider.getRecommended(Provider.UK);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Podcast> podcastsList) {
            Log.d("PODCAST__","ON POST EXECUTE" + podcastsList.size());
            podcastCallBack.updatePodcastList(podcastsList,argument);
        }
    }

    class FetchEpisodeListTask extends AsyncTask<String, Void, List<Episode> > {

        private String url;
        private String podcastProvider;

        @Override
        protected List<Episode> doInBackground(String... strings) {

            Log.d("PODCAST__", "provider: " + podcastProvider + " url: " + url);
            podcastProvider = strings[0];
            url = strings[1];

            return provider.getEpisodes(url);
        }

        @Override
        protected void onPostExecute(List<Episode> podcastsList) {
            episodeCallBack.updateEpisodeList(podcastsList, podcastProvider);
        }
    }

}