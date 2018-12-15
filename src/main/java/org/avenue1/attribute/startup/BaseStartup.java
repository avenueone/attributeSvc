package org.avenue1.attribute.startup;

import org.avenue1.attribute.domain.EntityType;
import org.avenue1.attribute.enums.EntityTypeEnum;
import org.avenue1.attribute.repository.EntityTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
public abstract class BaseStartup {

    private static final Logger log = LoggerFactory.getLogger(BaseStartup.class);


    @Autowired
    protected EntityTypeRepository entityTypeRepository;

    public BaseStartup() {


    }

    @PostConstruct
    public void required() {
        log.debug("Creating required entity types...");
        ResourceBundle bundle = ResourceBundle.getBundle("i18n.entities", Locale.getDefault());

        for (EntityTypeEnum type: EntityTypeEnum.values()) {

            log.debug("Checking type {} - {}", type.name(), bundle.getString(type.name()));

           entityTypeRepository.findAll();
            EntityType entityType = new EntityType().type(type).description(bundle.getString(type.name()));
            entityTypeRepository.save(entityType);
        }
    }
}
