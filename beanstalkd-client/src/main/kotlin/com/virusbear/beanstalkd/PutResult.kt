package com.virusbear.beanstalkd

sealed class PutResult {
    data class Inserted(val id: UInt)
    data class Buried(val id: UInt): PutResult()
    data object ExpectedCrLf: PutResult()
    data object JobTooBig: PutResult()
    data object Draining: PutResult()
}