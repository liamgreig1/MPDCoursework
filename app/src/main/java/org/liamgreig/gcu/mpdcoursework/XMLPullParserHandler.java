package org.liamgreig.gcu.mpdcoursework;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class XMLPullParserHandler {
    public static ArrayList<EarthquakeClass> parseData(String dataToParse) {
        EarthquakeClass earthquake = null;
        ArrayList<EarthquakeClass> earthquakeList = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(dataToParse));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("rss")) {
//                        Log.e("MyTag", "Start RSS Tag");
                        earthquakeList = new ArrayList<>();
                    } else if (xpp.getName().equalsIgnoreCase("channel")) {
//                        Log.e("MyTag", "Channel start tag found");
                    } else if (xpp.getName().equalsIgnoreCase("item")) {
                        earthquake = new EarthquakeClass();
//                        Log.e("MyTag", "Item start tag found");
                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        String temp = xpp.nextText();
//                        Log.e("MyTag", "Title is " + temp);
                        if (earthquake != null) {
                            earthquake.setTitle(temp);
                        }
                    } else if (xpp.getName().equalsIgnoreCase("description")) {
                        String temp = xpp.nextText();
//                        Log.e("MyTag", "Description is " + temp);
                        if (earthquake != null) {
                            earthquake.setDescription(temp);
                        }
                    } else if (xpp.getName().equalsIgnoreCase("link")) {
                        String temp = xpp.nextText();
//                        Log.e("MyTag", "Link is " + temp);
                        if (earthquake != null) {
                            earthquake.setLink(temp);
                        }
                    } else if (xpp.getName().equalsIgnoreCase("pubdate")) {
                        String temp = xpp.nextText();
//                        Log.e("MyTag", "Publication Date is " + temp);
                        if (earthquake != null) {
                            earthquake.setPubDate(temp);
                        }
                    } else if (xpp.getName().equalsIgnoreCase("category")) {
                        String temp = xpp.nextText();
//                        Log.e("MyTag", "Category is " + temp);
                        if (earthquake != null) {
                            earthquake.setCategory(temp);
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {
                        if (earthquake != null) {
//                            Log.e("MyTag", "Item is " + earthquake.toString());
                            earthquakeList.add(earthquake);
                        }
                    } else if (xpp.getName().equalsIgnoreCase("channel")) {
                        int size;
                        size = earthquakeList.size();
//                        Log.e("MyTag", "Channel size is " + size);
                    } else if (xpp.getName().equalsIgnoreCase("rss")) {
//                        Log.e("MyTag", "End RSS Tag");
                    }
                }

                eventType = xpp.next();

            }
        } catch (XmlPullParserException ae1) {
            Log.e("MyTag", "Parsing error" + ae1.toString());
        } catch (IOException ae1) {
            Log.e("MyTag", "IO error during parsing");
        }

//        Log.e("MyTag", "End document");

        return earthquakeList;
    }
}
