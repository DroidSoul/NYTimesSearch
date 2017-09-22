package com.droidsoul.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.loopj.android.http.RequestParams;

/**
 * Created by bear&bear on 9/19/2017.
 */
public class Query implements Parcelable {
    String API_KEY = "024e1d6494004a9c99dce62512b54af3";
    String queryStr;
    int page;
    String sortOrder;
    String newsdeskFilter;
    String beginDate;

    public Query() {

    }

    public void setNewsdeskFilter(String newsdeskFilter) {
        this.newsdeskFilter = newsdeskFilter;
    }

    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    public String getQueryStr() {
        return queryStr;
    }

    public int getPage() {
        return page;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public String getNewsdeskFilter() {
        return newsdeskFilter;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public Query(String queryStr, int page, String sortOrder, String newsdeskFilter, String beginDate) {
        this.queryStr = queryStr;
        this.page = page;
        this.sortOrder = sortOrder;
        this.newsdeskFilter = newsdeskFilter;
        this.beginDate = beginDate;
    }
    //no filter applied
    public Query(String queryStr) {
        this.queryStr = queryStr;
    }

    public RequestParams getParams() {
        RequestParams params = new RequestParams();
        params.put("q", queryStr);
        params.put("api-key", API_KEY);
        params.put("page", page);
        if (sortOrder != null) {
            params.put("sort", sortOrder);
        }
        if (newsdeskFilter != null) {
            params.put("fq", "news_desk:(" + newsdeskFilter + ")");
        }
        if (beginDate != null) {
            params.put("begin_date", beginDate);
        }
        return params;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.API_KEY);
        dest.writeString(this.queryStr);
        dest.writeInt(this.page);
        dest.writeString(this.sortOrder);
        dest.writeString(this.newsdeskFilter);
        dest.writeString(this.beginDate);
    }

    protected Query(Parcel in) {
        this.API_KEY = in.readString();
        this.queryStr = in.readString();
        this.page = in.readInt();
        this.sortOrder = in.readString();
        this.newsdeskFilter = in.readString();
        this.beginDate = in.readString();
    }

    public static final Parcelable.Creator<Query> CREATOR = new Parcelable.Creator<Query>() {
        @Override
        public Query createFromParcel(Parcel source) {
            return new Query(source);
        }

        @Override
        public Query[] newArray(int size) {
            return new Query[size];
        }
    };
}
