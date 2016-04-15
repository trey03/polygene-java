<!--
  ~  Licensed to the Apache Software Foundation (ASF) under one
  ~  or more contributor license agreements.  See the NOTICE file
  ~  distributed with this work for additional information
  ~  regarding copyright ownership.  The ASF licenses this file
  ~  to you under the Apache License, Version 2.0 (the
  ~  "License"); you may not use this file except in compliance
  ~  with the License.  You may obtain a copy of the License at
  ~
  ~       http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~  Unless required by applicable law or agreed to in writing, software
  ~  distributed under the License is distributed on an "AS IS" BASIS,
  ~  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~  See the License for the specific language governing permissions and
  ~  limitations under the License.
  ~
  ~
  -->
<?xml version="1.0"?>
<!--
  Used by AsciiDoc a2x(1) for w3m(1) based text generation.

  NOTE: The URL reference to the current DocBook XSL stylesheets is
  rewritten to point to the copy on the local disk drive by the XML catalog
  rewrite directives so it doesn't need to go out to the Internet for the
  stylesheets. This means you don't need to edit the <xsl:import> elements on
  a machine by machine basis.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
  <xsl:import
    href="http://docbook.sourceforge.net/release/xsl/current/xhtml/docbook.xsl"/>

  <!-- parameters for optimal text output -->
  <xsl:param name="callout.graphics" select="0"/>
  <xsl:param name="callout.unicode" select="0"/>
  <xsl:param name="section.autolabel" select="1"/>
  <xsl:param name="section.label.includes.component.label" select="1"/>
  <xsl:param name="generate.toc">
    appendix title
    article/appendix nop
    article toc,title
    book toc,title,figure,table,example,equation
    chapter title
    part toc,title
    preface toc,title
    qandadiv toc
    qandaset nop
    reference toc,title
    section toc
    set toc,title
  </xsl:param>

  <xsl:param name="qanda.defaultlabel">qanda</xsl:param>

  <xsl:template match="book/bookinfo/title | article/articleinfo/title" mode="titlepage.mode">
    <hr/>
    <xsl:apply-imports/>
    <hr/>
  </xsl:template>

  <xsl:template match="book/*/title | article/*/title" mode="titlepage.mode">
    <br/>
    <hr/>
    <xsl:apply-imports/>
    <hr/>
  </xsl:template>

  <xsl:template match="book/chapter/*/title | article/section/*/title" mode="titlepage.mode">
    <br/>
    <xsl:apply-imports/>
    <hr width="100" align="left"/>
  </xsl:template>

  <xsl:template match="ulink">
    <xsl:apply-templates/>
    <xsl:text> &lt;</xsl:text>
    <xsl:value-of select="@url"></xsl:value-of>
    <xsl:text>&gt;</xsl:text>
  </xsl:template>

  <!-- Remove revision history -->
  <xsl:template match="revhistory" mode="titlepage.mode"/>

  <!-- Remove license, only use title
    (the generated html file will be removed by the build) -->
  <xsl:param name="generate.legalnotice.link" select="1"></xsl:param>
  <xsl:param name="legalnotice.filename">license.html</xsl:param>

</xsl:stylesheet>

