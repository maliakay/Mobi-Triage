package com.example.mobiltriyaj;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiltriyaj.Class.Profile;
import com.example.mobiltriyaj.Class.QuestionAnswerItem;

import java.util.List;

public class QuestionAnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PROFILE = 0;
    private static final int TYPE_QUESTION_ANSWER = 1;

    private List<Object> itemList;
    private String selectedLang;

    public QuestionAnswerAdapter(List<Object> itemList, String selectedLang) {
        this.itemList = itemList;
        this.selectedLang = selectedLang;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof Profile) {
            return TYPE_PROFILE;
        } else {
            return TYPE_QUESTION_ANSWER;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PROFILE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
            return new ProfileViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
            return new QuestionAnswerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_PROFILE) {
            Profile profile = (Profile) itemList.get(position);
            ((ProfileViewHolder) holder).bind(profile, selectedLang);
        } else {
            QuestionAnswerItem item = (QuestionAnswerItem) itemList.get(position);
            ((QuestionAnswerViewHolder) holder).bind(item, selectedLang);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView profileInfoTextView;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            profileInfoTextView = itemView.findViewById(R.id.profile_info);
        }

        public void bind(Profile profile, String selectedLang) {
            String profileInfo;
            if ("en".equals(selectedLang)) {
                if(profile.getCinsiyet().equals("Erkek")){
                    profile.setCinsiyet("Male"); 
                }else{
                    profile.setCinsiyet("Female");
                }
                profileInfo =
                        "Name: " + profile.getAdı() + "\n" +
                                "Surname: " + profile.getSoyadı() + "\n" +
                                "Height: " + profile.getBoyu() + "\n" +
                                "Weight: " + profile.getKilosu() + "\n" +
                                "Birth Date: " + profile.getDogumTarihi() + "\n" +
                                "Gender: " + profile.getCinsiyet() + "\n" +
                                "Blood Type: " + profile.getKanGrubu();
            } else {
                profileInfo =
                        "Adı: " + profile.getAdı() + "\n" +
                                "Soyadı: " + profile.getSoyadı() + "\n" +
                                "Boyu: " + profile.getBoyu() + "\n" +
                                "Kilosu: " + profile.getKilosu() + "\n" +
                                "Doğum Tarihi: " + profile.getDogumTarihi() + "\n" +
                                "Cinsiyet: " + profile.getCinsiyet() + "\n" +
                                "Kan Grubu: " + profile.getKanGrubu();
            }
            profileInfoTextView.setText(profileInfo);
        }
    }

    static class QuestionAnswerViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        TextView answerTextView;

        public QuestionAnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.question_text);
            answerTextView = itemView.findViewById(R.id.answer_text);
        }

        public void bind(QuestionAnswerItem item, String selectedLang) {
            if ("en".equals(selectedLang)) {
                questionTextView.setText("Question: " + item.getQuestion());
                answerTextView.setText("Answer: " + item.getAnswer());
            } else {
                questionTextView.setText("Soru: " + item.getQuestion());
                answerTextView.setText("Cevap: " + item.getAnswer());
            }
        }
    }
}