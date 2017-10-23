package com.flancer.flancer.data;

import android.provider.BaseColumns;

/**
 * Created by Yannick on 23/10/2017.
 */

public class FlancerContract {
    public static final class CompanyEntry implements BaseColumns {
        public static final String TABLE_NAME = "companies";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_ZIP = "zip";
        public static final String COLUMN_COUNTRY = "country";

    }

    public static final class JobEntry implements BaseColumns {
        public static final String TABLE_NAME = "jobs";

        public static final String COLUMN_COMPANY_KEY = "compnay_id";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_MINIMUM_BID = "minimum_bid";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_ZIP = "zip";
        public static final String COLUMN_COUNTRY = "country";
    }

}
