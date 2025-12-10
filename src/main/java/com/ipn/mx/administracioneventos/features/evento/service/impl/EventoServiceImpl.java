package com.ipn.mx.administracioneventos.features.evento.service.impl;

import com.ipn.mx.administracioneventos.core.domain.Evento;
import com.ipn.mx.administracioneventos.features.evento.repository.EventoRepository;
import com.ipn.mx.administracioneventos.features.evento.service.EventoService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class EventoServiceImpl implements EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<com.ipn.mx.administracioneventos.core.domain.Evento> findAll() {
        return eventoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public com.ipn.mx.administracioneventos.core.domain.Evento findById(long id) {
        return eventoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public com.ipn.mx.administracioneventos.core.domain.Evento save(com.ipn.mx.administracioneventos.core.domain.Evento evento) {
        return eventoRepository.save(evento);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        eventoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayInputStream reportePDF(List<com.ipn.mx.administracioneventos.core.domain.Evento> listaEventos) {

        Document documento = new Document();
        ByteArrayOutputStream salida = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(documento, salida);
            documento.open();
            Font tipoDeLetra = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.LIGHT_GRAY);
            Paragraph parrafo = new Paragraph("Lista de eventos", tipoDeLetra);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            documento.add(parrafo);
            documento.add(Chunk.NEWLINE);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);
            PdfPTable table = new PdfPTable(5);
            Stream.of("ID", "Nombre", "Descripcion", "Fecha de inicio", "Fecha fin")
                    .forEach(headertitle ->{
                        PdfPCell encabezadoTabla = new PdfPCell();
                        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.RED);

                        encabezadoTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        encabezadoTabla.setHorizontalAlignment(Element.ALIGN_CENTER);
                        encabezadoTabla.setVerticalAlignment(Element.ALIGN_CENTER);
                        encabezadoTabla.setBorder(2);
                        encabezadoTabla.setPhrase(new Phrase(headertitle, headFont));
                        table.addCell(encabezadoTabla);
                    });

            for(com.ipn.mx.administracioneventos.core.domain.Evento evento : listaEventos){

                PdfPCell celdaId = new PdfPCell(new Phrase(String.valueOf(evento.getIdEvento()), textFont));
                celdaId.setPadding(1);
                celdaId.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaId.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(celdaId);

                PdfPCell celdaNombre = new PdfPCell(new Phrase(String.valueOf(evento.getNombreEvento()), textFont));
                celdaNombre.setPadding(1);
                celdaNombre.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaNombre.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(celdaNombre);

                PdfPCell celdaDescripcion = new PdfPCell(new Phrase(String.valueOf(evento.getDescripcionEvento()), textFont));
                celdaDescripcion.setPadding(1);
                celdaDescripcion.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaDescripcion.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(celdaDescripcion);

                PdfPCell celdaFechaInicio = new PdfPCell(new Phrase(String.valueOf(evento.getFechaInicio()), textFont));
                celdaFechaInicio.setPadding(1);
                celdaFechaInicio.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaFechaInicio.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(celdaFechaInicio);

                PdfPCell celdaFechaFin = new PdfPCell(new Phrase(String.valueOf(evento.getFechaFin()), textFont));
                celdaFechaFin.setPadding(1);
                celdaFechaFin.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaFechaFin.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(celdaFechaFin);
            }
            documento.add(table);
            documento.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(salida.toByteArray());
    }
}
