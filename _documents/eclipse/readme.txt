

    >>> VALORI DI STARTUP PER ECLIPSE ADT (LUNA) <<<

	
--launcher.XXMaxPermSize
512M

-vmargs
-Dosgi.requiredJavaVersion=1.6
-Xms512m
-Xmx1024m


    >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    >>> TIPS AND TRICKS <<<

	
	
	
	** ERRORI NELLE IMPORT SU ALCUNE CLASSI (package cannot be resolved)
	    (Eclipse Luna 4.4.0 - Android plugin 23.0.6)
	
	    > si verifica quando si lascia il project android 'aperto' quando si chiude eclipse
	      alla successiva riapertura di eclipse da questo errore di compilazione su alcune classi android
	      
	    > ipotesi: probabilmente dipende dalla sequena di operazione che deve fare quando fa lo startup di eclipse
	      >> allo startup di eclipse non riesce a ricostruire correttamente le dipendenze android
	      
	    > WORKAROUND:
	        - chiudere il project android
	        - fare un refresh sul project contenitore
	        - aprire il project android
	        - fare refresh del project android >> fa una build "piu' approfondita" che dovrebbe togliere i build error   