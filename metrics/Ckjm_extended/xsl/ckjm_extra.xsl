<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
 (C) Copyright 2005 Julien Rentrop, Diomidis Spinellis

 Permission to use, copy, and distribute this software and its
 documentation for any purpose and without fee is hereby granted,
 provided that the above copyright notice appear in all copies and that
 both that copyright notice and this permission notice appear in
 supporting documentation.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR IMPLIED
 WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF
 MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

 $Id: ckjm_extra.xsl 1.5 2005/11/05 08:32:32 dds Exp $

-->


<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<!-- Number of items to display in the top lists -->
<xsl:variable name="top" select="25"/>

<xsl:template match="/">
<html>
<head>
  <title>CKJM Chidamber and Kemerer Java Metrics</title>
  <meta name="Generator" content="$Id: ckjm_extra.xsl 1.5 2005/11/05 08:32:32 dds Exp $" />
  <style type="text/css">
      body {
        font:normal 68% verdana,arial,helvetica;
        color:#000000;
      }
      table {
        width: 100%;
      }

      table tr td, tr th {
        font:normal 68% verdana,arial,helvetica;
      }
      table.details tr th{
        font-weight: bold;
        text-align:left;
        background:#a6caf0;
      }
      table.details tr td{
        background:#eeeee0;
      }

      p {
        line-height:1.5em;
        margin-top:0.5em; margin-bottom:1.0em;
        margin-left:2em;
        margin-right:2em;
      }
      h1 {
        margin: 0px 0px 5px; font: 165% verdana,arial,helvetica
      }
      h2 {
        margin-top: 1em; margin-bottom: 0.5em; font: bold 125% verdana,arial,helvetica
      }
      h3 {
        margin-bottom: 0.5em; font: bold 115% verdana,arial,helvetica
      }
      h4 {
        margin-bottom: 0.5em; font: bold 100% verdana,arial,helvetica
      }
      h5 {
        margin-bottom: 0.5em; font: bold 100% verdana,arial,helvetica
      }
      h6 {
        margin-bottom: 0.5em; font: bold 100% verdana,arial,helvetica
      }
      .Error {
        font-weight:bold; color:red;
      }
      .Failure {
        font-weight:bold; color:purple;
      }
      .Properties {
        text-align:right;
      }
      </style>
</head>
<body>
<h1>CKJM Chidamber and Kemerer Java Metrics</h1>
<p align="right">Designed for use with <a href="http://gromit.iiar.pwr.wroc.pl/p_inf/ckjm/">CKJM_ext</a> and <a href="http://ant.apache.org">Ant</a>.</p>

