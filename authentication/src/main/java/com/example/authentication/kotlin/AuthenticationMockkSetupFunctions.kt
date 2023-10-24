package com.example.authentication.kotlin

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic

private val user: FirebaseUser = mockk()

fun initialAuthMockkSetup() {
    mockkStatic(FirebaseAuth::class)

    every { FirebaseAuth.getInstance() } returns mockk(relaxed = true)
    every { FirebaseAuth.getInstance().currentUser } returns mockk { user }
    every { FirebaseAuth.getInstance().currentUser?.uid } returns "UtzD8gLI62Xj5KS9yFh3gnJtbxE3"
    every { FirebaseAuth.getInstance().signOut() } returns mockk()
}