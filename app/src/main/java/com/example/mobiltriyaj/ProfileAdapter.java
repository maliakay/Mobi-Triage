package com.example.mobiltriyaj;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobiltriyaj.Class.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private Context context;
    private List<Profile> profileList;
    private DatabaseReference reference;

    public ProfileAdapter(Context context, List<Profile> profileList, DatabaseReference reference) {
        this.context = context;
        this.profileList = profileList;
        this.reference = reference;
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_item, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Profile profile = profileList.get(position);
        holder.mainUserName.setText(profile.getAdı() + " " + profile.getSoyadı());
        holder.mainBirthdayDate.setText(profile.getDogumTarihi());
        holder.mainWeight.setText(profile.getKilosu());
        holder.mainHeight.setText(profile.getBoyu());

        // Cinsiyet bilgisini kontrol et ve uygun şekilde ayarla
        String gender = profile.getCinsiyet();
        Locale currentLocale = Locale.getDefault();
        if (currentLocale.getLanguage().equals("en")) {
            if (gender.equals("Erkek")) {
                gender = "Male";
            } else if (gender.equals("Kadın")) {
                gender = "Female";
            }
        }else{
            if (gender.equals("Male")) {
                gender = "Erkek";
            } else if (gender.equals("Female")) {
                gender = "Kadın";
            }
        }
        holder.mainGender.setText(gender);

        holder.mainBloody.setText(profile.getKanGrubu());

        holder.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProfileActivity.class);
                intent.putExtra("profileId", profile.getKullanıcıAdi());
                intent.putExtra("name", profile.getAdı());
                intent.putExtra("surname", profile.getSoyadı());
                intent.putExtra("weight", profile.getKilosu());
                intent.putExtra("height", profile.getBoyu());
                intent.putExtra("birthDate", profile.getDogumTarihi());
                intent.putExtra("gender", profile.getCinsiyet());
                intent.putExtra("bloodGroup", profile.getKanGrubu());
                context.startActivity(intent);
            }
        });

        holder.btnKeepGoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TriageActivity.class);
                intent.putExtra("profile", profile);
                context.startActivity(intent);
            }
        });

        holder.btnDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.profil_sil))
                        .setMessage(context.getString(R.string.profil_sil_mesaj))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                reference.child(profile.getKullanıcıAdi()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            int position = holder.getAdapterPosition();
                                            if (position != RecyclerView.NO_POSITION) {
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        profileList.remove(position);
                                                        notifyItemRemoved(position);
                                                        notifyItemRangeChanged(position, profileList.size());
                                                        Toast.makeText(context, context.getString(R.string.toast_succes_delete_alert), Toast.LENGTH_SHORT).show();
                                                    }
                                                }, 1000); // 1 saniye gecikme
                                            }
                                        } else {
                                            Toast.makeText(context, context.getString(R.string.toast_unsucces_delete_alert), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {

        TextView mainUserName, mainBirthdayDate, mainWeight, mainHeight, mainGender, mainBloody;
        Button btnEditProfile, btnKeepGoing;
        ImageButton btnDeleteProfile;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            mainUserName = itemView.findViewById(R.id.mainUserName);
            mainBirthdayDate = itemView.findViewById(R.id.mainBirthdayDate);
            mainWeight = itemView.findViewById(R.id.mainWeight);
            mainHeight = itemView.findViewById(R.id.mainHeight);
            mainGender = itemView.findViewById(R.id.mainGender);
            mainBloody = itemView.findViewById(R.id.mainBloody);
            btnEditProfile = itemView.findViewById(R.id.btnEditProfile);
            btnKeepGoing = itemView.findViewById(R.id.btnKeepGoing);
            btnDeleteProfile = itemView.findViewById(R.id.btnDeleteProfile);
        }
    }
}
