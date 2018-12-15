package org.avenue1.attribute.cucumber.stepdefs;

import org.avenue1.attribute.AttributeSvcApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = AttributeSvcApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
