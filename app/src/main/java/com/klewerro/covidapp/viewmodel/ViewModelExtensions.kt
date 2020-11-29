package com.klewerro.covidapp.viewmodel

import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


fun AndroidViewModel.showToast(message: String) {
    GlobalScope.launch(Dispatchers.Main) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
    }
}