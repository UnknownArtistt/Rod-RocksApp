package herrero.julen.rodrocks;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.zip.Inflater;

import herrero.julen.rodrocks.databinding.FragmentSignUpBinding;
import model.User;
import viewmodel.UserViewModel;

public class SignUpFragment extends Fragment {

    String username, password, email, passwordHashed;

    FragmentSignUpBinding binding;

    NavController navController;
    UserViewModel userViewModel;
    Executor executor;

    private boolean pwdShown = false;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Inflater SignUpFragmentBinding;
        return(binding = FragmentSignUpBinding.inflate(inflater, container, false)).getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);
        navController = Navigation.findNavController(view);
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        executor = Executors.newSingleThreadExecutor();

        binding.showPwdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pwdShown = !pwdShown;

                if (pwdShown) {
                    binding.passwordSignUp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    binding.passwordSignUp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                binding.passwordSignUp.setSelection(binding.passwordSignUp.getText().length());
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    username = binding.usernameSignUp.getText().toString();
                    password = binding.passwordSignUp.getText().toString();
                    email = binding.emailSignUp.getText().toString();
                    HashFunction hf = Hashing.sha256();
                    HashCode code = hf.newHasher()
                            .putString(password, Charsets.UTF_8)
                            .hash();
                    passwordHashed = code.toString();
                    User user = new User(username, passwordHashed, email);
                    userViewModel.add(user);
                    navController.navigate(R.id.action_signUpFragment_to_loginFragment);

                    Toast t = Toast.makeText(getContext(), "Success creating an account", Toast.LENGTH_LONG);
                    t.show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.backLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_signUpFragment_to_loginFragment);
            }
        });
    }
}