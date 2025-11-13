package com.ipn.mx.administracioneventos.features.asistente.service.impl;

import com.ipn.mx.administracioneventos.core.domain.Asistente;
import com.ipn.mx.administracioneventos.features.asistente.repository.AsistenteRepository;
import com.ipn.mx.administracioneventos.features.asistente.service.AsistenteService;
import com.ipn.mx.administracioneventos.util.service.EmailService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
public class AsistenteServiceImpl implements AsistenteService {

    @Autowired
    private AsistenteRepository asistenteRepository;

    @Autowired
    private EmailService emailService; // ✅ SOLO aquí para asistentes

    @Override
    @Transactional(readOnly = true)
    public List<Asistente> findAllAsistentes() {
        return asistenteRepository.findAllAsistentes();
    }

    @Override
    @Transactional(readOnly = true)
    public Asistente findByIdAsistente(long id) {
        return asistenteRepository.findByIdAsistentes(id).orElse(null);
    }

    @Override
    @Transactional
    public Asistente saveAsistente(Asistente asistente) {
        Asistente asistenteGuardado = asistenteRepository.save(asistente);

        // ✅ SOLO para asistentes: enviar correo de confirmación
        enviarCorreoConfirmacion(asistenteGuardado);

        return asistenteGuardado;
    }

    @Override
    @Transactional
    public void deleteAsistente(Long id) {
        asistenteRepository.deleteByIdAsistente(id);
    }

    // ✅ MÉTODO PRIVADO para enviar correo (solo para asistentes)
    private void enviarCorreoConfirmacion(Asistente asistente) {
        try {
            String asunto = "✅ Registro Exitoso - Administración de Eventos";
            String mensaje = String.format("""
                <html>
                <body>
                    <h2 style="color: #2E86C1;">¡Registro Exitoso!</h2>
                    <p>Estimado/a <strong>%s %s %s</strong>,</p>
                    <p>Su registro en nuestro sistema de Administración de Eventos ha sido exitoso.</p>
                    
                    <div style="background-color: #F8F9F9; padding: 15px; border-left: 4px solid #2E86C1;">
                        <h3 style="color: #2E86C1;">Datos de su registro:</h3>
                        <ul>
                            <li><strong>Nombre:</strong> %s %s %s</li>
                            <li><strong>Email:</strong> %s</li>
                            <li><strong>Fecha de registro:</strong> %s</li>
                            <li><strong>ID de asistente:</strong> %s</li>
                        </ul>
                    </div>
                    
                    <p>Adjunto encontrará el diccionario de datos del evento.</p>
                    <p>Si tiene alguna pregunta, no dude en contactarnos.</p>
                    <br>
                    <p>Atentamente,<br>
                    <strong>Equipo de Administración de Eventos</strong></p>
                </body>
                </html>
                """,
                    asistente.getNombre(), asistente.getPaterno(), asistente.getMaterno(),
                    asistente.getNombre(), asistente.getPaterno(), asistente.getMaterno(),
                    asistente.getEmail(),
                    asistente.getFechaRegistro(),
                    asistente.getIdAsistente()
            );

            emailService.enviarCorreo(asistente.getEmail(), asunto, mensaje);
            log.info("📧 Correo enviado exitosamente a: {}", asistente.getEmail());

        } catch (Exception e) {
            log.error("❌ Error al enviar correo a: {}", asistente.getEmail(), e);
            // No lanzar excepción para no afectar el guardado del asistente
        }
    }

    @Override
    @Transactional
    public ByteArrayInputStream reportePDF(List<Asistente> listaAsistentes) {
        Document documentoAsistente = new Document();
        ByteArrayOutputStream salida = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(documentoAsistente, salida);
            documentoAsistente.open();

            Font tipoDeLetra = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.LIGHT_GRAY);
            Paragraph parrafo = new Paragraph("Lista de Asistentes", tipoDeLetra);
            parrafo.setAlignment(Element.ALIGN_CENTER);
            documentoAsistente.add(parrafo);
            documentoAsistente.add(Chunk.NEWLINE);

            Font textFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK);

            PdfPTable table = new PdfPTable(4);
            Stream.of("ID", "Nombre", "Email", "Fecha Registro")
                    .forEach(headerTitle -> {
                        PdfPCell encabezadoTabla = new PdfPCell();
                        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.RED);
                        encabezadoTabla.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        encabezadoTabla.setHorizontalAlignment(Element.ALIGN_CENTER);
                        encabezadoTabla.setVerticalAlignment(Element.ALIGN_CENTER);
                        encabezadoTabla.setBorder(2);
                        encabezadoTabla.setPhrase(new Phrase(headerTitle, headFont));
                        table.addCell(encabezadoTabla);
                    });

            for(Asistente asistente : listaAsistentes) {
                PdfPCell celdaId = new PdfPCell(new Phrase(String.valueOf(asistente.getIdAsistente()), textFont));
                celdaId.setPadding(1);
                celdaId.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaId.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(celdaId);

                PdfPCell celdaNombre = new PdfPCell(new Phrase(asistente.getNombre() + " " + asistente.getPaterno() + " " + asistente.getMaterno(), textFont));
                celdaNombre.setPadding(1);
                celdaNombre.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaNombre.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(celdaNombre);

                PdfPCell celdaEmail = new PdfPCell(new Phrase(asistente.getEmail(), textFont));
                celdaEmail.setPadding(1);
                celdaEmail.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaEmail.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(celdaEmail);

                PdfPCell celdaFecha = new PdfPCell(new Phrase(String.valueOf(asistente.getFechaRegistro()), textFont));
                celdaFecha.setPadding(1);
                celdaFecha.setVerticalAlignment(Element.ALIGN_MIDDLE);
                celdaFecha.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(celdaFecha);
            }

            documentoAsistente.add(table);
            documentoAsistente.close();

        } catch (Exception e) {
            log.error("Error al generar PDF de asistentes", e);
        }

        return new ByteArrayInputStream(salida.toByteArray());
    }
}