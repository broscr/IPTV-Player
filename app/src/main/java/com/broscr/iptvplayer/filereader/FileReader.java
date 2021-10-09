package com.broscr.iptvplayer.filereader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.broscr.iptvplayer.R;
import com.broscr.iptvplayer.database.IPTvRealm;
import com.broscr.iptvplayer.models.Channel;
import com.broscr.iptvplayer.ui.activitys.MainActivity;
import com.broscr.iptvplayer.utils.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class FileReader {
    private final String EXT_M3U = "#EXTM3U";
    private final String EXT_INF_SP = "#EXTINF:-1";
    private final String TVG_NAME = "tvg-name=";
    private final String TVG_LOGO = "tvg-logo=";
    private final String GROUP_TITLE = "group-title=";
    private final String WHITE_SPACE = " ";
    private final String COMMA = ",";
    private final String HTTP = "http://";
    private final Uri fileName;
    private final List<Channel> channelList;
    private Activity activity;

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
            StringBuilder allText = new StringBuilder();
            while ((currentLine = bufferedReader.readLine()) != null) {
                allText.append(currentLine).append("\n");
            }

            fileSplitter(allText.toString());

        } catch (IOException e) {
            Timber.e(e);
            Helper.showToast(activity, e.getLocalizedMessage());
        }
    }

    private void fileSplitter(String fileText) {

        String[] fileLines = fileText.replaceAll("\n", " ").split(EXT_M3U)[1].split(EXT_INF_SP);

        try {
            for (String line : fileLines) {
                if (!line.equals(WHITE_SPACE)) {

                    String tvgName = line.split(TVG_NAME)[1].split(TVG_LOGO)[0];
                    String tvgLogo = line.split(TVG_LOGO)[1].split(GROUP_TITLE)[0];
                    String groupTitle = line.split(GROUP_TITLE)[1].split(COMMA)[0];

                    if (line.split(COMMA)[1].split(HTTP).length > 1) {
                        String url = line.split(COMMA)[1].split(HTTP)[1];

                        if (!tvgName.contains("===")) {

                            Channel channel = new Channel();
                            channel.setChannelName(tvgName.replaceAll("\"", ""));
                            channel.setChannelGroup(groupTitle.replaceAll("\"", ""));
                            channel.setChannelImg(tvgLogo.replaceAll("\"", ""));
                            channel.setChannelUrl((HTTP + url).replaceAll("\"", ""));
                            channelList.add(channel);
                        }
                    }

                }
            }

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

        } catch (Exception e) {
            Timber.e(e);
            Helper.showToast(activity, String.format(e.getMessage() != null ?
                    e.getMessage() : e.toString(), activity.getString(R.string.error_file_read)));
        }
    }
}
