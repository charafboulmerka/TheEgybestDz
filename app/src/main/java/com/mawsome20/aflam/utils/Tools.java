package com.mawsome20.aflam.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;

import com.mawsome20.aflam.BuildConfig;
import com.mawsome20.aflam.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Tools {

    public static int dpToPx(Context c, int dp) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static void share(Context context, String body) {
        String url = "http://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID;
        String shareBody = "";
        if (body.equals("")){
             shareBody ="\n\n"+ context.getString(R.string.share_body)+"\n\n" +"\n"+ "رابط التطبيق على جوجل بلاي"+"\n"+ url;
        }
        else{
            shareBody = "شاهد الآن "+body+" المشوق على تطبيق The EgyBes"+"\n\n"+"رابط التطبيق على جوجل بلاي:"+"\n\n"+url;
        }

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareSub = "مشاركة مع الأصدقاء";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        context.startActivity(Intent.createChooser(sharingIntent, "النشر على "));
    }

    public static Boolean isValidUrlForPlayer(String type){
        if (type.equals("embed") || type.equals("vimeo") || type.equals("gdrive") || type.equals("youtube-live")){
            return false;
        }
        else return true;
    }

    public static String getTimeNow(){
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        String date = df.format(new Date());
        return date;
    }



    public static String byteToMb(long bytes) {
        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        long gigabyte = megabyte * 1024;
        long terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return bytes + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return (bytes / kilobyte) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return (bytes / megabyte) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return (bytes / gigabyte) + " GB";

        } else if (bytes >= terabyte) {
            return (bytes / terabyte) + " TB";

        } else {
            return bytes + " Bytes";
        }
    }

    public static String milliToDate(long millisecond) {
        Date date = new Date(millisecond);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy hh:ss a");
        return dateFormat.format(date);
    }

    public static boolean isValidFormat(String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }


    public static String insertPeriodically(String text, String insert, int period) {
        StringBuilder builder = new StringBuilder(text.length() + insert.length() * (text.length() / period) + 1);
        int index = 0;
        String prefix = "";
        while (index < text.length()) {
            builder.append(prefix);
            prefix = insert;
            builder.append(text.substring(index, Math.min(index + period, text.length())));
            index += period;
        }
        return builder.toString();
    }
