package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.MenuCategoria;
import com.restaurant.sabormarcona.model.MenuItem;
import com.restaurant.sabormarcona.repository.MenuCategoriaRepository;
import com.restaurant.sabormarcona.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MenuService {

    @Autowired
    private MenuCategoriaRepository categoriaRepository;
    
    @Autowired
    private MenuItemRepository itemRepository;

    // CATEGORÍAS
    public List<MenuCategoria> findAllCategories() {
        return categoriaRepository.findAllByOrderByNombreAsc();
    }

    public MenuCategoria addCategory(String nombre) {
        MenuCategoria categoria = new MenuCategoria();
        categoria.setNombre(nombre);
        return categoriaRepository.save(categoria);
    }

    public boolean deleteCategory(Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<MenuCategoria> findCategoryById(Long id) {
        return categoriaRepository.findById(id);
    }

    // ITEMS
    public List<MenuItem> findAllItems() {
        return itemRepository.findAll();
    }

    public List<MenuItem> findItemsByCategory(Long categoryId) {
        if (categoryId == null) return findAllItems();
        return itemRepository.findItemsDisponiblesPorCategoria(categoryId);
    }

    public MenuItem addItem(String nombre, BigDecimal precio, String descripcion, 
                           MenuCategoria categoria, boolean disponible) {
        MenuItem item = new MenuItem();
        item.setNombre(nombre);
        item.setPrecio(precio);
        item.setDescripcion(descripcion);
        item.setCategoria(categoria);
        item.setDisponible(disponible);
        return itemRepository.save(item);
    }

    public boolean removeItem(Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<MenuItem> findItem(Long id) {
        return itemRepository.findById(id);
    }
}
