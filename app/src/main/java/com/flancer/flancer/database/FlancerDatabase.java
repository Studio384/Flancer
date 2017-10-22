package com.flancer.flancer.database;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by 11500613 on 17/10/2017.
 */

@Database(name = FlancerDatabase.NAME, version = FlancerDatabase.VERSION)
public class FlancerDatabase {
    public static final String NAME = "Flancer";

    public static final int VERSION = 1;
}
