package com.example.podcasfy.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.podcasfy.api.Provider;
import com.example.podcasfy.model.Episode;
import com.example.podcasfy.repository.PodcastListRepository;
import com.example.podcasfy.model.Podcast;
import com.example.podcasfy.utils.EpisodeCallBack;
import com.example.podcasfy.utils.PodcastCallBack;

import java.util.List;

public class PodcastListViewModel extends ViewModel implements PodcastCallBack, EpisodeCallBack {


    private PodcastListRepository podcastRepository;

    // Podcast per each provider + subscriptions + search
    private MutableLiveData<List<Podcast>> spainRecommendedPodcastList;
    private MutableLiveData<List<Podcast>> ukRecommendedPodcastList;
    private MutableLiveData<List<Podcast>> subscribedPodcastList;
    private MutableLiveData<List<Podcast>> searchedPodcast;

    // Episodes per given Podcast
    private MutableLiveData<List<Episode>> episodeList;

    // Used to triggered a search when the user sets a query
    private MutableLiveData<String> searchQuery;


    public PodcastListViewModel (){

        podcastRepository = new PodcastListRepository(this, this);

        episodeList = new MutableLiveData<>();
        searchQuery = new MutableLiveData<>();
        searchedPodcast = new MutableLiveData<>();
    }


    public MutableLiveData<List<Podcast>> getSubscribedPodcastList() {

        if(subscribedPodcastList == null){
            subscribedPodcastList = new MutableLiveData<>();
            podcastRepository.getSubscribedPodcastList();
        }
        return subscribedPodcastList;
    }

    public MutableLiveData<List<Podcast>> getSpainRecommendedPodcastList(){

        if(spainRecommendedPodcastList == null){
            spainRecommendedPodcastList = new MutableLiveData<>();
            podcastRepository.getSpainRecommended();
        }

        return spainRecommendedPodcastList;
    }

    public MutableLiveData<List<Podcast>> getUKRecommended() {

        if(ukRecommendedPodcastList == null){
            ukRecommendedPodcastList = new MutableLiveData<>();
            podcastRepository.getUKRecommended();
        }

        return ukRecommendedPodcastList;
    }

    public MutableLiveData<List<Episode>> getEpisodeList() {
        return episodeList;
    }

    public MutableLiveData<List<Episode>> getSpainEpisodes(String podcastURL){
        podcastRepository.getSpainEpisodes(podcastURL);
        return episodeList;
    }

    public MutableLiveData<List<Episode>> getUKEpisodes(String podcastURL){
        podcastRepository.getUKEpisodes(podcastURL);
        return episodeList;
    }

    public MutableLiveData<List<Episode>> getSubscribedEpisodes(int podcastId){
        podcastRepository.getSubscribedEpisodes(podcastId);
        return episodeList;
    }

    public MutableLiveData<String> getSearchQuery() {
        return searchQuery;
    }

    public void searchPodcast(){
        podcastRepository.searchPodcast(searchQuery.getValue());
    }

    public MutableLiveData<List<Podcast>> getSearchedPodcast() {
        return searchedPodcast;
    }

    @Override
    public void updatePodcastList(List<Podcast> podcastList, String option) {
        if(option.equals(Provider.SPAIN)){
            spainRecommendedPodcastList.setValue(podcastList);
        } else if(option.equals(Provider.UK)){
            ukRecommendedPodcastList.setValue(podcastList);
        } else if(option.equals(Provider.SUBSCRIBED)){
            subscribedPodcastList.setValue(podcastList);
        }
        else
            searchedPodcast.setValue(podcastList);
    }

    @Override
    public void updateEpisodeList(List<Episode> episodeList, String option, String url) {
        this.episodeList.setValue(episodeList);
    }
}
