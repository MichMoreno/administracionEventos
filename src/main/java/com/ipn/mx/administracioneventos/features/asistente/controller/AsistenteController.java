package com.ipn.mx.administracioneventos.features.asistente.controller;

import com.ipn.mx.administracioneventos.core.domain.Asistente;
import com.ipn.mx.administracioneventos.features.asistente.service.AsistenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/asistentes")
public class AsistenteController {

    @Autowired
    private AsistenteService serviceAsistent;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Asistente> readAll(){
        return serviceAsistent.findAllAsistentes();
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable long id){
        Asistente asistente = null;
        Map<String, Object> respuesta = new HashMap<>();

        try {
            asistente = serviceAsistent.findByIdAsistente(id);
        } catch(DataAccessException e) {
            respuesta.put("mensaje", "Error al realizar la consulta en la base de datos");
            respuesta.put("error", e.getMessage().concat(":").concat(e.getCause().getMessage()));
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(asistente == null){
            respuesta.put("mensaje", "El asistente con ID:".concat(String.valueOf(id)).concat(" no existe en la base de datos"));
            return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
        }


        return new ResponseEntity<>(asistente, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Asistente asistente) {
        Asistente asistenteGuardado = null;
        Map<String, Object> respuesta = new HashMap<>();

        try {
            asistenteGuardado = serviceAsistent.saveAsistente(asistente);
        } catch(DataAccessException e) {
            respuesta.put("mensaje", "Error al insertar el asistente en la base de datos");
            respuesta.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        respuesta.put("mensaje", "El asistente se ha creado con éxito");
        respuesta.put("asistente", asistenteGuardado);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Asistente asistente, @PathVariable Long id){
        Asistente a = serviceAsistent.findByIdAsistente(id);
        Map<String, Object> respuesta = new HashMap<>();

        if(a == null){
            respuesta.put("mensaje", "Error: no se puede editar, el asistente con ID:".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
        }

        try {

            a.setNombre(asistente.getNombre());
            a.setEmail(asistente.getEmail());
            a.setMaterno(asistente.getMaterno());
            a.setPaterno(asistente.getPaterno());
            a.setFechaRegistro(asistente.getFechaRegistro());
            a.setIdEvento(asistente.getIdEvento());


            Asistente asistenteActualizado = serviceAsistent.saveAsistente(a);

        } catch(DataAccessException e) {
            respuesta.put("mensaje", "Error al actualizar el asistente en la base de datos");
            respuesta.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        respuesta.put("mensaje", "El asistente se ha actualizado con éxito");
        respuesta.put("asistente", a);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){
        Map<String, Object> respuesta = new HashMap<>();


        Asistente asistente = serviceAsistent.findByIdAsistente(id);
        if(asistente == null) {
            respuesta.put("mensaje", "El asistente con ID:".concat(String.valueOf(id)).concat(" no existe en la base de datos"));
            return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
        }

        try {
            serviceAsistent.deleteAsistente(id);
        } catch (DataAccessException e) {
            respuesta.put("mensaje", "Error al eliminar el registro de la base de datos");
            respuesta.put("error", e.getMessage().concat(":").concat(e.getCause().getMessage()));
            return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        respuesta.put("mensaje", "El asistente ha sido eliminado correctamente");
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }
    @GetMapping("/reporte-pdf")
    public ResponseEntity<byte[]> generarReporteAsistentesPDF() {
        try {
            List<Asistente> asistentes = serviceAsistent.findAllAsistentes();
            ByteArrayInputStream pdfStream = serviceAsistent.reportePDF(asistentes);

            byte[] pdfBytes = pdfStream.readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename("reporte-asistentes.pdf")
                    .build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
