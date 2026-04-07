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

package com.github.chandanv89.springdoc.redoc.webflux;

import com.github.chandanv89.springdoc.redoc.RedocConfigProperties;
import com.github.chandanv89.springdoc.redoc.RedocIndexTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Spring Boot auto-configuration for the Redoc API documentation UI in WebFlux applications.
 *
 * <p>This configuration is activated when:
 * <ul>
 *   <li>The application is a reactive web application</li>
 *   <li>The property {@code springdoc.redoc.enabled} is {@code true} (default)</li>
 * </ul>
 *
 * <p>It registers the following beans:
 * <ul>
 *   <li>{@link RedocIndexTransformer} — renders the HTML template with configuration</li>
 *   <li>{@link RouterFunction} — reactive route serving the Redoc HTML page</li>
 *   <li>{@link RedocWebFluxResourceConfigurer} — serves the bundled Redoc JS as a static resource</li>
 * </ul>
 *
 * @author Chandan Veerabhadrappa (@chandanv89)
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = Type.REACTIVE)
@ConditionalOnProperty(name = "springdoc.redoc.enabled", matchIfMissing = true)
@EnableConfigurationProperties(RedocConfigProperties.class)
public class RedocWebFluxAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RedocWebFluxAutoConfiguration.class);

    /**
     * Creates the {@link RedocIndexTransformer} bean that renders the HTML template.
     *
     * @param properties the Redoc configuration properties
     * @return a new {@link RedocIndexTransformer} instance
     */
    @Bean
    @ConditionalOnMissingBean
    public RedocIndexTransformer redocIndexTransformer(final RedocConfigProperties properties) {
        if (log.isInfoEnabled()) {
            log.info("Redoc UI (WebFlux) is enabled at endpoint `{}`", properties.getPath());
        }
        return new RedocIndexTransformer(properties);
    }

    /**
     * Creates the reactive {@link RouterFunction} that serves the Redoc page.
     *
     * @param indexTransformer the transformer that renders the HTML
     * @param properties the Redoc configuration properties
     * @return the router function bean
     */
    @Bean
    @ConditionalOnMissingBean(name = "redocRouterFunction")
    public RouterFunction<ServerResponse> redocRouterFunction(
            final RedocIndexTransformer indexTransformer,
            final RedocConfigProperties properties) {
        return new RedocWebFluxRouterConfig(indexTransformer, properties.getPath())
                .redocRouterFunction();
    }

    /**
     * Creates the {@link RedocWebFluxResourceConfigurer} bean that registers static resource handlers.
     *
     * @return a new {@link RedocWebFluxResourceConfigurer} instance
     */
    @Bean
    @ConditionalOnMissingBean
    public RedocWebFluxResourceConfigurer redocWebFluxResourceConfigurer() {
        return new RedocWebFluxResourceConfigurer();
    }
}
