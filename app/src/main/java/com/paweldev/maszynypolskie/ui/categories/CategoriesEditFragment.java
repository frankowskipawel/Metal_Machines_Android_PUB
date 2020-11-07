package com.paweldev.maszynypolskie.ui.categories;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.repository.CategoryRepository;
import com.paweldev.maszynypolskie.model.Category;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.FragmentUtils;
import com.paweldev.maszynypolskie.utils.KeyboardUtils;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class CategoriesEditFragment extends Fragment {
    private Fragment previousFragment;
    private Category currentCategory;
    Boolean isEdit = false;
    String idCategory;
    String name = "";

    public CategoriesEditFragment(Fragment previousFragment, Boolean isEdit, String idCategory, String name) {
        this.previousFragment = previousFragment;
        this.isEdit = isEdit;
        this.idCategory = idCategory;
        this.name = name;
    }

    public CategoriesEditFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_category_edit, container, false);

        final EditText name = v.findViewById(R.id.nameEditText);
        final EditText idCategory = v.findViewById(R.id.ideditText);
        Button saveButton = (Button) v.findViewById(R.id.saveShowCategoriesFragment);
        name.setText(this.name);
        Log.e(TAG, "onCreateView: "+idCategory );
        if (this.idCategory != null) {
            idCategory.setText(this.idCategory);
        }
        boolean isEdit = false;
        if (!name.getText().toString().equals("")) {
            isEdit = true;
        }
        final boolean finalIsEdit = isEdit;

// ADD NAME TO TOOLBAR //
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dodaj/Edycja kategorii");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");

//SAVE BUTTON//
        saveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    if (finalIsEdit) {
                        currentCategory.setName(name.getText().toString().toUpperCase());
                        CategoryRepository.updateCategory(currentCategory);
                        getFragmentManager().popBackStack();
                    } else {
                        currentCategory = new Category();
                        currentCategory.setName(name.getText().toString().toUpperCase());
                        CategoryRepository.insertCategory(currentCategory);
                        getFragmentManager().popBackStack();
                    }
                    KeyboardUtils.hideKeyboard(getContext(), container);
                } catch (Exception e) {
                    MessageFragment messageFragment = new MessageFragment();
                    messageFragment.setMessage(e.getMessage());
                    FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                    e.printStackTrace();
                }
            }
        });

// PREVIEW BUTTON //
        final Button prevButton = (Button) v.findViewById(R.id.prevAddNewCatogoriesFragment);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
                KeyboardUtils.hideKeyboard(getContext(), container);
            }
        });

        return v;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(Category currentCategory) {
        this.currentCategory = currentCategory;
    }
}
