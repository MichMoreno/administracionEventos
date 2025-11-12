package com.ipn.mx.administracioneventos.features.asistente.controller;

import com.ipn.mx.administracioneventos.core.domain.Asistente;
import com.ipn.mx.administracioneventos.core.domain.Evento;
import com.ipn.mx.administracioneventos.features.asistente.repository.AsistenteRepository;
import com.ipn.mx.administracioneventos.features.asistente.service.AsistenteService;
import com.ipn.mx.administracioneventos.features.evento.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/asistentes")

public class AsistenteController {
    @Autowired
    private AsistenteService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Asistente> readAll(){
        return service.findAllAsistentes();
    }

    @GetMapping("(/id)")
    public ResponseEntity<Asistente> readById(@PathVariable long id){
        Asistente asistente = null;
        Map<String, Object > respuesta = new  HashMap<>();
        try {
            asistente = service.findByIdAsistente(id);
        }catch(DataAccessException e){
            respuesta.put("mensaje", "Error al realizar la consulta en la base de datos");
            respuesta.put("error", e.getMessage().concat(":").concat(e.getCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(asistente == null){
            respuesta.put("mensaje","El asistente con ID:".concat(String.valueOf(id)).concat(" no existe en la base de datos"));
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Evento>(asistente, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody Asistente asistente, @PathVariable Long id){
        Asistente a = service.findByIdAsistente(id);
        Asistente AsistenteActualizado = null;
        Map<String, Object> respuesta = new HashMap<>();
        if(a == null){
            respuesta.put("mensaje", "Error: no se puede editar, el evento con ID:".concat(id.toString().concat(" no existe en la base de datos")));
            return new ResponseEntity<Map<String, Object>>(respuesta, HttpStatus.NOT_FOUND);
        }
        try{

        }
    }

}

