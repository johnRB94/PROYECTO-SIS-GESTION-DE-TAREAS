package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.MenuCategoria;
import com.restaurant.sabormarcona.model.MenuItem;
import com.restaurant.sabormarcona.repository.MenuCategoriaRepository;
import com.restaurant.sabormarcona.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MenuService {

    private final MenuCategoriaRepository categoriaRepository;
    private final MenuItemRepository itemRepository;

    // CATEGORÍAS
    public List<MenuCategoria> findAllCategories() {
        log.debug("Obteniendo todas las categorías ordenadas por nombre");
        return categoriaRepository.findAllByOrderByNombreAsc();
    }

    public MenuCategoria addCategory(String nombre) {
        log.debug("Agregando nueva categoría: {}", nombre);
        MenuCategoria categoria = new MenuCategoria();
        categoria.setNombre(nombre);
        MenuCategoria categoriGuardada = categoriaRepository.save(categoria);
        log.info("Categoría creada exitosamente con ID: {}", categoriGuardada.getId());
        return categoriGuardada;
    }

    public boolean deleteCategory(Long id) {
        log.debug("Eliminando categoría con ID: {}", id);
        
        if (id == null) {
            log.warn("ID de categoría es null al intentar eliminar");
            throw new IllegalArgumentException("El ID de la categoría no puede ser null");
        }
        
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            log.info("Categoría eliminada exitosamente con ID: {}", id);
            return true;
        }
        
        log.warn("Intento de eliminar categoría inexistente con ID: {}", id);
        return false;
    }

    public Optional<MenuCategoria> findCategoryById(Long id) {
        log.debug("Buscando categoría con ID: {}", id);
        
        if (id == null) {
            log.warn("ID de categoría es null al intentar buscar");
            throw new IllegalArgumentException("El ID de la categoría no puede ser null");
        }
        
        return categoriaRepository.findById(id);
    }

    // ITEMS
    public List<MenuItem> findAllItems() {
        log.debug("Obteniendo todos los items del menú");
        return itemRepository.findAll();
    }

    public List<MenuItem> findItemsByCategory(Long categoryId) {
        log.debug("Buscando items disponibles para categoryId: {}", categoryId);
        
        if (categoryId == null) {
            log.debug("CategoryId es null, retornando todos los items");
            return findAllItems();
        }
        
        return itemRepository.findItemsDisponiblesPorCategoria(categoryId);
    }

    public MenuItem addItem(String nombre, BigDecimal precio, String descripcion, 
                           MenuCategoria categoria, boolean disponible) {
        log.debug("Agregando nuevo item: {}", nombre);
        
        MenuItem item = new MenuItem();
        item.setNombre(nombre);
        item.setPrecio(precio);
        item.setDescripcion(descripcion);
        item.setCategoria(categoria);
        item.setDisponible(disponible);
        
        MenuItem itemGuardado = itemRepository.save(item);
        log.info("Item creado exitosamente con ID: {}", itemGuardado.getId());
        return itemGuardado;
    }

    public boolean removeItem(Long id) {
        log.debug("Eliminando item con ID: {}", id);
        
        if (id == null) {
            log.warn("ID de item es null al intentar eliminar");
            throw new IllegalArgumentException("El ID del item no puede ser null");
        }
        
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            log.info("Item eliminado exitosamente con ID: {}", id);
            return true;
        }
        
        log.warn("Intento de eliminar item inexistente con ID: {}", id);
        return false;
    }

    public Optional<MenuItem> findItem(Long id) {
        log.debug("Buscando item con ID: {}", id);
        
        if (id == null) {
            log.warn("ID de item es null al intentar buscar");
            throw new IllegalArgumentException("El ID del item no puede ser null");
        }
        
        return itemRepository.findById(id);
    }
}
