import com.virusbear.mayan.processor.MayanProcessor
import com.virusbear.mayan.processor.ProcessingContext
import com.virusbear.mayan.processor.Document

class MayanProcessorImpl(
    private val initializer: suspend () -> Unit,
    private val acceptor: suspend (Document) -> Boolean,
    private val processor: suspend ProcessingContext.() -> Unit,
    private val closer: suspend () -> Unit
): MayanProcessor {
    override suspend fun init() {
        initializer()
    }

    override suspend fun accept(document: Document): Boolean =
        acceptor(document)

    override suspend fun process(scope: ProcessingContext): Unit =
        scope.processor()

    override suspend fun close() {
        closer()
    }
}