package com.virusbear.beanstalkd.operation

import com.virusbear.beanstalkd.*
import com.virusbear.beanstalkd.response.*

abstract class AbstractOperation<T>: Operation<T> {
    override suspend fun read(response: Response): Result<T> =
        when(response) {
            is OutOfMemoryResponse -> Result.failure(OutOfMemoryException())
            is InternalErrorResponse -> Result.failure(InternalErrorException())
            is BadFormatResponse -> Result.failure(BadFormatException())
            is UnknownCommandResponse -> Result.failure(UnknownCommandException())
            else -> Result.failure(RuntimeException("An unknown error occurred reading response of type ${response::class.simpleName}"))
        }
}