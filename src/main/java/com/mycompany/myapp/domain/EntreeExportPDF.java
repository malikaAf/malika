package com.mycompany.myapp.domain;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import com.mycompany.myapp.service.dto.EntreeDTO;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

public class EntreeExportPDF {

    private List<EntreeDTO> listeEntree;

    public EntreeExportPDF(List<EntreeDTO> listeEntree) {
        this.listeEntree = listeEntree;
    }

    private void writeTableHeader() {}

    private void writeTableData() {}

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);

        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        document.add(new Paragraph("Liste de toutes les entrées"));
        document.close();
    }
}
