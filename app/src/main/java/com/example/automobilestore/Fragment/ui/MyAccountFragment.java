package com.example.automobilestore.Fragment.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.automobilestore.Activity.Add_ons;
import com.example.automobilestore.Activity.ForgotPassword;
import com.example.automobilestore.Activity.PostList;
import com.example.automobilestore.Activity.ProfileDetails;
import com.example.automobilestore.Activity.Services;
import com.example.automobilestore.Activity.WebViewCustom;
import com.example.automobilestore.MainActivity;
import com.example.automobilestore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

///**
// * A simple {@link Fragment} subclass.
// * Use the {@link MyAccountFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class MyAccountFragment extends PreferenceFragmentCompat {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "";
    CircleImageView ivProfile;
    TextView tvPersonName;
    LinearLayout llProfile;
    FirebaseUser fUser;
    private FirebaseAuth auth;
    private FirebaseFirestore mFirebaseFirestore;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        LoadLocal();
        setPreferencesFromResource(R.xml.preferences, rootKey);
//        LoadLocal();
        Preference profile = findPreference("profile");
        Preference security = findPreference("security");
        Preference manage = findPreference("manage");
        Preference logout = findPreference("logout");
        Preference add_ons = findPreference("Add-Ons");
        Preference services = findPreference("Services");
        Preference lang = findPreference("Language");


        profile.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getActivity(), ProfileDetails.class);
                startActivity(i);
                return true;
            }
        });

        security.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(getActivity(), ForgotPassword.class);
                startActivity(i);
                return true;
            }
        });
        manage.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getActivity(), PostList.class);
                startActivity(i);
//                Toast.makeText(getActivity().getApplicationContext(), "Manage", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
                Toast.makeText(getActivity(), "Logout Successfully", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        add_ons.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                Intent i = new Intent(getActivity(), Add_ons.class);
                startActivity(i);

                return true;
            }
        });
        services.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent i = new Intent(getActivity(), Services.class);
                startActivity(i);

                return true;
            }
        });
        lang.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showlanguageDialog();

//                final String[] langList={"English","français"};
//                AlertDialog.Builder mybuilder= new AlertDialog.Builder(getContext());
//                mybuilder.setTitle("Choose Language..");
//                mybuilder.setSingleChoiceItems(langList, -1, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if(which==0){
//                            setLocal("en");
//
//
//                            recreate(getActivity());
//                        }else if(which==1){
//                            setLocal("fr");
//                            recreate(getActivity());
//                        }
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog mDialog=mybuilder.create();
//                mDialog.show();
               return false;
            }
        });
    }

    private void showlanguageDialog() {
        setLocal("fr");
        final String[] langList={"English","français"};
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());

        builder.setTitle("Choose Language..");
        builder.setSingleChoiceItems(langList, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    setLocal("en");
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);

                }else if(which==1){
                    setLocal("fr");
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                }
                dialog.dismiss();
            }
        });
        AlertDialog mDialog=builder.create();
        mDialog.show();
    }

    private void setLocal(String lang) {
        Locale locale=new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration=new Configuration();
        configuration.locale=locale;
        getContext().getResources().updateConfiguration(configuration,getContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor= getActivity().getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("My_lang",lang);
        editor.apply();
    }
    public void LoadLocal(){
        SharedPreferences prefs=getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String Language=prefs.getString("My_lang","");
        setLocal(Language);
    }
}