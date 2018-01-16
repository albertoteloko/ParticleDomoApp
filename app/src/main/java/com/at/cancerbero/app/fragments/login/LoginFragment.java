package com.at.cancerbero.app.fragments.login;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.at.cancerbero.CancerberoApp.R;
import com.at.cancerbero.app.fragments.AppFragment;
import com.at.cancerbero.service.events.Event;
import com.at.cancerbero.service.events.ForgotPasswordFail;
import com.at.cancerbero.service.events.ForgotPasswordStart;
import com.at.cancerbero.service.events.LogInFail;

public class LoginFragment extends AppFragment {

    private EditText emailEditText;
    private EditText passwordEditText;

    private TextView inUsername;
    private TextView inPassword;

    private Button signUpButton;
    private TextView forgotPasswordButton;


    public LoginFragment() {
    }

    @Override
    public View onCreateViewApp(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        inUsername = (TextView) view.findViewById(R.id.textViewUserIdLabel);
        inPassword = (TextView) view.findViewById(R.id.textViewUserPasswordLabel);

        signUpButton = (Button) view.findViewById(R.id.buttonLogIn);
        forgotPasswordButton = (TextView) view.findViewById(R.id.textViewUserForgotPassword);

        emailEditText = (EditText) view.findViewById(R.id.editTextUserId);
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) view.findViewById(R.id.textViewUserIdLabel);
                    label.setText(R.string.label_email);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) view.findViewById(R.id.textViewUserIdMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) view.findViewById(R.id.textViewUserIdLabel);
                    label.setText("");
                }
            }
        });

        passwordEditText = (EditText) view.findViewById(R.id.editTextUserPassword);
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() == 0) {
                    TextView label = (TextView) view.findViewById(R.id.textViewUserPasswordLabel);
                    label.setText(R.string.label_password);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView label = (TextView) view.findViewById(R.id.textViewUserPasswordMessage);
                label.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    TextView label = (TextView) view.findViewById(R.id.textViewUserPasswordLabel);
                    label.setText("");
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                if (email == null || email.length() < 1) {
                    inUsername.setText(R.string.label_email_empty);
                    inUsername.setTextColor(Color.parseColor("red"));
                    return;
                }


                String password = passwordEditText.getText().toString();
                if (password == null || password.length() < 1) {
                    inPassword.setText(R.string.label_password_empty);
                    inPassword.setTextColor(Color.parseColor("red"));
                    return;
                }

                showErrorDialog("Signing in...");
                getMainService().login(email, password);
            }
        });

        TextView forgotPassword = (TextView) view.findViewById(R.id.textViewUserForgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpasswordUser(view);
            }
        });

        emailEditText.requestFocus();

        return view;
    }

    @Override
    public boolean handle(Event event) {
        boolean result = false;

        if (event instanceof LogInFail) {
            Exception exception = ((LogInFail) event).exception;

            if (!exception.getMessage().equals("user ID cannot be null")) {
                showErrorDialog("Unable to log in");
            }
            result = true;
        } else if (event instanceof ForgotPasswordStart) {
            ForgotPasswordStart input = ((ForgotPasswordStart) event);

            Bundle bundle = new Bundle();
            bundle.putString("userId", input.userId);
            changeFragment(ForgotPasswordFragment.class, bundle);
            result = true;
        } else if (event instanceof ForgotPasswordFail) {
            showErrorDialog("Unable to reset password");
            result = true;
        }

        return result;
    }

    private void forgotpasswordUser(View view) {
        String username = emailEditText.getText().toString();
        if (username == null) {
            TextView label = (TextView) view.findViewById(R.id.textViewUserIdMessage);
            label.setText(inUsername.getHint() + " cannot be empty");
            inUsername.setBackground(view.getContext().getDrawable(R.drawable.text_border_error));
            return;
        }

        if (username.length() < 1) {
            TextView label = (TextView) view.findViewById(R.id.textViewUserIdMessage);
            label.setText(inUsername.getHint() + " cannot be empty");
            inUsername.setBackground(view.getContext().getDrawable(R.drawable.text_border_error));
            return;
        }

        getMainService().forgotPassword(username);
    }
}