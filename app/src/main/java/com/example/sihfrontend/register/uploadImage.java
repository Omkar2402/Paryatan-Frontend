package com.example.sihfrontend.register;

import android.content.Intent;
import android.os.Bundle;

public interface uploadImage {

    public void onActivityResult(int requestCode, int resultCode, Intent data);

    public void onSaveInstanceState(Bundle outState);
}
