package com.virusbear.mayan.processor.impl

import kotlin.reflect.KMutableProperty

internal infix inline fun <T> KMutableProperty<T?>.providedBy(block: () -> T): T =
    getter.call() ?: block().also { setter.call(it) }