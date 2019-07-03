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
        	<xsl:apply-templates select="./collection"/>
        	<xsl:apply-templates select="./personen" mode="kuenstler"/>
        	<xsl:apply-templates select="./lebensdaten"/>
        	<xsl:apply-templates select="./personen[person/rolle = 'Verfasser']" mode="verfasser"/>
        	<xsl:apply-templates select="./personen[person/rolle = 'Herausgeber']" mode="herausgeber"/>
        	<xsl:apply-templates select="./personen[person/rolle = 'Adressat']" mode="adressat"/>
        	<xsl:apply-templates select="./objekttitel" />
        	<xsl:call-template name="entstehungszeit" />
        	<xsl:apply-templates select="./techniken" />
        	<xsl:apply-templates select="./materialien" />
        	<xsl:apply-templates select="./standortkurz" />	
			<xsl:apply-templates select="./hinweis" />		
			<xsl:apply-templates select="./textdeutsch" />	
			<xsl:apply-templates select="./titelenglisch" />
			<xsl:apply-templates select="./textenglisch" />	
			<xsl:apply-templates select="./status" />
			<xsl:apply-templates select="./masse" />	
			<xsl:apply-templates select="./artarchivalie" />
			<xsl:apply-templates select="./personen[person/rolle = 'Künstler/in (ehemalige Zuschreibung)']" mode="zuschreibung" /> 
			<xsl:call-template name="eingangsjahr" />   
			<xsl:apply-templates select="./urheberrecht" />  
			<xsl:call-template name="dokumentation" />				
			<xsl:call-template name="literatur" />
			
			<xsl:if test="./bilder/bild[contains(pfad,'.jpg') or contains(pfad, '.tif')]" >
				<xsl:text>,"bilder" : [</xsl:text>												 
					<xsl:for-each select="./bilder/bild[contains(pfad,'.jpg') or contains(pfad, '.tif')]" >
						<xsl:sort data-type="number" select="substring-before(substring-after(bezeichnung, '[Okat='), ']')" />
						<xsl:if test="position() != 1">,</xsl:if>
						<xsl:text>{"name" : "</xsl:text><xsl:value-of select="bezeichnung"/><xsl:text>"}</xsl:text>
					</xsl:for-each>
				<xsl:text>]</xsl:text>
			</xsl:if>    	      
        }
    </xsl:template> 
        
    <xsl:template match="imdasid">
    	<xsl:text>"imdasid" : "</xsl:text><xsl:value-of select="."/><xsl:text>"</xsl:text>
    </xsl:template>
    		
    <xsl:template match="inventarnummer">
    	<xsl:text>,"inventarnummer" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/><xsl:text>"</xsl:text>
    </xsl:template>
       		
    <xsl:template match="collection">
	    <xsl:text>,"museum" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./museum)"/><xsl:text>"</xsl:text>
		<xsl:text>,"sammlung" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./sammlung)"/><xsl:text>"</xsl:text>
	</xsl:template>
	
	<xsl:template match="personen" mode="kuenstler">
		<xsl:text>,"kuenstler" : "</xsl:text>
		<xsl:if test="count(./person[contains(rolle, 'Adressat')]) = 0 and ./person[rolle = 'Verfasser']">
			<xsl:value-of select="java:de.jotwerk.Util.toJson(./person[rolle = 'Verfasser']/anzeigename)" />				
		</xsl:if>
		<xsl:if test="count(./person[contains(rolle, 'Verfasser')]) = 0 and ./person[rolle = 'Adressat']">
			<xsl:value-of select="java:de.jotwerk.Util.toJson(./person[rolle = 'Adressat']/anzeigename)" />				
		</xsl:if>
		<xsl:if test="./person[rolle = 'Künstler/in'] and ./person/rolle = 'Adressat' and count(./person[contains(rolle, 'Verfasser')]) = 0">
			<xsl:value-of select="java:de.jotwerk.Util.toJson(./person[contains(rolle, 'Künstler/in')]/anzeigename)" />
				<xsl:text> an </xsl:text>
			<xsl:value-of select="./person[contains(rolle, 'Adressat')]/ANZEIGENAME" />
		</xsl:if>
		<xsl:if test="./person[contains(rolle, 'Verfasser')] and ./person[contains(rolle, 'Adressat')]">
			<xsl:call-template name="seperatedList">
				<xsl:with-param name="content" select="./person[contains(rolle, 'Verfasser')]/anzeigename" />
			</xsl:call-template>
			<xsl:text> an </xsl:text>
			<xsl:call-template name="seperatedList">
				<xsl:with-param name="content" select="./person[contains(rolle, 'Adressat')]/anzeigename" />
			</xsl:call-template>
		</xsl:if>
		<xsl:if test="count(./person/nachname) &gt; 0">
			<xsl:call-template name="kuenstler" >
				<xsl:with-param name="personen" select="./person[not(contains(rolle,'ehemalige Zuschreibung')) and not(contains(rolle,'Ikonographie'))]" />
			</xsl:call-template>
		</xsl:if>
		<xsl:text>"</xsl:text>
	</xsl:template>
	
	<xsl:template match="lebensdaten">
		<xsl:text>,"lebensdaten" : "</xsl:text>
		<xsl:call-template name="seperatedList">
			<xsl:with-param name="content" select="." />
		</xsl:call-template><xsl:text>"</xsl:text>
	</xsl:template>
	
	<xsl:template match="personen" mode="verfasser">
		<xsl:text>,"verfasser" : "</xsl:text>
		<xsl:call-template name="seperatedList">
			<xsl:with-param name="content" select="./person[contains(rolle, 'Verfasser')]/anzeigename" />
		</xsl:call-template><xsl:text>"</xsl:text>					
	</xsl:template>
				
	<xsl:template match="personen" mode="herausgeber">
		<xsl:text>,"herausgeber" : "</xsl:text>
		<xsl:call-template name="seperatedList">
			<xsl:with-param name="content" select="./person[contains(rolle, 'Herausgeber')]/anzeigename" />
		</xsl:call-template><xsl:text>"</xsl:text>					
	</xsl:template>
				
	<xsl:template match="personen" mode="adressat">
		<xsl:text>,"adressat" : "</xsl:text>
		<xsl:call-template name="seperatedList">
			<xsl:with-param name="content" select="./person[contains(rolle, 'Adressat')]/anzeigename" />
		</xsl:call-template><xsl:text>"</xsl:text>					
	</xsl:template>
				
	<xsl:template match="objekttitel">	
		<xsl:if test="count(../archivalienart) = 0">
			<xsl:text>,"titel" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(.)" /><xsl:text>"</xsl:text>			
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="entstehungszeit">
		<xsl:text>,"entstehungszeit" : "</xsl:text>
		<xsl:choose>
			<xsl:when test="./entstehungszeit">
				<xsl:value-of select="java:de.jotwerk.Util.toJson(./entstehungszeit)"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>nicht datiert</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text>"</xsl:text>			
	</xsl:template>
	
	<xsl:template match="techniken">
		<xsl:text>,"technik" : "</xsl:text>
		<xsl:call-template name="seperatedList">
			<xsl:with-param name="content" select="./technik/term" />
		</xsl:call-template>
		<xsl:text>"</xsl:text>
	</xsl:template>
	
	<xsl:template match="materialien">
		<xsl:text>,"material" : "</xsl:text>
		<xsl:call-template name="seperatedList">
			<xsl:with-param name="content" select="./material/term" />
		</xsl:call-template>
		<xsl:text>"</xsl:text>
	</xsl:template>
	
	<xsl:template match="standortkurz">
		<xsl:text>,"standort" : "</xsl:text>
		<xsl:value-of select="java:de.jotwerk.Util.toJson(.)" />
		<xsl:text>"</xsl:text>
	</xsl:template>
	
	<xsl:template match="hinweis">
		<xsl:text>,"hinweis" : "</xsl:text>
		<xsl:value-of select="java:de.jotwerk.Util.toJson(.)"/>
		<xsl:text>"</xsl:text>
	</xsl:template>
	
	<xsl:template match="textdeutsch">
		<xsl:text>,"textdeutsch" : "</xsl:text>
		<xsl:value-of select="java:de.jotwerk.Util.rtfToHtml(.)" />
		<xsl:text>"</xsl:text>
	</xsl:template>
	
	<xsl:template match="titelenglisch">
		<xsl:text>,"titelenglisch" : "</xsl:text>
		<xsl:value-of select="java:de.jotwerk.Util.toJson(.)" />
		<xsl:text>"</xsl:text>
	</xsl:template>
	
	<xsl:template match="textenglisch">
		<xsl:text>,"textenglisch" : "</xsl:text>
		<xsl:value-of select="java:de.jotwerk.Util.rtfToHtml(.)" />
		<xsl:text>"</xsl:text>
	</xsl:template>
	
	<xsl:template match="status">
		<xsl:text>,"status" : "</xsl:text>
		<xsl:value-of select="java:de.jotwerk.Util.toJson(.)" />
		<xsl:text>"</xsl:text>
	</xsl:template>
	
	<xsl:template match="artarchivalie">
		<xsl:value-of select="java:de.jotwerk.Util.toJson(.)" />
		<xsl:text>"</xsl:text>
	</xsl:template>
	
	<xsl:template match="personen" mode="zuschreibung">
		<xsl:text>,"zuschreibung" : "</xsl:text>
		<xsl:call-template name="seperatedList">
			<xsl:with-param name="content" select="./person[rolle = 'Künstler/in (ehemalige Zuschreibung)']/anzeigename" />
		</xsl:call-template>	
		<xsl:text>"</xsl:text>
	</xsl:template>	

	<xsl:template name="eingangsjahr">
		<xsl:if test="not(starts-with(./eingangsart, 'Leihgabe') or contains(./eingangsdatum, 'unbek.'))">
			<xsl:text>,"eingangsjahr" : "</xsl:text>		
			<xsl:choose>
				<xsl:when test="contains(./eingangsdatum, '/')">
					<xsl:value-of select="substring-before(./eingangsdatum,'/')" />
				</xsl:when>
				<xsl:when test="contains(./eingangsdatum, 'vor ')">
					<xsl:value-of select="substring-after(./eingangsdatum,'vor ')" />
				</xsl:when>
				<xsl:when test="contains(./eingangsdatum, 'nach ')">
					<xsl:value-of select="substring-after(./eingangsdatum,'nach ')" />
				</xsl:when>
				<xsl:when test="contains(./eingangsdatum, 'um ')">
					<xsl:value-of select="substring-after(./eingangsdatum,'um ')" />
				</xsl:when>
				<xsl:when test="contains(./eingangsdatum, '[')">
					<xsl:value-of select="substring-before(substring-after(./eingangsdatum,'['),']') " />
				</xsl:when>
				<xsl:when test="./eingangsdatum = 'Anfg.1920er'">1920</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="java:de.jotwerk.Util.extractYear(./eingangsdatum)" />
				</xsl:otherwise>
			</xsl:choose>				
			<xsl:text>"</xsl:text>
		</xsl:if>
    </xsl:template>		
	
	<xsl:template match="urheberrecht">
		<xsl:text>,"copyright" : "</xsl:text>
		<xsl:value-of select="." />				
		<xsl:text>"</xsl:text>
	</xsl:template>	
	
	<xsl:template name="dokumentation" >
		<xsl:if test="./dokumentationintern or ./dokumentationextern">
			<xsl:text>,"testimonial" : [</xsl:text>
			<xsl:for-each select="./dokumentationintern|./dokumentationextern" >
				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>	
				<xsl:text>{</xsl:text>
					<xsl:variable name="verfasser">
						<xsl:choose>
							<xsl:when test="./verfasser">
								<xsl:value-of select="./verfasser" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:value-of select="java:de.jotwerk.Util.exName(./anzeigename)" />
							</xsl:otherwise>
						</xsl:choose>			
					</xsl:variable>
					<xsl:text>"verfasser" : "</xsl:text><xsl:value-of select="$verfasser" /><xsl:text>"</xsl:text>
					<xsl:text>,"anzeigename" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./anzeigename)" /><xsl:text>"</xsl:text>					
					<xsl:text>,"notiz" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./notiz)" /><xsl:text>"</xsl:text>
					<xsl:text>,"text" : "</xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./text)" /><xsl:text>"</xsl:text>
				<xsl:text>}</xsl:text>
			</xsl:for-each>
			<xsl:text>]</xsl:text>	
		</xsl:if>
	</xsl:template>
		
	
	<xsl:template name="kuenstler">
		<xsl:param name="personen" />		
		<xsl:choose>
			<xsl:when test="count($personen) = 1">
				<xsl:value-of select="java:de.jotwerk.Util.toJson($personen/anzeigename)" />
			</xsl:when>
			<xsl:when test="$personen[./rolle = 'Zeichner']">
				<xsl:value-of select="java:de.jotwerk.Util.toJson($personen[./rolle = 'Zeichner']/anzeigename)" />
				<xsl:choose>
					<xsl:when test="count($personen[./rolle = 'Erfinder']) = 2">
						<xsl:text> nach </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson($personen[./rolle = 'Erfinder'][1]/anzeigename)" />
						<xsl:text> und </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson($personen[./rolle = 'Erfinder'][2]/anzeigename)" />
					</xsl:when>
					<xsl:when test="count($personen[./rolle = 'Erfinder']) = 1">
						<xsl:text> nach </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson($personen[./rolle = 'Erfinder'][1]/anzeigename)" />
					</xsl:when>
					<xsl:when test="$personen[./rolle = 'Stecher']">
						<xsl:text> und </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson($personen[./rolle = 'Stecher'][1]/anzeigename)" />
					</xsl:when>
				</xsl:choose>
			</xsl:when>
			<xsl:when test="$personen[./rolle = 'Stecher'] and $personen[./rolle = 'Erfinder']">
				<xsl:value-of select="java:de.jotwerk.Util.toJson($personen[./rolle = 'Stecher'][1]/anzeigename)" />
				<xsl:text> nach </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson($personen[./rolle = 'Erfinder'][1]/anzeigename)" />
			</xsl:when> 
			<xsl:when test="$personen[./rolle = 'Stecher'] and $personen[./rolle = 'Autor']">
				<xsl:value-of select="java:de.jotwerk.Util.toJson($personen[./rolle = 'Stecher'][1]/anzeigename)" />
				<xsl:text> mit </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson($personen[./rolle = 'Autor'][1]/anzeigename)" />
			</xsl:when>
			<xsl:when test="$personen[./rolle = 'Künstler/in'] and $personen[./rolle = 'Erfinder']">
				<xsl:value-of select="$personen[./rolle = 'Künstler/in'][1]/anzeigename" />
				<xsl:text> nach </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson($personen[./rolle = 'Erfinder'][1]/anzeigename)" />
			</xsl:when>			
			<xsl:when test="count($personen) &gt; 1">
				<xsl:call-template name="seperatedList">
					<xsl:with-param name="content" select="java:de.jotwerk.Util.toJson($personen/anzeigename)" />
				</xsl:call-template>												
			</xsl:when>
		</xsl:choose>			
	</xsl:template>	
	
	<xsl:template name="seperatedList">
		<xsl:param name="content" />
		<xsl:if test="$content" >
			<xsl:value-of select="java:de.jotwerk.Util.toJson($content[1])" />
			<xsl:if test="count($content) &gt; 1">
				<xsl:text>; </xsl:text>
				<xsl:call-template name="seperatedList" >
					<xsl:with-param name="content" select="$content[position() &gt; 1]" />
				</xsl:call-template>
		    </xsl:if>
		</xsl:if>
	</xsl:template>
		
	<xsl:template match="masse" >
		<xsl:text>,"masse" : "</xsl:text>
		<xsl:choose>
			<xsl:when test="count(./mass)=1"> 
				<xsl:value-of select="./mass/typ"/><xsl:text>: </xsl:text><xsl:value-of select="./mass/value"/><xsl:text>&#160;</xsl:text><xsl:value-of select="./mass/einheit" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="./mass"/>
			</xsl:otherwise>
		</xsl:choose>
		<xsl:text>"</xsl:text>		
	</xsl:template>
	
	<xsl:template match="mass" >
		<xsl:value-of select="./typ"/><xsl:apply-templates select="./teil" /><xsl:text>: </xsl:text><xsl:value-of select="./wert"/><xsl:text>&#160;</xsl:text><xsl:value-of select="./einheit" /><xsl:text>; </xsl:text> 
	</xsl:template>	
	
	<xsl:template match="teil">
		<xsl:text>-</xsl:text><xsl:value-of select="." />
	</xsl:template>	
	
	<xsl:template name="literatur">
		<xsl:if test="./literatur[contains(notiz, '[Okat=')]">
			<xsl:text>,"literatur" : [</xsl:text>
			<xsl:for-each select="./literatur[contains(notiz, '[Okat=')]">
				<xsl:sort data-type="number" select="substring-before(substring-after(./notiz, '[Okat='), ']')" />
				<xsl:if test="position() != 1"><xsl:text>,</xsl:text></xsl:if>
				<xsl:text>{</xsl:text>
					<xsl:text>"titel" : "</xsl:text>
					<xsl:value-of select="java:de.jotwerk.Util.toJson(./autor)" /><xsl:text>: </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./titel)" />
					<xsl:if test="./zusatz">
						<xsl:text>. </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./zusatz)" />
					</xsl:if>
					<xsl:if test="./inbuch">
						<xsl:text>, In: </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./inbuch)" />
					</xsl:if>
					<xsl:if test="./inzeitschrift">
						<xsl:text>, In: </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./inzeitschrift)" />
					</xsl:if>
					<xsl:if test="./band">
						<xsl:text>, Bd. </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./band)" />
					</xsl:if>
					<xsl:if test="./ort">
						<xsl:text>, </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./ort)" />
					</xsl:if>
					<xsl:if test="./jahr">
						<xsl:text> </xsl:text><xsl:value-of select="java:de.jotwerk.Util.toJson(./jahr)" />
					</xsl:if>
					<xsl:if test="./seite">
						<xsl:text>, S. </xsl:text><xsl:value-of select="./seite" />
					</xsl:if>
					<xsl:if test="count(./seite) = 0 and count(./abb) &gt; 0">
						<xsl:text>, </xsl:text><xsl:value-of select="./abb" />
					</xsl:if>
					<xsl:if test="./nr">
						<xsl:text>, Nr. </xsl:text><xsl:value-of select="./nr" />
						<xsl:text>. </xsl:text>
					</xsl:if>
					<xsl:if test="count(./abb) = 0 and count(./nr) = 0">
						<xsl:text>.</xsl:text>
					</xsl:if>
					<xsl:if test="count(./seite) &gt; 0 and count(./abb) &gt; 0 and count(./nr) = 0">
						<xsl:text>.</xsl:text>
					</xsl:if>
				<xsl:text>}</xsl:text>
			</xsl:for-each>
			<xsl:text>]</xsl:text>
		</xsl:if>
	</xsl:template>   		   			
   
</xsl:stylesheet>
