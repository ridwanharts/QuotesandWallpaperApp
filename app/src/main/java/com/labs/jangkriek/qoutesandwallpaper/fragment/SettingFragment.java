package com.labs.jangkriek.qoutesandwallpaper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import  com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.labs.jangkriek.qoutesandwallpaper.R;

import java.util.Objects;

public class SettingFragment extends Fragment {

    private static final int GOOGLE_SIGN_IN_CODE = 212;
    private GoogleSignInClient googleSignInClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            return inflater.inflate(R.layout.fragment_setting_default, container, false);
        }
        return inflater.inflate(R.layout.fragment_setting_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoogleSignInOptions gs = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        googleSignInClient = GoogleSignIn.getClient(getActivity(), gs);

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            ImageView ivUserImage = view.findViewById(R.id.iv_user);
            TextView tvUsername = view.findViewById(R.id.username_google);
            TextView tvEmail = view.findViewById(R.id.email_google);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Glide.with(getActivity()).load(user.getPhotoUrl().toString()).into(ivUserImage);
            tvUsername.setText(user.getDisplayName());

            SpannableString customText = new SpannableString(user.getEmail());
            customText.setSpan(new RelativeSizeSpan(.1f), 0, 3, 0);
            tvEmail.setText(user.getEmail());

            view.findViewById(R.id.logout_google).setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getActivity(), "Logout Successfull", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.content_main, new SettingFragment())
                            .commit();
                        }
                    });
                }
            });

        }else{
            view.findViewById(R.id.google_button_signin).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = googleSignInClient.getSignInIntent();
                    startActivityForResult(intent, GOOGLE_SIGN_IN_CODE);

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GOOGLE_SIGN_IN_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthGoogle(GoogleSignInAccount account) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Login Successfull", Toast.LENGTH_SHORT).show();
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_main, new SettingFragment())
                    .commit();
                }else {
                    Toast.makeText(getActivity(), "Login Failure", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
