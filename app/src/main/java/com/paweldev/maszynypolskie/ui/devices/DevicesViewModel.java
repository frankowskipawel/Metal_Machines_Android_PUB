package com.paweldev.maszynypolskie.ui.devices;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DevicesViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DevicesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Lista sprzedanych maszyn");
    }

    public LiveData<String> getText() {
        return mText;
    }
}