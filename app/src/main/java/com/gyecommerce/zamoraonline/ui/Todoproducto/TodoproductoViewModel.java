package com.gyecommerce.zamoraonline.ui.Todoproducto;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TodoproductoViewModel extends ViewModel {

    private MutableLiveData num;

    public TodoproductoViewModel() {
        num = new MutableLiveData<>();
        num.setValue(4);
    }

    public LiveData<Integer> getNum() {
        return num;
    }
}