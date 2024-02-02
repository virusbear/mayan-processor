package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiDocumentVersion

class DocumentVersion(
    private val client: DocumentVersionClient,
    private val documentVersion: ApiDocumentVersion
)