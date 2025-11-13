package com.ipn.mx.administracioneventos.features.asistente.service;

import com.ipn.mx.administracioneventos.core.domain.Asistente;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface AsistenteService {

    public List<Asistente> findAllAsistentes();
    public Asistente findByIdAsistente(long id);
    public Asistente saveAsistente(Asistente asistente);
    public void deleteAsistente(Long id);

    public ByteArrayInputStream reportePDF(List<Asistente> listaAsistente);

}
