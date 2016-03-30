package zarkorunjevac.mhm.mhm.model.pojo;

/**
 * Created by zarkorunjevac on 20/03/16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Blog {

    @SerializedName("siteid")
    @Expose
    private Integer siteid;
    @SerializedName("sitename")
    @Expose
    private String sitename;
    @SerializedName("siteurl")
    @Expose
    private String siteurl;
    @SerializedName("followers")
    @Expose
    private Integer followers;
    @SerializedName("region_name")
    @Expose
    private Boolean regionName;
    @SerializedName("total_tracks")
    @Expose
    private Integer totalTracks;
    @SerializedName("last_posted")
    @Expose
    private Integer lastPosted;
    @SerializedName("first_posted")
    @Expose
    private Integer firstPosted;
    @SerializedName("blog_image")
    @Expose
    private String blogImage;
    @SerializedName("blog_image_small")
    @Expose
    private String blogImageSmall;
    @SerializedName("ts_featured")
    @Expose
    private Integer tsFeatured;

    /**
     * @return The siteid
     */
    public Integer getSiteid() {
        return siteid;
    }

    /**
     * @param siteid The siteid
     */
    public void setSiteid(Integer siteid) {
        this.siteid = siteid;
    }

    /**
     * @return The sitename
     */
    public String getSitename() {
        return sitename;
    }

    /**
     * @param sitename The sitename
     */
    public void setSitename(String sitename) {
        this.sitename = sitename;
    }

    /**
     * @return The siteurl
     */
    public String getSiteurl() {
        return siteurl;
    }

    /**
     * @param siteurl The siteurl
     */
    public void setSiteurl(String siteurl) {
        this.siteurl = siteurl;
    }

    /**
     * @return The followers
     */
    public Integer getFollowers() {
        return followers;
    }

    /**
     * @param followers The followers
     */
    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    /**
     * @return The regionName
     */
    public Boolean getRegionName() {
        return regionName;
    }

    /**
     * @param regionName The region_name
     */
    public void setRegionName(Boolean regionName) {
        this.regionName = regionName;
    }

    /**
     * @return The totalTracks
     */
    public Integer getTotalTracks() {
        return totalTracks;
    }

    /**
     * @param totalTracks The total_tracks
     */
    public void setTotalTracks(Integer totalTracks) {
        this.totalTracks = totalTracks;
    }

    /**
     * @return The lastPosted
     */
    public Integer getLastPosted() {
        return lastPosted;
    }

    /**
     * @param lastPosted The last_posted
     */
    public void setLastPosted(Integer lastPosted) {
        this.lastPosted = lastPosted;
    }

    /**
     * @return The firstPosted
     */
    public Integer getFirstPosted() {
        return firstPosted;
    }

    /**
     * @param firstPosted The first_posted
     */
    public void setFirstPosted(Integer firstPosted) {
        this.firstPosted = firstPosted;
    }

    /**
     * @return The blogImage
     */
    public String getBlogImage() {
        return blogImage;
    }

    /**
     * @param blogImage The blog_image
     */
    public void setBlogImage(String blogImage) {
        this.blogImage = blogImage;
    }

    /**
     * @return The blogImageSmall
     */
    public String getBlogImageSmall() {
        return blogImageSmall;
    }

    /**
     * @param blogImageSmall The blog_image_small
     */
    public void setBlogImageSmall(String blogImageSmall) {
        this.blogImageSmall = blogImageSmall;
    }

    /**
     * @return The tsFeatured
     */
    public Integer getTsFeatured() {
        return tsFeatured;
    }

    /**
     * @param tsFeatured The ts_featured
     */
    public void setTsFeatured(Integer tsFeatured) {
        this.tsFeatured = tsFeatured;
    }

}