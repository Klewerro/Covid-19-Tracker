package com.klewerro.covidapp.ui

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.klewerro.covidapp.R


fun Fragment.showToast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

fun Fragment.checkPermissions(permission: String, successFunction: () -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        when {
            (ContextCompat.checkSelfPermission(requireContext(), permission)
                    == PackageManager.PERMISSION_GRANTED) -> successFunction()

            shouldShowRequestPermissionRationale(permission) -> showPermissionDialog(permission)

            else -> requestPermissions(arrayOf(permission),
                HomeFragment.PERMISSION_LOCATION_REQUEST_CODE
            )
        }
    }
}


private fun Fragment.showPermissionDialog(permission: String) {
    AlertDialog.Builder(requireContext())
        .setMessage(when(permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> getString(R.string.location_permission_required_description)
            else -> getString(R.string.other_permission_required_description)
        })
        .setTitle(getString(R.string.permission_required))
        .setPositiveButton(android.R.string.ok) { _, _ ->
            requestPermissions(arrayOf(permission),
                HomeFragment.PERMISSION_LOCATION_REQUEST_CODE
            )
        }.create()
        .show()
}