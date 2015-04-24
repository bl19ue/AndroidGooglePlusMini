package com.my.sumit.singleton;

import android.util.Log;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.plusDomains.PlusDomains;

/**
 * Created by Ken on 3/12/2015.
 */
public class MyPlusDomainAPI {
    private static PlusDomains plusDomains = null;

    public MyPlusDomainAPI(GoogleCredential credential){
        Log.i("weird_error:", credential.toString());
        if(this.plusDomains == null){
            this.plusDomains =  new PlusDomains.Builder(new NetHttpTransport(),
                    new JacksonFactory(), credential).setApplicationName("MiniPlusGoogle").build();
        }
    }

    public static PlusDomains getPlusDomains(){
        return plusDomains;
    }
}