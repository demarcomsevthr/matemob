
  =================================================================
  >>                INSTALLAZIONE DI PHONEGAP 3                  <<
  =================================================================

  >> AGG. 24/01/2014

  ________________________________________________________
  > INSTALLAZIONE DI NODEJS SU WIN
  
    >> SEE http://docs.phonegap.com/en/3.3.0/guide_cli_index.md.html#The%20Command-Line%20Interface         

    -- download nodejs (es: P:\OPT\nodejs\node-v0.10.24-x64.msi)
    
    -- setup msi (va fatto per forza! aggiorna il registry e modifica il path di sistema!)
                 (es: P:\OPT\nodejs\node-v0.10.24-x64)
             
             
  ________________________________________________________
  > INSTALLAZIONE PHONEGAP / CORDOVA
  
    >> SEE http://docs.phonegap.com/en/3.3.0/guide_cli_index.md.html#The%20Command-Line%20Interface         

    > vedi install-phonegap-cordova.bat (lanciare da prompt)
    
    > CD %NODEJS_HOME%

    > npm install -g phonegap
     
    > npm install -g cordova
    



  ________________________________________________________
  > CREAZIONE ANDROID PHONEGAP PROJECT
  
    >> SEE http://docs.phonegap.com/en/3.3.0/guide_platforms_android_index.md.html#Android%20Platform%20Guide                                             
    
    > vedi create-project-android.bat (creazione project di riferimento, adding android platform, adding plugins, android build)
                                      (lanciare da prompt)
    
    >> IMPORTANTE: OCCORRE AGGIUNGERE NEL SYSTEM PATH (Sistema/Properties/Variabili di Ambiente) I SEGUENTI PATH FISSI:
    
      > ANT_HOME\bin
      > JAVA_HOME\bin
      > ANDROID_SDK\platform-tools
      > ANDROID_SDK\tools
      
      (vedi extra-system-path.txt)
      
      >> RICORDARSI DI TOGLIERLI DOPO AVER FATTO LA BUILD
      
    > DAL PROJECT CREATO COPIARE NEL PROJECT ANDROID ECLIPSE:
    
    > platforms/android/assets/www/plugins
    > platforms/android/assets/www/config.xml
    > platforms/android/assets/www/cordova_plugins.js
    > platforms/android/assets/www/cordova.js
    > platforms/android/CordovaLib/bin/CordovaLib.jar --> libs
    > platforms/android/res/xml/config.xml (copiare e controllare le cose che serve aggiungere, es:
                                            exit-on-suspend
                                            propri plugins, ...)
    > platforms/android/src/ ... la derivata di CordovaActivity.java (nel package specifico)
    > platforms/android/src/org/... (plugins cordova installati)
    > platforms/android/AndroidManifest.xml (copiare e controllare le cose che servono:
                                             minSdkVersion
                                             hardwareAccelerated = false 
                                             ...)
                                             
    
  ________________________________________________________
  > AGGIUNTA DI NUOVI CORDOVA PLUGINS
  
    >> SEE http://docs.phonegap.com/en/3.3.0/guide_cli_index.md.html#The%20Command-Line%20Interface (Add Plugin Features)

    > OCCORRE RIFARE LA BUILD DEL PROJECT (O RICREARLO EX NOVO)
      E AGGIORNARE LE RISORSE MODIFICATE:
      
    > platforms/android/assets/www/plugins
    > platforms/android/assets/www/config.xml
    > platforms/android/assets/www/cordova_plugins.js
    > platforms/android/assets/www/cordova.js
    > platforms/android/res/xml/config.xml (copiare e controllare le cose che serve aggiungere, es:
                                            exit-on-suspend
                                            propri plugins, ...)
    > platforms/android/src/org/... (plugins cordova installati)
  



    
  ________________________________________________________
  > [25/04/2015] UPGRADE TO CORDOVA4

  >> PRIMA
  
    C:\Users\marcello>npm list -g cordova
	C:\Users\marcello\AppData\Roaming\npm
	+-- cordova@3.6.3-0.2.13
	
	
	C:\Users\marcello>npm list -g cordova-lib
	C:\Users\marcello\AppData\Roaming\npm
	+-- cordova@3.6.3-0.2.13
	¦ +-- cordova-lib@0.21.13
	+-- phonegap@3.6.0-0.21.19
	  +-- cordova-lib@0.21.13
	
	
	C:\Users\marcello>npm list -g cordova-js
	C:\Users\marcello\AppData\Roaming\npm
	+-- cordova@3.6.3-0.2.13
	¦ +-- cordova-lib@0.21.13
	¦   +-- cordova-js@3.6.4
	+-- phonegap@3.6.0-0.21.19
	  +-- cordova-lib@0.21.13
	    +-- cordova-js@3.6.4
	

  >> UPDATE
	
	npm update -g cordova
	
	C:\Users\marcello\AppData\Roaming\npm\cordova -> C:\Users\marcello\AppData\Roaming\npm\node_modules\cordova\bin\cordova
	cordova@5.0.0 C:\Users\marcello\AppData\Roaming\npm\node_modules\cordova
	+-- underscore@1.7.0
	+-- q@1.0.1
	+-- nopt@3.0.1 (abbrev@1.0.5)
	+-- cordova-lib@5.0.0 (valid-identifier@0.0.1, osenv@0.1.0, properties-parser@0.2.3, bplist-parser@0.0.6, 
	mime@1.2.11, unorm@1.3.3, s0.5.2, dep-graph@1.1.0, npmconf@0.1.16, glob@4.0.6, through2@0.6.3, xcode@0.6.7, 
	d8@0.4.4, cordova-app-hello-world@3.9.0, request@2.4-registry-mapper@1.1.1, tar@1.0.2, init-package-json@1.4.2, 
	plist@1.1.0, npm@1.3.4, cordova-js@3.9.0)
	



  
