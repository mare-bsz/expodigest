<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : json.xsl
    Created on : 2016-08-17
    Modiefied on: 2019-06-11
    Author     : christof.mainberger@bsz-bw.de; sophie.roelle@bsz-bw.de
-->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          		xmlns:java="http://xml.apache.org/xalan/java"
    			exclude-result-prefixes="java">
    			
          			
    <xsl:param name="trenner" select="''" />   			

    <xsl:output method="text" version="1.0" encoding="UTF-8" indent="no" media-type="application/json"/>
    
    <xsl:strip-space elements="*"/>
    
	<xsl:template match="/">
		<xsl:apply-templates select="record" />
	</xsl:template>  
    
    <xsl:template match="record">
    	<xsl:value-of select="$trenner" />
		{
			<xsl:call-template name="imdasid" />
			<xsl:call-template name="inventarnummer" />
			<xsl:call-template name="ausgestellt" />
			<xsl:call-template name="sammlung" />
			<xsl:call-template name="sammlungsgliederung" />
			<xsl:call-template name="meisterwerk" />
        	<xsl:call-template name="eingangsdatum" />
        	<xsl:call-template name="objektbezeichnung" />
        	<xsl:call-template name="objekttitel" />
        	<xsl:call-template name="genre" />
        	<xsl:call-template name="datierung" />
        	<xsl:call-template name="epoche" />
        	<xsl:call-template name="entstehungszeit" />
        	<xsl:call-template name="orte" />
        	<xsl:call-template name="personen" />
        	<xsl:call-template name="schlagworte" />
        	<xsl:call-template name="materialien" />
        	<xsl:call-template name="techniken" />
        	<xsl:call-template name="bilder" />
        	<xsl:call-template name="masse" />
        	<xsl:call-template name="katalogtext" />
            <xsl:call-template name="ausstellungen" />
            <xsl:call-template name="literaturliste" /> 
            <xsl:call-template name="provenienzonline" /> 
            <xsl:call-template name="trivia" /> 
            <xsl:call-template name="youtube" /> 
        }
    </xsl:template>

	<xsl:template name="ausgestellt">
		<xsl:text>,"ausgestellt" : "</xsl:text>
    	<xsl:choose>
			<xsl:when test="starts-with(./standort,'ausgeliehen')"><xsl:text>verliehen</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Bibliothek')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Brücke')"><xsl:text>depot</xsl:text></xsl:when>		
			<xsl:when test="starts-with(./standort,'Büro')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Dauerleihgabe')"><xsl:text>verliehen</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Depot')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'EG')"><xsl:text>ausgestellt</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Format')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Fotowerkstatt')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Gelber Schrank')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Junge Kunsthalle')"><xsl:text>ausgestellt</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Kabinett')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Kasten')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Kleinbilddepot')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Mappe')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Nicht mehr im Haus')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Oberlicht')"><xsl:text>ausgestellt</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Orangerie')"><xsl:text>ausgestellt</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Passagehof')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Regal')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Restaurierung')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Saal')"><xsl:text>ausgestellt</xsl:text></xsl:when>		
			<xsl:when test="starts-with(./standort,'Schrank')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Thoma')"><xsl:text>ausgestellt</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Thomakapelle')"><xsl:text>ausgestellt</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Treppenhaus')"><xsl:text>ausgestellt</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'vermisst')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'verstellt')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Vorlegesaal')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Vortragssaal')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Vortragssaal-Regal')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Vortragssaal-Wand')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Wand')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Weinbrenner-Tresen')"><xsl:text>depot</xsl:text></xsl:when>
			<xsl:when test="starts-with(./standort,'Westfassade')"><xsl:text>ausgestellt</xsl:text></xsl:when>			
			<xsl:otherwise><xsl:text>depot</xsl:text></xsl:otherwise>		
		</xsl:choose>
		<xsl:text>"</xsl:text>  
    </xsl:template>
	
	<xsl:template name="meisterwerk">
    	<xsl:if test="./meisterwerk">
    		<xsl:text>,"meisterwerk" : "</xsl:text><xsl:value-of select="./meisterwerk"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="genre">
    	<xsl:if test="./genre">
    		<xsl:text>,"genre" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./genre)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="epoche">
    	<xsl:if test="./epochen/epoche">
    		<xsl:text>,"epoche" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./epochen/epoche/term)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="provenienzonline">
    	<xsl:if test="./provenienzonline">
    		<xsl:text>,"provenienzonline" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./provenienzonline)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="trivia">
    	<xsl:if test="./trivia">
    		<xsl:text>,"trivia" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./trivia)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="youtube">
    	<xsl:if test="./youtube">
    		<xsl:text>,"youtube" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./youtube)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>  
    
    <xsl:template name="imdasid">
    	<xsl:text>"imdasid" : "</xsl:text><xsl:value-of select="./imdasid"/><xsl:text>"</xsl:text>
    </xsl:template>
    
    <xsl:template name="museum">
    	<xsl:if test="./collection/museum">
    		<xsl:text>,"institution" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./collection/museum)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="sammlung">
    	<xsl:if test="./collection/sammlung">
    		<xsl:text>,"sammlung" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./collection/sammlung)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="eingangsdatum">
    	<xsl:if test="./eingangsdatum">
    		<xsl:text>,"eingangsdatum" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./eingangsdatum)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="entstehungszeit">
    	<xsl:if test="./entstehungszeit">
    		<xsl:text>,"entstehungszeit" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./entstehungszeit)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="inventarnummer">
    	<xsl:if test="./inventarnummer">
    		<xsl:text>,"inventarnummer" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./inventarnummer)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="standort">
    	<xsl:if test="./standort">
    		<xsl:text>,"standort" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./standort)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="objektbezeichnung">
    	<xsl:if test="./objektbezeichnung">
    		<xsl:text>,"objektbezeichnung" : "</xsl:text>
    		<xsl:value-of select="java:de.jotwerk.Util.toJson(./objektbezeichnung)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="objekttitel">
    	<xsl:if test="./objekttitel">
    		<xsl:text>,"objekttitel" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./objekttitel)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="katalogtext">
    	<xsl:if test="./katalogtext">
    		<xsl:text>,"katalogtext" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./katalogtext)"/><xsl:text>"</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="materialien">
    	<xsl:if test="./materialien">
    		<xsl:text>,"materialien" : [</xsl:text>
    			<xsl:for-each select="./materialien/material">
    				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
	    			<xsl:text>{"term" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./term)"/><xsl:text>"</xsl:text>
	    			<xsl:text>,"id" : "</xsl:text><xsl:value-of select="./id"/><xsl:text>"}</xsl:text>
    			</xsl:for-each>
    		<xsl:text>]</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="sammlungsgliederung">
    	<xsl:if test="./sammlungsgliederungen">
    		<xsl:text>,"sammlungsgliederung" : [</xsl:text>
    			<xsl:for-each select="./sammlungsgliederungen/sammlungsgliederung">
    				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
	    			<xsl:text>{"term" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./term)"/><xsl:text>"</xsl:text>
	    			<xsl:text>,"id" : "</xsl:text><xsl:value-of select="./id"/><xsl:text>"}</xsl:text>
    			</xsl:for-each>
    		<xsl:text>]</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="techniken">
    	<xsl:if test="./technik">
    		<xsl:text>,"materialien" : [</xsl:text>
    			<xsl:for-each select="./techniken/technik">
    				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
	    			<xsl:text>{"term" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./term)"/><xsl:text>"</xsl:text>
	    			<xsl:text>,"id" : "</xsl:text><xsl:value-of select="./id"/><xsl:text>"}</xsl:text>
    			</xsl:for-each>
    		<xsl:text>]</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="schlagworte">
    	<xsl:if test="./schlagworte">
    		<xsl:text>,"schlagworte" : [</xsl:text>
    			<xsl:for-each select="./schlagworte/schlagwort">
    				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
	    			<xsl:text>{"term" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./term)"/><xsl:text>"</xsl:text>
	    			<xsl:text>,"id" : "</xsl:text><xsl:value-of select="./id"/><xsl:text>"}</xsl:text>
    			</xsl:for-each>
    		<xsl:text>]</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="orte">
    	<xsl:if test="./orte">
    		<xsl:text>,"orte" : [</xsl:text>
    			<xsl:for-each select="./orte/ort">
    				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
	    			<xsl:text>{"term" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./term)"/><xsl:text>"</xsl:text>
	    			<xsl:text>,"id" : "</xsl:text><xsl:value-of select="./id"/><xsl:text>"</xsl:text>
	    			<xsl:if test="./typ"><xsl:text>,"typ" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./typ)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./gnd"><xsl:text>,"gnd" : "</xsl:text><xsl:value-of select="./gnd"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:text>}</xsl:text>
    			</xsl:for-each>
    		<xsl:text>]</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="datierung">
    	<xsl:if test="./datierung">
    		<xsl:text>,"datierung" : [</xsl:text>
    			<xsl:for-each select="./datierung/datum">
    				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
	    			<xsl:text>{"term" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./term)"/><xsl:text>"</xsl:text>
	    			<xsl:text>,"id" : "</xsl:text><xsl:value-of select="./id"/><xsl:text>"</xsl:text>
	    			<xsl:if test="./anfang"><xsl:text>,"anfang" : "</xsl:text><xsl:value-of select="./anfang"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./ende"><xsl:text>,"ende" : "</xsl:text><xsl:value-of select="./ende"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:text>}</xsl:text>
    			</xsl:for-each>
    		<xsl:text>]</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="personen">
    	<xsl:if test="./personen/person">
    		<xsl:text>,"personen" : [</xsl:text>
    			<xsl:for-each select="./personen/person">
    				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
	    			<xsl:text>{"vorname" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./vorname)"/><xsl:text>"</xsl:text>
	    			<xsl:text>,"nachname" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./nachname)"/><xsl:text>"</xsl:text>
	    			<xsl:text>,"anzeigename" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./anzeigename)"/><xsl:text>"</xsl:text>
	    			<xsl:text>,"geschlecht" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./geschlecht)"/><xsl:text>"</xsl:text>
	    			<xsl:text>,"rolle" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./rolle)"/><xsl:text>"</xsl:text>
	    			<xsl:if test="./notiz"><xsl:text>,"notiz" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./notiz)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:text>}</xsl:text>
    			</xsl:for-each>
    		<xsl:text>]</xsl:text>
    	</xsl:if>    
    </xsl:template>	
    
    <xsl:template name="masse">
    	<xsl:if test="./masse/mass">
    		<xsl:text>,"masse" : [</xsl:text>
    			<xsl:for-each select="./masse/mass">
    				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
	    			<xsl:text>{"wert" : "</xsl:text><xsl:value-of select="./wert"/><xsl:text>"</xsl:text>
	    			<xsl:text>,"einheit" : "</xsl:text><xsl:value-of select="./einheit"/><xsl:text>"</xsl:text>
	    			<xsl:if test="./teil"><xsl:text>,"teil" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./teil)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./typ"><xsl:text>,"typ" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./typ)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:text>}</xsl:text>
    			</xsl:for-each>
    		<xsl:text>]</xsl:text>
    	</xsl:if>    
    </xsl:template>	
	
    <xsl:template name="bilder">
    	<xsl:if test="./bilder/bild">
    		<xsl:text>,"bilder" : [</xsl:text>
    			<xsl:for-each select="./bilder/bild">
    				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
	    			<xsl:text>{"bezeichnung" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./bezeichnung)"/><xsl:text>"</xsl:text>
	    			<xsl:text>}</xsl:text>
    			</xsl:for-each>
    		<xsl:text>]</xsl:text>
    	</xsl:if>    
    </xsl:template>	
    
    <xsl:template name="ausstellungen">
    	<xsl:if test="./ausstellungen/ausstellung">
    		<xsl:text>,"ausstellungen" : [</xsl:text>
    			<xsl:for-each select="./ausstellungen/ausstellung">
    				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
	    			<xsl:text>{"titel" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./titel)"/><xsl:text>"</xsl:text>
	    			<xsl:if test="./adresse"><xsl:text>,"adresse" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./adresse)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./ort"><xsl:text>,"ort" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./ort)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./notiz"><xsl:text>,"notiz" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./notiz)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:text>}</xsl:text>
    			</xsl:for-each>
    		<xsl:text>]</xsl:text>
    	</xsl:if>    
    </xsl:template>
    
    <xsl:template name="literaturliste">
    	<xsl:if test="./literaturliste/literatur">
    		<xsl:text>,"literaturliste" : [</xsl:text>
    			<xsl:for-each select="./literaturliste/literatur">
    				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
	    			<xsl:text>{"autor" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./autor)"/><xsl:text>"</xsl:text>
	    			<xsl:text>,"titel" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./titel)"/><xsl:text>"</xsl:text>
	    			<xsl:if test="./zusatz"><xsl:text>,"zusatz" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./zusatz)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./inbuch"><xsl:text>,"inbuch" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./inbuch)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./seite"><xsl:text>,"seite" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./seite)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./notiz"><xsl:text>,"notiz" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./notiz)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./jahr"><xsl:text>,"jahr" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./jahr)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./ort"><xsl:text>,"ort" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./ort)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./abb"><xsl:text>,"abb" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./abb)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./nr"><xsl:text>,"nr" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./nr)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./band"><xsl:text>,"band" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./band)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:if test="./inzeitschrift"><xsl:text>,"inzeitschrift" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./inzeitschrift)"/><xsl:text>"</xsl:text></xsl:if>
	    			<xsl:text>}</xsl:text>
    			</xsl:for-each>
    		<xsl:text>]</xsl:text>
    	</xsl:if>    
    </xsl:template>
   
</xsl:stylesheet>
