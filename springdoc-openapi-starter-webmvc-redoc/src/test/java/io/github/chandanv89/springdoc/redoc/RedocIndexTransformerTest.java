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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RedocIndexTransformerTest {

  @Test
  void render_replacesTitle() {
    var props = new RedocConfigProperties();
    props.setTitle("My Custom API Docs");
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render();
    assertThat(html).contains("<title>My Custom API Docs</title>");
  }

  @Test
  void render_replacesSpecUrl() {
    var props = new RedocConfigProperties();
    props.setSpecUrl("/custom/api-docs");
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render();
    assertThat(html).contains("spec-url='/custom/api-docs'");
  }

  @Test
  void render_usesBundledJsByDefault() {
    var props = new RedocConfigProperties();
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render();
    assertThat(html).contains("src=\"/webjars/redoc/redoc.standalone.js\"");
  }

  @Test
  void render_usesCdnUrlWhenEnabled() {
    var props = new RedocConfigProperties();
    props.setUseCdn(true);
    props.setCdnUrl("https://cdn.example.com/redoc.js");
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render();
    assertThat(html).contains("src=\"https://cdn.example.com/redoc.js\"");
    assertThat(html).doesNotContain("src=\"/webjars/redoc/");
  }

  @Test
  void render_withContextPath_prefixesBundledAssetUrls() {
    var props = new RedocConfigProperties();
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render("/example-app");
    assertThat(html).contains("src=\"/example-app/webjars/redoc/redoc.standalone.js\"");
    assertThat(html).contains("href=\"/example-app/webjars/redoc/favicon.ico\"");
  }

  @Test
  void render_includesRedocAttributes() {
    var props = new RedocConfigProperties();
    props.setDisableSearch(true);
    props.setHideLoading(true);
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render();
    assertThat(html).contains("disable-search=\"true\"");
    assertThat(html).contains("hide-loading=\"true\"");
  }

  @Test
  void render_includesCustomThemeAttribute() {
    var props = new RedocConfigProperties();
    props.setTheme("{\"sidebar\":{\"backgroundColor\":\"#333\"}}");
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render();
    assertThat(html).contains("theme='{\"sidebar\":{\"backgroundColor\":\"#333\"}}'");
  }

  @Test
  void render_includesDefaultFontThemeWhenNoCustomTheme() {
    var props = new RedocConfigProperties();
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render();
    assertThat(html).contains("theme='");
    assertThat(html).contains("Inter, sans-serif");
    assertThat(html).contains("JetBrains Mono");
  }

  @Test
  void render_includesDefaultFontUrl() {
    var props = new RedocConfigProperties();
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render();
    assertThat(html).contains("fonts.googleapis.com");
    assertThat(html).contains("Inter");
    assertThat(html).contains("JetBrains");
  }

  @Test
  void render_customFontUrl() {
    var props = new RedocConfigProperties();
    props.setFontUrl("https://fonts.example.com/custom.css");
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render();
    assertThat(html).contains("href=\"https://fonts.example.com/custom.css\"");
    assertThat(html).doesNotContain("fonts.googleapis.com");
  }

  @Test
  void render_emptyFontUrlDisablesFontLoading() {
    var props = new RedocConfigProperties();
    props.setFontUrl("");
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render();
    assertThat(html).contains("href=\"\"");
  }

  @Test
  void render_producesValidHtml() {
    var props = new RedocConfigProperties();
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render();
    assertThat(html).startsWith("<!DOCTYPE html>");
    assertThat(html).contains("<html>");
    assertThat(html).contains("</html>");
    assertThat(html).contains("<redoc");
    assertThat(html).contains("</body>");
  }

  @Test
  void render_defaultValues_producesExpectedOutput() {
    var props = new RedocConfigProperties();
    var transformer = new RedocIndexTransformer(props);

    var html = transformer.render();
    assertThat(html).contains("<title>API Documentation</title>");
    assertThat(html).contains("spec-url='/v3/api-docs'");
    assertThat(html).contains("src=\"/webjars/redoc/redoc.standalone.js\"");
    assertThat(html).contains("href=\"/webjars/redoc/favicon.ico\"");
    assertThat(html).contains("Inter");
  }
}
