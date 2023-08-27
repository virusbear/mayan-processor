package com.virusbear.mayan.processor.worker

interface MayanTaskIterator {
    suspend operator fun hasNext(): Boolean
    suspend operator fun next(): MayanTask
}