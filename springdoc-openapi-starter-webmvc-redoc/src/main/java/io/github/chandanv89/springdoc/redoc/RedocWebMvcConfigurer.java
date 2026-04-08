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

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configures Spring MVC resource handlers to serve the bundled Redoc standalone JS file.
 *
 * <p>Maps requests for {@code /webjars/redoc/**} to the classpath location
 * {@code META-INF/resources/webjars/redoc/} where the bundled JS is stored.</p>
 *
 * @author Chandan Veerabhadrappa (@chandanv89)
 */
public class RedocWebMvcConfigurer implements WebMvcConfigurer {

    /**
     * {@inheritDoc}
     *
     * <p>Adds a resource handler for the bundled Redoc JS webjar.</p>
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/redoc/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/redoc/");
    }
}
