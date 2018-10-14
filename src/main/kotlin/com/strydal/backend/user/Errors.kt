package com.strydal.backend.user

internal sealed class Error {
    object PasswordsAreDifferent : Error()
    object EmailAlreadyRegistered : Error()
}