package com.example.mobiltriyaj;

import com.example.mobiltriyaj.Class.Profile;
import com.example.mobiltriyaj.Class.QuestionAnswerItem;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.util.List;

public class PdfUtils {

    public static void createPdf(String dest, List<Object> itemList,String selected_lang) throws Exception {
        File file = new File(dest);
        file.getParentFile().mkdirs();

        PdfWriter writer = new PdfWriter(dest);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document doc = new Document(pdfDoc);

        // Türkçe karakterleri destekleyen yazı tipini yükleyin
        PdfFont font = PdfFontFactory.createFont("/assets/Geliat-ExtraLightRegular.otf", "Identity-H", true);

        // Başlık
        if(selected_lang.equals("tr")){
            Text titleText = new Text("Rapor")
                    .setFont(font)
                    .setFontSize(24)
                    .setBold()
                    .setFontColor(new DeviceRgb(66, 133, 244));
            Paragraph title = new Paragraph().add(titleText).setTextAlignment(TextAlignment.CENTER);
            doc.add(title);
        }else{
            Text titleText = new Text("Report")
                    .setFont(font)
                    .setFontSize(24)
                    .setBold()
                    .setFontColor(new DeviceRgb(66, 133, 244));
            Paragraph title = new Paragraph().add(titleText).setTextAlignment(TextAlignment.CENTER);
            doc.add(title);
        }
        // Boşluk
        doc.add(new Paragraph("\n"));

        // Profil bilgileri tablosu
        for (Object item : itemList) {
            if (item instanceof Profile) {
                Profile profile = (Profile) item;

                // Profil bilgilerini tablo olarak ekle
                float[] columnWidths = {2, 5}; // Sütun genişlikleri
                Table table = new Table(columnWidths);

                if(selected_lang.equals("tr")){
                    Cell cell = new Cell(1, 2)
                            .add(new Paragraph("Profil Bilgileri").setFont(font))
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(18)
                            .setBackgroundColor(new DeviceRgb(66, 133, 244))
                            .setFontColor(new DeviceRgb(255, 255, 255));
                    table.addHeaderCell(cell);

                    // Bilgi hücreleri
                    addProfileRow(table, "Adı:", profile.getAdı(), font);
                    addProfileRow(table, "Soyadı:", profile.getSoyadı(), font);
                    addProfileRow(table, "Boyu:", profile.getBoyu(), font);
                    addProfileRow(table, "Kilosu:", profile.getKilosu(), font);
                    addProfileRow(table, "Doğum Tarihi:", profile.getDogumTarihi(), font);
                    addProfileRow(table, "Cinsiyet:", profile.getCinsiyet(), font);
                    addProfileRow(table, "Kan Grubu:", profile.getKanGrubu(), font);
                    doc.add(table);
                } else{
                    Cell cell = new Cell(1, 2)
                            .add(new Paragraph("Profil Information").setFont(font))
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(18)
                            .setBackgroundColor(new DeviceRgb(66, 133, 244))
                            .setFontColor(new DeviceRgb(255, 255, 255));
                    table.addHeaderCell(cell);

                    // Bilgi hücreleri
                    addProfileRow(table, "Name:", profile.getAdı(), font);
                    addProfileRow(table, "Surname:", profile.getSoyadı(), font);
                    addProfileRow(table, "Height:", profile.getBoyu(), font);
                    addProfileRow(table, "Weight:", profile.getKilosu(), font);
                    addProfileRow(table, "Birth Date:", profile.getDogumTarihi(), font);
                    addProfileRow(table, "Gender:", profile.getCinsiyet(), font);
                    addProfileRow(table, "Blood Type:", profile.getKanGrubu(), font);
                    doc.add(table);
                }
                // Başlık hücresi
                // Boşluk
                doc.add(new Paragraph("\n"));
            }
        }
        if(selected_lang.equals("tr")){
            for (Object item : itemList) {
                if (item instanceof QuestionAnswerItem) {
                    QuestionAnswerItem questionAndAnswer = (QuestionAnswerItem) item;
                    Text questionText = new Text("Soru: " + questionAndAnswer.getQuestion() + "\n")
                            .setFont(font)
                            .setFontSize(18)
                            .setBold()
                            .setFontColor(new DeviceRgb(66, 133, 244));
                    Text answerText = new Text("Cevap: " + questionAndAnswer.getAnswer() + "\n")
                            .setFont(font)
                            .setFontSize(16)
                            .setFontColor(new DeviceRgb(0, 0, 0));

                    Paragraph question = new Paragraph().add(questionText);
                    Paragraph answer = new Paragraph().add(answerText);

                    doc.add(question);
                    doc.add(answer);
                    doc.add(new Paragraph("\n")); // Boşluk ekleme
                }
            }
        }else {
            for (Object item : itemList) {
                if (item instanceof QuestionAnswerItem) {
                    QuestionAnswerItem questionAndAnswer = (QuestionAnswerItem) item;
                    Text questionText = new Text("Question: " + questionAndAnswer.getQuestion() + "\n")
                            .setFont(font)
                            .setFontSize(18)
                            .setBold()
                            .setFontColor(new DeviceRgb(66, 133, 244));
                    Text answerText = new Text("Answer: " + questionAndAnswer.getAnswer() + "\n")
                            .setFont(font)
                            .setFontSize(16)
                            .setFontColor(new DeviceRgb(0, 0, 0));

                    Paragraph question = new Paragraph().add(questionText);
                    Paragraph answer = new Paragraph().add(answerText);

                    doc.add(question);
                    doc.add(answer);
                    doc.add(new Paragraph("\n")); // Boşluk ekleme
                }
            }
        }
        // Soru ve cevapları ekleme
        doc.close();
    }

    private static void addProfileRow(Table table, String label, String value, PdfFont font) {
        table.addCell(new Cell().add(new Paragraph(label).setFont(font).setBold()));
        table.addCell(new Cell().add(new Paragraph(value).setFont(font)));
    }
}