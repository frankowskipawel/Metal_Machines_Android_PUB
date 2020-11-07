package com.paweldev.maszynypolskie.ui.categories;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.repository.CategoryRepository;
import com.paweldev.maszynypolskie.model.Category;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.FragmentUtils;

import java.util.HashMap;


public class TrainingOperatorsSettingsFragment extends Fragment {
    View v;
    private Category currentCategory;
    Gson gson = new Gson();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.fragment_training_operators_settings, container, false);

// PREVIOUS BUTTON //
        Button prevButton = v.findViewById(R.id.prev_training_operators_settings_fragment);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

// EDITTEXT //
        final EditText editText = v.findViewById(R.id.editText_training_operators_fragment);
        editText.setText(gson.toJson((currentCategory.getOperatorTrainingList2())));

/////// SAVE BUTTON ////////////////////////////////////////////////////////////////////////////////
        Button saveButton = v.findViewById(R.id.save_button_training_operators_fragment);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCategory.setOperatorTrainingList2(gson.fromJson(editText.getText().toString(), HashMap.class));
                try {
                    CategoryRepository.updateCategory(currentCategory);
                } catch (Exception e) {
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setMessage(e.getMessage());
                    FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    public Category getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(Category currentCategory) {
        this.currentCategory = currentCategory;
    }
}