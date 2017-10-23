package com.flancer.flancer.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Yannick on 23/10/2017.
 */

public class FlancerContract {
    public static final String CONTENT_AUTHORITY = "com.flancer.flancer.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_JOB = "job";
    public static final String PATH_COMPANY = "company";
    public static final String PATH_FREELANCER = "freelancer";

    public static final class CompanyEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_COMPANY).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_COMPANY;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_COMPANY;

        public static final String TABLE_NAME = "companies";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_ZIP = "zip";
        public static final String COLUMN_COUNTRY = "country";

        public static Uri buildCompanyUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildCompany(String company_id) {
            return CONTENT_URI.buildUpon().appendPath(company_id).build();
        }

        public static String getCompanyFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class FreelancerEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FREELANCER).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_FREELANCER;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_FREELANCER;

        public static final String TABLE_NAME = "freelancers";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_STREET = "street";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_CITY = "city";
        public static final String COLUMN_ZIP = "zip";
        public static final String COLUMN_COUNTRY = "country";

        public static Uri buildFreelancerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildFreelancer(String freelancer_id) {
            return CONTENT_URI.buildUpon().appendPath(freelancer_id).build();
        }

        public static String getFreelancerFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class JobEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_JOB).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_JOB;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_JOB;

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

        public static Uri buildJobUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildJob(String job_id) {
            return CONTENT_URI.buildUpon().appendPath(job_id).build();
        }

        public static String getJobFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
