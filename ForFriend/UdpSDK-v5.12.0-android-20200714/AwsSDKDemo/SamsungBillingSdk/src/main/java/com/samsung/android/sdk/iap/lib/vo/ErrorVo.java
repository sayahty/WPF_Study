package com.samsung.android.sdk.iap.lib.vo;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorVo
{
    private int      mErrorCode    = 0;
    private String   mErrorString  = "";
    private String   mExtraString  = "";
    private boolean mShowDialog = false;

    public void setError( int _errorCode, String _errorString )
    {
        mErrorCode = _errorCode;
        mErrorString = _errorString;
    }

    public int getErrorCode()
    {
        return mErrorCode;
    }
    public String getErrorString()
    {
        return mErrorString;
    }
    
    public String getExtraString()
    {
        return mExtraString;
    }
    public void setExtraString( String _extraString )
    {
        mExtraString = _extraString;
    }

    public boolean isShowDialog() { return mShowDialog; }
    public void setShowDialog(boolean _showDialog) { mShowDialog = _showDialog; }


    public String dump()
    {
        //        String dump = "";
//
//        dump = "ErrorCode    : " + getErrorCode()    +  "\n" +
//               "ErrorString  : " + getErrorString()  +  "\n" +
//               "ExtraString  : " + getExtraString();
//
//        return dump;
        String dump = "";

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("mErrorCode",getErrorCode());
            jsonObject.put("mErrorString",getErrorString());
            jsonObject.put("mExtraString",getExtraString());
            dump = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            dump = "{'mErrorCode':'"+getErrorCode()+"',"+
                    "'mErrorString':'"+getErrorString()+"',"+
                    "'mExtraString':'"+getExtraString()+"'"+
                    "'}";
        }
        return dump;
    }
}
