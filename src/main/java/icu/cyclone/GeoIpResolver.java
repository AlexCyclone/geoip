package icu.cyclone;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Aleksey Babanin
 * @since 2021/01/26
 */
public class GeoIpResolver {
    private static final String SERVICE_URI = "http://ipwhois.app/json/%s";
    private static final int TIMEOUT = 5000;
    private static final String REQUEST_METHOD = "GET";
    private static final String PROPERTY_CONTENT_LENGTH = "Content-length";
    private static final String CONTENT_LENGTH = "0";

    public static List<Map<String, String>> getGeoIp(List<String> ipAddresses) {
        return ipAddresses
                .stream()
                .map(GeoIpResolver::getGeoIp)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static Map<String, String> getGeoIp(String ipAddress) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        String jsonString = getJSON(String.format(SERVICE_URI, ipAddress), TIMEOUT);
        return Strings.isNullOrEmpty(jsonString) ? null : gson.fromJson(jsonString, type);
    }

    private static String getJSON(String url, int timeout) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod(REQUEST_METHOD);
            c.setRequestProperty(PROPERTY_CONTENT_LENGTH, CONTENT_LENGTH);
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(timeout);
            c.setReadTimeout(timeout);
            c.connect();

            int status = c.getResponseCode();

            if (status >= 200 && status <= 201) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()))) {
                    return br.lines().collect(Collectors.joining("\n"));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return null;
    }
}
