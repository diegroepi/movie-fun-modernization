package org.superbiz.moviefun.albumsapi;


import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

public class AlbumsClient {

    private String albumUrl;
    private RestOperations restOperations;
    private static ParameterizedTypeReference<List<AlbumInfo>> albumListType = new ParameterizedTypeReference<List<AlbumInfo>>() {
    };

    public AlbumsClient(String albumUrl, RestOperations restOperations) {
        this.albumUrl = albumUrl;
        this.restOperations = restOperations;
    }

    public void addAlbum(AlbumInfo albumInfo) {
        restOperations.postForEntity(albumUrl, albumInfo, AlbumInfo.class);

    }

    public AlbumInfo find(long id) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(albumUrl).queryParam("id", id);
               return restOperations.getForObject(builder.toUriString(), AlbumInfo.class);

    }

    public List<AlbumInfo> getAlbums() {

        return restOperations.exchange(albumUrl, GET, null, albumListType).getBody();
    }


    public void deleteAlbum(AlbumInfo albumInfo) {
        restOperations.delete(albumUrl + "/" + albumInfo.getId());
    }

}
