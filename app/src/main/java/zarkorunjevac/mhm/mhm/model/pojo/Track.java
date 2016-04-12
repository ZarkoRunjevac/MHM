package zarkorunjevac.mhm.mhm.model.pojo;

/**
 * Created by zarkorunjevac on 20/03/16.
 */


import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Track {

    @SerializedName("itemid")
    @Expose
    private String itemid;
    @SerializedName("artist")
    @Expose
    private String artist;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("dateposted")
    @Expose
    private Integer dateposted;
    @SerializedName("siteid")
    @Expose
    private Integer siteid;
    @SerializedName("sitename")
    @Expose
    private String sitename;
    @SerializedName("posturl")
    @Expose
    private String posturl;
    @SerializedName("postid")
    @Expose
    private Integer postid;
    @SerializedName("loved_count")
    @Expose
    private Integer lovedCount;
    @SerializedName("posted_count")
    @Expose
    private Integer postedCount;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    @SerializedName("thumb_url_medium")
    @Expose
    private String thumbUrlMedium;
    @SerializedName("thumb_url_large")
    @Expose
    private String thumbUrlLarge;
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("itunes_link")
    @Expose
    private String itunesLink;


    private String mStreamUrl;

    public String getStreamUrl() {
        return mStreamUrl;
    }

    public void setStreamUrl(String streamUrl) {
        this.mStreamUrl = streamUrl;
    }
    /**
     *
     * @return
     * The itemid
     */
    public String getItemid() {
        return itemid;
    }

    /**
     *
     * @param itemid
     * The itemid
     */
    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    /**
     *
     * @return
     * The artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     *
     * @param artist
     * The artist
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The dateposted
     */
    public Integer getDateposted() {
        return dateposted;
    }

    /**
     *
     * @param dateposted
     * The dateposted
     */
    public void setDateposted(Integer dateposted) {
        this.dateposted = dateposted;
    }

    /**
     *
     * @return
     * The siteid
     */
    public Integer getSiteid() {
        return siteid;
    }

    /**
     *
     * @param siteid
     * The siteid
     */
    public void setSiteid(Integer siteid) {
        this.siteid = siteid;
    }

    /**
     *
     * @return
     * The sitename
     */
    public String getSitename() {
        return sitename;
    }

    /**
     *
     * @param sitename
     * The sitename
     */
    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    /**
     *
     * @return
     * The posturl
     */
    public String getPosturl() {
        return posturl;
    }

    /**
     *
     * @param posturl
     * The posturl
     */
    public void setPosturl(String posturl) {
        this.posturl = posturl;
    }

    /**
     *
     * @return
     * The postid
     */
    public Integer getPostid() {
        return postid;
    }

    /**
     *
     * @param postid
     * The postid
     */
    public void setPostid(Integer postid) {
        this.postid = postid;
    }

    /**
     *
     * @return
     * The lovedCount
     */
    public Integer getLovedCount() {
        return lovedCount;
    }

    /**
     *
     * @param lovedCount
     * The loved_count
     */
    public void setLovedCount(Integer lovedCount) {
        this.lovedCount = lovedCount;
    }

    /**
     *
     * @return
     * The postedCount
     */
    public Integer getPostedCount() {
        return postedCount;
    }

    /**
     *
     * @param postedCount
     * The posted_count
     */
    public void setPostedCount(Integer postedCount) {
        this.postedCount = postedCount;
    }

    /**
     *
     * @return
     * The thumbUrl
     */
    public String getThumbUrl() {
        return thumbUrl;
    }

    /**
     *
     * @param thumbUrl
     * The thumb_url
     */
    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    /**
     *
     * @return
     * The thumbUrlMedium
     */
    public String getThumbUrlMedium() {
        return thumbUrlMedium;
    }

    /**
     *
     * @param thumbUrlMedium
     * The thumb_url_medium
     */
    public void setThumbUrlMedium(String thumbUrlMedium) {
        this.thumbUrlMedium = thumbUrlMedium;
    }

    /**
     *
     * @return
     * The thumbUrlLarge
     */
    public String getThumbUrlLarge() {
        return thumbUrlLarge;
    }

    /**
     *
     * @param thumbUrlLarge
     * The thumb_url_large
     */
    public void setThumbUrlLarge(String thumbUrlLarge) {
        this.thumbUrlLarge = thumbUrlLarge;
    }

    /**
     *
     * @return
     * The time
     */
    public Integer getTime() {
        return time;
    }

    /**
     *
     * @param time
     * The time
     */
    public void setTime(Integer time) {
        this.time = time;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The itunesLink
     */
    public String getItunesLink() {
        return itunesLink;
    }

    /**
     *
     * @param itunesLink
     * The itunes_link
     */
    public void setItunesLink(String itunesLink) {
        this.itunesLink = itunesLink;
    }

}
