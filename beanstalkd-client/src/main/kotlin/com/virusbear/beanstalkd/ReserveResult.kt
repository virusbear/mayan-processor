package com.virusbear.beanstalkd

import kotlinx.coroutines.Job

sealed class ReserveResult {
    data object DeadlineSoon: ReserveResult()
    data object TimedOut: ReserveResult()
    data object NotFound: ReserveResult()
    data class Success(val job: Job): ReserveResult()
}