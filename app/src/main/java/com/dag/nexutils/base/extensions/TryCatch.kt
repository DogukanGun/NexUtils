package com.dag.nexutils.base.extensions

import android.util.Log

const val L_TRY_CATCH = "TRY_CATCH_"

inline fun <reified C, reified T> C.tryCatch(
    tryBlock: () -> T?,
    catchBlock: (Throwable) -> Any? = { null },
    finallyBlock: () -> Unit = {}
) = try {
    tryBlock()?.run {
        takeUnless { it is Unit }
    }
} catch (e: Exception) {
    Log.e(L_TRY_CATCH + C::class.simpleName, "$e")
    catchBlock(e)?.run {
        takeIf { it is T } as? T?
    }
} finally {
    finallyBlock()
}