package com.zarkorunjevac.mhm.common;

import java.util.Arrays;
import java.util.List;

/**
 * Created by com.zarkorunjevac.mhm on 12/04/16.
 */
public class Config {

    public static final String CLIENT_ID = "469a173c79c40c02c653a7255c503cd2";
    public static final String API_URL = "https://api.soundcloud.com";

    public static final List<String> LATEST_LIST_FOR_DOWNLOAD= Arrays.asList("all", "fresh", "remix", "noremix");
    public static final List<String> POPULAR_LIST_FOR_DOWNLOAD=Arrays.asList("now", "remix", "noremix");

    public static final String LIST_NAME="com.zarkorunjevac.mhm.common.list_name";
    public static final String LIST_TYPE=".com.zarkorunjevac.mhm.common.list_name";
}
