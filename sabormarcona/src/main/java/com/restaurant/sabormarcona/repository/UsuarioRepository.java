package com.restaurant.sabormarcona.repository;

import com.restaurant.sabormarcona.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

       Optional<Usuario> findByUsername(String username);

       Optional<Usuario> findByEmail(String email);

       List<Usuario> findByRol(String rol);

       List<Usuario> findByActivo(boolean activo);

       List<Usuario> findByNombreContainingIgnoreCase(String nombre);

       List<Usuario> findByRolAndActivo(String rol, boolean activo);

       List<Usuario> findAllByOrderByNombreAsc();

       boolean existsByUsername(String username);

       boolean existsByEmail(String email);

       boolean existsByUsernameAndActivo(String username, boolean activo);

       long countByRol(String rol);

       long countByActivo(boolean activo);

       @Query("SELECT u FROM Usuario u WHERE u.activo = true AND u.rol = :rol")
       List<Usuario> findUsuariosActivosPorRol(@Param("rol") String rol);

       @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.tareas WHERE u.id = :id")
       Optional<Usuario> findByIdWithTareas(@Param("id") Long id);

       @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.incidencias WHERE u.id = :id")
       Optional<Usuario> findByIdWithIncidencias(@Param("id") Long id);

       @Query("SELECT DISTINCT u FROM Usuario u JOIN u.tareas t WHERE t.estado = com.restaurant.sabormarcona.model.TaskStatus.PENDIENTE")
       List<Usuario> findUsuariosConTareasPendientes();

       @Query("SELECT u FROM Usuario u WHERE " +
                     "LOWER(u.username) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
                     "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
                     "LOWER(u.email) LIKE LOWER(CONCAT('%', :busqueda, '%'))")
       List<Usuario> buscarUsuarios(@Param("busqueda") String busqueda);

       @Query("SELECT u.rol, COUNT(u) FROM Usuario u GROUP BY u.rol")
       List<Object[]> contarUsuariosPorRol();

       @Query("SELECT u FROM Usuario u WHERE SIZE(u.tareas) > :cantidad")
       List<Usuario> findUsuariosConMasDeTareas(@Param("cantidad") int cantidad);

       @Query(value = "SELECT * FROM usuarios WHERE rol = :rol AND activo = 1", nativeQuery = true)
       List<Usuario> findUsuariosPorRolNative(@Param("rol") String rol);

       @Query(value = "SELECT u.*, COUNT(t.id) as total_tareas " +
                     "FROM usuarios u " +
                     "LEFT JOIN tareas t ON u.id = t.trabajador_id " +
                     "GROUP BY u.id", nativeQuery = true)
       List<Object[]> obtenerUsuariosConConteoTareas();

       @Modifying
       @Query("UPDATE Usuario u SET u.activo = :activo WHERE u.rol = :rol")
       int cambiarEstadoUsuariosPorRol(@Param("rol") String rol, @Param("activo") boolean activo);

       @Modifying
       @Query("UPDATE Usuario u SET u.rol = :nuevoRol WHERE u.id = :id")
       int actualizarRol(@Param("id") Long id, @Param("nuevoRol") String nuevoRol);

       @Modifying
       @Query(value = "DELETE FROM usuarios WHERE activo = 0 AND id NOT IN (SELECT DISTINCT trabajador_id FROM tareas WHERE trabajador_id IS NOT NULL)", nativeQuery = true)
       int eliminarUsuariosInactivosSinTareas();
}
