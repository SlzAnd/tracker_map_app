package com.example.authentication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.authentication.databinding.FragmentBaseBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

abstract class BaseFragment extends Fragment {

    private FragmentBaseBinding binding;

    protected abstract String getTitle();

    protected abstract String getButtonText();

    protected abstract String getBottomNavigationText();

    protected abstract String getBottomNavigationLink();

    protected abstract int getForgotPasswordVisibility();

    protected abstract void onCLickButtonEvent(String email, String password);

    protected abstract void onClickLinkEvent();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBaseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // widgets
        TextInputEditText emailEditText = binding.userNameEditText;
        TextInputEditText passwordEditText = binding.passwordEditText;
        TextInputLayout emailInputLayout = binding.userNameInputLayout;
        TextInputLayout passwordInputLayout = binding.passwordInputLayout;
        Button authButton = binding.authButton;
        TextView bottomNavLink = binding.bottomNavLink;
        TextView toolbarTitle = binding.toolbarTitle;

        toolbarTitle.setText(getTitle());
        binding.bottomNavMessage.setText(getBottomNavigationText());
        bottomNavLink.setText(getBottomNavigationLink());
        authButton.setText(getButtonText());
        binding.forgotPassTextView.setVisibility(getForgotPasswordVisibility());

        authButton.setOnClickListener(v -> {
            String email = String.valueOf(emailEditText.getText());
            String password = String.valueOf(passwordEditText.getText());

            if (TextUtils.isEmpty(email)) {
                emailInputLayout.setError("Error email");
                return;
            } else {
                emailInputLayout.setError(null);
            }

            if (TextUtils.isEmpty(password)) {
                passwordInputLayout.setError("Error password");
                return;
            } else {
                passwordInputLayout.setError(null);
            }

            onCLickButtonEvent(email, password);
        });

        bottomNavLink.setOnClickListener(v -> {
            onClickLinkEvent();
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}