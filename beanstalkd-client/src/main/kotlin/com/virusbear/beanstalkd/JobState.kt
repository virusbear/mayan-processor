package com.virusbear.beanstalkd

enum class JobState {
    Ready,
    Reserved,
    Delayed,
    Buried
}