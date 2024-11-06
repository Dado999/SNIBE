package com.example.base;

import com.example.exceptions.NotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@CrossOrigin(origins = "http://localhost:4200")
public abstract class CrudController<ID extends Serializable,DTO> {

    private final Class<DTO> respClass;
    private final CrudService<ID> crudService;

    protected CrudController(Class<DTO> respClass, CrudService<ID> crudService) {
        this.respClass = respClass;
        this.crudService = crudService;
    }

    @GetMapping
    List<DTO> findAll() { return crudService.findAll(respClass); }

    @GetMapping("/{id}")
    public DTO findById(@PathVariable ID id) throws NotFoundException {
        return crudService.findById(id,respClass);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable ID id) throws NotFoundException {
        crudService.delete(id);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DTO insert(@RequestBody DTO object) throws NotFoundException{
        return crudService.insert(object,respClass);
    }
    @PutMapping
    public DTO update(@PathVariable ID id, @RequestBody DTO object) throws NotFoundException{
        return crudService.update(id,object,respClass);
    }
}
