package org.pwc.com.model;

import java.util.Map;

public class TestConfig {
    private String testName;
    private String domain;
    private Map<String, String> headers;  // Headers için ekleme
    private Map<String, String> cookies;  // Cookies için ekleme
    private String path;
    private String protocol = "https"; // Varsayılan protokol
    private int threads;
    private int rampUp;
    private int duration;
    private String method;
    private String searchTerm;
    private boolean useHeader = true;
    public String getTestName() { return testName; }
    public String getDomain() { return domain; }
    public String getMethod() { return method; }
    public int getDuration() { return duration; }


    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }


    public String getPath() {
        return path;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public void setRampUp(int rampUp) {
        this.rampUp = rampUp;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setMethod(String method) {
        this.method = method;
    }



    public int getThreads() {
        return threads;
    }

    public int getRampUp() {
        return rampUp;
    }

    public TestConfig(String testName, String searchTerm, int duration) {
        this.testName = testName;
        this.searchTerm = searchTerm;
        this.duration = duration;
        this.threads = 1;
        this.rampUp = 1;
        this.method = "GET";
        this.domain = "www.n11.com";
        this.path = "/arama";
    }

    public TestConfig() {}


    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public boolean isUseHeader() {
        return useHeader;
    }

    public void setUseHeader(boolean useHeader) {
        this.useHeader = useHeader;
    }

}