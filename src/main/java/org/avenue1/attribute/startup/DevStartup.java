package org.avenue1.attribute.startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;

@Profile("dev")
@Controller
@Configuration
public class DevStartup extends BaseStartup {

    private static final Logger log = LoggerFactory.getLogger(DevStartup.class);

    

}
