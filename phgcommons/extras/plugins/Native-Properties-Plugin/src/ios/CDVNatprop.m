//
//  CDVNatprop.c
//
//  Created by marcello on 16/10/14.
//
//

#import "CDVNatprop.h"
#import <Cordova/CDV.h>

@implementation CDVNatprop

/* log a message */
- (void)getPropertiesAsString:(CDVInvokedUrlCommand*)command
{
    
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@""];
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    
}

@end
