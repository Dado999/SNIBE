package com.example.base;

import com.example.exceptions.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class CrudJpaService <E extends BaseEntity<ID>, ID extends Serializable> implements CrudService<ID>{

    private final JpaRepository<E,ID> repository;
    private final Class<E> entityClass;
    private final ModelMapper modelMapper;
    @PersistenceContext
    private EntityManager entityManager;

    public CrudJpaService(JpaRepository<E,ID> repository,ModelMapper modelMapper, Class<E> entityClass)
    {
        this.repository = repository;
        this.entityClass = entityClass;
        this.modelMapper = modelMapper;
    }

    @Override
    public <T> List<T> findAll(Class<T> resultDtoClass) {
        return repository.findAll().stream().map(e -> modelMapper.map(e,resultDtoClass)).collect(Collectors.toList());
    }
    @Override
    public <T> T findById(ID id, Class<T> resultDtoClass) throws NotFoundException {
        return modelMapper.map(findEntityById(id),resultDtoClass);
    }

    @Override
    public <T, U> T insert(U object, Class<T> resultDtoClass) {
        E entity = modelMapper.map(object,entityClass);
        entity.setId(null);
        entity = repository.saveAndFlush(entity);
        entityManager.refresh(entity);
        return modelMapper.map(entity,resultDtoClass);
    }

    @Override
    public <T, U> T update(ID id, U object, Class<T> resultDtoClass) throws NotFoundException {
        if(!repository.existsById(id))
            throw new NotFoundException();
        E entity = modelMapper.map(object,entityClass);
        entity.setId(id);
        entity = repository.saveAndFlush(entity);
        entityManager.refresh(entity);
        return  modelMapper.map(entity,resultDtoClass);
    }

    public E findEntityById(ID id) throws NotFoundException { return repository.findById(id).orElseThrow(NotFoundException::new);}
    @Override
    public void delete(ID id) throws NotFoundException {
        if(!repository.existsById(id))
            throw new NotFoundException();
        repository.deleteById(id);
    }
}
