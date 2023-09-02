package com.virusbear.beanstalkd

import com.virusbear.beanstalkd.operation.Operation
import java.io.Closeable

interface Connection: Closeable {
    suspend fun <T> handle(operation: Operation<T>): Result<T>
}