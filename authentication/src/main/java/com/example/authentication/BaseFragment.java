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

import java.util.regex.Pattern;

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
                emailInputLayout.setError("Error email. Email can't be empty");
                return;
            } else {
                if (!isValidEmail(email)) {
                    emailInputLayout.setError("Error email. Enter the correct email");
                    return;
                }
                emailInputLayout.setError(null);
            }

            if (TextUtils.isEmpty(password)) {
                passwordInputLayout.setError("Error password. Password can't be empty");
                return;
            } else {
                if (password.length() < 6) {
                    passwordInputLayout.setError("Error password. Password should be at least 6 symbols");
                    return;
                }
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

    private boolean isValidEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.compile(regexPattern).matcher(email).matches();
    }
}