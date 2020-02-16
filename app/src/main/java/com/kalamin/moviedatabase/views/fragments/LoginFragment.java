package com.kalamin.moviedatabase.views.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.model.entity.User;
import com.kalamin.moviedatabase.repository.FirebaseRepository;
import com.kalamin.moviedatabase.utils.Codes;
import com.kalamin.moviedatabase.utils.Toast;
import com.kalamin.moviedatabase.viewmodels.LoginViewModel;
import com.kalamin.moviedatabase.views.activities.HomeActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ConstantConditions")
public class LoginFragment extends Fragment {
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private LoginViewModel loginViewModel;
    private EditText txtEmail;
    private EditText txtPassword;

    public LoginFragment() {
    }

    @NotNull
    @Contract(" -> new")
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        txtEmail = view.findViewById(R.id.email);
        txtPassword = view.findViewById(R.id.password);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        view.findViewById(R.id.backArrow).setOnClickListener(v -> getParentFragmentManager().popBackStackImmediate());

        view.findViewById(R.id.btnLogin).setOnClickListener(v -> {
            progressDialog = new ProgressDialog(getContext());
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();
            Codes code = loginViewModel.checkCredentials(email, password);

            if (code != Codes.OK) {
                Toast.showErrorMsg(getActivity().getBaseContext(), code);
            } else {
                FirebaseRepository firebaseRepository = FirebaseRepository.getInstance(getContext());
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Please wait");
                progressDialog.show();
                auth = firebaseRepository.getFirebaseAuth();
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getCurrentUser().getUid());
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                user.setFirebaseUser(auth.getCurrentUser());
                                firebaseRepository.setCurrentUser(user);
                                firebaseRepository.startFavoriteMoviesListener(true);
                                progressDialog.dismiss();
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("DatabaseError", databaseError.getMessage());
                                Log.e("DatabaseError", databaseError.getDetails());
                            }
                        });

                    } else {
                        task.addOnFailureListener(getActivity(), e -> {
                            Toast.showInfo(getActivity().getBaseContext(), e.getMessage());
                            progressDialog.dismiss();
                        });
                    }
                });
            }
        });
    }
}
