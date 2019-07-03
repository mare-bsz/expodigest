<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output media-type="text/html" method="html" omit-xml-declaration="yes"/>
	
	<xsl:template match="/">
		<xsl:apply-templates select="./record"/>
	</xsl:template>
	
	<xsl:template match="record">
		<div class="record">
			<div class="header">
				<xsl:if test="./personen">
			<strong>
				<xsl:choose>
					<xsl:when test="./personen/person[1]/anzeigename"><xsl:value-of select="./personen/person[1]/anzeigename" /></xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="./personen/person[1]/vorname" /> <xsl:value-of select="./personen/person[1]/nachname" />
					</xsl:otherwise>
				</xsl:choose>
			</strong><xsl:text> : </xsl:text>
		</xsl:if>
		<xsl:value-of select="./record/objektbezeichnungen/deskriptor[1]/term" />
		<xsl:if test="./record/titelde"><xsl:text> (</xsl:text><xsl:value-of select="./record/titelde" /><xsl:text>)</xsl:text></xsl:if>
		<xsl:text> [</xsl:text><xsl:value-of select="./record/inventarnummer" /><xsl:text>]</xsl:text> 	
			</div>
			<div><xsl:value-of select="./collection/museum"/><xsl:text> : </xsl:text><xsl:value-of select="./collection/sammlung"/></div>
			<h3 class="objektbezeichnung"><xsl:value-of select="./objektbezeichnungen/deskriptor[1]/term" /></h3>
			<div class="footer">
				<xsl:if test="./kurztextde">
					<p><xsl:text>Kurztext: </xsl:text><span class="kurzbeschreibung"><xsl:value-of select="./kurztextde"/></span></p>
				</xsl:if>
				<xsl:if test="./entstehungszeit"><xsl:text>Datierung: </xsl:text><span class="datierung"><xsl:value-of select="./entstehungszeit"/></span></xsl:if>
				<xsl:apply-templates select="./personen" />
				<xsl:apply-templates select="./orte" />
				<xsl:apply-templates select="./schlagwort" />
				<xsl:apply-templates select="./material" />
				<xsl:apply-templates select="./traegermaterial" />
				<xsl:apply-templates select="./technik" />
				<xsl:apply-templates select="./masse" />
				<xsl:apply-templates select="./leihgeber" />
			</div>
		</div>	
	</xsl:template>

	<xsl:template match="personen|leihgeber">		
		<div>
			<xsl:for-each select="./person">
				<xsl:if test="position() != 1"><xsl:text>; </xsl:text></xsl:if>
				<xsl:value-of select="./anzeigename"/> 
				<xsl:if test="./rolle"><xsl:text> (</xsl:text><xsl:value-of select="./rolle"/><xsl:text>)</xsl:text></xsl:if>
			</xsl:for-each>
		</div>
	</xsl:template>	
	
	<xsl:template match="schlagwort">
		<p><span class="label schlagworte">Schlagworte: </span>
		<span class="value schlagworte">
			<xsl:for-each select="./deskriptor">
				<xsl:if test="position() != 1"><xsl:text>; </xsl:text></xsl:if>
				<xsl:value-of select="./term" /> 
			</xsl:for-each>
			</span>			
		</p>	
	</xsl:template>
	
	<xsl:template match="material">
		<p><span class="label materialien">Materialien: </span>
		<span class="value materialien">
			<xsl:for-each select="./deskriptor">
				<xsl:if test="position() != 1"><xsl:text>; </xsl:text></xsl:if>
				<xsl:value-of select="./term" /> 
			</xsl:for-each>
			</span>			
		</p>	
	</xsl:template>
	
	<xsl:template match="traegermaterial">
		<p><span class="label materialien">Trägermat.: </span>
		<span class="value materialien">
			<xsl:for-each select="./deskriptor">
				<xsl:if test="position() != 1"><xsl:text>; </xsl:text></xsl:if>
				<xsl:value-of select="./term" /> 
			</xsl:for-each>
			</span>			
		</p>	
	</xsl:template>
	
	<xsl:template match="technik">
		<p><span class="label materialien">Techniken: </span>
		<span class="value materialien">
			<xsl:for-each select="./deskriptor">
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

