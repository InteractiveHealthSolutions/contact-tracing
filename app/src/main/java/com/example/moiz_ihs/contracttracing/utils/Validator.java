package com.example.moiz_ihs.contracttracing.utils;

import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by Moiz-IHS on 6/1/2017.
 */

public class Validator {

    public Boolean validateRadioGroupEmpty(RadioGroup radioGroup)
    {
        Boolean isEmpty = false;

        if(radioGroup.getCheckedRadioButtonId()<=0)
            isEmpty = true;

        return isEmpty;
    }

    public Boolean validateEditTextEmpty(EditText editText)
    {
        Boolean isEmpty = false;

        if(editText.getText().equals(""))
            isEmpty = true;

        return isEmpty;
    }


}
