/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

/**
 *
 * @author DOC
 */
import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {
    void guardar(T entidad);
    void actualizar(T entidad);
    void eliminar(T entidad);
    Optional<T> buscarPorId(Long id);
    List<T> listarTodos();
}