/*
   public String getArName(String old_name){
        String new_title="";
        switch (old_name.trim()){
            case "Action":
                new_title="أكشن";
                break;

            case "Adventure":
                new_title="مغامرة";
                break;

            case "Comedy":
                new_title="كوميدي";
                break;

            case "Crime":
                new_title="جريمة";
                break;

            case "Drama":
                new_title="دراما";
                break;

            case "Fantasy":
                new_title="خيال";
                break;

            case "Horror":
                new_title="رعب";
                break;

            case "Mystery":
                new_title="لـغز";
                break;

            case "Romance":
                new_title="رومانسي";
                break;

            case "Thriller":
                new_title="إثارة";
                break;

            case "Si-Fi":
                new_title="خيال العلمي";
                break;

            case "Animation":
                new_title="أنـمـي";
                break;

            case "Biography":
                new_title="سيرة شخصية";
                break;

            case "Documentary":
                new_title="وثائقي";
                break;

            case "Family":
                new_title="أسرة";
                break;


            case "Music":
                new_title="موسيقى";
                break;

            case "Musical":
                new_title="موسيقي";
                break;

            case "History":
                new_title="تاريخي";
                break;

            case "War":
                new_title="حرب";
                break;



            case "TV Series":
                new_title="مسلسل تلفزيوني";
                break;

            case "Short":
                new_title="قصير";
                break;

            case "Sport":
                new_title="رياضة";
                break;

            case "Action & Adventure":
                new_title="أكشن و مغامرة";
                break;

            case "Sci-Fi & Fantasy":
                new_title="خيال علمي";
                break;

            case "Science Fiction":
                new_title="خيالي علمي";
                break;

            case "Sci-Fi":
                new_title="خيالي علمي";
                break;

            case "TV Movie":
                new_title="فيلم تلفزيوني";
                break;

            case "News":
                new_title="أخبار";
                break;

            case "Reality":
                new_title="واقعي";
                break;


            case "Kids":
                new_title="أطفال";
                break;



            case "Historical fiction":
                new_title="خيالي تاريخي";
                break;



            case "Philosophical":
                new_title="فلسفي";
                break;

            case "Political":
                new_title="سياسي";
                break;

            case "Saga":
                new_title="قصة طويلة";
                break;



            case "Social":
                new_title="اجتماعي";
                break;

            case "Speculative":
                new_title="تخميني";
                break;

            case "Urban":
                new_title="حضاري";
                break;

            case "Film-Noir":
                new_title="دراما و جريمة قديمة";
                break;


            case "Magical realism":
                new_title="السحر الواقعي";
                break;

            case "Paranoid fiction":
                new_title="خيالي";
                break;

            case "Satire":
                new_title="هجاء";
                break;

            case "Adult":
                new_title="للكبار";
                break;

            case "Western":
                new_title="واستيرن";
                break;

            default:
                new_title=old_name;
                break;
        }
        return new_title;
    }



 */

    public static List<String> DeletedGeners(){
        return Arrays.asList("music","si-fi","tv series","sci-fi & fantasy","paranoid fiction","sci-fi","tv movie","reality-tv","adult");
    }



    public static String getArName(String old_name){
        String new_title="";
        switch (old_name.trim().toLowerCase()){
            case "action":
                new_title="أكشن";
                break;

            case "adventure":
                new_title="مغامرة";
                break;

            case "comedy":
                new_title="كوميدي";
                break;

            case "crime":
                new_title="جريمة";
                break;

            case "drama":
                new_title="دراما";
                break;

            case "fantasy":
                new_title="خيال";
                break;

            case "horror":
                new_title="رعب";
                break;

            case "mystery":
                new_title="غموض";
                break;

            case "romance":
                new_title="رومانسي";
                break;

            case "thriller":
                new_title="إثارة";
                break;


            case "animation":
                new_title="أنـمـي";
                break;


            case "biography":
                new_title="سيرة شخصية";
                break;

            case "documentary":
                new_title="وثائقي";
                break;

            case "family":
                new_title="عائلي";
                break;

            case "musical":
                new_title="موسيقي";
                break;

            case "history":
                new_title="تاريخي";
                break;

            case "war":
                new_title="حرب";
                break;


            case "short":
                new_title="قصير";
                break;

            case "sport":
                new_title="رياضة";
                break;

            case "action & adventure":
                new_title="أكشن و مغامرة";
                break;



            case "science fiction":
                new_title="خيالي علمي";
                break;


            case "news":
                new_title="أخبار";
                break;

            case "reality":
                new_title="واقعي";
                break;


            case "kids":
                new_title="أطفال";
                break;



            case "historical fiction":
                new_title="خيال تاريخي";
                break;


            case "philosophical":
                new_title="فلسفي";
                break;

            case "political":
                new_title="سياسي";
                break;

            case "saga":
                new_title="قصة طويلة";
                break;



            case "social":
                new_title="اجتماعي";
                break;

            case "speculative":
                new_title="تخميني";
                break;

            case "urban":
                new_title="حضاري";
                break;

            case "film-noir":
                new_title="دراما و جريمة قديمة";
                break;


            case "magical realism":
                new_title="السحر الواقعي";
                break;


            case "satire":
                new_title="هجاء";
                break;

            case "adult":
                new_title="للكبار";
                break;

            case "western":
                new_title="واستيرن";
                break;

            default:
                new_title=old_name;
                break;
        }
        return new_title;
    }

    public static String getAdNetwork(Context ctx){
        return  "";

    }
}
