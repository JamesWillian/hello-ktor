package com.jammes

import at.favre.lib.crypto.bcrypt.BCrypt

fun hashPassword(password: String): String {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray())
}

fun verifyPassword(password: String, storedHash: String): Boolean {
    return BCrypt.verifyer().verify(password.toCharArray(), storedHash).verified
}
