package checkRTMP

import java.nio.ByteBuffer

// Модель для нашего распарсенного пакета
data class RtmpPacket(
    val typeId: Int,       // 0x09 для видео, 0x08 для аудио
    val payload: ByteArray // Готовые байты для MediaCodec
)

class RtmpStreamParser {

    fun parseNextPacket(buffer: ByteBuffer): RtmpPacket? {
        // Проверяем, есть ли вообще данные для чтения (хотя бы Basic Header)
        if (!buffer.hasRemaining()) return null

        // ШАГ 1: Парсим Basic Header (1 байт)
        val basicHeader = buffer.get().toInt() and 0xFF
        val fmt = basicHeader shr 6
        val csid = basicHeader and 0x3F

        // ШАГ 2: Парсим Message Header
        // Для простоты рассмотрим случай fmt = 0 (новое полноценное сообщение)
        if (fmt == 0) {
            // Проверяем, что в буфере хватает байт для заголовка (11 байт)
            if (buffer.remaining() < 11) return null

            // Пропускаем timestamp (3 байта)
            buffer.get(); buffer.get(); buffer.get()

            // Читаем Message Length (3 байта). Переводим 3 отдельных байта в один Int
            val byte1 = buffer.get().toInt() and 0xFF
            val byte2 = buffer.get().toInt() and 0xFF
            val byte3 = buffer.get().toInt() and 0xFF
            val messageLength = (byte1 shl 16) or (byte2 shl 8) or byte3

            // Читаем Message Type ID (1 байт)
            val typeId = buffer.get().toInt() and 0xFF

            // Пропускаем Message Stream ID (4 байта)
            buffer.getInt() // Сдвигает курсор сразу на 4 байта

            // ШАГ 3: Читаем Payload (сами данные)
            // Проверяем, что все байты сообщения уже долетели по сети
            if (buffer.remaining() < messageLength) {
                // Если данных не хватает, в реальном коде мы бы подождали дозагрузки сокета
                return null
            }

            val payload = ByteArray(messageLength)
            buffer.get(payload) // Копируем байты видео/аудио в наш массив

            // ШАГ 4: Возвращаем результат, если это видео
            if (typeId == 0x09) {
                println("-> Распарсили ВИДЕО пакет! Размер: $messageLength байт")
                return RtmpPacket(typeId, payload)
            } else {
                println("Пропустили служебный пакет или аудио с типом: $typeId")
            }
        }

        return null
    }
}

fun main() {
    // Симулируем байты от сервера (fmt=0, тип=0x09 (видео), длина=3 байта, данные=[10, 20, 30])
    val fakeRtmpData = byteArrayOf(
        0x00,                 // Basic Header (fmt=0, csid=0)
        0x00, 0x00, 0x00,     // Timestamp (0)
        0x00, 0x00, 0x03,     // Message Length = 3 байта (0x000003)
        0x09,                 // Message Type ID = 0x09 (ВИДЕО)
        0x00, 0x00, 0x00, 0x00, // Stream ID (0)
        0x0A, 0x0B, 0x0C      // Payload: 3 байта самого видео (10, 11, 12)
    )

    val buffer = ByteBuffer.wrap(fakeRtmpData)
    val parser = RtmpStreamParser()

    val packet = parser.parseNextPacket(buffer)

    if (packet != null) {
        println("Пакет успешно извлечен!")
        println("Данные видео (в Hex): ${packet.payload.joinToString { String.format("0x%02X", it) }}")
        // Вот этот packet.payload ты и отдаешь в MediaCodec!
    }
}