<hr size="2"/>
<!-- Iterate over the metric elements, don't iterate over the name element -->
<xsl:for-each select="ckjm/class[1]/*[name() != 'name']">
<!-- This variable is needed for storing the sort attribute because at the point of sorting the name function will return 'class' instead of a metric attribute -->
<xsl:variable name="sort" select="name()" />
<table>
<tr>
  <td>
    <a>
     <xsl:attribute name="name">NV<xsl:value-of select="name()" /></xsl:attribute> 
     <h2>Top <xsl:value-of select="$top"/>: <xsl:value-of select="name()" /></h2>
     </a>
  </td>
  <td align="right">
    <xsl:for-each select="/ckjm/class[1]/*[name() != 'name']">
      [<a><xsl:attribute name="href">#NV<xsl:value-of select="name()" /></xsl:attribute> <xsl:value-of select="name()"/></a>]
    </xsl:for-each>
    [<a href="#NVexplanations">explanations</a>]
  </td>
</tr>
</table>

<table class="details">
<tr>
<xsl:for-each select="/ckjm/class[1]/*">
  <th>
      <xsl:if test="name() = 'cc'">
          avg
      </xsl:if>    
      <xsl:value-of select="name()"/>
  </th>
</xsl:for-each>
</tr>
<xsl:for-each select="/ckjm/class/*[name() = $sort]">
    <xsl:sort select="sum(./method) div count(./method)" data-type="number" order="descending"/>
    <xsl:sort select="." data-type="number" order="descending"/>
    <xsl:if test="position() &lt;= $top">
    <tr>
      <xsl:for-each select="../*">
      <td>
          <xsl:if test="name() = 'cc'">
            <xsl:value-of select="sum(method) div count(method)"/>
          </xsl:if>
          <xsl:value-of select="text()"/>
      </td>
      </xsl:for-each>
    </tr>
    </xsl:if>
</xsl:for-each>
</table>
</xsl:for-each>

    <a name="NVexplanations">
      <h2>Explanations</h2>
    </a>
<dl>
<dt>WMC - Weighted methods per class</dt>
<dd>A class's <em>weighted methods per class</em>
WMC
metric is simply the sum of the complexities of its methods.
As a measure of complexity we can use the cyclomatic complexity,
or we can abritrarily assign a complexity value of 1 to each method.
The <em>ckjm</em> program assigns a complexity value of 1
to each method,
and therefore the value of the WMC is equal to the number of methods
in the class.
</dd>
<dt>DIT - Depth of Inheritance Tree</dt>
<dd>The <em>depth of inheritance tree</em> (DIT)
metric provides
for each class a measure of the inheritance levels from the object
hierarchy top.
In Java where all classes inherit Object the minimum value
of DIT is 1.
</dd>
<dt>NOC - Number of Children</dt>
<dd>A class's <em>number of children</em> (NOC)
metric simply
measures the number of immediate descendants of the class.
</dd>
<dt>CBO - Coupling between object classes</dt>
<dd>The <em>coupling between object classes</em>
(CBO)
metric represents the number of classes coupled to a given
class (efferent couplings and afferent couplings).
This coupling can occur through method calls,
field accesses, inheritance, arguments, return types,
and exceptions.
</dd>
<dt>RFC - Response for a Class</dt>
<dd>The metric called the <em>response for a class</em>
(RFC)
measures the number of different
methods that can be executed when an object of that
class receives a message (when a method is invoked
for that object).
Ideally, we would want to find for each method
of the class, the methods that class will call,
and repeat this for each called method,
calculating what is called the <em>transitive closure</em>
of the method's call graph.
This process can however be both expensive and quite
inaccurate.
In <em>ckjm</em>, we calculate a rough
approximation to the response set
by simply inspecting method calls within the class's
method bodies. The value of RFC is the sum of number of methods called
within the class's method bodies and the number of class's methods.
This simplification was also used in the 1994 Chidamber and Kemerer
description of the metrics.
</dd>
<dt>LCOM - Lack of cohesion in methods</dt>
<dd>A class's <em>lack of cohesion in methods</em>
(LCOM)
metric counts the sets of methods in a class that are not related
through the sharing of some of the class's fields.
The original definition of this metric
(which is the one used in <em>ckjm</em>)
considers all pairs of a class's methods.
In some of these pairs both methods access at
least one common field of the class, while in
other pairs the two methods to not share any
common field accesses.
The lack of cohesion in methods is then calculated
by subtracting from the number of method pairs
that don't share a field access the number of
method pairs that do.
Note that subsequent definitions
of this metric used as a measurement basis the number
of disjoint graph components of the class's methods.
Others modified the definition of connectedness to
include calls between the methods of the class.
The program <em>ckjm</em> follows the original (1994)
definition
by Chidamber and Kemerer.
</dd>
<dt>Ca - Afferent couplings</dt>
<dd>A class's afferent couplings is a measure of how many other
classes
use the specific class.
Coupling has the same definition in context of Ca as that used for
calculating CBO.
</dd>
<dt>Ce - Efferent couplings</dt>
<dd>A class's efferent couplings is a measure of how many other
classes is used by the specific class.
Coupling has the same definition in context of Ce as that used for
calculating CBO.
</dd>
<dt>NPM - Number of Public Methods</dt>
<dd>The NPM metric simply counts all the methods in a class
that
are declared as public.
It can be used to measure the size of an API provided by a package.</dd>
<dt>LCOM3 -Lack of cohesion in methods.</dt>
<dd>LCOM3 varies between 0 and 2.</dd>
<dt>LOC - Lines of Code.</dt>
<dd>The lines are counted from java binary code and it is the sum of
number of fields, number of methods and number of instructions in every
method of given class.</dd>
<dt>DAM: Data Access Metric</dt>
<dd>This metric is the ratio of the number of private (protected)
attributes to the total number of attributes declared in the class. A
high value for DAM is desired. (Range 0 to 1)</dd>
<dt>MOA: Measure of Aggregation</dt>
<dd>This metric measures the extent of the part-whole relationship,
realized by using attributes. The metric is a count of the number of
data declarations (class fields) whose types are user defined classes.</dd>
<dt>MFA: Measure of Functional Abstraction</dt>
<dd>This metric is the ratio of the number of methods inherited by a
class to the total number of methods accessible by member methods of
the class. The constructors and the java.lang.Object (as parent) are
ignored. (Range 0 to 1) </dd>
<dt>CAM: Cohesion Among Methods of Class</dt>
<dd>This metric computes the relatedness among methods of a class
based upon the parameter list of the methods. The metric is computed
using the summation of number of different types of method parameters
in every method divided by a multiplication of number of different
method parameter types in whole class and number of methods. A metric
value close to 1.0 is preferred. (Range 0 to 1).</dd>
<dt>IC: Inheritance Coupling</dt>
<dd>This metric provides the number of parent classes to which a
given class is coupled.
A class is coupled to its parent class if one of its inherited methods
functionally dependent
on the new or redefined methods in the class. A class is coupled to its
parent class if one
of the following conditions is satisfied:
<li>One of its inherited methods uses a variable (or data member)
that is defined in a new/redefined method.</li>
<li>One of its inherited methods calls a redefined method.</li>
<li>One of its inherited methods is called by a redefined method
and uses a parameter that is defined in the redefined method.</li>
</dd>
<dt>CBM: Coupling Between Methods</dt>
<dd>The metric measure the total number of new/redefined methods to
which all the inherited
methods are coupled. There is a coupling when one of the given in the
IC metric definition
conditions holds.</dd>
<dt>AMC: Average Method Complexity</dt>
<dd>This metric measures the average method size for each class. Size
of a method is equal
to the number of java binary codes in the method.</dd>
<dt>CC - The McCabe's cyclomatic complexity</dt>
<dd>It is equal to number of
different paths in a method (function) plus one.</dd>
</dl>

</body>
</html>
</xsl:template>

</xsl:stylesheet>
