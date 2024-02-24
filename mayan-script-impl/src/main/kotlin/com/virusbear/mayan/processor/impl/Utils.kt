package com.virusbear.mayan.processor.impl

import kotlin.reflect.KMutableProperty

internal inline infix fun <T> KMutableProperty<T?>.providedBy(block: () -> T): T =
    getter.call() ?: block().also { setter.call(it) }