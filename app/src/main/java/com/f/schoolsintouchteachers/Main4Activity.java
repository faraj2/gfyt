package com.f.schoolsintouchteachers;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class Main4Activity extends AppCompatActivity {
    Myfragments myfragments;
    Profilefragment profilefragment;
    CoordinatorLayout frameLayout;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        ViewPager viewPager=findViewById(R.id.vp_vertical_ntb);
        RelativeLayout relativeLayout=findViewById(R.id.back);
        relativeLayout.addView(new BlueView(this));

        myfragments=new Myfragments(getSupportFragmentManager());
        viewPager.setAdapter(myfragments);

profilefragment=new Profilefragment();
        final String[] colors = getResources().getStringArray(R.array.colors);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb);
        frameLayout=findViewById(R.id.frame1);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.hom),
                        Color.parseColor(colors[0]))
                        .title("WALL")
                        //.selectedIcon(getResources().getDrawable(R.drawable.ic_launcher_background))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.profl),
                        Color.parseColor(colors[1]))
                      //  .selectedIcon(getResources().getDrawable(R.drawable.ic_launcher_background))
                        .title("PROFILE")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.inbox),
                        Color.parseColor(colors[2]))
                       // .selectedIcon(getResources().getDrawable(R.drawable.ic_launcher_background))
                        .title("INBOX")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.report),
                        Color.parseColor(colors[3]))
                      //  .selectedIcon(getResources().getDrawable(R.drawable.ic_launcher_background))
                        .title("REPORTS")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.settings),
                        Color.parseColor(colors[4]))
                   //     .selectedIcon(getResources().getDrawable(R.drawable.ic_launcher_background))
                        .title("SETTINGS")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.bell),
                        Color.parseColor(colors[5]))

                       // .selectedIcon(getResources().getDrawable(R.drawable.ic_launcher_background))
                        .title("REMINDER")
                        .build()
        );
//        models.add(
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.ic_action_user),
//                        Color.parseColor(colors[6]))
//                     //   .selectedIcon(getResources().getDrawable(R.drawable.ic_launcher_background))
//                        .title("ic_seventh")
//                        .build()
//        );
//        models.add(
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.ic_action_user),
//                        Color.parseColor(colors[7]))
//                      //  .selectedIcon(getResources().getDrawable(R.drawable.ic_launcher_background))
//                        .title("ic_eighth")
//                        .build()
//        );

        navigationTabBar.setModels(models);
       navigationTabBar.setViewPager(viewPager,6);





        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                Log.e("INDEX",String.valueOf(index));
                snackbar= Snackbar.make(frameLayout,model.getTitle(),Snackbar.LENGTH_SHORT);
               if (snackbar.isShown()){
                   snackbar.dismiss();
                   View view = snackbar.getView();
                   TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
                 //  tv.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.red_EC1C24));
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                       tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                   } else {
                       tv.setGravity(Gravity.CENTER_HORIZONTAL);
                   }

                   snackbar.show();

               }else {

                   View view = snackbar.getView();
                   TextView tv = view.findViewById(android.support.design.R.id.snackbar_text);
                   //  tv.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.red_EC1C24));
                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                       tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                   } else {
                       tv.setGravity(Gravity.CENTER_HORIZONTAL);
                   }

                   snackbar.show();
               }

            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {

            }
        });

    }
    public void browsepic(View view){
        profilefragment.myclickmethod(view);
    }
    class Myfragments extends FragmentPagerAdapter{

        public Myfragments(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new Homefragment();
                case 1:
                    Profilefragment profilefragment=new Profilefragment();
                    return profilefragment.getInstance(getParent());
                case 2 :
                    return new Inboxfragment();
                case 3:
                    return new Reportfr();

                case 4:
                    return new Settingfragment();
                case 5 :
                    return new Reminderfragment();
                    default:
                        return new Homefragment();

            }

        }

        @Override
        public int getCount() {
            return 6;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("CLOSED","MAINACTIVITY4");
    }
}
