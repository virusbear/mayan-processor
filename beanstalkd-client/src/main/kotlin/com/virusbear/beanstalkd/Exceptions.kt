package com.virusbear.beanstalkd

class DisconnectedException: RuntimeException("Disconnected from beanstalkd server")
class InvalidResponseException: RuntimeException("The server responded with an invalid response")
class UnknownResponseException: RuntimeException("The server responded with an unknwon response")

class OutOfMemoryException: RuntimeException("Beanstalkd out of memory")

class InternalErrorException: RuntimeException("Beanstalkd encountered an internal error")

class BadFormatException: RuntimeException("Command malformed")

class UnknownCommandException: RuntimeException("Command unknown")

class ExpectedCrlfException: RuntimeException("Expected Crlf")

class JobTooBigException: RuntimeException("Job too big")

class DrainingException: RuntimeException("Draining")
class BuriedException(val id: UInt): RuntimeException("Job $id is buried")

class DeadlineSoonException: RuntimeException("TTR approaches deadline")
class TimedOutException: RuntimeException("Request timed out")
class NotFoundException: RuntimeException("Not found")