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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RedocConfigPropertiesTest {

  @Test
  void defaultValues() {
    var props = new RedocConfigProperties();
    assertTrue(props.isEnabled());
    assertEquals("/redoc", props.getPath());
    assertEquals("/v3/api-docs", props.getSpecUrl());
    assertEquals("API Documentation", props.getTitle());
    assertFalse(props.isUseCdn());
    assertEquals("https://cdn.redoc.ly/redoc/latest/bundles/redoc.standalone.js", props.getCdnUrl());
    assertTrue(props.getFontUrl().contains("Inter"));
    assertTrue(props.getFontUrl().contains("JetBrains+Mono"));
    assertNull(props.getTheme());
  }

  @Test
  void effectiveTheme_returnsDefaultFontThemeWhenNoCustomThemeSet() {
    var props = new RedocConfigProperties();
    var effectiveTheme = props.getEffectiveTheme();
    assertTrue(effectiveTheme.contains("Inter, sans-serif"));
    assertTrue(effectiveTheme.contains("JetBrains Mono"));
  }

  @Test
  void effectiveTheme_returnsCustomThemeWhenSet() {
    var props = new RedocConfigProperties();
    var customTheme = "{\"colors\":{\"primary\":{\"main\":\"#ff0000\"}}}";
    props.setTheme(customTheme);
    assertEquals(customTheme, props.getEffectiveTheme());
  }

  @Test
  void buildRedocAttributes_alwaysIncludesTheme() {
    var props = new RedocConfigProperties();
    var attributes = props.buildRedocAttributes();
    assertEquals(1, attributes.size());
    assertTrue(attributes.containsKey("theme"));
    assertTrue(attributes.get("theme").contains("Inter"));
  }

  @Test
  void buildRedocAttributes_includesExplicitOptionsAndTheme() {
    var props = new RedocConfigProperties();
    props.setDisableSearch(true);
    props.setHideDownloadButtons(false);
    props.setExpandResponses("200,201");

    var attributes = props.buildRedocAttributes();
    assertEquals(4, attributes.size());
    assertEquals("true", attributes.get("disable-search"));
    assertEquals("false", attributes.get("hide-download-buttons"));
    assertEquals("200,201", attributes.get("expand-responses"));
    assertTrue(attributes.containsKey("theme"));
  }

  @Test
  void buildRedocAttributeString_includesDefaultThemeWhenNoOptionsSet() {
    var props = new RedocConfigProperties();
    var result = props.buildRedocAttributeString();
    assertTrue(result.contains("theme='"));
    assertTrue(result.contains("Inter"));
  }

  @Test
  void buildRedocAttributeString_formatsCorrectly() {
    var props = new RedocConfigProperties();
    props.setDisableSearch(true);
    props.setNativeScrollbars(true);

    var result = props.buildRedocAttributeString();
    assertTrue(result.contains("disable-search=\"true\""));
    assertTrue(result.contains("native-scrollbars=\"true\""));
    assertTrue(result.contains("theme='"));
  }

  @Test
  void buildRedocAttributeString_customThemeUsesSingleQuotes() {
    var props = new RedocConfigProperties();
    props.setTheme("{\"sidebar\":{\"backgroundColor\":\"#fafafa\"}}");

    var result = props.buildRedocAttributeString();
    assertTrue(result.contains("theme='{\"sidebar\""));
    assertTrue(result.contains("#fafafa"));
  }

  @Test
  void fontUrl_defaultsToInterAndJetBrainsMono() {
    var props = new RedocConfigProperties();
    assertTrue(props.getFontUrl().startsWith("https://fonts.googleapis.com/"));
    assertTrue(props.getFontUrl().contains("Inter"));
    assertTrue(props.getFontUrl().contains("JetBrains+Mono"));
  }

  @Test
  void fontUrl_canBeOverridden() {
    var props = new RedocConfigProperties();
    props.setFontUrl("https://fonts.example.com/custom-font.css");
    assertEquals("https://fonts.example.com/custom-font.css", props.getFontUrl());
  }

  @Test
  void fontUrl_canBeSetToEmptyToDisable() {
    var props = new RedocConfigProperties();
    props.setFontUrl("");
    assertEquals("", props.getFontUrl());
  }

  @ParameterizedTest
  @CsvSource({
      "/custom-docs, /custom-docs",
      "/api/redoc, /api/redoc"
  })
  void setPath_updatesValue(String input, String expected) {
    var props = new RedocConfigProperties();
    props.setPath(input);
    assertEquals(expected, props.getPath());
  }

  @Test
  void allRedocNativeOptionsMapping() {
    var props = new RedocConfigProperties();
    props.setDisableSearch(true);
    props.setHideDownloadButtons(true);
    props.setSortRequiredPropsFirst(true);
    props.setExpandResponses("all");
    props.setPathInMiddlePanel(true);
    props.setHideHostname(true);
    props.setHideLoading(true);
    props.setNativeScrollbars(true);
    props.setJsonSamplesExpandLevel("5");
    props.setTheme("{\"colors\":{\"primary\":{\"main\":\"#0066cc\"}}}");

    var attributes = props.buildRedocAttributes();
    assertEquals(10, attributes.size());
    assertEquals("true", attributes.get("disable-search"));
    assertEquals("true", attributes.get("hide-download-buttons"));
    assertEquals("true", attributes.get("sort-required-props-first"));
    assertEquals("all", attributes.get("expand-responses"));
    assertEquals("true", attributes.get("path-in-middle-panel"));
    assertEquals("true", attributes.get("hide-hostname"));
    assertEquals("true", attributes.get("hide-loading"));
    assertEquals("true", attributes.get("native-scrollbars"));
    assertEquals("5", attributes.get("json-samples-expand-level"));
    assertTrue(attributes.get("theme").contains("0066cc"));
  }
}
