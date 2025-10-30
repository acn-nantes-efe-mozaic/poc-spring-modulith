package com.accenture.modules;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.modulith.core.ApplicationModules;

@SpringBootTest
class SpringModulithApplicationTests {


    @Test
    void afficheLesModulesDetectes() {
        var modules = ApplicationModules.of(SpringModulithApplication.class);
        modules.forEach(m -> System.out.println("📦 " + m.getName()));    }

    @Test
    void checkCandidatureExports() {
        var modules = org.springframework.modulith.core.ApplicationModules.of(com.accenture.modules.SpringModulithApplication.class);
        var candidature = modules.getModuleByName("candidature").orElseThrow();
        System.out.println("📦 Exports : " + candidature.getNamedInterfaces());
    }
}
