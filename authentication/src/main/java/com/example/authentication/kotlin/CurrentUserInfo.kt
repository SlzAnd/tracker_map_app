package com.example.authentication.kotlin

import com.google.firebase.auth.FirebaseAuth

fun getUserUID(): String? {
    return FirebaseAuth.getInstance().currentUser?.uid
}