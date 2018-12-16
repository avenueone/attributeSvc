package org.avenue1.attribute.startup;

import org.avenue1.attribute.domain.Attribute;
import org.avenue1.attribute.domain.AttributeValue;
import org.avenue1.attribute.domain.EntityType;
import org.avenue1.attribute.domain.enumeration.DataTypeEnum;
import org.avenue1.attribute.enums.EntityTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Profile("dev")
@Controller
@Configuration
public class DevStartup extends BaseStartup {

    private static final Logger log = LoggerFactory.getLogger(DevStartup.class);

    @PostConstruct
    private void addSampleData() {
        log.debug("Checking for base dev data...");
        for (EntityTypeEnum type: EntityTypeEnum.values()) {
            List<EntityTypeEnum> types = new ArrayList<>();
            types.add(type);
            List<Attribute> allByType = attributeRepository.findAllByEntityTypes(types);

            if ( allByType.isEmpty() ) {
                Optional<EntityType> byType = entityTypeRepository.findByType(type);
                if ( byType.isPresent()) {

                    Attribute sample = new Attribute().active(true).hasValidValues(true).description("auto created")
                        .name("test " + type.name().toLowerCase()).dataType(DataTypeEnum.STRING)
                        .mandatory(true).addEntityType(byType.get());
                    sample = attributeRepository.save(sample);

                    for ( int i = 0; i < 10; i++ ){
                        String value = "attr val " + i;
                        Optional<AttributeValue> attributeValue = attributeValueRepository.findByAttributeAndValue(sample,value);
                        if ( attributeValue.isPresent()) {
                            sample.addAttributeValue(attributeValue.get());
                        } else {
                            AttributeValue attributeValue1 = new AttributeValue().value(value);
                            AttributeValue savedAttVal = attributeValueRepository.save(attributeValue1);
                            sample.addAttributeValue(savedAttVal);
                            savedAttVal.setAttribute(sample);
                            attributeValueRepository.save(savedAttVal);
                        }
                    }

                    attributeRepository.save(sample);
                } else {
                    log.error("Entity type dbref is not found {}", type);
                }

            }else {
                log.debug("{} attributes for {}", allByType.size(), type.name());
            }
        }
    }

}
