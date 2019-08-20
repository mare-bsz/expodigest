<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : json.xsl
    Created on : 2019
    Modiefied on: 2019
    Author     : christof.mainberger@bsz-bw.de; sophie.roelle@bsz-bw.de
-->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          		xmlns:java="http://xml.apache.org/xalan/java"
          		xmlns:solr="org.apache.solr.common.SolrDocument"
    			exclude-result-prefixes="java solr"
    			extension-element-prefixes="solr">
    			
          			
    <xsl:param name="trenner" select="''" />
	<xsl:param name="likes" select="0" />	
	<xsl:param name="doc" />

    <xsl:output method="text" version="1.0" encoding="UTF-8" indent="no" media-type="application/json"/>
    
    <xsl:strip-space elements="*"/>
    
	<xsl:template match="/">
		<xsl:apply-templates select="record" />
	</xsl:template>  
    
    <xsl:template match="record">
    	<xsl:value-of select="$trenner" />
		{		
        	<xsl:apply-templates select="./imdasid"/>
			<xsl:text>, "likes": "</xsl:text><xsl:value-of select="java:bsz.expo.Util.getSolrValue($doc, 'likes')"/><xsl:text>"</xsl:text>
			<xsl:apply-templates select="./inventarnummer"/>
        	<xsl:apply-templates select="./eingangsdatum"/>
        	<xsl:apply-templates select="./eingangsdatumtext"/>
        	<xsl:apply-templates select="./eingangsart"/>
			<xsl:apply-templates select="./standort_digitaler_katalog"/>
			<xsl:call-template name="zugang" />
        	<xsl:apply-templates select="./collection"/>
        	<xsl:call-template name="ueberschriften" />
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
        	<xsl:apply-templates select="./audiosde"/>
        	<xsl:apply-templates select="./audiosen"/>
        	<xsl:apply-templates select="./audiosfr"/>
        	<xsl:apply-templates select="./videosde"/>
        	<xsl:apply-templates select="./videosen"/>
        	<xsl:apply-templates select="./videosfr"/>
        	<xsl:apply-templates select="./nutzungsrechte"/>
        	<xsl:apply-templates select="./personen"/>
        	<xsl:apply-templates select="./mineralischesystematik"/>       
        	<xsl:apply-templates select="./entstehungszeit"/>
        	<xsl:apply-templates select="./datumsangaben"/>
        	<xsl:apply-templates select="./geologischedatierung"/>
        	<xsl:apply-templates select="./orte"/>
        	<xsl:apply-templates select="./ethnie"/>
        	<xsl:apply-templates select="./inschrift"/>
        	<xsl:apply-templates select="./schlagwort"/>
        	<xsl:apply-templates select="./material"/>
        	<xsl:apply-templates select="./traegermaterial"/>
        	<xsl:apply-templates select="./technik"/>
        	<xsl:apply-templates select="./provenienz"/>
        	<xsl:apply-templates select="./systematik"/>
        	<xsl:apply-templates select="./masse"/>
        	<xsl:apply-templates select="./leihgeber"/>
        	<xsl:apply-templates select="./invnrleihgeber"/>
        	<xsl:apply-templates select="./invnradelhausenstiftung"/>
        	<xsl:apply-templates select="./invnrwaisenhausstiftung"/>
        	<xsl:apply-templates select="./invnrheiliggeistspitalstiftung"/>
        	<xsl:apply-templates select="./sammler"/>
        	<xsl:apply-templates select="./verwandte"/>       
        }
    </xsl:template> 
    
    		<xsl:template match="imdasid">
    			<xsl:text>"imdasid" : "</xsl:text><xsl:value-of select="."/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="inventarnummer">
    			<xsl:text>,"inventarnummer" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="eingangsdatum">
        		<xsl:text>,"eingangsdatum" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="eingangsdatumtext">
        		<xsl:text>,"eingangsdatumtext" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="eingangsart">
    			<xsl:text>,"eingangsart" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
			
			<xsl:template match="standort_digitaler_katalog">
    			<xsl:text>,"standort" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
    		<xsl:template name="zugang">
    			<xsl:text>,"zugang" : "</xsl:text>
    				<xsl:value-of select="java:bsz.expo.Util.toJson(./eingangsart)" />
    				<xsl:choose>
    					<xsl:when test="./sammler">
    						<xsl:text> </xsl:text><xsl:call-template name="personkurz"><xsl:with-param name="person" select="./sammler/person" /></xsl:call-template>
    					</xsl:when>
    					<xsl:when test="./leihgeber">
    						<xsl:text> </xsl:text><xsl:call-template name="personkurz"><xsl:with-param name="person" select="./leihgeber/person" /></xsl:call-template>
    					</xsl:when>
    				</xsl:choose>
    				<xsl:text>, </xsl:text>
    				<xsl:choose>
    					<xsl:when test="./eingangsdatum">
    						<xsl:value-of select="java:bsz.expo.Util.extractYear(./eingangsdatum)" />
    					</xsl:when>
    					<xsl:otherwise>
    						<xsl:value-of select="java:bsz.expo.Util.extractYear(./eingangsdatumtext)" />
    					</xsl:otherwise>
    				</xsl:choose>
    			<xsl:text>"</xsl:text>
    		</xsl:template>
    		
    		<xsl:template name="personkurz">
    			<xsl:param name="person" />
    			<xsl:choose>
    				<xsl:when test="$person/anzeigename" >
    					<xsl:value-of select="$person/anzeigename" />
    				</xsl:when>
    				<xsl:otherwise>    					
    					<xsl:if test="$person/vorname"><xsl:value-of select="$person/vorname"/><xsl:text> </xsl:text></xsl:if>
    					<xsl:value-of select="$person/nachname" />
    				</xsl:otherwise>
    			</xsl:choose>			
    		</xsl:template>
    		
        	<xsl:template match="collection">
    			<xsl:text>,"museum" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./museum)"/><xsl:text>"</xsl:text>
    			<xsl:text>,"sammlung" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./sammlung)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
    		<xsl:template name="ueberschriften">
    			<xsl:choose>
	    			<xsl:when test="./personen/person[rollede='Künstler/in']">
	    				<xsl:text>, "ueberschrift1de" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./personen/person/anzeigename)"/><xsl:text>"</xsl:text>
	    				<xsl:text>, "ueberschrift1en" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./personen/person/anzeigename)"/><xsl:text>"</xsl:text>
	    				<xsl:text>, "ueberschrift1fr" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./personen/person/anzeigename)"/><xsl:text>"</xsl:text>
	    			</xsl:when>
	    			<xsl:otherwise>
	    				<xsl:text>, "ueberschrift1de" : "</xsl:text><xsl:value-of select="java:bsz.expo.SmfUtil2.clean(./objektbezeichnungen/./deskriptor[1]/term)"/><xsl:text>"</xsl:text>
	    				<xsl:text>, "ueberschrift1en" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./objektbezeichnung_en)"/><xsl:text>"</xsl:text>
	    				<xsl:text>, "ueberschrift1fr" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./objektbezeichnung_fr)"/><xsl:text>"</xsl:text>	    			
	    			</xsl:otherwise>
    			</xsl:choose>
    			<xsl:choose>
	    			<xsl:when test="./collection/museum = 'Museum Natur und Mensch'">
	    				<xsl:call-template name="ue2">							
							<xsl:with-param name="lbl" select="'ueberschrift2de'"/>
							<xsl:with-param name="val" select="./taxon"/>
						</xsl:call-template>
						<xsl:call-template name="ue2">							
							<xsl:with-param name="lbl" select="'ueberschrift2en'"/>
							<xsl:with-param name="val" select="./taxon"/>
						</xsl:call-template>
						<xsl:call-template name="ue2">							
							<xsl:with-param name="lbl" select="'ueberschrift2fr'"/>
							<xsl:with-param name="val" select="./taxon"/>
						</xsl:call-template>	    				
	    			</xsl:when>
	    			<xsl:otherwise>
						<xsl:call-template name="ue2">							
							<xsl:with-param name="lbl" select="'ueberschrift2de'"/>
							<xsl:with-param name="val" select="./titelde"/>
						</xsl:call-template>
						<xsl:call-template name="ue2">							
							<xsl:with-param name="lbl" select="'ueberschrift2en'"/>
							<xsl:with-param name="val" select="./titelen"/>
						</xsl:call-template>
						<xsl:call-template name="ue2">							
							<xsl:with-param name="lbl" select="'ueberschrift2fr'"/>
							<xsl:with-param name="val" select="./titelfr"/>
						</xsl:call-template>	    				
	    			</xsl:otherwise>
    			</xsl:choose>
    		</xsl:template>
			    		
    		<xsl:template name="ue2">
				<xsl:param name="lbl" />
				<xsl:param name="val" />
				<xs:if test="$val or ./entstehungszeit">
					<xsl:text>, "</xsl:text><xsl:value-of select="$lbl"/><xsl:text>" : "</xsl:text>
					<xsl:if test="$val"><xsl:value-of select="java:bsz.expo.Util.toJson($val)"/></xsl:if>
					<xsl:if test="$val and ./entstehungszeit"><xsl:text>, </xsl:text></xsl:if>
					<xsl:if test="./entstehungszeit"><xsl:value-of select="java:bsz.expo.Util.toJson(./entstehungszeit)"/></xsl:if>
					<xsl:text>"</xsl:text>
				</xs:if>
    		</xsl:template>
    		
        	<xsl:template match="objektbezeichnungen">
    			<xsl:text>,"objektbezeichnungde" : "</xsl:text><xsl:value-of select="java:bsz.expo.SmfUtil2.clean(./deskriptor[1]/term)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="objektbezeichnung_en">
    			<xsl:text>,"objektbezeichnungen" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="objektbezeichnung_fr">
    			<xsl:text>,"objektbezeichnungfr" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="titelde">
    			<xsl:text>,"titelde" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="titelen">
    			<xsl:text>,"titelen" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="titelfr">
    			<xsl:text>,"titelfr" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="taxon">
    			<xsl:text>,"taxon" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="indigenebez">
    			<xsl:text>,"indigenebez" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="kurztextde">
    			<xsl:text>,"kurztextde" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="kurztexten">
    			<xsl:text>,"kurztexten" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="kurztextfr">
    			<xsl:text>,"kurztextfr" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="langtextde">
    			<xsl:text>,"langtextde" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="langtexten">
    			<xsl:text>,"langtexten" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="langtextfr">
    			<xsl:text>,"langtextfr" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
    		<xsl:template match="submasters">
    			<xsl:text>,"bilder" : [</xsl:text><xsl:apply-templates select="./submaster" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="fotos">
    			<xsl:text>,"foto" : [</xsl:text><xsl:apply-templates select="./foto" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="audiosde">
    			<xsl:text>,"audiode" : [</xsl:text><xsl:apply-templates select="./audio" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
    		<xsl:template match="audiosen">
    			<xsl:text>,"audioen" : [</xsl:text><xsl:apply-templates select="./audio" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
    		<xsl:template match="audiosfr">
    			<xsl:text>,"audiofr" : [</xsl:text><xsl:apply-templates select="./audio" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="videosde">
    			<xsl:text>,"videode" : [</xsl:text><xsl:apply-templates select="./video" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
    		<xsl:template match="videosen">
    			<xsl:text>,"videoen" : [</xsl:text><xsl:apply-templates select="./video" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
    		<xsl:template match="videosfr">
    			<xsl:text>,"videofr" : [</xsl:text><xsl:apply-templates select="./video" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		    		
        	<xsl:template match="nutzungsrechte">
    			<xsl:text>,"nutzungsrechte" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="personen">
    			<xsl:text>,"person" : [</xsl:text><xsl:apply-templates select="./person" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="mineralischesystematik">
    			<xsl:text>,"mineralogischesystematik" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="entstehungszeit">
    			<xsl:text>,"entstehungszeit" : "</xsl:text><xsl:value-of select="java:bsz.expo.SmfUtil.editDatum(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="datumsangaben">
    			<xsl:text>,"datierung" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="geologischedatierung">
    			<xsl:text>,"geologischedatierung" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="orte">
    			<xsl:text>,"orte" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="ethnie">
    			<xsl:text>,"ethnie" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="inschrift">
    			<xsl:text>,"inschrift" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="schlagwort">
    			<xsl:text>,"schlagwort" : [</xsl:text>
    			<xsl:text>{"term" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(../collection/domain)" /><xsl:text>"},</xsl:text>
    			<xsl:text>{"term" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(../collection/sammlung)" /><xsl:text>"},</xsl:text>
    			<xsl:apply-templates select="./deskriptor" />
    			<xsl:text>]</xsl:text>
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
    		
    		<xsl:template match="provenienz">
    			<xsl:text>,"provenienz" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)" /><xsl:text>"</xsl:text>
    		</xsl:template>
    		
    		<xsl:template match="systematik">
    			<xsl:text>,"familie-gattung-art" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>   		
    		
        	<xsl:template match="masse">
    			<xsl:text>,"masse" : [</xsl:text><xsl:apply-templates select="./mass" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="leihgeber">
    			<xsl:text>,"leihgeber" : [</xsl:text><xsl:apply-templates select="./person" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="invnrleihgeber">
    			<xsl:text>,"invnrleihgeber" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="invnradelhausenstiftung">
    			<xsl:text>,"invnradelhausenstiftung" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="invnrwaisenhausstiftung">
    			<xsl:text>,"invnrwaisenhausstiftung" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="invnrheiliggeistspitalstiftung">
    			<xsl:text>,"invnrheiliggeistspitalstiftung" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(.)"/><xsl:text>"</xsl:text>
    		</xsl:template>
    		
        	<xsl:template match="sammler">
    			<xsl:text>,"sammler" : [</xsl:text><xsl:apply-templates select="./person" /><xsl:text>]</xsl:text>
    		</xsl:template>  
    		
    		<xsl:template match="verwandte">
    			<xsl:text>,"verwandte" : [</xsl:text><xsl:apply-templates select="./deskriptor" /><xsl:text>]</xsl:text>
    		</xsl:template>
    		
    		<xsl:template match="deskriptor">
    			<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
    			<xsl:text>{</xsl:text>
    			<xsl:text>"term" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./term)" /><xsl:text>"</xsl:text>
    			<xsl:if test="./id"><xsl:text>,"id" : "</xsl:text><xsl:value-of select="./id" /><xsl:text>"</xsl:text></xsl:if>
    			<xsl:if test="./typ"><xsl:text>,"typ" : "</xsl:text><xsl:value-of select="./typ" /><xsl:text>"</xsl:text></xsl:if>
    			<xsl:if test="./anfang"><xsl:text>,"anfang" : "</xsl:text><xsl:value-of select="./anfang" /><xsl:text>"</xsl:text></xsl:if>
    			<xsl:if test="./ende"><xsl:text>,"ende" : "</xsl:text><xsl:value-of select="./ende" /><xsl:text>"</xsl:text></xsl:if>
    			<xsl:if test="./synonyme"><xsl:text>,"synonym" :[</xsl:text><xsl:apply-templates select="./synonyme/deskriptor" /><xsl:text>]</xsl:text></xsl:if>
    			<xsl:if test="./parents"><xsl:text>,"parent" : [</xsl:text><xsl:apply-templates select="./parents/deskriptor" /><xsl:text>]</xsl:text></xsl:if>
    			<xsl:if test="./notiz"><xsl:text>,"notiz" : "</xsl:text><xsl:value-of select="./notiz" /><xsl:text>"</xsl:text></xsl:if>
    			<xsl:text>}</xsl:text>    			    			
    		</xsl:template>  		
    		    		    		
    		<xsl:template match="person">
    			<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
				<xsl:text>{"nachname" : "</xsl:text><xsl:value-of select="./nachname" /><xsl:text>"</xsl:text>
				<xsl:if test="./vorname"><xsl:text>,"vorname" : "</xsl:text><xsl:value-of select="./vorname" /><xsl:text>"</xsl:text></xsl:if>
				<xsl:if test="./anzeigename"><xsl:text>,"anzeigename" : "</xsl:text><xsl:value-of select="./anzeigename" /><xsl:text>"</xsl:text></xsl:if>
				<xsl:if test="./rollede"><xsl:text>,"rollede" : "</xsl:text><xsl:value-of select="./rollede" /><xsl:text>"</xsl:text></xsl:if> 
				<xsl:if test="./rolleen"><xsl:text>,"rolleen" : "</xsl:text><xsl:value-of select="./rolleen" /><xsl:text>"</xsl:text></xsl:if> 
				<xsl:if test="./rollefr"><xsl:text>,"rollefr" : "</xsl:text><xsl:value-of select="./rollefr" /><xsl:text>"</xsl:text></xsl:if> 				
    			<xsl:if test="./datumsangaben">
    				<xsl:text>,"datumsangaben" : [</xsl:text><xsl:apply-templates select="./datumsangaben/deskriptor"/><xsl:text>]</xsl:text>
    			</xsl:if>
    			<xsl:if test="./ortsangaben">
    				<xsl:text>,"ortsangaben" : [</xsl:text><xsl:apply-templates select="./ortsangaben/deskriptor"/><xsl:text>]</xsl:text>
    			</xsl:if>
    			<xsl:if test="./aliasnamen">
    				<xsl:text>,"aliasnamen" : [</xsl:text><xsl:apply-templates select="./aliasnamen/deskriptor"/><xsl:text>]</xsl:text>
    			</xsl:if>
    			<xsl:if test="./normdaten">
    				<xsl:text>,"normdaten" : [</xsl:text><xsl:apply-templates select="./normdaten/deskriptor"/><xsl:text>]</xsl:text>
    			</xsl:if>
				<xsl:if test="./portraitde">
    				<xsl:text>,"portraitde" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./portraitde)"/><xsl:text>"</xsl:text>
    			</xsl:if>
				<xsl:if test="./portraiten">
    				<xsl:text>,"portraiten" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./portraiten)"/><xsl:text>"</xsl:text>
    			</xsl:if>
				<xsl:if test="./portraitfr">
    				<xsl:text>,"portraitfr" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./portraitfr)"/><xsl:text>"</xsl:text>
    			</xsl:if>
    			<xsl:if test="./notiz"><xsl:text>,"notiz" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./notiz)" /><xsl:text>"</xsl:text></xsl:if><xsl:text>}</xsl:text>    			    			   		
    		</xsl:template> 
    		    		
    		<xsl:template match="mass">
    			<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
				<xsl:text>{"typde" : "</xsl:text><xsl:value-of select="./typde" /><xsl:text>",</xsl:text>
				<xsl:text>"typen" : "</xsl:text><xsl:value-of select="./typen" /><xsl:text>",</xsl:text>
				<xsl:text>"typfr" : "</xsl:text><xsl:value-of select="./typfr" /><xsl:text>",</xsl:text>
				<xsl:text>"wert" : "</xsl:text><xsl:value-of select="./wert" /><xsl:text>",</xsl:text>
				<xsl:text>"einheit" : "</xsl:text><xsl:value-of select="./einheit" /><xsl:text>"</xsl:text>
				<xsl:if test="./teilde"><xsl:text>,"teilde" : "</xsl:text><xsl:value-of select="./teilde" /><xsl:text>"</xsl:text></xsl:if>
				<xsl:if test="./teilen"><xsl:text>,"teilen" : "</xsl:text><xsl:value-of select="./teilen" /><xsl:text>"</xsl:text></xsl:if>
				<xsl:if test="./teilfr"><xsl:text>,"teilfr" : "</xsl:text><xsl:value-of select="./teilfr" /><xsl:text>"</xsl:text></xsl:if><xsl:text>}</xsl:text>				
    		</xsl:template>
    		
    		<xsl:template match="foto|audio">
    			<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
    			<xsl:text>{"bezeichnung" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./bezeichnung)" /><xsl:text>"}</xsl:text>
    		</xsl:template> 
    		
    		<xsl:template match="video">
    			<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
    			<xsl:text>{"bezeichnung" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./bezeichnung)" /><xsl:text>"</xsl:text>
    			<xsl:text>,"pfad" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./pfad)" /><xsl:text>"}</xsl:text>
    		</xsl:template>
    		
    		<xsl:template match="submaster">
    			<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
    			<xsl:text>{"bild" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./uuid)" /><xsl:text>"</xsl:text>
    			<xsl:if test="./rechte"><xsl:text>,"rechte" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./rechte)" /><xsl:text>"</xsl:text></xsl:if>
				<xsl:choose>
					<xsl:when test="./rechte = 'Gemeinfrei'"><xsl:text>,"anzeige" : "voll","download" : "ja","teilen" : "ja"</xsl:text></xsl:when>
					<xsl:when test="./rechte = 'CC BY 4.0'"><xsl:text>,"anzeige" : "voll","download" : "ja","teilen" : "ja"</xsl:text></xsl:when>
					<xsl:when test="./rechte = 'CC BY-NC 4.0'"><xsl:text>,"anzeige" : "voll","download" : "ja","teilen" : "email"</xsl:text></xsl:when>
					<xsl:when test="./rechte = 'Copyright'"><xsl:text>,"anzeige" : "1000","download" : "nein","teilen" : "email"</xsl:text></xsl:when>
					<xsl:when test="./rechte = 'Copyright_VG Bild-Kunst'"><xsl:text>,"anzeige" : "1000","download" : "nein","teilen" : "ja"</xsl:text></xsl:when>
					<xsl:when test="./rechte = 'urheberrechtlich geschützt'"><xsl:text>,"anzeige" : "1000","download" : "nein","teilen" : "email"</xsl:text></xsl:when>
					<xsl:when test="./rechte = 'Urheberechtlich geschützt'"><xsl:text>,"anzeige" : "1000","download" : "nein","teilen" : "email"</xsl:text></xsl:when>
					<xsl:otherwise><xsl:text>,"anzeige" : "1000","download" : "nein","teilen" : "email"</xsl:text></xsl:otherwise>
				</xsl:choose>
    			<xsl:if test="./bildnachweis"><xsl:text>,"bildnachweis" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./bildnachweis)" /><xsl:text>"</xsl:text></xsl:if>
    			<xsl:if test="./alttext"><xsl:text>,"alttext" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./alttext)" /><xsl:text>"</xsl:text></xsl:if>
    			<xsl:if test="./notiz"><xsl:text>,"notiz" : "</xsl:text><xsl:value-of select="java:bsz.expo.Util.toJson(./notiz)" /><xsl:text>"</xsl:text></xsl:if><xsl:text>}</xsl:text>    			
    		</xsl:template>   			
   
</xsl:stylesheet>
