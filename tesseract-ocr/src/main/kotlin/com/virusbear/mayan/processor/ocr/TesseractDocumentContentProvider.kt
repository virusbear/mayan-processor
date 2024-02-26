package com.virusbear.mayan.processor.ocr

import com.virusbear.mayan.processor.Document
import com.virusbear.mayan.processor.DocumentContentProvider
import com.virusbear.mayan.processor.Region
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.sourceforge.tess4j.ITessAPI.TessOcrEngineMode
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode
import net.sourceforge.tess4j.Tesseract
import java.awt.Rectangle
import java.io.ByteArrayInputStream
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.ceil
import kotlin.math.floor

class TesseractDocumentContentProvider(
    private val dataPath: File,
    private val ocrEngineMode: Int = TessOcrEngineMode.OEM_LSTM_ONLY,
    private val pageSegmentationMode: Int = TessPageSegMode.PSM_AUTO_OSD
): DocumentContentProvider {
    private lateinit var document: Document

    override fun withDocument(doc: Document) {
        document = doc
    }

    override suspend fun regex(pattern: String, group: Int): String =
        pattern.toRegex().find(document.content())?.groups?.get(group)?.value ?: ""

    override suspend fun regex(pattern: String, group: String): String =
        pattern.toRegex().find(document.content())?.groups?.get(group)?.value ?: ""

    override suspend fun ocr(region: Region): String {
        val imageData = when(region.page) {
            Region.Page.First -> document.pages().first()
            Region.Page.Last -> document.pages().last()
        }.image()

        val img = withContext(Dispatchers.IO) {
            ImageIO.read(ByteArrayInputStream(imageData))
        }

        with(Tesseract()) {
            setDatapath(dataPath.absolutePath)
            setOcrEngineMode(ocrEngineMode)
            setPageSegMode(pageSegmentationMode)

            return doOCR(
                img,
                null,
                listOf(
                    Rectangle(
                        floor(img.width * region.x).toInt(),
                        floor(img.height * region.y).toInt(),
                        ceil(img.width * region.width).toInt(),
                        ceil(img.height * region.height).toInt()
                    )
                ))
        }
    }
}