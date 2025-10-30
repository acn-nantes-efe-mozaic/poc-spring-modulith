package com.accenture.modules;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

class ModularityDocumentationTest {
    @Test
    void writeDocumentation() {
        ApplicationModules modules = ApplicationModules.of("com.accenture.modules");
        new Documenter(modules).writeDocumentation();
    }
}
