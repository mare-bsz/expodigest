<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output media-type="text/html" method="html" omit-xml-declaration="yes"/>
	
	<xsl:template match="/">
		<xsl:if test="./record/personen">
			<strong>
				<xsl:choose>
					<xsl:when test="./record/personen/person[1]/anzeigename"><xsl:value-of select="./record/personen/person[1]/anzeigename" /></xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="./record/personen/person[1]/vorname" /> <xsl:value-of select="./record/personen/person[1]/nachname" />
					</xsl:otherwise>
				</xsl:choose>
			</strong><xsl:text> : </xsl:text>
		</xsl:if>
		<xsl:value-of select="./record/objektbezeichnungen/deskriptor[1]/term" />
		<xsl:if test="./record/titelde"><xsl:text> (</xsl:text><xsl:value-of select="./record/titelde" /><xsl:text>)</xsl:text></xsl:if>
		<xsl:text> [</xsl:text><xsl:value-of select="./record/inventarnummer" /><xsl:text>]</xsl:text>		
	</xsl:template>
	
</xsl:stylesheet>