package com.restaurant.sabormarcona.service;

import com.restaurant.sabormarcona.model.Menu;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final List<Menu> categories = new ArrayList<>();
    private final List<Menu.MenuItem> items = new ArrayList<>(); 
    private final AtomicLong catSeq = new AtomicLong(0);
    private final AtomicLong itemSeq = new AtomicLong(0);

    public MenuService() {
        Menu entradas = addCategory("Entradas");
        Menu fondos   = addCategory("Platos de Fondo");
        Menu bebidas  = addCategory("Bebidas");
        Menu postres  = addCategory("Postres");

        addItem("Ceviche clásico", new BigDecimal("28.00"), "Pescado fresco con limón y ají limo", entradas, true);
        addItem("Arroz con mariscos", new BigDecimal("32.00"), "Mixto con mariscos de la casa", fondos, true);
        addItem("Chicha morada", new BigDecimal("8.00"), "Vaso 500ml", bebidas, true);
        addItem("Suspiro a la limeña", new BigDecimal("12.00"), "Porción individual", postres, true);
    }

    public List<Menu> findAllCategories() {
        return new ArrayList<>(categories);
    }

    public Menu addCategory(String nombre) {
        Menu c = new Menu(catSeq.incrementAndGet(), nombre);
        categories.add(c);
        return c;
    }

    public boolean deleteCategory(Long id) {
        return categories.removeIf(c -> Objects.equals(c.getId(), id));
    }

    public Optional<Menu> findCategoryById(Long id) {
        return categories.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst();
    }

    public List<Menu.MenuItem> findAllItems() { 
        return new ArrayList<>(items);
    }

    public List<Menu.MenuItem> findItemsByCategory(Long categoryId) { 
        if (categoryId == null) return findAllItems();
        return items.stream()
                .filter(i -> i.getCategoria() != null && Objects.equals(i.getCategoria().getId(), categoryId))
                .collect(Collectors.toList());
    }

    public Menu.MenuItem addItem(String nombre, BigDecimal precio, String descripcion, Menu categoria, boolean disponible) {
        Menu.MenuItem item = new Menu.MenuItem(itemSeq.incrementAndGet(), nombre, precio, descripcion, categoria, disponible);
        items.add(item);
        return item;
    }

    public boolean removeItem(Long id) {
        return items.removeIf(i -> Objects.equals(i.getId(), id));
    }

    public Optional<Menu.MenuItem> findItem(Long id) { 
        return items.stream().filter(i -> Objects.equals(i.getId(), id)).findFirst();
    }
}