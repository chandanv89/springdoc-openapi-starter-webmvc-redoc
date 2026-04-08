/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.chandanv89.springdoc.redoc;

import org.junit.jupiter.api.Test;

import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class RedocAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(RedocAutoConfiguration.class));

    @Test
    void autoConfiguration_registersAllBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(RedocConfigProperties.class);
            assertThat(context).hasSingleBean(RedocIndexTransformer.class);
            assertThat(context).hasSingleBean(RedocWelcomeController.class);
            assertThat(context).hasSingleBean(RedocWebMvcConfigurer.class);
        });
    }

    @Test
    void autoConfiguration_disabled_whenPropertySetToFalse() {
        contextRunner
                .withPropertyValues("springdoc.redoc.enabled=false")
                .run(context -> {
                    assertThat(context).doesNotHaveBean(RedocIndexTransformer.class);
                    assertThat(context).doesNotHaveBean(RedocWelcomeController.class);
                    assertThat(context).doesNotHaveBean(RedocWebMvcConfigurer.class);
                });
    }

    @Test
    void autoConfiguration_bindsCustomProperties() {
        contextRunner
                .withPropertyValues(
                        "springdoc.redoc.path=/api-docs",
                        "springdoc.redoc.spec-url=/custom/api-docs",
                        "springdoc.redoc.title=My API",
                        "springdoc.redoc.use-cdn=true",
                        "springdoc.redoc.disable-search=true",
                        "springdoc.redoc.hide-download-buttons=true"
                )
                .run(context -> {
                    var props = context.getBean(RedocConfigProperties.class);
                    assertThat(props.getPath()).isEqualTo("/api-docs");
                    assertThat(props.getSpecUrl()).isEqualTo("/custom/api-docs");
                    assertThat(props.getTitle()).isEqualTo("My API");
                    assertThat(props.isUseCdn()).isTrue();
                    assertThat(props.getDisableSearch()).isTrue();
                    assertThat(props.getHideDownloadButtons()).isTrue();
                });
    }

    @Test
    void autoConfiguration_respectsConditionalOnMissingBean() {
        contextRunner
                .withBean(RedocIndexTransformer.class,
                        () -> new RedocIndexTransformer(new RedocConfigProperties()))
                .run(context ->
                        assertThat(context).hasSingleBean(RedocIndexTransformer.class));
    }
}
