package com.ipn.mx.administracioneventos.features.evento.controller;



import com.ipn.mx.administracioneventos.core.domain.Evento;
import com.ipn.mx.administracioneventos.features.evento.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.NestedCheckedException;
import org.springframework.web.servlet.HandlerMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin    (origins={"*"})
@RestController
@RequestMapping("/api/v1/eventos")
public class EventoController {
    @Autowired
    private EventoService service;
    @Autowired
    private HandlerMapping resourceHandlerMapping;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Evento> readAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> readById(@PathVariable long id){
        Evento evento = null;
        Map<String, Object> respuesta = new HashMap<>();
        try{
            evento = service.findById(id);
        }catch(DataAccessException e){
            respuesta.put("mensaje", "Error al realizar la consulta en la base de datos");
            respuesta.put("error", e.getMessage().concat(":").concat(e.getCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(evento == null){
            respuesta.put("mensaje","El evento con ID:".concat(String.valueOf(id)).concat(" no existe en la base de datos"));
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Evento>(evento, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Evento evento){

        Evento e = null;
        Map<String, Object> respuesta = new HashMap<>();
        try{
            e = service.save(evento);
        }catch(DataAccessException ex){
            respuesta.put("mensaje", "Error al insertar el evento en la base de datos");
            respuesta.put("error", ex.getMessage().concat(":").concat(ex.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        respuesta.put("mensaje", "El evento se ha creado con éxito");
        respuesta.put("evento", e);
        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Evento evento, @PathVariable Long id){

        Evento e = service.findById(id);
        Evento eventoActualizado = null;
        Map<String, Object> respuesta = new HashMap<>();
        if(e == null){
            respuesta.put("mensaje", "Error: no se puede editar, el evento con ID:".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
        }
        try {
            e.setNombreEvento(evento.getNombreEvento());
            e.setDescripcionEvento(evento.getDescripcionEvento());
            e.setFechaInicio(evento.getFechaInicio());
            e.setFechaFin(evento.getFechaFin());

            e = service.save(evento);
        } catch(DataAccessException ex){
            respuesta.put("mensaje", "Error al insertar el evento en la base de datos");
            respuesta.put("error", ex.getMessage().concat(":").concat(ex.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        respuesta.put("mensaje", "El evento se ha actualizado con éxito");
        respuesta.put("evento", e);
        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){
        Map<String, Object> respuesta = new HashMap<>();
        try{
            service.delete(id);
        }catch(DataAccessException e){
            respuesta.put("mensaje", "Error al eliminar el registro de la base de datos");
            respuesta.put("error", e.getMessage().concat(":").concat(e.getCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        respuesta.put("mensaje", "el evento ha sido eliminado corrrectamente");
        return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.OK);
    }

}