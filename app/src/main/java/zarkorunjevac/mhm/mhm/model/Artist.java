package zarkorunjevac.mhm.mhm.model;

/**
 * Created by zarkorunjevac on 20/03/16.
 */


import java.util.HashMap;
import java.util.Map;

public class Artist {

    private String artist;
    private Integer cnt;
    private String thumbUrlArtist;
    private Integer rank;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * @param artist The artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * @return The cnt
     */
    public Integer getCnt() {
        return cnt;
    }

    /**
     * @param cnt The cnt
     */
    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    /**
     * @return The thumbUrlArtist
     */
    public String getThumbUrlArtist() {
        return thumbUrlArtist;
    }

    /**
     * @param thumbUrlArtist The thumb_url_artist
     */
    public void setThumbUrlArtist(String thumbUrlArtist) {
        this.thumbUrlArtist = thumbUrlArtist;
    }

    /**
     * @return The rank
     */
    public Integer getRank() {
        return rank;
    }

    /**
     * @param rank The rank
     */
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
