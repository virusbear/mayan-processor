id = "test1234"

accept {
    "hetzner" in content && type == "Rechnung"
}

process {
    metadata("invoiceno", regex("Rechnungsnummer: +(.*?)\\n"))
    val invoiceDate = regex("Rechnungsdatum: +(.*?)\\n")
    metadata("documentdate", invoiceDate)
    metadata("invoicedate", invoiceDate)
    metadata("paymentmethod", regex("Zahlungsart: +(.*?)\\n"))
    metadata("invoiceamount", regex("Zu zahlender Betrag: +(.*?) *€"))
    metadata("iban", regex("IBAN: (\\w{2}\\d{2}( \\d{4}){4} \\d{2})\n"))
}
