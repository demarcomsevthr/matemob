//
//  CDVNatprop.h
//
//  Created by marcello on 16/10/14.
//
//

#ifndef PostScriptum_CDVNatprop_h
#define PostScriptum_CDVNatprop_h

#import <Cordova/CDVPlugin.h>

@interface CDVNatprop : CDVPlugin

- (void)getPropertiesAsString:(CDVInvokedUrlCommand*)command;

@end

#endif
