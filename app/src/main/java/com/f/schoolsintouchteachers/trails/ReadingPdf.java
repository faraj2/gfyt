package com.f.schoolsintouchteachers.trails;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.IOException;
import java.util.ArrayList;

public class ReadingPdf {
    public static void readfromdoc(Activity activity) throws IOException {
        ArrayList<String> schools = new ArrayList<>();
        ArrayList<String> codes=new ArrayList<>();

        PdfReader reader = new PdfReader(activity.getAssets().open(""));
        //  PrintWriter out = new PrintWriter(new FileOutputStream(Environment.getExternalStorageDirectory()+""));
        Rectangle rect = new Rectangle(100, 250, 530, 450);
        RenderFilter filter = new RegionTextRenderFilter(rect);
        TextExtractionStrategy strategy;
        int k = 1;
        int j = 0;
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            if (i > 2 + j) {
                j = 2;
                i += j;
                j = i;
            } else {
                strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
                if (k % 2 == 0) {
//                    String[] arr = PdfTextExtractor.getTextFromPage(reader, i, strategy).toLowerCase().split("\\s");
//                    for (String s : arr) {
//                     //   Log.d("MOE", s);
////
                    k++;
                } else {

                    String[] arr = PdfTextExtractor.getTextFromPage(reader, i, strategy).split("\\s+");
                    StringBuilder summary = new StringBuilder();
                    for (String s : arr) {

                        if (s.matches("(([A-Z]){2,}\\.\\w+([A-Z]){2,}'\\w+)|([A-Z]){2,}\\.\\w+|([A-Z]){2,}'\\w+|([A-Z]){2,}\\w+") && !s.equals("MOE")) {
                            if (s.startsWith("SCHO") || s.startsWith("SEC") || s.startsWith("HIGH")) {
                                summary.append(s);
                                schools.add(summary.toString());
                                summary = new StringBuilder();

                            } else {
                                summary.append(s).append(" ");

                            }
//Log.d("SCHOOLS",s);

                        }else if (s.startsWith("3")){
                            codes.add(s);
                        }
                    }
                    k++;
                }
                //  System.out.println(PdfTextExtractor.getTextFromPage(reader, i, strategy));


            }

        }
        Log.d("UNUS", String.valueOf(schools.size()));
        for (int f=0;f<schools.size();f++){
//            if (schools.get(f).endsWith("SEC") || schools.get(f).endsWith("SECONDARY")|
//                    schools.get(f).endsWith("SCH")|schools.get(f).endsWith("SCHOOL")
//                    |schools.get(f).endsWith("HIGH"))
//            {
//                Log.d("UNUSUAL",schools.get(f));
//            }else {
//                Log.d("USUAL",schools.get(f));
//            }
            if (schools.get(f).startsWith("SCH")){
                schools.set(f,schools.get(f).replace("SCHOOL","").trim());
                schools.set(f,schools.get(f).replace("SCH","").trim());
                if (schools.get(f).startsWith("SEC") || TextUtils.isEmpty(schools.get(f))){
                    schools.remove(f);
                    // schools.set(f,schools.get(f).replace("SCH","").trim());
                    //  schools.set(f,schools.get(f).replace("SCHOOL","").trim());
                }
                // schools.remove(string);
            }else if (schools.get(f).startsWith("SEC")){
                schools.set(f,schools.get(f).replace("SECONDARY","").trim());
                schools.set(f,schools.get(f).replace("SEC","").trim());
                if (schools.get(f).startsWith("SCH") || TextUtils.isEmpty(schools.get(f))){
                    schools.remove(f);
                    // schools.set(f,schools.get(f).replace("SCH","").trim());
                    //  schools.set(f,schools.get(f).replace("SCHOOL","").trim());
                }
            }else if (schools.get(f).startsWith("HIGH")){
                schools.set(f,schools.get(f).replace("HIGH","").trim());
                //  schools.set(f,schools.get(f).replace("SEC",""));
            }
            Log.d("MOE", schools.get(f));


        }

        for (int v=0;v<schools.size();v++) {
            if (TextUtils.isEmpty(schools.get(v))) {
                schools.remove(v);
            }
        }
//           // v++;
////            FirebaseDatabase.getInstance().getReference().child("Schools").child(String.valueOf(v)).child("name")
////                    .setValue(s);
////            v++;
//        }
        Log.d("ARRAYLISTSIZE","names: "+ String.valueOf(schools.size())+"codes: "+String.valueOf(codes.size()));
//        out.flush();
//        out.close();
        reader.close();
    }
}
