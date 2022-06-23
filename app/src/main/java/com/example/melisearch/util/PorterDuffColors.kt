package com.example.melisearch.util

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter

enum class PorterDuffColors (val value :PorterDuffColorFilter) {
    RED(PorterDuffColorFilter (-65536,
        PorterDuff.Mode.SRC_ATOP)),
    GREY(PorterDuffColorFilter (-7829368,
        PorterDuff.Mode.SRC_ATOP))
}