package com.notes.app.Methods;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import es.dmoral.toasty.Toasty;
import com.notes.app.Constant.Constant;
import com.notes.app.R;
import com.notes.app.entities.Note;
import com.notes.app.listeners.InterAdListener;


public class Methods {

    // Ресурстар мен жүйелік қызметтерге қол жеткізу үшін қолданылатын қосымшаның контексті
    private Context context;
    // Интерстициалды жарнамалық іс-шараларды тыңдаушы
    private InterAdListener interAdListener;
    // Google-дың интерстициальды жарнама нысаны
    private InterstitialAd interstitialAd;
    // Facebook-тің интерстициальды жарнама нысаны
    private com.facebook.ads.InterstitialAd interstitialAdFB;

    // Қолданбалы контекстпен инициализациялауға арналған конструктор
    public Methods(Context context) {
        this.context = context;
    }

    // Мәтінмәнмен және жарнама тыңдаушысымен инициализациялауға арналған шамадан тыс жүктелген конструктор
    public Methods(Context context, InterAdListener interAdListener) {
        this.context = context;
        this.interAdListener = interAdListener; // Осы конструктор пайдаланылған кезде жарнамаларды жүктеңіз
        loadad();
    }

    // Toasty library көмегімен сәттілік немесе қате туралы хабарды көрсету әдісі
    public void showSnackBar(String message, String type) {
        if (type.equals("success")){
            Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
        }else {
            Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
        }
    }

    // Желінің қолжетімділігін тексеру әдісі
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Жарнаманы жекелендіруге пайдаланушының келісімі негізінде жарнамаларды жүктеу әдісі
    private void loadad() {
        if (Constant.isAdmobInterAd) {
            interstitialAd = new InterstitialAd(context);
            AdRequest adRequest;
            if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.PERSONALIZED) {
                adRequest = new AdRequest.Builder()
                        .build();
            } else {
                Bundle extras = new Bundle();
                extras.putString("npa", "1");   // Жекелендірілмеген жарнама жалауы
                adRequest = new AdRequest.Builder()
                        .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                        .build();
            }
            interstitialAd.setAdUnitId(context.getString(R.string.interstitia_ad_unit_id));
            interstitialAd.loadAd(adRequest);
        } else if (Constant.isFBInterAd) {
            interstitialAdFB = new com.facebook.ads.InterstitialAd(context, context.getString(R.string.fb_ad_interstitia_id));
            interstitialAdFB.loadAd();
        }
    }

    // Интерстициальды жарнаманы көрсету немесе жарнама жүктелмеген жағдайда әрекетті орындау әдісі
    public void showInter(final int pos, final Note note, final String type) {
        if (Constant.isAdmobInterAd) {

                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                } else {
                    interAdListener.onClick(pos, note, type);   // Әрекетті жарнамасыз орындаңыз
                }
                loadad();   // Келесі жарнаманы жүктеңіз
            } else {
                interAdListener.onClick(pos, note, type);   // Әрекетті жарнамасыз орындаңыз
            }
        }

    // Баннерлік жарнамаларды Сызықтық Қабатта көрсету әдісі
    public void showBannerAd(LinearLayout linearLayout) {
        if (isNetworkAvailable() && linearLayout != null) {
            if (ConsentInformation.getInstance(context).getConsentStatus() == ConsentStatus.NON_PERSONALIZED) {
                showNonPersonalizedAds(linearLayout);
            } else {
                showPersonalizedAds(linearLayout);
            }
        }
    }

    // Жекелендірілген баннер жарнамаларын көрсетудің көмекші әдісі
    private void showPersonalizedAds(LinearLayout linearLayout) {
        if (Constant.isAdmobBannerAd) {
            AdView adView = new AdView(context);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("0336997DCA346E1612B610471A578EEB").build();
            adView.setAdUnitId(context.getString(R.string.banner_ad_unit_id));
            adView.setAdSize(AdSize.SMART_BANNER);
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
        } else if (Constant.isFBBannerAd) {
            com.facebook.ads.AdView adView;
            adView = new com.facebook.ads.AdView(context, context.getString(R.string.fb_ad_banner_id), com.facebook.ads.AdSize.BANNER_HEIGHT_90);
            adView.loadAd();
            linearLayout.addView(adView);
        }
    }

    // Жекелендірілмеген баннерлік жарнамаларды көрсетудің көмекші әдісі
    private void showNonPersonalizedAds(LinearLayout linearLayout) {
        if (Constant.isAdmobBannerAd) {
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            AdView adView = new AdView(context);
            AdRequest adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
            adView.setAdUnitId(context.getString(R.string.banner_ad_unit_id));
            adView.setAdSize(AdSize.SMART_BANNER);
            linearLayout.addView(adView);
            adView.loadAd(adRequest);
        } else if (Constant.isFBBannerAd) {
            com.facebook.ads.AdView adView;
            adView = new com.facebook.ads.AdView(context, context.getString(R.string.fb_ad_banner_id), com.facebook.ads.AdSize.BANNER_HEIGHT_90);
            adView.loadAd();
            linearLayout.addView(adView);
        }
    }

}