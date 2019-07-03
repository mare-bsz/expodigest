<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document    : json.xsl
    Created on  : 2018-12-18
    Modiefied on: 2019-02-28
    Author      : christof.mainberger@bsz-bw.de
    Beschreibung: Setzt Expo-XML wie es für die AIB exportiert wird in json.xml um.
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
			"imdasid" : "<xsl:value-of select="./imdasid"/>"
			,"standort" :"<xsl:value-of select="java:bsz.expo.Util.toJson(./standort)"/>"
			,"zugang" :"<xsl:value-of select="./zugang"/>"
			<xsl:if test="./highlight">
    			,"highlight" : "<xsl:value-of select="./highlight"/>"
    		</xsl:if>
			<xsl:if test="./inventarnummer">
    			,"inventarnummer" : "<xsl:value-of select="./inventarnummer"/>"
    		</xsl:if>    		
			<xsl:if test="./objektbezeichnungen">		
	    		,"objektbezeichnung" : {
	    			<xsl:for-each select="./objektbezeichnungen/objektbezeichnung">
						"term" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./term)"/>"
						, "id" : "<xsl:value-of select="./id"/>"
						<xsl:call-template name="glossar" />
						<xsl:apply-templates select="./parents" />						
					</xsl:for-each>	    			    			
				}
			</xsl:if> 			  		
			<xsl:if test="./objekttitel">
				,"objekttitel" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./objekttitel)"/>"
			</xsl:if>
			<xsl:if test="./text">
				,"text" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./textdeutsch)"/>"
			</xsl:if>			
			<!-- xsl:if test="./textdeutsch">
				,"textdeutsch" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./textdeutsch)"/>"
			</xsl:if>
			<xsl:if test="./textenglisch">
				,"textenglisch" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./textenglisch)"/>"
			</xsl:if>
			<xsl:if test="./textfamilie">
				,"textfamilie" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./textfamilie)"/>"
			</xsl:if>
			<xsl:if test="./texteinfach">
				,"texteinfach" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./texteinfach)"/>"
			</xsl:if -->			
			<xsl:if test="./datierung">
				,"datierung" : [
				    <xsl:for-each select="./datierung/datum">
						<xsl:if test="position() != 1">, </xsl:if>
						{"datum" : "<xsl:value-of select="./term"/>",
						"anfang" : "<xsl:value-of select="./anfang"/>",
						"ende" : "<xsl:value-of select="./ende"/>"
						<xsl:call-template name="glossar" />
						<xsl:apply-templates select="./parents" />}
					</xsl:for-each>
				]
			</xsl:if>
			<xsl:if test="./personen">
				,"personen" : [
				<xsl:for-each select="./personen/person">
					<xsl:if test="position() != 1">, </xsl:if>
					{<xsl:if test="./vorname">
						"vorname" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./vorname)"/>",
					</xsl:if>
					<xsl:if test="./nachname">
						"nachname" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./nachname)"/>",
					</xsl:if>
					<xsl:if test="./anzeigename">
						"anzeigename" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./anzeigename)"/>",
					</xsl:if>
					<xsl:if test="./geschlecht">
						"geschlecht" : "<xsl:value-of select="./geschlecht"/>",
					</xsl:if>
					<xsl:if test="./rolle">
						"rolle" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./rolle)"/>"
					</xsl:if>
					<xsl:if test="./datumsangaben">
						,"datumsangaben" : [
						<xsl:for-each select="./datumsangaben/datumsangabe">
							<xsl:if test="position() != 1">, </xsl:if>
							<xsl:text>{</xsl:text>
							"datum" : "<xsl:value-of select="./datum"/>"
							<xsl:if test="./typ">
								,"typ" : "<xsl:value-of select="./typ"/>"
							</xsl:if>
							<xsl:if test="./notiz">
								,"notiz" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./notiz)"/>"
							</xsl:if>
							<xsl:text>}</xsl:text>							
						</xsl:for-each>						
						]
					</xsl:if>
					<xsl:if test="./orte">
						,"orte" : [
						<xsl:for-each select="./orte/ort">
							<xsl:if test="position() != 1">, </xsl:if>
							<xsl:text>{</xsl:text>
							"ort" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./term)"/>"							
							<xsl:if test="./typ">
								,"typ" : "<xsl:value-of select="./typ"/>"
							</xsl:if>
							<xsl:if test="./notiz">
								,"notiz" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./notiz)"/>"
							</xsl:if>
							<xsl:text>}</xsl:text>							
						</xsl:for-each>						
						]
					</xsl:if>			
					<xsl:if test="./aliasnamen">
						,"aliasnamen" : [
						<xsl:for-each select="./aliasnamen/aliasname">
							<xsl:if test="position() != 1">, </xsl:if>
							<xsl:text>{</xsl:text>
							"datum" : "<xsl:value-of select="./datum"/>"
							<xsl:if test="./typ">
								,"typ" : "<xsl:value-of select="./typ"/>"
							</xsl:if>
							<xsl:if test="./notiz">
								,"notiz" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./notiz)"/>"
							</xsl:if>
							<xsl:text>}</xsl:text>							
						</xsl:for-each>						
						]
					</xsl:if>
					<xsl:if test="./normdaten">
						,"normdaten" : [
						<xsl:for-each select="./normdaten/normdatum">
							<xsl:if test="position() != 1">, </xsl:if>
							<xsl:text>{</xsl:text>
							"id" : "<xsl:value-of select="./id"/>"
							<xsl:if test="./typ">
								,"typ" : "<xsl:value-of select="./typ"/>"
							</xsl:if>
							<xsl:if test="./notiz">
								,"url" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./url)"/>"
							</xsl:if>
							<xsl:text>}</xsl:text>							
						</xsl:for-each>						
						]
					</xsl:if>
					<xsl:if test="./notiz">
						,"notiz" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./notiz)"/>"
					</xsl:if>
					<xsl:if test="./notizintern">
						,"notizintern" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./notizintern)"/>"
					</xsl:if>}
				</xsl:for-each>
				]
			</xsl:if>					
			<xsl:if test="./orte">
				,"orte" : [
					<xsl:for-each select="./orte/ort">
						<xsl:if test="position() != 1">, </xsl:if>
						{
							"term" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./term)"/>"
							, "ortstyp" : "<xsl:value-of select="./typ"/>"
							, "gnd" : "<xsl:value-of select="./gnd"/>"
							, "id" : "<xsl:value-of select="./id"/>"
							<xsl:if test="./geoname">
							, "latitude" : "<xsl:value-of select="./geoname/latitude" />"
							, "longitude" : "<xsl:value-of select="./geoname/longitude"/>" 
							</xsl:if>							
							<xsl:call-template name="glossar" />
							<xsl:apply-templates select="./parents" />							
						}
					</xsl:for-each>
				]
			</xsl:if>
			<xsl:if test="./schlagworte">
				,"schlagworte" : [
					<xsl:for-each select="./schlagworte/schlagwort">
						<xsl:if test="position() != 1">, </xsl:if>
						{
							"term" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./term)"/>"
							, "id" : "<xsl:value-of select="./id"/>"
							<xsl:call-template name="glossar" />
							<xsl:apply-templates select="./parents" />						
						}
					</xsl:for-each> 
				]
			</xsl:if>	
			<xsl:if test="./materialien">
				,"materialien" : [
					<xsl:for-each select="./materialien/material">
						<xsl:if test="position() != 1">, </xsl:if>
						{
							"term" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./term)"/>"							
							, "id" : "<xsl:value-of select="./id"/>"
							<xsl:call-template name="glossar" />
							<xsl:apply-templates select="./parents" />						
						}
					</xsl:for-each>				
				]
			</xsl:if>
			<xsl:if test="./techniken">
				,"techniken" : [
					<xsl:for-each select="./techniken/technik">
						<xsl:if test="position() != 1">, </xsl:if>
						{
							"term" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./term)"/>"
							, "id" : "<xsl:value-of select="./id"/>"
							<xsl:call-template name="glossar" />
							<xsl:apply-templates select="./parents" />						
						}
					</xsl:for-each>					
				]				
			</xsl:if>	
			<xsl:if test="./aibsystematik">
				,"aibsystematik" : [
					<xsl:for-each select="./aibsystematik/systematikstelle">
						<xsl:if test="position() != 1">, </xsl:if>
						{ "systematikstelle" : [
						<xsl:for-each select="./term">
							<xsl:if test="position() != 1">, </xsl:if>
							{ "term" : "<xsl:value-of select="java:bsz.expo.Util.toJson(.)" />" } 
						</xsl:for-each>
						] }
					</xsl:for-each>
				]			
			</xsl:if>
			<xsl:if test="./masse">
				,"masse" : [
					<xsl:for-each select="./masse/mass">
						<xsl:if test="position() != 1">, </xsl:if>
						{
							<xsl:if test="./typ">
								"typ" : "<xsl:value-of select="./typ"/>", 
							</xsl:if>
							"wert" : "<xsl:value-of select="./wert"/>", 
							"einheit" : "<xsl:value-of select="./einheit"/>"
						}
					</xsl:for-each>					
				]
			</xsl:if>
			<xsl:if test="./bilder">
				,"bilder" : [
					<xsl:for-each select="./bilder/bild">
						<xsl:if test="position() != 1">, </xsl:if>
						{
							<xsl:if test="./bezeichnung">
								"bezeichnung" : "<xsl:value-of select="./bezeichnung"/>", 
							</xsl:if>
							"pfad" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./pfad)"/>", 
							"dateiname" : "<xsl:value-of select="./dateiname"/>"
						}
					</xsl:for-each>					
				]
			</xsl:if>								
    	}
    </xsl:template>	
    
    <xsl:template match="parents">
        ,"parents" : [
    	<xsl:for-each select="./parent">
    		<xsl:if test="position() != 1">, </xsl:if>
    		{
    			"term" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./term)"/>"
				, "id" : "<xsl:value-of select="./id"/>"
    			<xsl:call-template name="glossar" />
    		}
    	</xsl:for-each>
    	]    
    </xsl:template>     
    
    <xsl:template name="glossar">
		<xsl:if test="./glossardeutsch">
			,"glossartext" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./glossardeutsch)" />"
		</xsl:if>    	
    	<!-- xsl:if test="./glossardeutsch">
			,"glossardeutsch" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./glossardeutsch)" />"
		</xsl:if -->
    	<!-- xsl:if test="./glossarenglisch">
			,"glossarenglisch" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./glossarenglisch)" />"
		</xsl:if -->
		<!-- xsl:if test="./glossarfamilie">
			,"glossarfamilie" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./glossarfamilie)" />"
		</xsl:if -->
		<!-- xsl:if test="./glossareinfach">
			,"glossareinfach" : "<xsl:value-of select="java:bsz.expo.Util.toJson(./glossareinfach)" />"
		</xsl:if -->
    </xsl:template>
      
 </xsl:stylesheet>
