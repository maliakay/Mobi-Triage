package com.example.mobiltriyaj;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobiltriyaj.Class.Option;
import com.example.mobiltriyaj.Class.Profile;
import com.example.mobiltriyaj.Class.QuestionAnswerItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TriageActivity extends AppCompatActivity {
    private List<String> userAnswers = new ArrayList<>();
    private List<String> questionsAndAnswersTr = new ArrayList<>();
    private List<String> questionsAndAnswersEn = new ArrayList<>();
    private int currentQuestionId = 1;
    private Profile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triage);

        profile = getIntent().getParcelableExtra("profile");
        String gender = profile.getCinsiyet();

        // Cinsiyete göre başlangıç sorusunu belirle
        if ("Erkek".equalsIgnoreCase(gender) || "Male".equalsIgnoreCase(gender)) {
            currentQuestionId = 4;
        } else if ("Kadın".equalsIgnoreCase(gender) || "Female".equalsIgnoreCase(gender)) {
            currentQuestionId = 2;
        } else {
            // Varsayılan başlangıç sorusu
            currentQuestionId = 1;
        }
        loadQuestion(currentQuestionId);
    }

    private void loadQuestion(int questionId) {
        String questionKey = "question_" + questionId;
        String optionsKey = "options_" + questionId;

        String questionTextTr = getLocalizedString(this, questionKey, new Locale("tr"));
        String questionTextEn = getLocalizedString(this, questionKey, Locale.ENGLISH);
        String[] optionsWithNextIdsTr = getLocalizedStringArray(this, optionsKey, new Locale("tr"));
        String[] optionsWithNextIdsEn = getLocalizedStringArray(this, optionsKey, Locale.ENGLISH);

        List<Option> options = new ArrayList<>();
        for (int i = 0; i < optionsWithNextIdsTr.length; i++) {
            String[] partsTr = optionsWithNextIdsTr[i].split("\\|");
            String[] partsEn = optionsWithNextIdsEn[i].split("\\|");
            options.add(new Option(partsTr[0].trim(), partsEn[0].trim(), Integer.parseInt(partsTr[1].trim())));
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, QuestionFragment.newInstance(questionTextTr, questionTextEn, options))
                .commit();
    }

    public void recordAnswer(String answerTr, String answerEn, int nextQuestionId) {
        userAnswers.add(answerTr);

        // Türkçe veriyi çekmek için getLocalizedString metodunu kullanıyoruz
        String currentQuestionTextTr = getLocalizedString(this, "question_" + currentQuestionId, new Locale("tr"));
        questionsAndAnswersTr.add(currentQuestionTextTr + ": " + answerTr);

        // İngilizce veriyi çekmek için de getLocalizedString metodunu kullanıyoruz
        String currentQuestionTextEn = getLocalizedString(this, "question_" + currentQuestionId, Locale.ENGLISH);
        questionsAndAnswersEn.add(currentQuestionTextEn + ": " + answerEn);

        if (nextQuestionId != -1) {
            currentQuestionId = nextQuestionId;
            loadQuestion(currentQuestionId);
        } else {
            showReport();
        }
    }

    private void showReport() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.select_language)
                .setItems(new String[]{"Türkçe", "English"}, (dialog, which) -> {
                    Intent intent = new Intent(this, ReportActivity.class);
                    intent.putExtra("profile", profile);
                    if (which == 0) {
                        intent.putExtra("selected_lang","tr");
                        intent.putStringArrayListExtra("questionsAndAnswers", (ArrayList<String>) questionsAndAnswersTr);
                    } else {
                        intent.putExtra("selected_lang","en");
                        intent.putStringArrayListExtra("questionsAndAnswers", (ArrayList<String>) questionsAndAnswersEn);
                    }
                    startActivity(intent);
                })
                .show();
    }

    private String getLocalizedString(TriageActivity triageActivity, String name, Locale locale) {
        Resources resources = triageActivity.getResources();
        Configuration conf = new Configuration(resources.getConfiguration());
        conf.setLocale(locale);
        Resources localizedResources = triageActivity.createConfigurationContext(conf).getResources();
        return localizedResources.getString(localizedResources.getIdentifier(name, "string", triageActivity.getPackageName()));
    }

    private String[] getLocalizedStringArray(TriageActivity triageActivity, String name, Locale locale) {
        Resources resources = triageActivity.getResources();
        Configuration conf = new Configuration(resources.getConfiguration());
        conf.setLocale(locale);
        Resources localizedResources = triageActivity.createConfigurationContext(conf).getResources();
        return localizedResources.getStringArray(localizedResources.getIdentifier(name, "array", triageActivity.getPackageName()));
    }
}