package com.restaurant.sabormarcona.repository;

import com.restaurant.sabormarcona.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsumoRepository extends JpaRepository<Insumo, Long> {

       List<Insumo> findByTipo(String tipo);

       List<Insumo> findByNombreContainingIgnoreCase(String nombre);

       List<Insumo> findByStockLessThan(Integer stock);

       List<Insumo> findByStockGreaterThan(Integer stock);

       List<Insumo> findByStockBetween(Integer stockMin, Integer stockMax);

       List<Insumo> findByUnidad(String unidad);

       List<Insumo> findAllByOrderByStockAsc();

       List<Insumo> findAllByOrderByNombreAsc();

       long countByTipo(String tipo);

       long countByStockLessThan(Integer stock);

       boolean existsByNombre(String nombre);

       @Query("SELECT i FROM Insumo i WHERE i.stock <= :cantidad")
       List<Insumo> findInsumosConBajoStock(@Param("cantidad") Integer cantidad);

       @Query("SELECT i FROM Insumo i WHERE i.stock = 0")
       List<Insumo> findInsumosSinStock();

       @Query("SELECT i FROM Insumo i WHERE i.tipo = :tipo AND i.stock < :stockMinimo")
       List<Insumo> findInsumosPorTipoConBajoStock(@Param("tipo") String tipo,
                     @Param("stockMinimo") Integer stockMinimo);

       @Query("SELECT i.tipo, COUNT(i), SUM(i.stock) FROM Insumo i GROUP BY i.tipo")
       List<Object[]> obtenerEstadisticasPorTipo();

       @Query("SELECT i FROM Insumo i WHERE " +
                     "(:tipo IS NULL OR i.tipo = :tipo) AND " +
                     "(:unidad IS NULL OR i.unidad = :unidad) AND " +
                     "(:stockMinimo IS NULL OR i.stock >= :stockMinimo)")
       List<Insumo> buscarInsumosPorCriterios(@Param("tipo") String tipo,
                     @Param("unidad") String unidad,
                     @Param("stockMinimo") Integer stockMinimo);

       @Query(value = "SELECT * FROM insumos WHERE stock < 5 ORDER BY stock ASC", nativeQuery = true)
       List<Insumo> findInsumosEnStockCritico();

       @Query(value = "SELECT tipo, COUNT(*) as total, SUM(stock) as stock_total " +
                     "FROM insumos " +
                     "GROUP BY tipo " +
                     "ORDER BY stock_total ASC", nativeQuery = true)
       List<Object[]> obtenerResumenPorTipo();

       @Modifying
       @Query("UPDATE Insumo i SET i.stock = i.stock + :cantidad WHERE i.id = :id")
       int incrementarStock(@Param("id") Long id, @Param("cantidad") Integer cantidad);

       @Modifying
       @Query("UPDATE Insumo i SET i.stock = i.stock - :cantidad WHERE i.id = :id AND i.stock >= :cantidad")
       int decrementarStock(@Param("id") Long id, @Param("cantidad") Integer cantidad);

       @Modifying
       @Query("UPDATE Insumo i SET i.stock = 0 WHERE i.stock < 0")
       int corregirStockNegativo();

       @Modifying
       @Query(value = "DELETE FROM insumos WHERE stock = 0 AND id NOT IN (SELECT DISTINCT insumo_id FROM pedidos WHERE insumo_id IS NOT NULL)", nativeQuery = true)
       int eliminarInsumosSinStockNoUtilizados();
}
