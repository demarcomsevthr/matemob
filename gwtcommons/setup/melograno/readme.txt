	_____________________________________________________________	


		IMPORTANTE


	_____________________________________________________________	


	File da copiare in /users/user/.m2

	Per utilizzare il mirror melograno escludendo i repository axeiya (ckeditor) e plugins (gwtquery-plugins)
	
	
	
	In particolare l'esclusione si fa così:
	
    <mirror>
      <id>nexus</id>
      <mirrorOf>*,!axeiya,!plugins</mirrorOf>
      <url>http://helpdesk.melograno.it/nexus-2.0.6/content/groups/public</url>
    </mirror>

