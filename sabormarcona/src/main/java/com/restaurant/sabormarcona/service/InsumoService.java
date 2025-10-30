package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.exception.DuplicateResourceException;
import com.restaurant.sabormarcona.exception.ResourceNotFoundException;
import com.restaurant.sabormarcona.model.Insumo;
import com.restaurant.sabormarcona.repository.InsumoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InsumoService {

    private final InsumoRepository insumoRepository;

    // CREATE
    public Insumo agregarInsumo(Insumo nuevoInsumo) {
        log.debug("Agregando nuevo insumo: {}", nuevoInsumo.getNombre());

        if (insumoRepository.existsByNombre(nuevoInsumo.getNombre())) {
            log.warn("Intento de crear insumo duplicado: {}", nuevoInsumo.getNombre());
            throw new DuplicateResourceException(
                    "El insumo '" + nuevoInsumo.getNombre() + "' ya existe");
        }

        Insumo insumoGuardado = insumoRepository.save(nuevoInsumo);
        log.info("Insumo creado exitosamente con ID: {}", insumoGuardado.getId());
        return insumoGuardado;
    }

    // READ
    @Transactional(readOnly = true)
    public List<Insumo> obtenerTodosLosInsumos() {
        log.debug("Obteniendo todos los insumos");
        return insumoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Insumo obtenerInsumoPorId(Long id) {
        log.debug("Buscando insumo con ID: {}", id);
        return insumoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Insumo no encontrado con ID: {}", id);
                    return new ResourceNotFoundException("Insumo no encontrado con ID: " + id);
                });
    }

    @Transactional(readOnly = true)
    public List<Insumo> obtenerInsumosConBajoStock(int stockMinimo) {
        log.debug("Obteniendo insumos con stock menor a: {}", stockMinimo);
        return insumoRepository.findByStockLessThan(stockMinimo);
    }

    @Transactional(readOnly = true)
    public List<Insumo> buscarInsumosPorNombre(String nombre) {
        log.debug("Buscando insumos con nombre similar a: {}", nombre);
        return insumoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // UPDATE
    public Insumo actualizarInsumo(Insumo insumoActualizado) {
        log.debug("Actualizando insumo con ID: {}", insumoActualizado.getId());

        Insumo insumoExistente = obtenerInsumoPorId(insumoActualizado.getId());

        if (!insumoExistente.getNombre().equals(insumoActualizado.getNombre())) {
            if (insumoRepository.existsByNombre(insumoActualizado.getNombre())) {
                throw new DuplicateResourceException(
                        "El nombre '" + insumoActualizado.getNombre() + "' ya existe");
            }
        }

        insumoExistente.setNombre(insumoActualizado.getNombre());
        insumoExistente.setStock(insumoActualizado.getStock());

        Insumo insumoActualizadoDB = insumoRepository.save(insumoExistente);
        log.info("Insumo actualizado exitosamente con ID: {}", insumoActualizadoDB.getId());
        return insumoActualizadoDB;
    }

    public void incrementarStock(Long id, Integer cantidad) {
        log.debug("Incrementando stock del insumo ID: {} en {} unidades", id, cantidad);

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        Insumo insumo = obtenerInsumoPorId(id);
        int stockAnterior = insumo.getStock();
        insumo.setStock(stockAnterior + cantidad);
        insumoRepository.save(insumo);

        log.info("Stock incrementado para insumo '{}'. Stock anterior: {}, Nuevo stock: {}",
                insumo.getNombre(), stockAnterior, insumo.getStock());
    }

    public void decrementarStock(Long id, Integer cantidad) {
        log.debug("Decrementando stock del insumo ID: {} en {} unidades", id, cantidad);

        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        Insumo insumo = obtenerInsumoPorId(id);
        int stockAnterior = insumo.getStock();

        if (stockAnterior < cantidad) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para el insumo '" + insumo.getNombre() +
                            "'. Stock actual: " + stockAnterior + ", Cantidad solicitada: " + cantidad);
        }

        insumo.setStock(stockAnterior - cantidad);
        insumoRepository.save(insumo);

        log.info("Stock decrementado para insumo '{}'. Stock anterior: {}, Nuevo stock: {}",
                insumo.getNombre(), stockAnterior, insumo.getStock());
    }

    public void actualizarStock(Long id, Integer nuevoStock) {
        log.debug("Actualizando stock del insumo ID: {} a {}", id, nuevoStock);

        if (nuevoStock == null || nuevoStock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        Insumo insumo = obtenerInsumoPorId(id);
        int stockAnterior = insumo.getStock();
        insumo.setStock(nuevoStock);
        insumoRepository.save(insumo);

        log.info("Stock actualizado para insumo '{}'. Stock anterior: {}, Nuevo stock: {}",
                insumo.getNombre(), stockAnterior, nuevoStock);
    }

    // DELETE
    public void eliminarInsumo(Long id) {
        log.debug("Eliminando insumo con ID: {}", id);

        if (!insumoRepository.existsById(id)) {
            log.warn("Intento de eliminar insumo inexistente con ID: {}", id);
            throw new ResourceNotFoundException("Insumo no encontrado con ID: " + id);
        }

        insumoRepository.deleteById(id);
        log.info("Insumo eliminado exitosamente con ID: {}", id);
    }

    // MÉTODOS ADICIONALES
    @Transactional(readOnly = true)
    public boolean existeInsumo(String nombre) {
        return insumoRepository.existsByNombre(nombre);
    }

    @Transactional(readOnly = true)
    public long contarInsumos() {
        return insumoRepository.count();
    }

    @Transactional(readOnly = true)
    public long contarInsumosBajoStock(int stockMinimo) {
        return insumoRepository.countByStockLessThan(stockMinimo);
    }
}
