package com.potatomeme.appdesiginformat.helper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RestHelper {
    final private String KEY="zAMPzR5%2Flm2aC5TiMYF%2Bmc0NcbK48YqlhthfFQ4b3Trl7%2BEEuRjp4bCJAvv6%2B5xZWXG7CZCctF8jnjT%2BJVbYjA%3D%3D";
    private Disposable backgroundTask;
    private static ArrayList<String> restList;

    public RestHelper(){
        Observable<ArrayList<String>> source = Observable.fromCallable(
                () -> {
                    String apiUrl = "https://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo?serviceKey=" + KEY;
                    String url;

                    //xml파싱용
                    XmlPullParserFactory factory;
                    XmlPullParser parser;
                    URL xmlUrl;
                    InputStream is;
                    String returnResult = "";
                    ArrayList<String> list = new ArrayList<>();

                    //SSL허용
                    postHttps(apiUrl, 1000, 1000);

                    for(int i=-1; i<2; i++){
                        try {
                            String year = "&solYear=" + (getCurrentYear()+i);

                            boolean nameFlag = false;
                            url = apiUrl + year + "&numOfRows=100";

                            xmlUrl = new URL(url);
                            xmlUrl.openConnection().getInputStream();

                            factory = XmlPullParserFactory.newInstance();
                            parser = factory.newPullParser();

                            is = xmlUrl.openStream();

                            parser.setInput(is, "UTF-8");

                            int eventType = parser.getEventType();

                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                switch (eventType) {
                                    case XmlPullParser.START_TAG:
                                        if (parser.getName().equals("locdate"))
                                            nameFlag = true;
                                        break;
                                    case XmlPullParser.END_TAG:
                                        if (parser.getName().equals("item")) {
                                            list.add(returnResult);
                                            returnResult = "";
                                        }
                                        break;
                                    case XmlPullParser.TEXT:
                                        if (nameFlag == true) {
                                            returnResult += parser.getText();
                                            nameFlag = false;
                                        }
                                        break;
                                }
                                eventType = parser.next();
                            }
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }


                    return list;
                }
        );

        backgroundTask = source.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ArrayList<String>>() {
                    @Override
                    public void accept(ArrayList<String> list) {
                        restList = list;
                        backgroundTask.dispose();
                    }
                });
    }

    //현재 년도
    private int getCurrentYear() {
        SimpleDateFormat formatter
                = new SimpleDateFormat("yyyy", Locale.KOREA);
        Date date = new Date();
        int currentDate = Integer.parseInt(formatter.format(date));


        return currentDate;
    }

    public static ArrayList<String> getRestList(){
        return restList;
    }



    //----------------- SSL허용 -------------------
    // always verify the host - dont check for certificate
    final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    /**
     * Trust every server - don't check for any certificate
     */
    private void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[] {};
            }
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};
        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public HttpsURLConnection postHttps(String url, int connTimeout, int readTimeout) {
        trustAllHosts();
        HttpsURLConnection https = null;
        try {
            https = (HttpsURLConnection) new URL(url).openConnection();
            https.setHostnameVerifier(DO_NOT_VERIFY);
            https.setConnectTimeout(connTimeout);
            https.setReadTimeout(readTimeout);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return https;
    }
}
