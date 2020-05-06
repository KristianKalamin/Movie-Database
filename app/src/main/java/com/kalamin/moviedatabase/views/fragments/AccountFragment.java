package com.kalamin.moviedatabase.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kalamin.moviedatabase.R;
import com.kalamin.moviedatabase.repository.FirebaseRepository;
import com.kalamin.moviedatabase.utils.Codes;
import com.kalamin.moviedatabase.utils.Hash;
import com.kalamin.moviedatabase.utils.Toast;
import com.kalamin.moviedatabase.viewmodels.AccountViewModel;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class AccountFragment extends Fragment {
    private AccountViewModel accountViewModel;
    private FirebaseRepository firebaseRepository;
    private static int parentActivityId;

    public AccountFragment() {
    }

    @NotNull
    @Contract("_ -> new")
    public static AccountFragment newInstance(int activityId) {
        parentActivityId = activityId;
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("User Account");

        accountViewModel = new ViewModelProvider(getActivity()).get(AccountViewModel.class);
        firebaseRepository = FirebaseRepository.getInstance();
        EditText txtUsername = view.findViewById(R.id.username);
        EditText txtEmail = view.findViewById(R.id.email);

        firebaseRepository.getUserLiveData().observe(getViewLifecycleOwner(), user -> {
            txtEmail.setText(user.getEmail());
            txtUsername.setText(user.getUsername());
        });

        view.findViewById(R.id.btnSave).setOnClickListener(v -> {
            EditText txtOldPassword = view.findViewById(R.id.old_password);
            EditText txtNewPassword = view.findViewById(R.id.new_password);

            String oldPassword = txtOldPassword.getText().toString().trim();
            String newPassword = txtNewPassword.getText().toString().trim();
            String oldPasswordHash = Hash.password(oldPassword);
            String newPasswordHash = Hash.password(newPassword);

            Codes codes = accountViewModel.checkCredentials(oldPasswordHash, newPasswordHash);
            if (codes != Codes.OK) {
                Toast.showErrorMsg(getContext(), codes);
            } else {
                firebaseRepository.setNewUserPassword(newPassword);
                Toast.showInfo(getContext(), "Updated");
                firebaseRepository.logout();
                ((NavigationHost) getActivity()).navigateTo(parentActivityId, LoginFragment.newInstance(), false);
            }
        });

        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            firebaseRepository.logout();
            Toast.showInfo(getContext(), "Logged out");
            getParentFragmentManager().popBackStackImmediate();
        });
    }
}
