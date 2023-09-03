package com.virusbear.beanstalkd.response

object OutOfMemoryResponse: UnitResponse("OUT_OF_MEMORY")
object InternalErrorResponse: UnitResponse("INTERNAL_ERROR")
object BadFormatResponse: UnitResponse("BAD_FORMAT")
object UnknownCommandResponse: UnitResponse("UNKNOWN_COMMAND")

object ExpectedCrlfResponse: UnitResponse("EXPECTED_CRLF")
object JobTooBigResponse: UnitResponse("JOB_TOO_BIG")
object DrainingResponse: UnitResponse("DRAINING")

object DeadlineSoonResponse: UnitResponse("DEADLINE_SOON")
object TimedOutResponse: UnitResponse("TIMED_OUT")
object NotFoundResponse: UnitResponse("NOT_FOUND")

object DeletedResponse: UnitResponse("DELETED")
object ReleasedResponse: UnitResponse("RELEASED")
object TouchedResponse: UnitResponse("TOUCHED")
object NotIgnoredResponse: UnitResponse("NOT_IGNORED")
object PausedResponse: UnitResponse("PAUSED")