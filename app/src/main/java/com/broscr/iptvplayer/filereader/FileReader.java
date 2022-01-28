package com.broscr.iptvplayer.filereader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.broscr.iptvplayer.R;
import com.broscr.iptvplayer.database.IPTvRealm;
import com.broscr.iptvplayer.models.Channel;
import com.broscr.iptvplayer.ui.activitys.main.MainActivity;
import com.broscr.iptvplayer.utils.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class FileReader {
    private final String EXT_INF_SP = "#EXTINF:-1";
    private final String KOD_IP_DROP_TYPE = "#KODIPROP:inputstream.adaptive.license_type=";
    private final String KOD_IP_DROP_KEY = "#KODIPROP:inputstream.adaptive.license_key=";
    private final String TVG_NAME = "tvg-name=";
    private final String TVG_LOGO = "tvg-logo=";
    private final String GROUP_TITLE = "group-title=";
    private final String COMMA = ",";
    private final String HTTP = "http://";
    private final String HTTPS = "https://";
    private final Uri fileName;
    private final List<Channel> channelList;
    private final Activity activity;

    public FileReader(Activity activity, Uri fileName) {
        this.activity = activity;
        this.fileName = fileName;
        this.channelList = new ArrayList<>();
    }

    public void readFile() {
        try {
            InputStream inputStreamReader = activity.getContentResolver().openInputStream(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamReader));

            String currentLine;

            Channel channel = new Channel();
            while ((currentLine = bufferedReader.readLine()) != null) {
                currentLine = currentLine.replaceAll("\"", "");
                if (currentLine.startsWith(KOD_IP_DROP_TYPE)) {
                    channel.setChannelDrmType(currentLine.split(KOD_IP_DROP_TYPE)[1].trim());
                    continue;
                }

                if (currentLine.startsWith(KOD_IP_DROP_KEY)) {
                    channel.setChannelDrmKey(currentLine.split(KOD_IP_DROP_KEY)[1].trim());
                    continue;
                }

                if (currentLine.startsWith(EXT_INF_SP)) {
                    channel.setChannelName(currentLine.split(TVG_NAME).length > 1 ?
                            currentLine.split(TVG_NAME)[1].split(TVG_LOGO)[0] :
                            currentLine.split(COMMA)[1]);
                    channel.setChannelGroup(currentLine.split(GROUP_TITLE)[1].split(COMMA)[0]);
                    channel.setChannelImg(currentLine.split(TVG_LOGO).length > 1 ?
                            currentLine.split(TVG_LOGO)[1].split(GROUP_TITLE)[0] : "");
                    continue;
                }

                if (currentLine.startsWith(HTTP) || currentLine.startsWith(HTTPS)) {
                    channel.setChannelUrl(currentLine);
                    channelList.add(channel);
                    channel = new Channel();
                }

            }

        } catch (IOException e) {
            Timber.e(e);
            Helper.showToast(activity, e.getLocalizedMessage());
        } finally {
            if (channelList.size() > 0) {

                if (new IPTvRealm().channelListSave(channelList)) {
                    activity.startActivity(new Intent(activity, MainActivity.class));
                    activity.finish();
                } else {
                    Helper.showToast(activity, activity.getString(R.string.error_save_list));
                }

                Helper.showToast(activity, activity.getString(R.string.success_file_read));

            } else {
                Helper.showToast(activity, activity.getString(R.string.error_file_read_exp));
            }
        }
    }
}
