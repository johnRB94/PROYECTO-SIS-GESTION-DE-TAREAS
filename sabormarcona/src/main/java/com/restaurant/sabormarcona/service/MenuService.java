package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.MenuCategory;
import com.restaurant.sabormarcona.model.MenuItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final List<MenuCategory> categories = new ArrayList<>();
    private final List<MenuItem> items = new ArrayList<>();
    private final AtomicLong catSeq = new AtomicLong(0);
    private final AtomicLong itemSeq = new AtomicLong(0);

    public MenuService() {
        // Semilla de categorías
        MenuCategory entradas = addCategory("Entradas");
        MenuCategory fondos   = addCategory("Platos de Fondo");
        MenuCategory bebidas  = addCategory("Bebidas");
        MenuCategory postres  = addCategory("Postres");

        // Semilla de ítems del menú
        addItem("Ceviche clásico", new BigDecimal("28.00"), "Pescado fresco con limón y ají limo", entradas, true);
        addItem("Arroz con mariscos", new BigDecimal("32.00"), "Mixto con mariscos de la casa", fondos, true);
        addItem("Chicha morada", new BigDecimal("8.00"), "Vaso 500ml", bebidas, true);
        addItem("Suspiro a la limeña", new BigDecimal("12.00"), "Porción individual", postres, true);
    }

    // ===== CATEGORÍAS =====
    public List<MenuCategory> findAllCategories() {
        return new ArrayList<>(categories);
    }

    public MenuCategory addCategory(String nombre) {
        MenuCategory c = new MenuCategory(catSeq.incrementAndGet(), nombre);
        categories.add(c);
        return c;
    }

    public boolean deleteCategory(Long id) {
        // Si eliminas categoría, no tocamos los ítems en esta demo.
        return categories.removeIf(c -> Objects.equals(c.getId(), id));
    }

    public Optional<MenuCategory> findCategoryById(Long id) {
        return categories.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst();
    }

    // ===== ÍTEMS =====
    public List<MenuItem> findAllItems() {
        return new ArrayList<>(items);
    }

    public List<MenuItem> findItemsByCategory(Long categoryId) {
        if (categoryId == null) return findAllItems();
        return items.stream()
                .filter(i -> i.getCategoria() != null && Objects.equals(i.getCategoria().getId(), categoryId))
                .collect(Collectors.toList());
    }

    public MenuItem addItem(String nombre, BigDecimal precio, String descripcion, MenuCategory categoria, boolean disponible) {
        MenuItem item = new MenuItem(itemSeq.incrementAndGet(), nombre, precio, descripcion, categoria, disponible);
        items.add(item);
        return item;
    }

    public boolean removeItem(Long id) {
        return items.removeIf(i -> Objects.equals(i.getId(), id));
    }

    public Optional<MenuItem> findItem(Long id) {
        return items.stream().filter(i -> Objects.equals(i.getId(), id)).findFirst();
    }
}