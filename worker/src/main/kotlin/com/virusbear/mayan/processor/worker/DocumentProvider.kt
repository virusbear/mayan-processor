package com.virusbear.mayan.processor.worker

import com.virusbear.mayan.processor.impl.MayanDocument

interface DocumentProvider {
    suspend fun provide(documentId: Int): MayanDocument
}