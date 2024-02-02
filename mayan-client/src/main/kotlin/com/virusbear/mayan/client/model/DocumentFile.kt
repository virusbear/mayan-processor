package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentFile

class DocumentFile(
    private val client: DocumentFileClient,
    private val documentFile: ApiDocumentFile
)