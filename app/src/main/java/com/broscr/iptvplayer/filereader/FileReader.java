package com.broscr.iptvplayer.filereader;

import android.content.Context;

public class FileReader {

    //#EXTM3U
//#EXTINF:-1 tvg-id="" tvg-name="===((  TR SPOR  ))===" tvg-logo="" group-title="TR SPOR",===((  TR SPOR  ))===
// http://custom.url/username/password/23640
//#EXTINF:-1 tvg-id="" tvg-name="Bein sport  HD" tvg-logo="http://customurl/images/95235e9c030dde800eaa38740e09457a.png"
// group-title="TR SPOR",Bein sport  HD
// http://customurl/username/password/23679
//#EXTINF:-1 tvg-id="tr.TR| BEIN SPORT 1 HD" tvg-name="Bein sport HD +" tvg-logo="http://customurl/images/95235e9c030dde800eaa38740e09457a.png"
// group-title="TR SPOR",Bein sport HD +
    private Context context;
    private String fileName;

    public FileReader(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public void readFile() {

    }
}
