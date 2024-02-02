package com.virusbear.mayan.client.model

import com.virusbear.mayan.api.client.model.ApiComment

class Comment(
    private val client: CommentClient,
    private val comment: ApiComment
)