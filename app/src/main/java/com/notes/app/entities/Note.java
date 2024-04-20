package com.notes.app.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes")
public class Note implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "date_time")
    private String dateTime;

    @ColumnInfo(name = "subtitle")
    private String Subtitle;

    @ColumnInfo(name = "note_text")
    private String noteText;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "web_link")
    private String webLink;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSubtitle() {
        return Subtitle;
    }

    public void setSubtitle(String subtitle) {
        Subtitle = subtitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWebLink() {
        return webLink;
    }

    public void setWebLink(String webLink) {
        this.webLink = webLink;
    }


    @NonNull
    @Override
    public String toString() {
        return title + " : " + dateTime;
    }
}

/*Бұл код Android қолданбасында Пайдалану үшін Note деп аталатын Room Нысанын анықтайды. Room - sqlite бойынша
абстракция қабатын қамтамасыз ететін табандылық кітапханасы.
@Entity Нысанының аннотациясы бұл сыныптың "notes" деп аталатын дерекқор кестесін көрсетеді.
Әрбір өріс (мысалы, тақырып, күн уақыты және т.б.)) мәліметтер базасының кестесіндегі бағанға сәйкес келеді.
@PrimaryKey(autoGenerate = true) аннотациясы идентификатор өрісінің негізгі кілт екенін және оның мәні автоматты
түрде жасалатынын көрсетеді.
@ColumnInfo (аты ="...") аннотациялар әр өріс үшін баған атауларын анықтайды.
даналарын сериялауға және дезериализациялауға мүмкіндік беру үшін Serializable жүзеге асырады.
ToString () әдісі тақырып және күн уақыты өрістерін біріктіретін пішімделген жолды қайтарады.
Тұтастай алғанда, бұл сынып Room көмегімен дерекқордан сақтауға және алуға болатын әртүрлі сипаттары (тақырыбы,
күні, субтитрі, мәтіні, кескін жолы, түсі және веб-сілтемесі) бар жазбаны білдіреді. ToString () әдісі күйін
келтіру немесе көрсету мақсатында жазбаның қысқаша көрінісін қамтамасыз етеді.*/