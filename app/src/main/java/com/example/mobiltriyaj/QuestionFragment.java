package com.example.mobiltriyaj;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.mobiltriyaj.Class.Option;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class QuestionFragment extends Fragment {
    private static final String ARG_QUESTION_TR = "question_tr";
    private static final String ARG_QUESTION_EN = "question_en";
    private static final String ARG_OPTIONS = "options";

    private String questionTr;
    private String questionEn;
    private List<Option> options;
    private View headRegion, chestRegion, leftArmRegion, rightArmRegion, abdomenRegion, leftLegRegion, rightLegRegion;

    public static QuestionFragment newInstance(String questionTr, String questionEn, List<Option> options) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_QUESTION_TR, questionTr);
        args.putString(ARG_QUESTION_EN, questionEn);
        args.putParcelableArrayList(ARG_OPTIONS, (ArrayList<Option>) options);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionTr = getArguments().getString(ARG_QUESTION_TR);
            questionEn = getArguments().getString(ARG_QUESTION_EN);
            options = getArguments().getParcelableArrayList(ARG_OPTIONS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        TextView questionTextView = view.findViewById(R.id.question_text);

        // Mevcut dili kontrol et ve uygun soruyu göster
        Locale currentLocale = getCurrentLocale();
        if (currentLocale.getLanguage().equals(new Locale("tr").getLanguage())) {
            questionTextView.setText(questionTr); // Display question in Turkish
        } else {
            questionTextView.setText(questionEn); // Display question in English
        }

        LinearLayout optionsLayout = view.findViewById(R.id.options_layout);
        ImageView imageView = view.findViewById(R.id.imageView);
        headRegion = view.findViewById(R.id.headRegion);
        chestRegion = view.findViewById(R.id.chestRegion);
        leftArmRegion = view.findViewById(R.id.leftArmRegion);
        rightArmRegion = view.findViewById(R.id.rightArmRegion);
        abdomenRegion = view.findViewById(R.id.abdomenRegion);
        leftLegRegion = view.findViewById(R.id.leftLegRegion);
        rightLegRegion = view.findViewById(R.id.rightLegRegion);

        boolean isTextInputRequired = false;
        boolean isImageRequired = false;
        int x = 0;
        Option textInputOption = null;
        boolean isMultipleChoice = false;
        List<Option> multipleChoiceOptions = new ArrayList<>();

        for (Option option : options) {
            if (option.getTextTr().startsWith("Coklu") || option.getTextEn().startsWith("Multiple")) {
                isMultipleChoice = true;
                multipleChoiceOptions.add(option);
            } else if (option.getTextTr().equals("Input") || option.getTextEn().equals("Input")) {
                isTextInputRequired = true;
                textInputOption = option;
                break;
            } else if (option.getTextTr().equals("Sekil") || option.getTextEn().equals("Shape")) {
                isImageRequired = true;
                x = option.getNextQuestionId();
                break;
            }
        }

        if (isMultipleChoice) {
            List<CheckBox> checkBoxes = new ArrayList<>();
            for (Option option : multipleChoiceOptions) {
                String optionText = currentLocale.getLanguage().equals(new Locale("tr").getLanguage()) ? option.getTextTr() : option.getTextEn();
                optionText = optionText.substring(optionText.indexOf("Coklu")+6).trim();
                CheckBox checkBox = new CheckBox(getContext());
                checkBox.setText(optionText);
                checkBox.setPadding(8, 8, 8, 8);
                checkBox.setBackgroundResource(R.drawable.background_options_layout); // Arka plan ekleme
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 8, 0, 8); // Şıklar arasına mesafe ekleme
                checkBox.setLayoutParams(layoutParams);
                optionsLayout.addView(checkBox);
                checkBoxes.add(checkBox);
            }

            Button submitButton = new Button(getContext());
            submitButton.setText(getResources().getString(R.string.btn_main));

            submitButton.setOnClickListener(v -> {
                StringBuilder userInputTr = new StringBuilder();
                StringBuilder userInputEn = new StringBuilder();
                for (CheckBox checkBox : checkBoxes) {
                    if (checkBox.isChecked()) {
                        if (currentLocale.getLanguage().equals(new Locale("tr").getLanguage())) {
                            userInputTr.append(checkBox.getText().toString()).append(", ");
                            String checkboxTxt =getCorrespondingOptionEn(checkBox.getText().toString());
                            checkboxTxt = checkboxTxt.substring(checkboxTxt.indexOf("Coklu")+6).trim();
                            userInputEn.append(checkboxTxt).append(",");
                        } else {
                            userInputEn.append(checkBox.getText().toString()).append(", ");
                            String checkboxTxt =getCorrespondingOptionTr(checkBox.getText().toString());
                            checkboxTxt = checkboxTxt.substring(checkboxTxt.indexOf("Coklu")+6).trim();
                            userInputTr.append(checkboxTxt).append(", ");
                        }
                    }
                }
                if (userInputTr.length() > 0) {
                    userInputTr.setLength(userInputTr.length() - 2); // Son virgülü ve boşluğu kaldır
                    userInputEn.setLength(userInputEn.length() - 2); // Son virgülü ve boşluğu kaldır
                }
                int nextQuestionId = multipleChoiceOptions.get(0).getNextQuestionId();
                ((TriageActivity) getActivity()).recordAnswer(userInputTr.toString(), userInputEn.toString(), nextQuestionId);
            });
            optionsLayout.addView(submitButton);

        } else if (isImageRequired) {
            optionsLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            headRegion.setVisibility(View.VISIBLE);
            chestRegion.setVisibility(View.VISIBLE);
            leftArmRegion.setVisibility(View.VISIBLE);
            rightArmRegion.setVisibility(View.VISIBLE);
            abdomenRegion.setVisibility(View.VISIBLE);
            leftLegRegion.setVisibility(View.VISIBLE);
            rightLegRegion.setVisibility(View.VISIBLE);

            setRegionClickListener(headRegion, "Kafatası", "Head", 32);
            setRegionClickListener(chestRegion, "Göğüs", "Chest", x);
            setRegionClickListener(leftArmRegion, "Sol Kol", "Left Arm", x);
            setRegionClickListener(rightArmRegion, "Sağ Kol", "Right Arm", x);
            setRegionClickListener(abdomenRegion, "Karın", "Abdomen", x);
            setRegionClickListener(leftLegRegion, "Sol Bacak", "Left Leg", x);
            setRegionClickListener(rightLegRegion, "Sağ Bacak", "Right Leg", x);

        } else if (isTextInputRequired && textInputOption != null) {
            EditText editText = new EditText(getContext());
            editText.setHint(getResources().getString(R.string.set_hint));
            editText.setPadding(8, 8, 8, 8);
            editText.setBackgroundResource(R.drawable.background_options_layout); // Arka plan ekleme
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 8, 0, 8); // Şıklar arasına mesafe ekleme
            editText.setLayoutParams(layoutParams);
            optionsLayout.addView(editText);

            final Option finalTextInputOption = textInputOption;
            Button submitButton = new Button(getContext());
            submitButton.setText(getResources().getString(R.string.btn_main));
            submitButton.setOnClickListener(v -> {
                String userInput = editText.getText().toString();
                ((TriageActivity) getActivity()).recordAnswer(userInput, userInput, finalTextInputOption.getNextQuestionId());
            });
            optionsLayout.addView(submitButton);

        } else {
            for (Option option : options) {
                String optionText = currentLocale.getLanguage().equals(new Locale("tr").getLanguage()) ? option.getTextTr() : option.getTextEn();
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setText(optionText);
                radioButton.setPadding(8, 8, 8, 8);
                radioButton.setBackgroundResource(R.drawable.background_options_layout);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutParams.setMargins(0, 8, 0, 8); // Şıklar arasına mesafe ekleme
                radioButton.setLayoutParams(layoutParams);
                radioButton.setOnClickListener(v -> {
                    if (currentLocale.getLanguage().equals(new Locale("tr").getLanguage())) {
                        ((TriageActivity) getActivity()).recordAnswer(option.getTextTr(), option.getTextEn(), option.getNextQuestionId());
                    } else {
                        ((TriageActivity) getActivity()).recordAnswer(option.getTextTr(), option.getTextEn(), option.getNextQuestionId());
                    }
                });
                optionsLayout.addView(radioButton);
            }
        }

        return view;
    }

    private void setRegionClickListener(final View region, final String regionNameTr, final String regionNameEn, final int nextQuestionId) {
        region.setOnClickListener(v -> {
            region.setBackgroundColor(Color.argb(150, 173, 216, 230)); // Hafif mavi, yarı şeffaf

            // Tıklanan bölgenin adını kaydet ve bir sonraki soruya geç
            if (getCurrentLocale().getLanguage().equals(new Locale("tr").getLanguage())) {
                ((TriageActivity) getActivity()).recordAnswer(regionNameTr,regionNameEn , nextQuestionId);
            } else {
                ((TriageActivity) getActivity()).recordAnswer(regionNameTr, regionNameEn, nextQuestionId);
            }
        });
    }

    private String getCorrespondingOptionEn(String textTr) {
        for (Option option : options) {
            if (option.getTextTr().contains(textTr)) {
                return option.getTextEn();
            }
        }
        return textTr; // Eğer İngilizce karşılığı bulunamazsa Türkçeyi döndür
    }

    private String getCorrespondingOptionTr(String textEn) {
        for (Option option : options) {
            if (option.getTextEn().contains(textEn)) {
                return option.getTextTr();
            }
        }
        return textEn; // Eğer Türkçe karşılığı bulunamazsa İngilizceyi döndür
    }

    private Locale getCurrentLocale() {
        Configuration config = getResources().getConfiguration();
        return config.getLocales().get(0);
    }
}