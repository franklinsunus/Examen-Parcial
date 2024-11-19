package ExamenParcial.ExamenParcial.controllers;

import ExamenParcial.ExamenParcial.models.Alumno;
import ExamenParcial.ExamenParcial.repository.Repository;
import ExamenParcial.ExamenParcial.utils.Endpoints;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(Endpoints.ALUMNOS_BASE)
public class Controller {

    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private Repository repository;

    @GetMapping(Endpoints.ALUMNOS_ESTADO)
    public String index() {
        logger.info("Se verificó el estado de conexión");
        return "CONECTADO";
    }

    @GetMapping()
    public List<Alumno> getAlumnos() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            logger.error("Error al obtener la lista de alumnos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener la lista de alumnos", e);
        }
    }

    @GetMapping(Endpoints.ALUMNOS_BUSCAR_ID)
    public Alumno getAlumnoById(@PathVariable Long id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));
        } catch (RuntimeException e) {
            logger.error("Error al obtener el alumno con ID: {} - {}", id, e.getMessage());
            throw new RuntimeException("Error al obtener el alumno con ID: " + id, e);
        }
    }

    @PostMapping(Endpoints.ALUMNOS_CREAR)
    public String post(@RequestBody Alumno alumno) {
        try {
            repository.save(alumno);
            logger.info("Alumno creado correctamente con nombre: {}", alumno.getNombre());
            return "Alumno creado correctamente";
        } catch (IllegalArgumentException e) {
            logger.error("Error al crear el alumno: {}", e.getMessage());
            throw new RuntimeException("Error al crear el alumno: datos inválidos", e);
        } catch (Exception e) {
            logger.error("Error desconocido al crear el alumno: {}", e.getMessage());
            throw new RuntimeException("Error desconocido al crear el alumno", e);
        }
    }

    @PutMapping(Endpoints.ALUMNOS_EDITAR)
    public String update(@PathVariable Long id, @RequestBody Alumno alumno) {
        try {
            Alumno updateAlumno = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));
            updateAlumno.setNombre(alumno.getNombre());
            updateAlumno.setNota(alumno.getNota());
            repository.save(updateAlumno);
            logger.info("Alumno con ID: {} editado correctamente", id);
            return "Alumno editado correctamente";
        } catch (RuntimeException e) {
            logger.error("Error al editar el alumno con ID: {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al editar el alumno con ID: " + id, e);
        } catch (Exception e) {
            logger.error("Error desconocido al editar el alumno: {}", e.getMessage());
            throw new RuntimeException("Error desconocido al editar el alumno", e);
        }
    }

    @DeleteMapping(Endpoints.ALUMNOS_ELIMINAR)
    public String delete(@PathVariable Long id) {
        try {
            Alumno deleteAlumno = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + id));
            repository.delete(deleteAlumno);
            logger.info("Alumno con ID: {} eliminado correctamente", id);
            return "Alumno eliminado correctamente";
        } catch (RuntimeException e) {
            logger.error("Error al eliminar el alumno con ID: {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al eliminar el alumno con ID: " + id, e);
        } catch (Exception e) {
            logger.error("Error desconocido al eliminar el alumno: {}", e.getMessage());
            throw new RuntimeException("Error desconocido al eliminar el alumno", e);
        }
    }
}
