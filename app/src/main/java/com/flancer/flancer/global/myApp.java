package com.flancer.flancer.global;

import android.app.Application;

import com.flancer.flancer.beans.User;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by 11500613 on 17/10/2017.
 */

public class myApp extends Application {

    public User user = new User();

    @Override
    public void onCreate(){
        super.onCreate();

        FlowManager.init(new FlowConfig.Builder(this).build());
    }
}
