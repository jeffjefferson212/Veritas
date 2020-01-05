package edu.cis.clientapp.Model;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Request {
    private String command;
    private Map<String, String> params;

    private static String decode(String str) {
        try {
            return URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalArgumentException(var2);
        }
    }

    private static String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            throw new IllegalArgumentException(var2);
        }
    }

    public static Request fromUrl(String url) {
        String[] requestParts = url.split("\\?");
        String command = requestParts[0];
        Request request = new Request(command);
        if (requestParts.length == 2) {
            String paramStr = requestParts[1];
            String[] paramParts = paramStr.split("&");
            String[] var9 = paramParts;
            int var8 = paramParts.length;

            for(int var7 = 0; var7 < var8; ++var7) {
                String paramPart = var9[var7];
                String key = paramPart.split("=")[0];
                String value = "";
                if (paramPart.split("=").length == 2) {
                    value = paramPart.split("=")[1];
                }

                request.addRaw(key, value);
            }
        }

        return request;
    }

    public Request(String cmd) {
        if (cmd == null) {
            throw new NullPointerException("null command verb passed");
        } else {
            this.command = this.sanitize(cmd).replace("?", "_").replace(" ", "_").replace("&", "_");
            this.params = new LinkedHashMap();
        }
    }

    public void addParam(String key, String value) {
        this.params.put(this.sanitize(key), encode(value));
    }

    private void addRaw(String key, String value) {
        this.params.put(this.sanitize(key), value);
    }

    public String getCommand() {
        return this.command;
    }

    public String getParam(String key) {
        return this.hasParam(key) ? decode((String)this.params.get(this.sanitize(key))) : null;
    }

    public Iterator<String> getParamNames() {
        return this.params.keySet().iterator();
    }

    public boolean hasParam(String key) {
        return this.params.containsKey(this.sanitize(key));
    }

    public void removeParam(String key) {
        this.params.remove(this.sanitize(key));
    }

    private String sanitize(String key) {
        return String.valueOf(key).trim();
    }

    public String toGetRequest() {
        String getRequest = this.command;
        if (!this.params.isEmpty()) {
            boolean isFirst = true;
            Iterator var4 = this.params.keySet().iterator();

            while(var4.hasNext()) {
                String key = (String)var4.next();
                if (isFirst) {
                    getRequest = getRequest + "?" + key + "=" + (String)this.params.get(key);
                    isFirst = false;
                } else {
                    getRequest = getRequest + "&" + key + "=" + (String)this.params.get(key);
                }
            }
        }

        return getRequest;
    }

    public String toString() {
        String str = this.command + " (";
        boolean isFirst = true;

        for(Iterator var4 = this.params.keySet().iterator(); var4.hasNext(); isFirst = false) {
            String p = (String)var4.next();
            if (!isFirst) {
                str = str + ", ";
            }

            str = str + p + "=" + this.getParam(p);
        }

        str = str + ")";
        return str;
    }
}