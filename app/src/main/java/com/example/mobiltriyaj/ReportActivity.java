package com.example.mobiltriyaj;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiltriyaj.Class.Profile;
import com.example.mobiltriyaj.Class.QuestionAnswerItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    private List<Object> itemList = new ArrayList<>();
    private Profile profile;
    private String selected_lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        selected_lang = getIntent().getStringExtra("selected_lang");
        profile = getIntent().getParcelableExtra("profile");
        ArrayList<String> questionsAndAnswers = getIntent().getStringArrayListExtra("questionsAndAnswers");

        // Profile nesnesini listeye ekle
        itemList.add(profile);

        // Soru ve cevapları listeye ekle
        for (String qa : questionsAndAnswers) {
            String[] parts = qa.split(":");
            if (parts.length >= 2) {
                String question = parts[0].trim();
                String answer = parts[1].trim();
                itemList.add(new QuestionAnswerItem(question, answer));
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new QuestionAnswerAdapter(itemList,selected_lang));

        Button savePdfButton = findViewById(R.id.save_pdf_button);
        savePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndOpenPdf(selected_lang);
            }
        });
    }

    private void saveAndOpenPdf(String selected_lang) {
        try {
            String pdfPath = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/report.pdf";
            PdfUtils.createPdf(pdfPath, itemList,selected_lang);
            openPdf(pdfPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPdf(String filePath) {
        File pdfFile = new File(filePath);
        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", pdfFile);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }
}