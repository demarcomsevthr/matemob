
  ______________________________________________________________________________________

	VIRTUAL MACHINE SETTINGS
	
	RAM = 1,5 GB (almeno)
	
	darwin.iso --> contiene VMware Tools setup (attenzione utilizzare versione con dim. 9662kb)
	
  ______________________________________________________________________________________
  >
  >   [01/10/2014]
  >   UPGRADE CORDOVA 3.6.3 (A SEGUITO ANDROID SECURITY ISSUE)
  >
  
  >> AGGIORNAMENTO CORDOVA
  
     sudo npm update -g cordova
  
  >> UPDATE/MERGE TRA PROJECT RICREATO E BACKUP CDV 3.3.0
  
    > platforms/ios/www/plugins                                 (CHECK > NO UPDATES NEED)
    > platforms/ios/www/*.js                                    (COPY)
    > platforms/ios/www/*.html                                  (COPY)
    > platforms/ios/www/main                                    (COPY)
    > platforms/ios/<project>/Resources/icons                   (SUBSTITUTE)
    > platforms/ios/<project>/Resources/splash                  (SUBSTITUTE)

  
  ------------------------------------------------    

  >> platforms/ios/PostScriptum/config.xml:
  
        <!-- modificato value da cloud a local 
             serve per evitare il warning 
             "Started backup to iCloud! Please be careful. 
             Your application might be rejected by Apple if you store too much data." 
             -->
        <preference name="BackupWebStorage" value="local" />

	
  ______________________________________________________________________________________

	MISSING TEST FLIGHT BETA ENTITLEMENT
	
	[17/10/2014]
	
      Ho dovuto ricreare il provisioning profile per App Store perchè su itunes connect dava il warning:
      "To use TestFlight Beta Testing, this build must contain the correct beta entitlement."
       see also http://stackoverflow.com/questions/25756669/app-does-not-contain-the-correct-beta-entitlement	
	
  ______________________________________________________________________________________

    XCODE 6 EXPORT

    [30/10/2014]
    
      XCODE 6 > SEMBRA CHE NON FUNZIONI BENE L'EXPORT DEL FILE IPA E LA SCELTA DEL PROVISIONING PROFILE
      (vedi http://stackoverflow.com/questions/25056144/xcode-6-how-to-pick-signing-certificate-provisioning-profile-for-ad-hoc-distri)
    
      WORKAROUND: utilizzare il comando xcodebuild
    
      (Es: /protoph/extras/ios/bin/protoph-xcodebuild.command)
	
  ______________________________________________________________________________________

    INSTALLAZIONE MULTIPLA XCODE
    
	> APPLE DOWNLOADS >> https://developer.apple.com/downloads/index.action
	
	> scaricare >> Xcode 5.1.1 (10/04/2014) (xcode_5.1.1.dmg)

    >> http://iosdevelopertips.com/xcode/install-multiple-versions-of-xcode.html

	  1 - mkdir /Applications/Xcode5.1.1
	  
	  2 - OPEN DMG FILE
	
	  3 - USING FINDER DRAG/DROP THE Xcode.app ICON IN THE CREATED FOLDER
	      (Copying Xcode to Xcode5.1.1...)
	      (click su Xcode app icon -> ci sta un pò...)
	      (Xcode is an application downloaded from internet. Are you sure you want to open? > Open)
	  
	  4 - LAUNCH Xcode.app
	  
	>> HO PROVATO ANCHE A FARE IL CONTRARIO (prima XC5 poi XC6 e sembra funzionare lo stesso)

	>> SELEZIONARE LA VERSIONE DI XCODE DA UTILIZZARE (SEMBRA CHE NON SERVA)
	
	  $sudo xcode-select --switch /Applications/Xcode5.1.1/Xcode.app
	  
	  $sudo xcode-select --switch /Applications/Xcode.app
	  
	  > SE NON FUNZIONA APRIRE XCODE (VERSIONE X) E DOVREBBE MOSTRARE I PROGETTI APERTI CON LA VERSIONE DI DEFAULT
	  
	
  ______________________________________________________________________________________

    AGGIORNAMENTO DI CORDOVA PLATFORM	  
	  
	  > SPECIFICARE LA VERSIONE NEL COMANDO cordova platform add, ES:
	  
	  cordova platform add ios@4.1.1
	  
	  (nella create del progetto)
	  
	  
  ______________________________________________________________________________________

    RELEASE NOTES CORDOVA PLATFORM	  
	  
	  > https://cordova.apache.org/announcements/2015/12/08/cordova-ios-4.0.0.html

      > https://cordova.apache.org/announcements/2016/03/02/ios-4.1.0.html
	  
  ______________________________________________________________________________________

    WHITE PAGE ISSUE	  
	  
	  > Può capitare che allo startup dell'app rimanga lo schermo vuoto (white blank page)
	  > (Anche senza aver cambiato niente nel progetto!)
	  > Nel log di Xcode si trova il seguente messaggio:
	  
	      ERROR Internal navigation rejected - <allow-navigation> not set for url='about:blank'
	      
	  >> WORKAROUND [08/04/2016]
	  
	     >> RESET DEL SIMULATOR (Simulator > Reset cont and settings)
	     
	     >> Sembra che questo risolva
	     
	     >> Ambiente provato:
	         > Xcode 7.3
	         > OS X 10.11.4 (El Capitan)
	         > cordova-ios@4.1.1
	         
	  >> [01/02/2017]
	  
	     > basta cancellare il file dell'app nel simulator 
	     
	     Es: /Users/marcello/Library/Developer/CoreSimulator/Devices/68A04009-76BC-4D7C-B0CE-79D7CBC0CAAE/...

  ______________________________________________________________________________________
  >
  > [01/02/2017]
  > UPGRADE CORDOVA 4.3.1
  >
  
  >> AGGIORNAMENTO CORDOVA
  
     sudo npm update -g cordova
  
  >> INSTALLAZIONE COCOAPODS (necessario per phonegap-plugin-push)
  
     sudo gem install cocoapods
     
     pod setup (crea il repository locale di pods, ci sta un pò...Resolving deltas...)
     
  >> ATTENZIONE: CON I PROGETTI PODS IN XCODE SI DEVE APRIRE:
  
     <Project Name>.xcworkspace
     
     (workspace del progetto che contiene anche la libreria Pods)
	  
	>> NOTA: NEL SIMULATORE SI RILEVANO MESSAGGI DI ERRORE DEL TIPO
	
	   __nwlog_err_simulate_crash simulate crash already simulated nw_socket_set_common_sockopts setsockopt SO_NOAPNFALLBK failed Protocol not available
	   
	   >> http://stackoverflow.com/questions/39545603/error-protocol-not-available-dumping-backtrace
	   
	   >> sembrano dovuti al fatto che il push plugin nel simulatore non funziona
	   
	   >> dicono anche di provare a settare l'environment variable OS_ACTIVITY_MODE = disable  (ma non ho provato)