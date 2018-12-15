package org.avenue1.attribute.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.avenue1.attribute.domain.EntityType;
import org.avenue1.attribute.service.EntityTypeService;
import org.avenue1.attribute.web.rest.errors.BadRequestAlertException;
import org.avenue1.attribute.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EntityType.
 */
@RestController
@RequestMapping("/api")
public class EntityTypeResource {

    private final Logger log = LoggerFactory.getLogger(EntityTypeResource.class);

    private static final String ENTITY_NAME = "attributeSvcEntityType";

    private final EntityTypeService entityTypeService;

    public EntityTypeResource(EntityTypeService entityTypeService) {
        this.entityTypeService = entityTypeService;
    }

    /**
     * POST  /entity-types : Create a new entityType.
     *
     * @param entityType the entityType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entityType, or with status 400 (Bad Request) if the entityType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entity-types")
    @Timed
    public ResponseEntity<EntityType> createEntityType(@Valid @RequestBody EntityType entityType) throws URISyntaxException {
        log.debug("REST request to save EntityType : {}", entityType);
        if (entityType.getId() != null) {
            throw new BadRequestAlertException("A new entityType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EntityType result = entityTypeService.save(entityType);
        return ResponseEntity.created(new URI("/api/entity-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entity-types : Updates an existing entityType.
     *
     * @param entityType the entityType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entityType,
     * or with status 400 (Bad Request) if the entityType is not valid,
     * or with status 500 (Internal Server Error) if the entityType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entity-types")
    @Timed
    public ResponseEntity<EntityType> updateEntityType(@Valid @RequestBody EntityType entityType) throws URISyntaxException {
        log.debug("REST request to update EntityType : {}", entityType);
        if (entityType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EntityType result = entityTypeService.save(entityType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entity-types : get all the entityTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of entityTypes in body
     */
    @GetMapping("/entity-types")
    @Timed
    public List<EntityType> getAllEntityTypes() {
        log.debug("REST request to get all EntityTypes");
        return entityTypeService.findAll();
    }

    /**
     * GET  /entity-types/:id : get the "id" entityType.
     *
     * @param id the id of the entityType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entityType, or with status 404 (Not Found)
     */
    @GetMapping("/entity-types/{id}")
    @Timed
    public ResponseEntity<EntityType> getEntityType(@PathVariable String id) {
        log.debug("REST request to get EntityType : {}", id);
        Optional<EntityType> entityType = entityTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(entityType);
    }

    /**
     * DELETE  /entity-types/:id : delete the "id" entityType.
     *
     * @param id the id of the entityType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entity-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntityType(@PathVariable String id) {
        log.debug("REST request to delete EntityType : {}", id);
        entityTypeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id)).build();
    }
}
