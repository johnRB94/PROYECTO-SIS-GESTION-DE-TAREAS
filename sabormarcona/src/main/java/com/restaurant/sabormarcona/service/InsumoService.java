package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Insumo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class InsumoService {
    private final List<Insumo> insumos = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public InsumoService() {
        inicializarDatosPrueba();
    }

    private void inicializarDatosPrueba() {
        agregarInsumo(new Insumo("Arroz", "Alimentos", 50, "kg"));
        agregarInsumo(new Insumo("Aceite", "Alimentos", 20, "L"));
        agregarInsumo(new Insumo("Azúcar", "Alimentos", 100, "Kg"));
    }

    public List<Insumo> obtenerTodosLosInsumos() {
        return new ArrayList<>(insumos);
    }

    public Optional<Insumo> obtenerInsumoPorId(Long id) {
        return insumos.stream().filter(i -> i.getId().equals(id)).findFirst();
    }

    public Insumo agregarInsumo(Insumo insumo) {
        insumo.setId(idGenerator.getAndIncrement());
        insumos.add(0, insumo); 
        return insumo;
    }

    public boolean eliminarInsumo(Long id) {
        return insumos.removeIf(i -> i.getId().equals(id));
    }

    public boolean actualizarInsumo(Insumo insumo) {
        Optional<Insumo> existente = obtenerInsumoPorId(insumo.getId());
        if (existente.isPresent()) {
            Insumo i = existente.get();
            i.setNombre(insumo.getNombre());
            i.setTipo(insumo.getTipo());
            i.setStock(insumo.getStock());
            i.setUnidad(insumo.getUnidad());
            return true;
        }
        return false;
    }

    public List<Insumo> obtenerInsumosPorTipo(String tipo) {
        List<Insumo> resultado = new ArrayList<>();
        for (Insumo i : insumos) {
            if (i.getTipo().equalsIgnoreCase(tipo)) {
                resultado.add(i);
            }
        }
        return resultado;
    }
}
