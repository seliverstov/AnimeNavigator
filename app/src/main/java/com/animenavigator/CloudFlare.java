package com.animenavigator;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by a.g.seliverstov on 01.04.2016.
 */
public class CloudFlare {
    public static final String HOST = "cherry-pudding-87894.herokuapp.com";

    public static String bypass(String originUrl) {
        URL url = null;
        try {
            url = new URL(originUrl);
        } catch (MalformedURLException e) {
            return null;
        }
        return originUrl.replace(url.getHost(), HOST);
    }
}