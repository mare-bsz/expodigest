<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output media-type="text/html" method="html" omit-xml-declaration="yes"/>
	
	<xsl:template match="/">
		<strong><xsl:value-of select="/record/objektbezeichnung/term" /></strong><xsl:text> (InvNr.: </xsl:text><xsl:value-of select="/record/inventarnummer" /><xsl:text>)</xsl:text> 		
	</xsl:template>
	
</xsl:stylesheet>