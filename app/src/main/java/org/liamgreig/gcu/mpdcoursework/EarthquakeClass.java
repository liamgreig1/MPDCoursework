package org.liamgreig.gcu.mpdcoursework;

public class EarthquakeClass {

    private String title;
    private String description;
    private String link;
    private String pubDate;
    private String category;
    private String geoLat;
    private String geoLong;

    public EarthquakeClass(){
        title = "";
        description ="";
        link = "";
        pubDate = "";
        category = "";
        geoLat = "";
        geoLong = "";
    }

    public EarthquakeClass(String aTitle, String aDescription, String aLink, String aPubDate, String aCategory, String aGeoLat, String aGeoLong){
        title = aTitle;
        description = aDescription;
        link = aLink;
        pubDate = aPubDate;
        category = aCategory;
        geoLat = aGeoLat;
        geoLong = aGeoLong;
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
        String longlat = descriptionArray[2];
        String [] longlatArray = longlat.split("\\s*:\\s*");
        String longlatNum = longlatArray[1];
        String [] longlatNumArray = longlatNum.split("\\s*,\\s*");
        setGeoLat(longlatNumArray[0]);
        setGeoLong(longlatNumArray[1]);
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
                "title='" + title + '\n' +
                "description='" + description + '\n' +
                "link='" + link + '\n' +
                "pubDate='" + pubDate + '\n' +
                "category='" + category + '\n' +
                "geoLat='" + geoLat + '\n' +
                "geoLong='" + geoLong + '\n' +
                '}' + '\n';
    }
}
