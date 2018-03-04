package org.lazywizard.lazylib.ext.logging

import org.apache.log4j.Level
import org.apache.log4j.Logger

inline fun Logger.i(message: () -> String, ex: Throwable? = null) {
    if (isEnabledFor(Level.INFO)) info(message.invoke(), ex)
}

inline fun Logger.d(message: () -> String, ex: Throwable? = null) {
    if (isEnabledFor(Level.DEBUG)) debug(message.invoke(), ex)
}

inline fun Logger.w(message: () -> String, ex: Throwable? = null) {
    if (isEnabledFor(Level.WARN)) warn(message.invoke(), ex)
}

inline fun Logger.e(message: () -> String, ex: Throwable? = null) {
    if (isEnabledFor(Level.ERROR)) error(message.invoke(), ex)
}

inline fun Logger.f(message: () -> String, ex: Throwable? = null) {
    if (isEnabledFor(Level.FATAL)) fatal(message.invoke(), ex)
}
