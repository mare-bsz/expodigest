<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : blm2blank.xsl
    Created on : 2016-08-17
    Modiefied on: 2017-03-14
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
        	<xsl:apply-templates select="./imdasid"/>
        	<xsl:apply-templates select="./inventarnummer"/>
        	<xsl:apply-templates select="./eingangsdatum"/>
        	<xsl:apply-templates select="./eingangsdatumtext"/>
        	<xsl:apply-templates select="./eingangsart"/>
        	<xsl:apply-templates select="./Collection"/>
        	<xsl:apply-templates select="./objektbezeichnungen"/>
        	<xsl:apply-templates select="./objektbezeichnung_en"/>
        	<xsl:apply-templates select="./objektbezeichnung_fr"/>
        	<xsl:apply-templates select="./titelde"/>
        	<xsl:apply-templates select="./titelen"/>
        	<xsl:apply-templates select="./titelfr"/>
        	<xsl:apply-templates select="./taxon"/>
        	<xsl:apply-templates select="./indigenebez"/>
        	<xsl:apply-templates select="./kurztextde"/>
        	<xsl:apply-templates select="./kurztexten"/>
        	<xsl:apply-templates select="./kurztextfr"/>
        	<xsl:apply-templates select="./langtextde"/>
        	<xsl:apply-templates select="./langtexten"/>
        	<xsl:apply-templates select="./langtextfr"/>
        	<xsl:apply-templates select="./submasters"/>
        	<xsl:apply-templates select="./fotos"/>
        	<xsl:apply-templates select="./audios"/>
        	<xsl:apply-templates select="./videos"/>
        	<xsl:apply-templates select="./nutzungsrechte"/>
        	<xsl:apply-templates select="./personen"/>
        	<xsl:apply-templates select="./mineralogischesystematik"/>       
        	<xsl:apply-templates select="./entstehungszeit"/>
        	<xsl:apply-templates select="./datierung"/>
        	<xsl:apply-templates select="./geologischedatierung"/>
        	<xsl:apply-templates select="./orte"/>
        	<xsl:apply-templates select="./ethnie"/>
        	<xsl:apply-templates select="./inschrift"/>
        	<xsl:apply-templates select="./schlagwort"/>
        	<xsl:apply-templates select="./material"/>
        	<xsl:apply-templates select="./traegermaterial"/>
        	<xsl:apply-templates select="./technik"/>
        	<xsl:apply-templates select="./masse"/>
        	<xsl:apply-templates select="./leihgeber"/>
        	<xsl:apply-templates select="./invnrleihgeber"/>
        	<xsl:apply-templates select="./invnradelhausenstiftung"/>
        	<xsl:apply-templates select="./invnrwaisenhausstiftung"/>
        	<xsl:apply-templates select="./invnrheiliggeistspitalstiftung"/>
        	<xsl:apply-templates select="./sammler"/>       
        }
    </xsl:template> 
    
    		<xsl:template match="imdasid">
    			<xsl:text>"imdasid" : "</xsl:text><xsl:value-of select="."/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="inventarnummer">
    			<xsl:text>,"inventarnummer" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="eingangsdatum">
        		<xsl:text>,"eingangsdatum" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="eingangsdatumtext">
        		<xsl:text>,"eingangsdatumtext" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="eingangsart">
    			<xsl:text>,"eingangsart" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="collection">
    			<xsl:text>,"museum" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./museum)"/><xsl:text>"</xsl:text>
    			<xsl:text>,"sammlung" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./sammlung)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="objektbezeichnungen">
    			<xsl:text>,"objektbezeichnung" : "</xsl:text><xsl:value-of select="./deskriptor[1]/term"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="objektbezeichnung_en">
    			<xsl:text>,"objektbezeichnung_en" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="objektbezeichnung_fr">
    			<xsl:text>,"objektbezeichnung_fr" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="titelde">
    			<xsl:text>,"titelde" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="titelen">
    			<xsl:text>,"titelen" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="titelfr">
    			<xsl:text>,"titelfr" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="taxon">
    			<xsl:text>,"taxon" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="indigenebez">
    			<xsl:text>,"indigenebez" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="kurztextde">
    			<xsl:text>,"kurztextde" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="kurztexten">
    			<xsl:text>,"kurztexten" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="kurztextfr">
    			<xsl:text>,"kurztextfr" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="langtextde">
    			<xsl:text>,"langtextde" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="langtexten">
    			<xsl:text>,"langtexten" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="langtextfr">
    			<xsl:text>,"langtextfr" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
    		<xsl:template match="submasters">
    			<xsl:text>,"submaster" : [</xsl:text><xsl:apply-templates select="./submaster" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="fotos">
    			<xsl:text>,"foto" : [</xsl:text><xsl:apply-templates select="./foto" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="audios">
    			<xsl:text>,"audio" : [</xsl:text><xsl:apply-templates select="./audio" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="videos">
    			<xsl:text>,"video" : [</xsl:text><xsl:apply-templates select="./video" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="nutzungsrechte">
    			<xsl:text>,"nutzungsrechte" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="personen">
    			<xsl:text>,"person" : [</xsl:text><xsl:apply-templates select="./person" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="mineralogischesystematik">
    			<xsl:text>,"mineralogischesystematik" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="entstehungszeit">
    			<xsl:text>,"entstehungszeit" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="datierung">
    			<xsl:text>,"datierung" : [</xsl:text><xsl:apply-templates select="./datum" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="geologischedatierung">
    			<xsl:text>,"geologischedatierung" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="orte">
    			<xsl:text>,"orte" : [</xsl:text><xsl:apply-templates select="./ort" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="ethnie">
    			<xsl:text>,"ethnie" : [</xsl:text><xsl:apply-templates select="./ort" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="inschrift">
    			<xsl:text>,"inschrift" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="schlagwort">
    			<xsl:text>,"schlagwort" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="material">
    			<xsl:text>,"material" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="traegermaterial">
    			<xsl:text>,"traegermaterial" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="technik">
    			<xsl:text>,"technik" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="masse">
    			<xsl:text>,"masse" : [</xsl:text><xsl:apply-templates select="./mass" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="leihgeber">
    			<xsl:text>,"leihgeber" : [</xsl:text><xsl:apply-templates select="./person" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="invnrleihgeber">
    			<xsl:text>,"invnrleihgeber" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="invnradelhausenstiftung">
    			<xsl:text>,"invnradelhausenstiftung" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="invnrwaisenhausstiftung">
    			<xsl:text>,"invnrwaisenhausstiftung" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="invnrheiliggeistspitalstiftung">
    			<xsl:text>,"invnrheiliggeistspitalstiftung" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="sammler">
    			<xsl:text>,"sammler" : [</xsl:text><xsl:apply-templates select="./person" /><xsl:text>]</xsl:text>
    		</xsl:template>  
    		
    		<xsl:template match="deskriptor|ort|datum">
    			<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
    			<xsl:text>{</xsl:text>
    			<xsl:text>"term" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./term)" /><xsl:text>"</xsl:text>
    			<xsl:if test="./id"><xsl:text>,"id" : "</xsl:text><xsl:value-of select="./id" /><xsl:text>"</xsl:text></xsl:if>
    			<xsl:if test="./parents"><xsl:text>,"parent" : [</xsl:text><xsl:apply-templates select="./parents/parent" /><xsl:text>]</xsl:text></xsl:if>
    			<xsl:text>}</xsl:text>    			    			
    		</xsl:template>  		
    		
    		<xsl:template match="parent">
    			<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
    			<xsl:text>{"term" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./term)" /><xsl:text>"</xsl:text>
    			<xsl:if test="./id"><xsl:text>,"id" : "</xsl:text><xsl:value-of select="./id" /><xsl:text>"</xsl:text></xsl:if>
    			<xsl:text>}</xsl:text>
    		</xsl:template>
    		    		
    		<xsl:template match="person">
    			<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
				<xsl:text>{"nachname" : "</xsl:text><xsl:value-of select="./nachname" /><xsl:text>"</xsl:text>
				<xsl:if test="./vorname"><xsl:text>,"vorname" : "</xsl:text><xsl:value-of select="./vorname" /><xsl:text>"</xsl:text></xsl:if>
				<xsl:if test="./anzeigename"><xsl:text>,"anzeigename" : "</xsl:text><xsl:value-of select="./anzeigename" /><xsl:text>"</xsl:text></xsl:if>
				<xsl:if test="./rolle"><xsl:text>,"rolle" : "</xsl:text><xsl:value-of select="./rolle" /><xsl:text>"</xsl:text></xsl:if><xsl:text>}</xsl:text> 				
    		</xsl:template>  
    		
    		<xsl:template match="mass">
    			<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
				<xsl:text>{"typ" : "</xsl:text><xsl:value-of select="./typ" /><xsl:text>",</xsl:text>
				<xsl:text>"wert" : "</xsl:text><xsl:value-of select="./wert" /><xsl:text>",</xsl:text>
				<xsl:text>"einheit" : "</xsl:text><xsl:value-of select="./einheit" /><xsl:text>"</xsl:text>
				<xsl:if test="./teil"><xsl:text>,"teil" : "</xsl:text><xsl:value-of select="./teil" /><xsl:text>"</xsl:text></xsl:if><xsl:text>}</xsl:text>    				
    		</xsl:template>
    		
    		<xsl:template match="foto|video|audio">
    			<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
    			<xsl:text>{"bezeichnung" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./bezeichnung)" /><xsl:text>"}</xsl:text>
    		</xsl:template> 
    		
    		<xsl:template match="submaster">
    			<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
    			<xsl:text>{"uuid" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./uuid)" /><xsl:text>"}</xsl:text>
    		</xsl:template>   			
   
</xsl:stylesheet>
