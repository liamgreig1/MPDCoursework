package org.liamgreig.gcu.mpdcoursework;

import android.os.Parcel;
import android.os.Parcelable;

public class EarthquakeClass implements Parcelable {

    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String category;
    private String geoLat;
    private String geoLong;
    private String strength;
    private String location;
    private String depth;

    public EarthquakeClass(){
        title = "";
        description ="";
        link = "";
        pubDate = "";
        category = "";
        geoLat = "";
        geoLong = "";
        strength = "";
        location = "";
    }

    public EarthquakeClass(String aTitle, String aDescription, String aLink, String aPubDate, String aCategory, String aGeoLat, String aGeoLong, String aStrength, String aLocation){
        title = aTitle;
        description = aDescription;
        link = aLink;
        pubDate = aPubDate;
        category = aCategory;
        geoLat = aGeoLat;
        geoLong = aGeoLong;
        strength = aStrength;
        location = aLocation;
    }

    protected EarthquakeClass(Parcel in) {
        title = in.readString();
        description = in.readString();
        link = in.readString();
        pubDate = in.readString();
        category = in.readString();
        geoLat = in.readString();
        geoLong = in.readString();
        strength = in.readString();
        location = in.readString();
        depth = in.readString();
    }

    public static final Creator<EarthquakeClass> CREATOR = new Creator<EarthquakeClass>() {
        @Override
        public EarthquakeClass createFromParcel(Parcel in) {
            return new EarthquakeClass(in);
        }

        @Override
        public EarthquakeClass[] newArray(int size) {
            return new EarthquakeClass[size];
        }
    };

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        String[] descriptionArray = this.description.split("\\s*;\\s*");

        //To get Lat and Long
        String longlat = descriptionArray[2];
        String [] longlatArray = longlat.split("\\s*:\\s*");
        String longlatNum = longlatArray[1];
        String [] longlatNumArray = longlatNum.split("\\s*,\\s*");
        setGeoLat(longlatNumArray[0]);
        setGeoLong(longlatNumArray[1]);

        //To get strength
        String mag = descriptionArray[4];
        String [] magArray = mag.split("\\s*:\\s*");
        String strength = magArray[1];
        setStrength(strength);

        //To get Location Name
        String loc = descriptionArray[1];
        String[] locArray = loc.split("\\s*:\\s*");
        String locName = locArray[1];
        setLocation(locName);

        String depth = descriptionArray[3];
        String[] depthArray = depth.split("\\s*:\\s*");
        String depthName = depthArray[1];
        setDepth(depthName);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(String geoLat) {
        this.geoLat = geoLat;
    }

    public String getGeoLong() {
        return geoLong;
    }

    public void setGeoLong(String geoLong) {
        this.geoLong = geoLong;
    }

    @Override
    public String toString() {
        return "EarthquakeClass{" + '\n' +
                "title=" + title + '\n' +
                "description=" + description + '\n' +
                "link=" + link + '\n' +
                "pubDate=" + pubDate + '\n' +
                "category=" + category + '\n' +
                "geoLat=" + geoLat + '\n' +
                "geoLong=" + geoLong + '\n' +
                "strength=" + strength + '\n' +
                "location=" + location + '\n' +
                '}' + '\n';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeString(pubDate);
        dest.writeString(category);
        dest.writeString(geoLat);
        dest.writeString(geoLong);
        dest.writeString(strength);
        dest.writeString(location);
        dest.writeString(depth);
    }
}
