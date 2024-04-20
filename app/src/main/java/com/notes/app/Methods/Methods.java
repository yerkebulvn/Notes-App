package com.notes.app.Methods;

import android.content.Context;

import android.widget.Toast;

import es.dmoral.toasty.Toasty;

import com.notes.app.entities.Note;
import com.notes.app.listeners.InterAdListener;


public class Methods {

    // Ресурстар мен жүйелік қызметтерге қол жеткізу үшін қолданылатын қосымшаның контексті
    private Context context;
    // Интерстициалды жарнамалық іс-шараларды тыңдаушы
    private InterAdListener interAdListener;
    // Google-дың интерстициальды жарнама нысаны

    // Қолданбалы контекстпен инициализациялауға арналған конструктор
    public Methods(Context context) {
        this.context = context;
    }

    // Мәтінмәнмен және жарнама тыңдаушысымен инициализациялауға арналған шамадан тыс жүктелген конструктор
    public Methods(Context context, InterAdListener interAdListener) {
        this.context = context;
        this.interAdListener = interAdListener; // Осы конструктор пайдаланылған кезде жарнамаларды жүктеңіз
    }

    // Toasty library көмегімен сәттілік немесе қате туралы хабарды көрсету әдісі
    public void showSnackBar(String message, String type) {
        if (type.equals("success")) {
            Toasty.success(context, message, Toast.LENGTH_SHORT, true).show();
        } else {
            Toasty.error(context, message, Toast.LENGTH_SHORT, true).show();
        }
    }

    // Интерстициальды жарнаманы көрсету немесе жарнама жүктелмеген жағдайда әрекетті орындау әдісі
    public void showInter(final int pos, final Note note, final String type) {

        interAdListener.onClick(pos, note, type);   // Әрекетті жарнамасыз орындаңыз

    }

}