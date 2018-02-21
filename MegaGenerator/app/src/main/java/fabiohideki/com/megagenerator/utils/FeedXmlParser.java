package fabiohideki.com.megagenerator.utils;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import fabiohideki.com.megagenerator.model.NewsItem;

/**
 * Created by hidek on 20/02/2018.
 */

public class FeedXmlParser {

    private static final String ns = null;
    private List<NewsItem> newsItems;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        Log.d("fabio", "FeedXmlParser: ");
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            return readFeed(parser);

        } finally {
            in.close();
        }

    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        newsItems = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "rss");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String nameChannel = parser.getName();

            if (nameChannel.equals("channel")) {
                parser.require(XmlPullParser.START_TAG, ns, "channel");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();
                    // Starts by looking for the entry tag
                    if (name.equals("item")) {
                        newsItems.add(readItem(parser));
                    } else {
                        skip(parser);
                    }
                }
            } else {
                skip(parser);
            }
        }


        return newsItems;
    }

    private NewsItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, ns, "item");

        String title = "";
        String link = "";
        String description = "";
        String pubDate = "";

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else if (name.equals("link")) {
                link = readLink(parser);
            } else if (name.equals("description")) {
                description = readDescription(parser);
            } else if (name.equals("pubDate")) {
                pubDate = readPubDate(parser);
            } else {
                skip(parser);
            }

        }

        NewsItem newsItem = new NewsItem();

        newsItem.setTitle(title);
        newsItem.setLink(link);
        newsItem.setDescription(description);
        newsItem.setPubDate(pubDate);

        return newsItem;
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }


    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "link");
        return link;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "description");
        return description;
    }

    private String readPubDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "pubDate");
        String pubDate = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "pubDate");
        return pubDate;
    }


    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}
