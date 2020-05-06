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
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.repository.FirebaseRepository;
import com.kalamin.moviedatabase.utils.Codes;
import com.kalamin.moviedatabase.utils.Hash;
import com.kalamin.moviedatabase.utils.Toast;
import com.kalamin.moviedatabase.viewmodels.RegisterViewModel;
import com.kalamin.moviedatabase.views.activities.HomeActivity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

@SuppressWarnings("ConstantConditions")
public class RegisterFragment extends Fragment {
    private ProgressDialog progressDialog;
    private RegisterViewModel registerViewModel;
    private EditText txtUsername;
    private EditText txtEmail;
    private EditText txtPassword;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @NotNull
    @Contract(" -> new")
    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.backArrow).setOnClickListener(v -> getParentFragmentManager().popBackStackImmediate());
        registerViewModel = new ViewModelProvider(getActivity()).get(RegisterViewModel.class);

        view.findViewById(R.id.btnRegister).setOnClickListener(v -> {
            txtUsername = view.findViewById(R.id.username);
            txtEmail = view.findViewById(R.id.email);
            txtPassword = view.findViewById(R.id.password);

            String username = txtUsername.getText().toString().trim();
            String email = txtEmail.getText().toString().trim();
            String password = txtPassword.getText().toString().trim();

            Codes code = registerViewModel.checkCredentials(username, email, password);
            if (code != Codes.OK) {
                Toast.showErrorMsg(getContext(), code);
            } else {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                FirebaseRepository firebaseRepository = FirebaseRepository.getInstance();
                FirebaseAuth auth = firebaseRepository.getFirebaseAuth();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        DatabaseReference reference = firebaseRepository.getUserReference().child(firebaseUser.getUid());

                        HashMap<String, Object> user = new HashMap<>();
                        user.put("username", username);
                        user.put("email", email);
                        user.put("password", Hash.password(password));

                        reference.setValue(user).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                firebaseRepository.setCurrentUser(user, firebaseUser);
                                firebaseRepository.startFavoriteMoviesListener(true);
                                progressDialog.dismiss();
                                getActivity().finish();
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                            } else {
                                FirebaseAuthException e = (FirebaseAuthException) task.getException();
                                Log.e("Firebase", e.getMessage());
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
