package com.flancer.flancer.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.media.MediaCasException;
import android.net.Uri;

public class FlancerProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FlancerDbHelper mOpenHelper;

    static final int JOB = 100;
    static final int JOB_WITH_ID = 101;
    static final int FREELANCER = 200;
    static final int FREELANCER_WITH_ID = 201;
    static final int COMPANY = 300;
    static final int COMPANY_WITH_ID = 301;

    private static final SQLiteQueryBuilder sJobByIdQueryBuilder;
    private static final SQLiteQueryBuilder sCompanyByIdQueryBuilder;
    private static final SQLiteQueryBuilder sFreelancerByIdQueryBuilder;

    static{
        sJobByIdQueryBuilder = new SQLiteQueryBuilder();
        sCompanyByIdQueryBuilder = new SQLiteQueryBuilder();
        sFreelancerByIdQueryBuilder = new SQLiteQueryBuilder();

        sJobByIdQueryBuilder.setTables(
                FlancerContract.JobEntry.TABLE_NAME + " INNER JOIN " +
                        FlancerContract.CompanyEntry.TABLE_NAME +
                        " ON " + FlancerContract.JobEntry.TABLE_NAME +
                        "." + FlancerContract.JobEntry.COLUMN_COMPANY_KEY +
                        " = " + FlancerContract.JobEntry.TABLE_NAME +
                        "." + FlancerContract.JobEntry.COLUMN_ID);

        sCompanyByIdQueryBuilder.setTables(
                FlancerContract.CompanyEntry.TABLE_NAME);

        sFreelancerByIdQueryBuilder.setTables(
                FlancerContract.FreelancerEntry.TABLE_NAME);
    }

    private static final String sJobByIdSelection =
            FlancerContract.JobEntry.TABLE_NAME+
                    "." + FlancerContract.JobEntry.COLUMN_ID + " = ? ";

    private static final String sCompanyByIdSelection =
            FlancerContract.CompanyEntry.TABLE_NAME+
                    "." + FlancerContract.JobEntry.COLUMN_ID + " = ? ";

    private static final String sFreelancerByIdSelection =
            FlancerContract.FreelancerEntry.TABLE_NAME+
                    "." + FlancerContract.FreelancerEntry.COLUMN_ID + " = ? ";

    private Cursor getJobById(Uri uri, String[] projection, String sortOrder) {
        String jobId = FlancerContract.JobEntry.getJobFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sJobByIdSelection;
        selectionArgs = new String[]{jobId};

        return sJobByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getCompanyById(Uri uri, String[] projection, String sortOrder) {
        String jobId = FlancerContract.CompanyEntry.getCompanyFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sCompanyByIdSelection;
        selectionArgs = new String[]{jobId};

        return sCompanyByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getFreelancerById(Uri uri, String[] projection, String sortOrder) {
        String jobId = FlancerContract.FreelancerEntry.getFreelancerFromUri(uri);

        String[] selectionArgs;
        String selection;

        selection = sFreelancerByIdSelection;
        selectionArgs = new String[]{jobId};

        return sFreelancerByIdQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FlancerContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FlancerContract.PATH_JOB, JOB);
        matcher.addURI(authority, FlancerContract.PATH_JOB + "/*", JOB_WITH_ID);

        matcher.addURI(authority, FlancerContract.PATH_FREELANCER, FREELANCER);
        matcher.addURI(authority, FlancerContract.PATH_FREELANCER + "/*", FREELANCER_WITH_ID);

        matcher.addURI(authority, FlancerContract.PATH_COMPANY, COMPANY);
        matcher.addURI(authority, FlancerContract.PATH_COMPANY + "/*", COMPANY_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FlancerDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case JOB_WITH_ID:
                return FlancerContract.JobEntry.CONTENT_ITEM_TYPE;
            case FREELANCER_WITH_ID:
                return FlancerContract.FreelancerEntry.CONTENT_ITEM_TYPE;
            case COMPANY_WITH_ID:
                return FlancerContract.CompanyEntry.CONTENT_ITEM_TYPE;
            case JOB:
                return FlancerContract.JobEntry.CONTENT_TYPE;
            case FREELANCER:
                return FlancerContract.FreelancerEntry.CONTENT_TYPE;
            case COMPANY:
                return FlancerContract.CompanyEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "job/*"
            case JOB_WITH_ID:
            {
                retCursor = getJobById(uri, projection, sortOrder);
                break;
            }
            // "freelancer/*"
            case FREELANCER_WITH_ID:
            {
                retCursor = getFreelancerById(uri, projection, sortOrder);
                break;
            }
            // "company/*"
            case COMPANY_WITH_ID: {
                retCursor = getCompanyById(uri, projection, sortOrder);
                break;
            }
            // "job"
            case JOB: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FlancerContract.JobEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "freelancer"
            case FREELANCER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FlancerContract.FreelancerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "company"
            case COMPANY: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        FlancerContract.CompanyEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case JOB: {
                long _id = db.insert(FlancerContract.JobEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FlancerContract.JobEntry.buildJobUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case FREELANCER: {
                long _id = db.insert(FlancerContract.FreelancerEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FlancerContract.FreelancerEntry.buildFreelancerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case COMPANY: {
                long _id = db.insert(FlancerContract.CompanyEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = FlancerContract.CompanyEntry.buildCompanyUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (null == selection) selection = "1";

        switch (match) {
            case JOB:
                rowsDeleted = db.delete(
                        FlancerContract.JobEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case FREELANCER:
                rowsDeleted = db.delete(
                        FlancerContract.FreelancerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case COMPANY:
                rowsDeleted = db.delete(
                        FlancerContract.CompanyEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case JOB:
                rowsUpdated  = db.update(
                        FlancerContract.JobEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FREELANCER:
                rowsUpdated  = db.update(
                        FlancerContract.FreelancerEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case COMPANY:
                rowsUpdated  = db.update(
                        FlancerContract.CompanyEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case JOB:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FlancerContract.JobEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}