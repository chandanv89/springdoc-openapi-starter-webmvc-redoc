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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transforms the Redoc HTML template by replacing placeholders with configured values.
 *
 * <p>The template uses the following placeholders:
 * <ul>
 *   <li>{@code {{title}}} — the page title</li>
 *   <li>{@code {{specUrl}}} — the OpenAPI spec URL</li>
 *   <li>{@code {{options}}} — Redoc HTML attributes (disable-search, theme, etc.)</li>
 *   <li>{@code {{redocJsUrl}}} — the URL to the Redoc standalone JS file</li>
 *   <li>{@code {{fontUrl}}} — the Google Fonts (or custom) URL for web fonts</li>
 * </ul>
 *
 * @author Chandan Veerabhadrappa (@chandanv89)
 */
public class RedocIndexTransformer {

    private static final Logger log = LoggerFactory.getLogger(RedocIndexTransformer.class);

    private static final String TEMPLATE_PATH = "META-INF/resources/redoc/redoc.html";
    private static final String BUNDLED_JS_PATH = "/webjars/redoc/redoc.standalone.js";

    private final RedocConfigProperties properties;
    private final String template;

    /**
     * Creates a new transformer, loading the HTML template from the classpath.
     *
     * @param properties the Redoc configuration properties
     */
    public RedocIndexTransformer(final RedocConfigProperties properties) {
        this.properties = properties;
        this.template = loadTemplate();
    }

    /**
     * Renders the Redoc HTML page by substituting all placeholders in the template.
     *
     * @return the fully rendered HTML string
     */
    public String render() {
        final var jsUrl = properties.isUseCdn() ? properties.getCdnUrl() : BUNDLED_JS_PATH;
        final var optionsStr = properties.buildRedocAttributeString();

        final var fontUrl = properties.getFontUrl() != null ? properties.getFontUrl() : "";

        return template
                .replace("{{title}}", escapeHtml(properties.getTitle()))
                .replace("{{specUrl}}", escapeHtml(properties.getSpecUrl()))
                .replace("{{options}}", optionsStr)
                .replace("{{redocJsUrl}}", escapeHtml(jsUrl))
                .replace("{{fontUrl}}", escapeHtml(fontUrl));
    }

    private String loadTemplate() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(TEMPLATE_PATH)) {
            if (is == null) {
                log.error("Redoc HTML template not found at classpath:{}", TEMPLATE_PATH);
                throw new IllegalStateException("Redoc HTML template not found at classpath:" + TEMPLATE_PATH);
            }
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load Redoc HTML template", e);
        }
    }

    private static String escapeHtml(final String input) {
        if (input == null) {
            return "";
        }
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
