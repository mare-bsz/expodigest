<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
          		xmlns:java="http://xml.apache.org/xalan/java"
    			exclude-result-prefixes="java">

	<xsl:output media-type="text/html" method="html" omit-xml-declaration="yes"/>
	
	<xsl:template match="/">
		<xsl:apply-templates select="./record"/>
	</xsl:template>
	
	<xsl:template match="record">
		<div class="record">
			<div class="header">
				<p><span class="inventarnummer"><xsl:value-of select="./personen/person[1]/anzeigename" /></span><xsl:text>: </xsl:text><xsl:value-of select="./objekttitel" /></p>
				<p><xsl:text> [</xsl:text><span class="inventarnummer"><xsl:value-of select="./inventarnummer" /></span><xsl:text>] </xsl:text></p>
			</div>
			<h3 class="objektbezeichnung"><xsl:value-of select="./objektbezeichnungen/objektbezeichnung/term" /></h3>
			<div class="footer">
				<xsl:if test="./textdeutsch">
					<p><xsl:text>Abstrakt: </xsl:text><span class="kurzbeschreibung"><xsl:value-of select="java:de.jotwerk.Util.rtfToHtml(./textdeutsch)"/></span></p>
				</xsl:if>
				<xsl:text>Entstehungszeit: </xsl:text><span class="datierung"><xsl:value-of select="./entstehungszeit"/></span>
				<xsl:apply-templates select="./schlagworte" />
				<xsl:apply-templates select="./materialien" />
				<xsl:apply-templates select="./techniken" />
				<xsl:apply-templates select="./masse" />
			</div>
		</div>	
	</xsl:template>	
	
	<xsl:template match="schlagworte">
		<p><span class="label schlagworte">Schlagworte: </span>
		<span class="value schlagworte">
			<xsl:for-each select="./schlagwort">
				<xsl:if test="position() != 1"><xsl:text>; </xsl:text></xsl:if>
				<xsl:value-of select="./term" /> 
			</xsl:for-each>
			</span>			
		</p>	
	</xsl:template>
	
	<xsl:template match="materialien">
		<p><span class="label materialien">Materialien: </span>
		<span class="value materialien">
			<xsl:for-each select="./material">
				<xsl:if test="position() != 1"><xsl:text>; </xsl:text></xsl:if>
				<xsl:value-of select="./term" /> 
			</xsl:for-each>
			</span>			
		</p>	
	</xsl:template>
	
	<xsl:template match="techniken">
		<p><span class="label materialien">Techniken: </span>
		<span class="value materialien">
			<xsl:for-each select="./technik">
				<xsl:if test="position() != 1"><xsl:text>; </xsl:text></xsl:if>
				<xsl:value-of select="./term" /> 
			</xsl:for-each>
			</span>			
		</p>	
	</xsl:template>
	
	<xsl:template match="orte">
		<p><span class="label materialien">Orte: </span>
		<span class="value materialien">
			<xsl:for-each select="./ort">
				<xsl:if test="position() != 1"><xsl:text>; </xsl:text></xsl:if>
				<xsl:value-of select="./term" /> 
			</xsl:for-each>
			</span>			
		</p>	
	</xsl:template>
	
	<xsl:template match="masse">
		<p><span class="label masse">Maße: </span>
		<span class="value mass">
			<xsl:for-each select="./mass">
				<xsl:if test="position() != 1"><xsl:text>; </xsl:text></xsl:if>				
				<xsl:value-of select="./typ"/><xsl:text>: </xsl:text>
				<xsl:value-of select="./wert"/><xsl:text> </xsl:text>
				<xsl:value-of select="./einheit"/>
				<xsl:if test="./teil != 'Gesamt'"><xsl:text> (</xsl:text><xsl:value-of select="./teil"/><xsl:text>)</xsl:text></xsl:if>
			</xsl:for-each>
		</span>
		</p>
		
	</xsl:template>	
	
</xsl:stylesheet>

